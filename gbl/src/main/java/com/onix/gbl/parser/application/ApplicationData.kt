package com.onix.gbl.parser.application

data class ApplicationData(
    val type: UInt,
    val version: UInt,
    val capabilities: UInt,
    val productId: UByte
)
