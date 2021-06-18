package com.example.ryote.converter

import com.example.ryote.dao.Site
import com.example.ryote.dto.SiteDto

private const val MOD = 1_000_000_007
fun Site.toDto() = SiteDto(
    id = (this.id!! % MOD).toInt(),
    siteType = this.siteType,
    day = this.day,
    ord = this.ord,
    name = this.name,
    detail = this.detail,
    startTime = this.startTime,
    endTime = this.endTime,
)
