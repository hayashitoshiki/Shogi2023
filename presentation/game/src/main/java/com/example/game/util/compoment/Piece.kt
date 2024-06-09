package com.example.game.util.compoment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.core.theme.Shogi2023Theme
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.game.util.extension.getIcon

@Composable
fun PieceImage(modifier: Modifier = Modifier, piece: Piece, turn: Turn) {
    val pieceImage = piece.getIcon(turn)

    Image(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize(),
        painter = painterResource(pieceImage),
        contentDescription = "",
    )
}

@Preview(showBackground = true)
@Composable
fun PieceImagePreview(
    @PreviewParameter(PieceProvider::class) param: Pair<Piece, Turn>
) {
    Shogi2023Theme {
        PieceImage(
            modifier = Modifier.size(50.dp),
            piece = param.first,
            turn = param.second
        )
    }
}


class PieceProvider : PreviewParameterProvider<Pair<Piece, Turn>> {
    override val values = sequenceOf(
        Piece.Surface.Fu to Turn.Normal.Black,
        Piece.Surface.Kyosya to Turn.Normal.Black,
        Piece.Surface.Keima to Turn.Normal.Black,
        Piece.Surface.Gyoku to Turn.Normal.Black,
        Piece.Surface.Kin to Turn.Normal.Black,
        Piece.Surface.Kaku to Turn.Normal.Black,
        Piece.Surface.Hisya to Turn.Normal.Black,
        Piece.Surface.Gyoku to Turn.Normal.Black,
        Piece.Reverse.To to Turn.Normal.Black,
        Piece.Reverse.Narikyo to Turn.Normal.Black,
        Piece.Reverse.Narikei to Turn.Normal.Black,
        Piece.Reverse.Narigin to Turn.Normal.Black,
        Piece.Reverse.Uma to Turn.Normal.Black,
        Piece.Reverse.Ryu to Turn.Normal.Black,
        Piece.Surface.Fu to Turn.Normal.White,
        Piece.Surface.Kyosya to Turn.Normal.White,
        Piece.Surface.Keima to Turn.Normal.White,
        Piece.Surface.Ou to Turn.Normal.White,
        Piece.Surface.Kin to Turn.Normal.White,
        Piece.Surface.Kaku to Turn.Normal.White,
        Piece.Surface.Hisya to Turn.Normal.White,
        Piece.Surface.Gyoku to Turn.Normal.White,
        Piece.Reverse.To to Turn.Normal.White,
        Piece.Reverse.Narikyo to Turn.Normal.White,
        Piece.Reverse.Narikei to Turn.Normal.White,
        Piece.Reverse.Narigin to Turn.Normal.White,
        Piece.Reverse.Uma to Turn.Normal.White,
        Piece.Reverse.Ryu to Turn.Normal.White,
    )
}

