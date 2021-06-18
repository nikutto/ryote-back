package com.example.ryote.configuration

import com.example.ryote.dao.Site
import com.example.ryote.dao.SiteType
import com.example.ryote.repository.SiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
// import org.springframework.stereotype.Component

@Configuration
class ItineraryDbInitialDataConfiguration(
    @Autowired val siteRepository: SiteRepository
) {

    @EventListener(ContextRefreshedEvent::class)
    @SuppressWarnings("MagicNumber")
    fun site() {
        fun siteOf(
            siteType: SiteType,
            day: Int,
            ord: Int,
            name: String,
            detail: String
        ) = Site(
            id = null,
            siteType = siteType,
            day = day,
            ord = ord,
            name = name,
            detail = detail,
            startTime = null,
            endTime = null,
        )

        val sites = listOf(
            siteOf(SiteType.LANDMARK, 0, 0, "Kyoto Tower", "Good view"),
            siteOf(SiteType.LANDMARK, 0, 1, "Kyoto Station", ""),
            siteOf(SiteType.TRANSPORTATION, 0, 2, "Shinkansen", "JR"),
            siteOf(SiteType.LANDMARK, 0, 3, "Tokyo Station", "Big station."),
            siteOf(SiteType.TRANSPORTATION, 0, 4, "Yamanote-Line", "Kanjosen"),
            siteOf(SiteType.LANDMARK, 0, 5, "Jinbocho", "Many book stores"),

            siteOf(SiteType.LANDMARK, 1, 0, "Jinbocho", "Good curries"),
            siteOf(SiteType.TRANSPORTATION, 1, 1, "Toho", "About 30 min."),
            siteOf(SiteType.LANDMARK, 1, 2, "Koukyo", "Good walking place"),

            siteOf(SiteType.LANDMARK, 2, 0, "Tokyo Station.", "Buy Tokyo banana"),
            siteOf(SiteType.TRANSPORTATION, 2, 1, "Shikansen", "Reserved seat"),
            siteOf(SiteType.LANDMARK, 2, 2, "Kyoto Station", "Tadaima"),
        )
        for (site in sites) {
            siteRepository.save(site)
        }
    }
}
