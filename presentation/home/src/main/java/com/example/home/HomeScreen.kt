package com.example.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.NavigationScreens
import com.example.core.theme.Shogi2023Theme
import com.example.home.compoment.card.FirstCheckShogiSettingCard
import com.example.home.compoment.card.NormalShogiSettingCard
import com.example.home.model.GameRuleSettingUiModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val gameRule = viewModel.uiState.collectAsState()
    val navigateGameScreen = remember { mutableStateOf<HomeViewModel.Effect.GameStart?>(null) }
    LaunchedEffect(true) {
        viewModel.gameStartEffect.collect { navigateGameScreen.value = it }
    }
    navigateGameScreen.value?.apply {
        navController.navigate(NavigationScreens.GAME_SCREEN.route) {
            popUpTo(NavigationScreens.HOMET_SCREEN.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val tabs: List<@Composable () -> Unit> = gameRule.value.ruleItems.map {
        return@map when (it) {
            is GameRuleSettingUiModel.FirstCheck -> {
                {
                    FirstCheckShogiSettingCard(
                        selected = it.selectedHande,
                        onChange = viewModel::changePieceHandeByFirstCheckItem,
                    )
                }
            }

            is GameRuleSettingUiModel.Normal -> {
                {
                    NormalShogiSettingCard(
                        selected = it.selectedHande,
                        onChange = viewModel::changePieceHandeByNormalItem,
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            RuleSettingPager(
                tabs = tabs,
                changePage = viewModel::changePage,
            )
            Spacer(modifier = Modifier.size(16.dp))
            ElevatedButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = viewModel::onGameStartClick
            ) {
                Text(text = stringResource(R.string.home_game_start_button))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RuleSettingPager(
    tabs: List<@Composable () -> Unit>,
    changePage: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    LaunchedEffect(pagerState.currentPage) {
        changePage(pagerState.currentPage)
    }

    Column {
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TabsContent(tabs: List<@Composable () -> Unit>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            tabs[page]()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HandeSettingBoxPreview() {
    Shogi2023Theme {
        HomeScreen(
            navController = rememberNavController(),
            viewModel = hiltViewModel(),
        )
    }
}
