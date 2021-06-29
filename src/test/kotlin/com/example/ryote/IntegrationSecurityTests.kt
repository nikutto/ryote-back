package com.example.ryote

import com.example.ryote.controller.ItineraryController
import com.example.ryote.security.ClientConfig
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
class IntegrationSecurityTests(
    @Autowired val environment: Environment,
    @Autowired val clientConfig: ClientConfig,
) {

    val port = environment.getProperty("local.server.port", Int::class.java)
    val webClient: WebClient = WebClient.create("http://localhost:$port")
    val webTestUtil = WebTestUtil(webClient)
    val logger = LoggerFactory.getLogger(IntegrationTests::class.java)

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
    fun testSession() {
        runBlocking {
            val sessionStr = webTestUtil.getSessionStr()
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

    @Test
    fun testValidOrigin() {
        runBlocking {
            val msg = webClient.get()
                .uri("/health")
                .header("Origin", clientConfig.getOrigin())
                .retrieve()
                .awaitBody<String>()
            assertThat(msg).isEqualTo(ItineraryController.HEALTH_MSG)
        }
    }

    @Test
    fun testInvalidOrigin() {
        runBlocking {
            try {
                webClient.get()
                    .uri("/health")
                    .header("Origin", "http://localhost:2929")
                    .retrieve()
                    .awaitBody<String>()
                throw AssertionError("Invalid origin request should fail.")
            } catch (e: WebClientResponseException) {
                assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN)
            }
        }
    }
}
