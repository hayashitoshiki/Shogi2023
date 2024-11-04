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
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.core.component.BaseScreen
import com.example.home.compoment.card.CustomShogiSettingCard
import com.example.home.compoment.card.FirstCheckShogiSettingCard
import com.example.home.compoment.card.NormalShogiSettingCard
import com.example.home.compoment.card.TimeLimitSettingCard
import com.example.home.model.GameRuleSettingUiModel
import com.example.core.navigation.NavigationScreens
import com.example.core.theme.Shogi2023Theme
import kotlinx.coroutines.flow.Flow

object HomeScreen: BaseScreen<HomeViewModel, HomeViewModel.UiState, HomeViewModel.Effect, HomeViewModel.Action>() {
    @Composable
    override fun Effect(
        navController: NavHostController,
        effect: Flow<HomeViewModel.Effect>,
        uiState: State<HomeViewModel.UiState>,
        action: (HomeViewModel.Action) -> Unit,
    ) {
        LaunchedEffect(true) {
            effect.collect {
                when (it) {
                    is HomeViewModel.Effect.GameStart -> {
                        navController.navigate(NavigationScreens.GAME_SCREEN.route) {
                            popUpTo(NavigationScreens.HOME_SCREEN.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun View(
        modifier: Modifier,
        uiState: State<HomeViewModel.UiState>,
        action: (HomeViewModel.Action) -> Unit
    ) {
        val tabs: List<@Composable () -> Unit> = uiState.value.ruleItems.map {
            return@map when (it) {
                is GameRuleSettingUiModel.NonCustom.FirstCheck -> {
                    {
                        FirstCheckShogiSettingCard(
                            selected = it.selectedHande,
                            onChange = { action(HomeViewModel.Action.ClickPieceHandeButtonByFirstCheckItem(it)) },
                        )
                    }
                }

                is GameRuleSettingUiModel.NonCustom.Normal -> {
                    {
                        NormalShogiSettingCard(
                            selected = it.selectedHande,
                            onChange = { action(HomeViewModel.Action.ClickPieceHandeButtonByNormalItem(it)) },
                        )
                    }
                }

                is GameRuleSettingUiModel.Custom -> {
                    {
                        CustomShogiSettingCard(
                            custom = it,
                            onChangeFirstCheck = { turn, isFirstCheck ->
                                action(HomeViewModel.Action.ClickFirstCheckButton(turn, isFirstCheck))
                            },
                            onChangeHande = {
                                action(HomeViewModel.Action.ClickPieceHandeButtonByCustomItem(it))
                            },
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
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    TimeLimitSettingCard(
                        uiModel = uiState.value.timeLimitCard,
                        onChangeTimeLimitTotalTime = { turn, second ->
                            action(HomeViewModel.Action.SelectTimeLimitTotalTimeDropdown(turn, second))
                        },
                        onChangeTimeLimitSecond = { turn, second ->
                            action(HomeViewModel.Action.SelectTimeLimitSecondDropdown(turn, second))
                        },
                    )
                }

                Spacer(modifier = Modifier.size(64.dp))
                RuleSettingPager(
                    tabs = tabs,
                    changePage = { action(HomeViewModel.Action.ScrollRuleSettingCards(it)) },
                )
                Spacer(modifier = Modifier.size(16.dp))
                ElevatedButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { action(HomeViewModel.Action.ClickGameStartButton) },
                ) {
                    Text(text = stringResource(R.string.home_game_start_button))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RuleSettingPager(
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
        HomeScreen.Screen(
            navController = rememberNavController(),
            viewModel = hiltViewModel(),
        )
    }
}
