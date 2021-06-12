package com.example.ryote.repository

import com.example.ryote.dao.Landmark
import com.example.ryote.dao.Transportation
import org.springframework.stereotype.Repository

@Repository
class ItineraryRepository {

  fun getLandmarks(day: Int) = if (day == 0) {
    listOf(
      Landmark(100 * day + 0, "Kyoto station", "Traditional Japanese city."),
      Landmark(100 * day + 1, "Tokyo station", "Central Japanese city."),
      Landmark(100 * day + 2, "Jimbocho", "Famous for its variety of book stores.")
    )
  } else {
    listOf(
      Landmark(100 * day + 0, "Asakusa", "Sensoji temple"),
      Landmark(100 * day + 1, "Ikebukuro", "Nothing especially"),
      Landmark(100 * day + 2, "Shinjuku", "Big city")
    )
  }
  
  fun getTransportations(day: Int) = if (day == 0) {
      listOf(
        Transportation(100 * day + 0, "Shikansen", "Tokaido"),
        Transportation(100 * day + 1, "Yamanote Line", "Kanjosen"),
      )
  } else {
      listOf(
        Transportation(100 * day + 0, "Yamanote Line", "Kanjosen"),
        Transportation(100 * day + 1, "Yamanote Line", "Kanjosen"),
      )
    }
}