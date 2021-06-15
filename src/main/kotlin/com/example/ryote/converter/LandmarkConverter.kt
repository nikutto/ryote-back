package com.example.ryote.converter

import com.example.ryote.dao.Landmark
import com.example.ryote.dto.LandmarkDto

private const val MOD = 1_000_000_007
fun Landmark.toDto() = LandmarkDto(
    id = (this.id % MOD).toInt(),
    day = this.day,
    name = this.name,
    detail = this.detail
)
