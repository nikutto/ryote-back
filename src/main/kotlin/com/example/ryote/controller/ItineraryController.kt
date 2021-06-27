package com.example.ryote.controller

import com.example.ryote.dto.LandmarkDto
import com.example.ryote.dto.SiteDto
import com.example.ryote.service.ItineraryService
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
class ItineraryController(
    @Autowired val service: ItineraryService,
) {
    @GetMapping("/landmark")
    suspend fun getLandmarks(@RequestParam(value = "day") day: Int) = service.getLandmarks(day)

    @GetMapping("/transportation")
    suspend fun getTransportations(@RequestParam(value = "day") day: Int) =
        service.getTransportations(day)

    @PostMapping("/landmark/register")
    suspend fun addLandmark(
        @RequestBody landmark: Mono<LandmarkDto>,
    ) = landmark.block()!!.let {
        landmark ->
        service.addLandmark(landmark.day, landmark.name, landmark.detail)
    }

    @GetMapping("/site")
    suspend fun getSites(@RequestParam(value = "day") day: Int) = service.getSites(day)

    @PostMapping("/site/register")
    suspend fun addSites(
        @RequestBody site: Mono<SiteDto>,
    ) {
        site.awaitSingle().let {
            service.addSite(it)
        }
    }

    @GetMapping("/login_plz")
    suspend fun getLoginPageInvalid(): Nothing = throw ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Login must be done by /login with form"
    )
}
