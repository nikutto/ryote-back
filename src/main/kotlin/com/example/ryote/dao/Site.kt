package com.example.ryote.dao

import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

enum class SiteType {
    LANDMARK, TRANSPORTATION
}

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["day", "ord"])])
data class Site(
    @Id @GeneratedValue val id: Long?,
    val siteType: SiteType,
    val day: Int,
    val ord: Int,
    val name: String,
    val detail: String,
    val startTime: LocalTime?,
    val endTime: LocalTime?,
)
