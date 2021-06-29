package com.example.ryote.converter

import com.example.ryote.dao.Site
import com.example.ryote.dto.SiteDto

fun Site.toDto() = SiteDto(
    siteType = this.siteType,
    day = this.day,
    ord = this.ord,
    name = this.name,
    detail = this.detail,
    startTime = this.startTime,
    endTime = this.endTime,
)

fun SiteDto.toDao() = Site(
    id = null,
    siteType = this.siteType,
    day = this.day,
    ord = this.ord,
    name = this.name,
    detail = this.detail,
    startTime = this.startTime,
    endTime = this.endTime,
)
