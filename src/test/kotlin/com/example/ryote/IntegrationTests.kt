package com.example.ryote.controller

import com.example.ryote.dao.SiteType
import com.example.ryote.dto.LandmarkDto
import com.example.ryote.dto.SiteDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus

@Import(TestEntityManagerAutoConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTests(
    @Autowired val restTemplate: TestRestTemplate,
) {

    @Test
    fun getLandmarksTest() {
        val landmark = LandmarkDto(0, 2, "Kyoto Station", "with Kyoto tower.")
        restTemplate.postForEntity<Unit>("/landmark/register", landmark, Unit::class.java)
        val entity = restTemplate.getForEntity<String>("/landmark?day=2", String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Kyoto")
    }

    @Test
    fun getSitesTest() {
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

        restTemplate.postForEntity<Unit>("/site/register", site, Unit::class.java)
        val entity = restTemplate.getForEntity<String>("/site?day=100", String::class.java)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("Kyoto Tower")
    }
}
