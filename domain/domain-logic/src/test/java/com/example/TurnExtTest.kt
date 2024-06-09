package com.example

import com.example.domainLogic.rule.changeNextTurn
import com.example.domainLogic.rule.getOpponentTurn
import com.example.domainObject.game.rule.Turn
import org.junit.Assert
import org.junit.Test

/**
 * 手番に関するドメインロジック
 *
 */
class TurnExtTest {

    @Test
    fun `手番変更`() {
        data class Param(
            val case: Turn,
            val result: Turn,
        )

        val params = listOf(
            Param(
                case = Turn.Normal.Black,
                result = Turn.Normal.White,
            ),
            Param(
                case = Turn.Normal.Black,
                result = Turn.Normal.White,
            ),
        )

        params.forEach {
            val expected = it.case.changeNextTurn()
            Assert.assertEquals(expected, it.result)
        }
    }

    @Test
    fun `相手の手番取得`() {
        data class Param(
            val case: Turn,
            val result: Turn,
        )

        val params = listOf(
            Param(
                case = Turn.Normal.Black,
                result = Turn.Normal.White,
            ),
            Param(
                case = Turn.Normal.Black,
                result = Turn.Normal.White,
            ),
        )

        params.forEach {
            val expected = it.case.getOpponentTurn()
            Assert.assertEquals(expected, it.result)
        }
    }
}