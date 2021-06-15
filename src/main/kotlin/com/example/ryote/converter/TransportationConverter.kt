package com.example.ryote.converter

import com.example.ryote.dao.Transportation
import com.example.ryote.dto.TransportationDto

private const val MOD = 1_000_000_007
fun Transportation.toDto() = TransportationDto(
    id = (this.id % MOD).toInt(),
    name = this.name,
    detail = this.detail
)
