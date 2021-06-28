package com.example.ryote

import com.example.ryote.dao.SiteType
import com.example.ryote.dto.SiteDto
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBody

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(
    @Autowired val environment: Environment
) {

    val port = environment.getProperty("local.server.port", Int::class.java)
    val webClient: WebClient = WebClient.create("http://localhost:$port")
    val logger = LoggerFactory.getLogger(IntegrationTests::class.java)

    suspend fun getSessionStr(): String {

        val loginData = LinkedMultiValueMap<String, String>()
        loginData.add("username", "user")
        loginData.add("password", "password")

        val resp = webClient.post()
            .uri("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(loginData)
            .retrieve()
            .awaitBodilessEntity()

        val cookie = resp.getHeaders().get("Set-Cookie")!!

        assert(cookie.any { it.startsWith("SESSION=") })

        val sessionStr = cookie.find {
            it.startsWith("SESSION=")
        }!!.split(";")
            .find {
                it.startsWith("SESSION=")
            }!!
        return sessionStr
    }

    suspend fun registerSite(site: SiteDto, sessionStr: String) = webClient
        .post()
        .uri("/site/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(site)
        .header("Cookie", sessionStr)
        .retrieve()
        .awaitBodilessEntity()

    @Test
    fun getSitesTest() {
        runBlocking {

            val sessionStr = getSessionStr()

            val day = 292929
            val landmarkName = "Kyoto Tower"

            val site = SiteDto(
                id = 0,
                siteType = SiteType.LANDMARK,
                day = day,
                ord = 0,
                name = landmarkName,
                detail = "Good view.",
                startTime = null,
                endTime = null,
            )

            val respRegister = registerSite(site, sessionStr)
            assertThat(respRegister.statusCode).isEqualTo(HttpStatus.OK)

            val entity = webClient
                .get()
                .uri("/site?day=$day")
                .header("Cookie", sessionStr)
                .retrieve()

            assertThat(entity.awaitBodilessEntity().statusCode).isEqualTo(HttpStatus.OK)
            assertThat(entity.awaitBody<String>()).contains(landmarkName)
        }
    }
}
