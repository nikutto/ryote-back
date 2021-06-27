package com.example.ryote.repository

import com.example.ryote.dao.Site
import org.springframework.data.repository.CrudRepository

interface SiteRepository : CrudRepository<Site, Long> {
    fun findByDay(day: Int): List<Site>
    fun save(site: Site)
}
