package com.example.ryote.controller

import com.example.ryote.dto.LandmarkDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration
import org.springframework.context.annotation.Import

@Import(TestEntityManagerAutoConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItineraryControllerTests(
  @Autowired val restTemplate: TestRestTemplate,
) {

  @Test
  fun getLandmarksTest() {
    /*
    val landmark = LandmarkDto(0, 2, "Kyoto Station", "with Kyoto tower.")
    restTemplate.postForEntity<Unit>("/landmark/register", landmark, Unit::class.java)  
     */
    val entity = restTemplate.getForEntity<String>("/landmark?day=2", String::class.java)
    assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
  }
}