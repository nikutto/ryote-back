package com.example.ryote.dao

import javax.persistence.*

@Entity
data class Landmark(
  @Id @GeneratedValue val id: Long = 0,
  val day: Int,
  val name: String,
  val detail: String,
)
