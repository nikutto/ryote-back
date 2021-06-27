package com.example.ryote.service

import com.example.ryote.converter.toDao
import com.example.ryote.converter.toDto
import com.example.ryote.dto.SiteDto
import com.example.ryote.repository.SiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItineraryService(
    @Autowired val siteRepository: SiteRepository,
) {

    fun getSites(day: Int) = siteRepository
        .findByDay(day)
        .map { it.toDto() }
        .toList()

    fun addSite(site: SiteDto) = siteRepository
        .save(site.toDao())
}
