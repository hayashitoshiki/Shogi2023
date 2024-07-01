package com.example.testDomainObject.game

import com.example.domainObject.game.game.Seconds

fun Seconds.Companion.fake(
    value: Long = 0,
): Seconds {
    return setSeconds(value)
}
