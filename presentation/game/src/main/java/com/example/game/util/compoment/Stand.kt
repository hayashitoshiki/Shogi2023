package com.example.game.util.compoment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.game.R
import com.example.core.theme.Shogi2023Theme

@Composable
fun StandBox(
    modifier: Modifier = Modifier,
    stand: Stand,
    timeLimit: TimeLimit,
    turn: Turn,
    onClick: (Piece, Turn) -> Unit,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val widthPadding = 4
    val boardSizeWidth = 9
    val pieceList = listOf(
        Piece.Surface.Fu,
        Piece.Surface.Kyosya,
        Piece.Surface.Keima,
        Piece.Surface.Gin,
        Piece.Surface.Kin,
        Piece.Surface.Kaku,
        Piece.Surface.Hisya,
    ).let {
        when (turn) {
            Turn.Normal.Black -> it
            Turn.Normal.White -> it.reversed()
        }
    }

    Row(modifier = modifier.padding(horizontal = widthPadding.dp)) {
        val cellSize = (screenWidthDp - widthPadding * 2) / boardSizeWidth
        when (turn) {
            Turn.Normal.Black -> Unit
            Turn.Normal.White -> {
                TimeLimitBox(
                    modifier = Modifier
                        .height(cellSize.dp)
                        .width((cellSize * 2).dp)
                        .graphicsLayer(rotationZ = 180f),
                    timeLimit = timeLimit,
                )
            }
        }
        for (piece in pieceList) {
            Row {
                StandCellBox(
                    size = cellSize,
                    stand = stand,
                    piece = piece,
                    turn = turn,
                    onClick = {
                        if (stand.pieces.contains(piece)) {
                            onClick(piece, turn)
                        } else {
                            Unit
                        }
                    },
                )
            }
        }
        when (turn) {
            Turn.Normal.Black -> {
                TimeLimitBox(
                    modifier = Modifier
                        .height(cellSize.dp)
                        .width((cellSize * 2).dp),
                    timeLimit = timeLimit,
                )
            }
            Turn.Normal.White -> Unit
        }
    }
}

@Composable
fun StandCellBox(size: Int, stand: Stand, piece: Piece, onClick: () -> Unit, turn: Turn) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clickable(onClick = onClick),
    ) {
        Image(
            modifier = Modifier.size(size.dp),
            painter = painterResource(R.drawable.ic_backgound_board),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )

        if (stand.pieces.contains(piece)) {
            val pieceCount = stand.pieces.count { it == piece }
            val alignment = when (turn) {
                Turn.Normal.Black -> Alignment.BottomEnd
                Turn.Normal.White -> Alignment.TopStart
            }
            PieceImage(piece = piece, turn = turn)
            PieceCountImage(
                modifier = Modifier
                    .padding(4.dp)
                    .size((size / 3).dp)
                    .align(alignment),
                pieceCount = pieceCount,
                turn = turn,
            )
        }
    }
}

@Composable
fun PieceCountImage(modifier: Modifier = Modifier, pieceCount: Int, turn: Turn) {
    val countImage = when (turn) {
        Turn.Normal.Black -> when (pieceCount) {
            1 -> R.drawable.ic_piece_count_1_black
            2 -> R.drawable.ic_piece_count_2_black
            3 -> R.drawable.ic_piece_count_3_black
            4 -> R.drawable.ic_piece_count_4_black
            5 -> R.drawable.ic_piece_count_5_black
            6 -> R.drawable.ic_piece_count_6_black
            7 -> R.drawable.ic_piece_count_7_black
            8 -> R.drawable.ic_piece_count_8_black
            9 -> R.drawable.ic_piece_count_9_black
            10 -> R.drawable.ic_piece_count_10_black
            11 -> R.drawable.ic_piece_count_11_black
            12 -> R.drawable.ic_piece_count_12_black
            13 -> R.drawable.ic_piece_count_13_black
            14 -> R.drawable.ic_piece_count_14_black
            15 -> R.drawable.ic_piece_count_15_black
            16 -> R.drawable.ic_piece_count_16_black
            17 -> R.drawable.ic_piece_count_17_black
            18 -> R.drawable.ic_piece_count_18_black
            else -> null
        }

        Turn.Normal.White -> when (pieceCount) {
            1 -> R.drawable.ic_piece_count_1_white
            2 -> R.drawable.ic_piece_count_2_white
            3 -> R.drawable.ic_piece_count_3_white
            4 -> R.drawable.ic_piece_count_4_white
            5 -> R.drawable.ic_piece_count_5_white
            6 -> R.drawable.ic_piece_count_6_white
            7 -> R.drawable.ic_piece_count_7_white
            8 -> R.drawable.ic_piece_count_8_white
            9 -> R.drawable.ic_piece_count_9_white
            10 -> R.drawable.ic_piece_count_10_white
            11 -> R.drawable.ic_piece_count_11_white
            12 -> R.drawable.ic_piece_count_12_white
            13 -> R.drawable.ic_piece_count_13_white
            14 -> R.drawable.ic_piece_count_14_white
            15 -> R.drawable.ic_piece_count_15_white
            16 -> R.drawable.ic_piece_count_16_white
            17 -> R.drawable.ic_piece_count_17_white
            18 -> R.drawable.ic_piece_count_18_white
            else -> null
        }
    }
    if (countImage != null) {
        Image(
            modifier = modifier,
            painter = painterResource(countImage),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StandBoxPreview() {
    Shogi2023Theme {
        val stand = Stand()
        stand.add(Piece.Surface.Fu)
        StandBox(
            stand = stand,
            timeLimit = TimeLimit.INIT,
            turn = Turn.Normal.Black,
            onClick = { _, _ -> },
        )
    }
}
