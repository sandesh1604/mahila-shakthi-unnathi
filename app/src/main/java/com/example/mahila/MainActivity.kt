package com.example.mahila

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.mahila.ui.screens.*
import com.example.mahila.ui.theme.MahilaTheme
import com.example.mahila.ui.viewmodel.MahilaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MahilaTheme {
                val mahilaViewModel: MahilaViewModel = viewModel()
                val rootNavController = rememberNavController()

                val logoutAction = {
                    mahilaViewModel.logout()
                    rootNavController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }

                NavHost(navController = rootNavController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(onGetStartedClick = { 
                            if (mahilaViewModel.isLoggedIn) {
                                rootNavController.navigate("main") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            } else {
                                rootNavController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        })
                    }
                    composable("register") {
                        RegistrationScreen(
                            viewModel = mahilaViewModel,
                            onRegistrationSuccess = { shg, leader, mobile, password ->
                                mahilaViewModel.register(shg, leader, mobile, password)
                                rootNavController.navigate("main") {
                                    popUpTo("register") { inclusive = true }
                                }
                            },
                            onBackToLogin = {
                                rootNavController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            showSignUp = true,
                            onLoginAttempt = { mobile, password ->
                                mahilaViewModel.verifyLogin(mobile, password)
                            },
                            onLoginSuccess = { 
                                rootNavController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onSignUpClick = { 
                                rootNavController.navigate("register")
                            }
                        )
                    }
                    composable("main") {
                        MainContainer(
                            viewModel = mahilaViewModel,
                            onInterestCalcClick = { rootNavController.navigate("interest_calc") },
                            onReportsClick = { rootNavController.navigate("reports") },
                            onLogout = logoutAction
                        )
                    }
                    composable("interest_calc") {
                        InterestCalculatorScreen(onBackClick = { rootNavController.popBackStack() })
                    }
                    composable("reports") {
                        ReportsScreen(
                            viewModel = mahilaViewModel, 
                            onBackClick = { rootNavController.popBackStack() },
                            onLogout = logoutAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainContainer(
    viewModel: MahilaViewModel,
    onInterestCalcClick: () -> Unit,
    onReportsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        Screen.Dashboard,
        Screen.Members,
        Screen.Savings,
        Screen.Loans,
        Screen.More
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            bottomNavController.navigate(screen.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { 
                DashboardScreen(
                    viewModel = viewModel,
                    onAddSavingsClick = { bottomNavController.navigate(Screen.Savings.route) },
                    onInterestCalcClick = onInterestCalcClick
                ) 
            }
            composable(Screen.Members.route) { 
                MemberDirectoryScreen(
                    viewModel = viewModel,
                    onMemberClick = { memberName ->
                        bottomNavController.navigate("member_details/$memberName")
                    }
                ) 
            }
            composable(Screen.Savings.route) { 
                SavingsEntryScreen(
                    viewModel = viewModel,
                    onBackClick = { bottomNavController.popBackStack() }
                ) 
            }
            composable(Screen.Loans.route) { 
                LoanEntryScreen(
                    viewModel = viewModel,
                    onBackClick = { bottomNavController.popBackStack() }
                ) 
            }
            composable(Screen.More.route) { 
                ReportsScreen(
                    viewModel = viewModel,
                    onBackClick = { /* Tab root */ },
                    onLogout = onLogout
                )
            }
            composable("member_details/{memberName}") { backStackEntry ->
                val memberName = backStackEntry.arguments?.getString("memberName") ?: ""
                MemberDetailsScreen(
                    memberName = memberName,
                    viewModel = viewModel,
                    onBackClick = { bottomNavController.popBackStack() }
                )
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Members : Screen("members", "Members", Icons.Default.Group)
    object Savings : Screen("savings", "Savings", Icons.Default.Payments)
    object Loans : Screen("loans", "Loans", Icons.Default.AccountBalance)
    object More : Screen("more", "More", Icons.Default.MoreHoriz)
}
