package com.example.ryote.repository

import com.example.ryote.dao.Landmark
import com.example.ryote.dao.Transportation
import org.springframework.data.repository.CrudRepository

interface LandmarkRepository: CrudRepository<Landmark, Long> {
  fun findByDay(day: Int): List<Landmark>
  fun save(landmark: Landmark)
}

interface TransportationRepository: CrudRepository<Transportation, Long> {
  fun findByDay(day: Int): List<Transportation>
}
