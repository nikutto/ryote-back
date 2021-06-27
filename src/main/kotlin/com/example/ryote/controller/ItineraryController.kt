package com.example.ryote.controller

import com.example.ryote.dto.SiteDto
import com.example.ryote.service.ItineraryService
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ItineraryController(
    @Autowired val service: ItineraryService,
) {

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
}
