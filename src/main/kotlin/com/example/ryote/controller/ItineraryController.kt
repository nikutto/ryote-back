package com.example.ryote.controller

import com.example.ryote.dto.LandmarkDto
import com.example.ryote.dto.SiteDto
import com.example.ryote.service.ItineraryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ItineraryController(
    @Autowired val service: ItineraryService,
) {
    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/landmark")
    fun getLandmarks(@RequestParam(value = "day") day: Int) = service.getLandmarks(day)

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/transportation")
    fun getTransportations(@RequestParam(value = "day") day: Int) = service.getTransportations(day)

    @PostMapping("/landmark/register")
    fun addLandmark(
        @RequestBody landmark: LandmarkDto,
    ) = service.addLandmark(landmark.day, landmark.name, landmark.detail)

    @GetMapping("/site")
    fun getSites(@RequestParam(value = "day") day: Int) = service.getSites(day)

    @PostMapping("/site/register")
    fun addSites(
        @RequestBody site: SiteDto,
    ) = service.addSite(site)
}
