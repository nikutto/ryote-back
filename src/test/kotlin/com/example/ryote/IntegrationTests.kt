package com.example.ryote

import com.example.ryote.controller.ItineraryController
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
import org.springframework.web.reactive.function.client.WebClientResponseException
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

    @Test
    fun testLoginSuccess() {
        runBlocking {
            val loginData = LinkedMultiValueMap<String, String>()
            loginData.add("username", "user")
            loginData.add("password", "password")

            val resp = webClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(loginData)
                .retrieve()
                .awaitBodilessEntity()

            assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(
                resp
                    .getHeaders()
                    .get("Set-Cookie")
                    ?.filter {
                        it.startsWith("SESSION=")
                    }
                    ?.count()
            ).isEqualTo(1)
        }
    }

    @Test
    fun testLoginFailure() {
        runBlocking {
            val loginData = LinkedMultiValueMap<String, String>()
            loginData.add("username", "puni-chan")
            loginData.add("password", "abcd_punipuni")

            try {
                webClient.post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(loginData)
                    .retrieve()
                    .awaitBodilessEntity()
                throw AssertionError("Retrive must be fail due to 401")
            } catch (e: WebClientResponseException) {
                assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED)
            }
        }
    }

    @Test
    fun testHealth() {
        runBlocking {
            val msg = webClient.get()
                .uri("/health")
                .retrieve()
                .awaitBody<String>()
            assertThat(msg).isEqualTo(ItineraryController.HEALTH_MSG)
        }
    }
    @Test
    fun testSession() {
        runBlocking {
            val sessionStr = getSessionStr()
            val msg = webClient
                .get()
                .uri("/health_authenticated")
                .header("Cookie", sessionStr)
                .retrieve()
                .awaitBody<String>()
            assertThat(msg).isEqualTo(ItineraryController.HEALTH_AUTHENTICATED_MSG)
        }
    }

    @Test
    fun testAuthenticatedWithNoSession() {
        runBlocking {
            val entity = webClient
                .get()
                .uri("/health_authenticated")
                .retrieve()
                .awaitBodilessEntity()
            assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FOUND)
        }
    }

    @Test
    fun testAuthenticatedWithInvalidSession() {
        runBlocking {
            val entity = webClient
                .get()
                .uri("/health_authenticated")
                .header("Cookie", "SESSION=invalidSessionString")
                .retrieve()
                .awaitBodilessEntity()
            assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FOUND)
        }
    }
}
