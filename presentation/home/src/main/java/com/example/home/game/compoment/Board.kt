package com.example.home.game.compoment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.entity.game.board.Board
import com.example.entity.game.board.Cell
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.home.R
import com.example.shogi2023.ui.theme.Shogi2023Theme

@Composable
fun BoardBox(modifier: Modifier = Modifier, board: Board, onClick: () -> Unit) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val widthPadding = 4
    val boardSizeWidth = board.size.row
    val boardSizeHeight = board.size.column

    Column(modifier = modifier.padding(horizontal = widthPadding.dp)) {
        for (column in 1..boardSizeHeight) {
            Row {
                for (row in boardSizeWidth downTo 1) {
                    val cellSize = (screenWidthDp - widthPadding * 2) / boardSizeWidth
                    val cell = board.getCellByPosition(Position(row, column))
                    CellBox(
                        size = cellSize,
                        cell = cell,
                        onClick = onClick,
                    )
                }
            }
        }
    }
}

@Composable
fun CellBox(size: Int, cell: Cell, onClick: () -> Unit) {
    val borderStroke = BorderStroke(0.5.dp, Color.Black)

    Box(
        modifier = Modifier
            .size(size.dp)
            .border(borderStroke)
            .clickable(onClick = onClick),
    ) {
        Image(
            modifier = Modifier.size(size.dp),
            painter = painterResource(R.drawable.ic_backgound_board),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        when (val piece = cell.getStatus()) {
            CellStatus.Empty -> Unit
            is CellStatus.Fill.FromPiece -> PieceImage(piece = piece.piece, turn = piece.turn)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardBoxPreview() {
    Shogi2023Theme {
        BoardBox(
            board = Board(),
            onClick = { },
        )
    }
}
