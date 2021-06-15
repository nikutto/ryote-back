package com.example.ryote.dao

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Landmark(
    @Id @GeneratedValue val id: Long = 0,
    val day: Int,
    val name: String,
    val detail: String,
)
