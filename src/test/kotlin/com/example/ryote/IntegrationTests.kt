package com.example.ryote.controller

import com.example.ryote.dao.SiteType
import com.example.ryote.dto.SiteDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import java.net.URI

@Import(TestEntityManagerAutoConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(
    @Autowired val restTemplate: TestRestTemplate,
) {

    val logger: Logger = LoggerFactory.getLogger(IntegrationTests::class.java)

    fun getSessionStr(): String {
        val loginData = LinkedMultiValueMap<String, String>()
        loginData.add("username", "user")
        loginData.add("password", "password")

        val requestEntity = RequestEntity.post(
            URI("/login")
        ).contentType(MediaType.MULTIPART_FORM_DATA)
            .body(loginData)

        val responseEntity = restTemplate.exchange(requestEntity, Unit::class.java)

        val cookie = responseEntity.getHeaders().get("Set-Cookie")!!

        assert(cookie.any { it.startsWith("JSESSIONID=") })

        val sessionStr = cookie.find {
            it.startsWith("JSESSIONID=")
        }!!.split(";")
            .find {
                it.startsWith("JSESSIONID=")
            }!!
        logger.info(responseEntity.headers.toString())
        logger.info(cookie.toString())
        logger.info(sessionStr)
        return sessionStr
    }

    @Test
    fun getSitesTest() {
        logger.info("getSitesTestStarted")
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
        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Cookie", sessionStr)

        val requestEntity =
            RequestEntity<SiteDto>(
                site, headers, HttpMethod.POST, URI("/site/register"), SiteDto::class.java
            )
        logger.info(requestEntity.headers.toString())
        restTemplate.exchange<Unit>(requestEntity, Unit::class.java)

        val getHeaders = LinkedMultiValueMap<String, String>()
        getHeaders.add("Cookie", sessionStr)
        val getRequestEntity =
            HttpEntity<Unit>(
                getHeaders,
            )
        val entity = restTemplate
            .exchange<String>(URI("/site?day=100"), HttpMethod.GET, getRequestEntity, String::class.java)

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Kyoto Tower")
    }
}
