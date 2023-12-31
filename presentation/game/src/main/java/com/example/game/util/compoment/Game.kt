package com.example.game.util.compoment

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn

@Composable
fun GameBox(
    board: Board,
    whiteStand: Stand,
    blackStand: Stand,
    onStandClick: (Piece, Turn) -> Unit,
    onBoardClick: (Position) -> Unit,
    hintList: List<Position>,
) {
    Column {
        StandBox(
            stand = whiteStand,
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
            turn = Turn.Normal.Black,
            onClick = onStandClick,
        )
    }
}
