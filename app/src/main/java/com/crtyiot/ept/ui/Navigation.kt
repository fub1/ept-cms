/*
* indexScreen点击FloatingActionButton跳转到NewTaskScreen
* indexScreen点击LazyColumn 中的buildAnnotatedString 跳转到scanScreen,需要带参数task.taskId,这样实现跳转到不同的编译页面
* NewTaskScreen 点击navigationIcon跳转到indexScreen
* scanScreen 点击navigationIcon跳转到indexScreen
 */

package com.crtyiot.ept.ui

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crtyiot.ept.ui.screen.IndexScreen
import com.crtyiot.ept.ui.screen.NewTaskScreen
import com.crtyiot.ept.ui.screen.ScanScreen
import androidx.compose.runtime.Composable

sealed class Screen(val route: String) {
    object IndexScreen : Screen("indexScreen")
    object NewTaskScreen : Screen("newTaskScreen")
    data class ScanScreen(val taskId: String) : Screen("scanScreen/{taskId}") {
        fun withTaskId(taskId: String) = "scanScreen/$taskId"
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.IndexScreen.route) {
        composable(Screen.IndexScreen.route) { IndexScreen(navController) }
        composable(Screen.NewTaskScreen.route) { NewTaskScreen(navController) }
        composable(Screen.ScanScreen.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            if (taskId != null) {
                ScanScreen(taskId, navController)
            }
        }
    }
}