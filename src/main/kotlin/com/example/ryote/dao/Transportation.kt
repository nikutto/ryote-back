package com.example.ryote.dao

import javax.persistence.*

@Entity
data class Transportation(
  @Id @GeneratedValue val id: Long,
  val day: Int,
  val name: String,
  val detail: String,
)