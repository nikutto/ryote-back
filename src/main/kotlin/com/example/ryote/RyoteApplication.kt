package com.example.ryote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class RyoteApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<RyoteApplication>(*args)
}
