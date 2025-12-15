package com.mjperezm.v3_fitlife.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mjperezm.v3_fitlife.ui.screens.*
import com.mjperezm.v3_fitlife.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Progress : Screen("progress")
    object PlanList : Screen("plan_list")
    object PlanDetail : Screen("plan_detail/{planId}") {
        fun createRoute(planId: String) = "plan_detail/$planId"
    }
    object NutritionDetail : Screen("nutrition_detail/{planId}") {
        fun createRoute(planId: String) = "nutrition_detail/$planId"
    }
    // ✨ Nuevas pantallas
    object Appointments : Screen("appointments")
    object BookAppointment : Screen("book_appointment")
    object EditProfile : Screen("edit_profile")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()

    val startDestination = if (authState.isAuthenticated) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ==================== AUTENTICACIÓN ====================
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                viewModel = authViewModel
            )
        }

        // ==================== HOME ====================
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToProgress = {
                    navController.navigate(Screen.Progress.route)
                },
                onNavigateToPlans = {
                    navController.navigate(Screen.PlanList.route)
                },
                onNavigateToAppointments = {
                    navController.navigate(Screen.Appointments.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }

        // ==================== PERFIL ====================
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEdit = {
                    navController.navigate(Screen.EditProfile.route)
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== PROGRESO ====================
        composable(Screen.Progress.route) {
            ProgressScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== PLANES ====================
        composable(Screen.PlanList.route) {
            PlanListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPlanSelected = { planId ->
                    navController.navigate(Screen.PlanDetail.createRoute(planId))
                }
            )
        }

        composable(
            route = Screen.PlanDetail.route,
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            PlanDetailScreen(
                planId = planId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToNutrition = { nutritionId ->
                    navController.navigate(Screen.NutritionDetail.createRoute(nutritionId))
                }
            )
        }

        composable(
            route = Screen.NutritionDetail.route,
            arguments = listOf(
                navArgument("planId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val planId = backStackEntry.arguments?.getString("planId") ?: ""
            NutritionDetailScreen(
                planId = planId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== CITAS ====================
        composable(Screen.Appointments.route) {
            AppointmentsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToBook = {
                    navController.navigate(Screen.BookAppointment.route)
                }
            )
        }

        composable(Screen.BookAppointment.route) {
            BookAppointmentScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onBookingSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // ==================== CONFIGURACIÓN ====================
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                authViewModel = authViewModel
            )
        }
    }
}