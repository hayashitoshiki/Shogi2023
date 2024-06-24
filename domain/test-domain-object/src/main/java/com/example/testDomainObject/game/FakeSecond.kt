package com.example.testDomainObject.game

import com.example.domainObject.game.game.Second

fun Second.Companion.fake(
    value: Long = 0,
) : Second {
    return Second(value)
}