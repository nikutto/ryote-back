package com.example.ryote.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

data class Landmark(val id: Int, val name: String, val detail: String)
data class Transportation(val id: Int, val name: String, val detail: String)

@RestController
public class ItineraryController {

  @GetMapping("/landmark")
  public fun getLandmarks(@RequestParam(value = "day") day: Int) = listOf(
    Landmark(100 * day + 0, "Kyoto station", "Traditional Japanese city."),
    Landmark(100 * day + 1, "Tokyo station", "Central Japanese city."),
    Landmark(100 * day + 2, "Jimbocho", "Famous for its variety of book stores.")
  )
  
  @GetMapping("/transportation")
  public fun getTransportations(@RequestParam(value = "day") day: Int) = listOf(
    Transportation(100 * day + 0, "Shikansen", "Tokaido"),
    Transportation(100 * day + 1, "Yamanote Line", "Kanjosen"),
  )
}