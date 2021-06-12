package com.example.ryote.repository

import com.example.ryote.dao.Landmark
import com.example.ryote.dao.Transportation
import org.springframework.stereotype.Repository

@Repository
class ItineraryRepository {

  fun getLandmarks(day: Int) = listOf(
    Landmark(100 * day + 0, "Kyoto station", "Traditional Japanese city."),
    Landmark(100 * day + 1, "Tokyo station", "Central Japanese city."),
    Landmark(100 * day + 2, "Jimbocho", "Famous for its variety of book stores.")
  )
  
  fun getTransportations(day: Int) = listOf(
    Transportation(100 * day + 0, "Shikansen", "Tokaido"),
    Transportation(100 * day + 1, "Yamanote Line", "Kanjosen"),
  )

}