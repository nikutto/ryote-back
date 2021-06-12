package com.example.ryote.converter

import com.example.ryote.dao.Transportation
import com.example.ryote.dto.TransportationDto

fun Transportation.toDto() = TransportationDto(
    id = this.id,
    name = this.name,
    detail = this.detail
  )
