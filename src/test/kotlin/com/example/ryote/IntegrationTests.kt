package com.example.ryote.controller

import com.example.ryote.dao.SiteType
import com.example.ryote.dto.SiteDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(
    @Autowired val restTemplate: TestRestTemplate,
) {
    val logger = LoggerFactory.getLogger(IntegrationTests::class.java)

    fun getSessionStr(): String {
        val loginData = LinkedMultiValueMap<String, String>()
        loginData.add("username", "user")
        loginData.add("password", "password")

        val requestEntity = RequestEntity.post(
            URI("/login")
        ).contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(loginData)

        val responseEntity = restTemplate.exchange(requestEntity, Unit::class.java)

        val cookie = responseEntity.getHeaders().get("Set-Cookie")!!

        assert(cookie.any { it.startsWith("SESSION=") })

        val sessionStr = cookie.find {
            it.startsWith("SESSION=")
        }!!.split(";")
            .find {
                it.startsWith("SESSION=")
            }!!
        return sessionStr
    }

    @Test
    fun getSitesTest() {
        val sessionStr = getSessionStr()

        val site = SiteDto(
            id = 0,
            siteType = SiteType.LANDMARK,
            day = 100,
            ord = 0,
            name = "Kyoto Tower",
            detail = "Good view.",
            startTime = null,
            endTime = null,
        )

        val requestEntity = RequestEntity
            .post("/site/register")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Cookie", sessionStr)
            .body<SiteDto>(site)

        assertThat(restTemplate.exchange<Unit>(requestEntity, Unit::class.java).statusCode).isEqualTo(HttpStatus.OK)

        val getHeaders = LinkedMultiValueMap<String, String>()
        getHeaders.add("Cookie", sessionStr)
        val getRequestEntity =
            HttpEntity<Unit>(
                getHeaders,
            )

        val entity = restTemplate
            .exchange<String>(URI("/site?day=100"), HttpMethod.GET, getRequestEntity, String::class.java)

        logger.info(entity.headers.toString())
        logger.info(entity.body.toString())
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Kyoto Tower")
    }
}
