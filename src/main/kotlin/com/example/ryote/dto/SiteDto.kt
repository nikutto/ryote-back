package com.example.ryote.dto

import com.example.ryote.dao.SiteType
import java.time.LocalTime

data class SiteDto(
    val id: Int,
    val siteType: SiteType,
    val day: Int,
    val ord: Int,
    val name: String,
    val detail: String,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
)
