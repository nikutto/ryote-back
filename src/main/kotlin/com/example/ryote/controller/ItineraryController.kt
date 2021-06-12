package com.example.ryote.controller

import com.example.ryote.service.ItineraryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

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
    
}