package com.crtyiot.ept.ui

/*indexScreen按返回键应该推出到桌面
NewTaskScreenn按返回键应该推出indexScreen*/
/*indexScreen点击FloatingActionButton跳转到NewTaskScreen
indexScreen点击LazyColumn 中的buildAnnotatedString 跳转到scanScreen,需要带参数task.taskId,
这样实现跳转到不同的编译页面（通过Navi向这个页面传递taskId）
NewTaskScreen 点击navigationIcon跳转到indexScreen scanScreen
点击navigationIcon跳转到indexScree*/


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crtyiot.ept.ui.screen.NewTaskScreen
import com.crtyiot.ept.ui.screen.ScanScreen
import androidx.compose.runtime.Composable
import com.crtyiot.ept.ui.screen.indexScreen

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
        composable(Screen.IndexScreen.route) { indexScreen(navController = navController) }
        composable(Screen.NewTaskScreen.route) { NewTaskScreen(navController = navController) }
        composable("scanScreen/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            if (taskId != null) {
                ScanScreen(taskId = taskId, navController = navController)
            }
        }

    }
}

