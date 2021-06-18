package com.example.ryote.service

import com.example.ryote.converter.toDao
import com.example.ryote.converter.toDto
import com.example.ryote.dao.Landmark
import com.example.ryote.dto.SiteDto
import com.example.ryote.repository.LandmarkRepository
import com.example.ryote.repository.SiteRepository
import com.example.ryote.repository.TransportationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItineraryService(
    @Autowired val landmarkRepository: LandmarkRepository,
    @Autowired val transportationRepository: TransportationRepository,
    @Autowired val siteRepository: SiteRepository,
) {

    fun getLandmarks(day: Int) = landmarkRepository
        .findByDay(day)
        .map { it.toDto() }
        .toList()

    fun getTransportations(day: Int) = transportationRepository
        .findByDay(day)
        .map { it.toDto() }
        .toList()

    fun addLandmark(day: Int, name: String, detail: String) = landmarkRepository
        .save(Landmark(0, day, name, detail))

    fun getSites(day: Int) = siteRepository
        .findByDay(day)
        .map { it.toDto() }
        .toList()

    fun addSite(site: SiteDto) = siteRepository
        .save(site.toDao())
}
