package com.example.ryote.controller

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItineraryControllerTests(
  @Autowired val restTemplate: TestRestTemplate
) {

  @Test
  fun getLandmarksTest() {
    val entity = restTemplate.getForEntity<String>("/landmark?day=1", String::class.java)
    assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(entity.body).contains("Kyoto")
  }
}