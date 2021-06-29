package com.example.ryote

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

class WebTestUtil(val webClient: WebClient) {

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

        assertThat(cookie.any { it.startsWith("SESSION=") }).isTrue()

        val sessionStr = cookie.find {
            it.startsWith("SESSION=")
        }!!.split(";")
            .find {
                it.startsWith("SESSION=")
            }!!
        return sessionStr
    }
}
