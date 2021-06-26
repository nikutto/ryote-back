package com.example.ryote.controller

import com.example.ryote.dao.SiteType
import com.example.ryote.dto.SiteDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
        val headers = LinkedMultiValueMap<String, String>()
        headers.add("Cookie", sessionStr)

        val requestEntity =
            RequestEntity<SiteDto>(
                site, headers, HttpMethod.POST, URI("/site/register"), SiteDto::class.java
            )
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
