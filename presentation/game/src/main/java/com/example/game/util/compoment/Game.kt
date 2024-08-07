package com.example.game.util.compoment

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn

@Composable
fun GameBox(
    board: Board,
    whiteStand: Stand,
    blackStand: Stand,
    blackTimeLimit: TimeLimit,
    whiteTimeLimit: TimeLimit,
    onStandClick: (Piece, Turn) -> Unit,
    onBoardClick: (Position) -> Unit,
    hintList: List<Position>,
) {
    Column {
        StandBox(
            stand = whiteStand,
            timeLimit = whiteTimeLimit,
            turn = Turn.Normal.White,
            onClick = onStandClick,
        )
        BoardBox(
            board = board,
            onClick = onBoardClick,
            hintList = hintList,
        )
        StandBox(
            stand = blackStand,
            timeLimit = blackTimeLimit,
            turn = Turn.Normal.Black,
            onClick = onStandClick,
        )
    }
}
