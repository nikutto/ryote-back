package com.example.ryote.converter

import com.example.ryote.dao.Landmark
import com.example.ryote.dto.LandmarkDto

fun Landmark.toDto() = LandmarkDto(
  id = (this.id % 1_000_000_007).toInt(),
    name = this.name,
    detail = this.detail
  )
