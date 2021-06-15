package com.example.ryote.dao

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Transportation(
    @Id @GeneratedValue val id: Long,
    val day: Int,
    val name: String,
    val detail: String,
)
