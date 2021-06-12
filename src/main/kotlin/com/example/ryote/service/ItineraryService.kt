package com.example.ryote.service

import com.example.ryote.converter.*
import com.example.ryote.dto.LandmarkDto
import com.example.ryote.dto.TransportationDto
import com.example.ryote.repository.ItineraryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItineraryService(
  @Autowired val repository: ItineraryRepository
) {

  fun getLandmarks(day: Int) = repository
        .getLandmarks(day)
        .map { it.toDto() }
        .toList()

  fun getTransportations(day: Int) = repository
        .getTransportations(day)
        .map { it.toDto() }
        .toList()

}