package com.example.ryote.converter

import com.example.ryote.dao.Transportation
import com.example.ryote.dto.TransportationDto

fun Transportation.toDto() = TransportationDto(
    id = (this.id % 1_000_000_007).toInt(),
    name = this.name,
    detail = this.detail
)
