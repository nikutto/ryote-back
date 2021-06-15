package com.example.ryote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RyoteApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<RyoteApplication>(*args)
}
