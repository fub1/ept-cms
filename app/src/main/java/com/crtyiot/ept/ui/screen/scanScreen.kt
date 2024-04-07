package com.crtyiot.ept.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crtyiot.ept.ui.Screen

import com.crtyiot.ept.ui.viewModel.scanViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.crtyiot.ept.data.model.ScanData
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    taskId: String,
    navController: NavHostController,
    viewModel: scanViewModel = hiltViewModel()

) {
    // 回跳首页
    BackHandler {
        navController.navigate(Screen.IndexScreen.route)
    }
    // LaunchedEffect 将ID传递给ViewModel
    LaunchedEffect(taskId) {
        viewModel.setTaskId(taskId)
    }

    // 加载已扫数据
    val scanedData = viewModel.scanTaskList.collectAsState(initial = emptyList())


    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(30.dp),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.IndexScreen.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "防错扫码") }
            )
        }
    ) {
        // ，TopAppBar 高度56dp，所以这里加一个56dp的Spacer
        Column {
            Spacer(Modifier.height(31.dp))

            ScanFidle(viewModel)

            ScanedTable(scanedData = scanedData.value)
        }
    }
}


// LazyColumn 用来显示列表数据scanedData
@Composable
fun ScanedTable(scanedData: List<ScanData>) {

    var rowCounter = 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "内部料号", modifier = Modifier.weight(1f))
            Text(text = "客户料号", modifier = Modifier.weight(1f))
            Text(text = "包装序号", modifier = Modifier.weight(1f))
            Text(text = "任务操作", modifier = Modifier.weight(1f))
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.45f)

        ) {
            items(scanedData) { item ->
                val backgroundColor = if (rowCounter % 2 == 0) Color.LightGray else Color.White
                Row(
                    modifier = Modifier
                        .background(backgroundColor)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = item.cmsMatCode,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.vdaMatCode,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.vdaSerialCode,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                append("预留")
                            }
                        },
                        modifier = Modifier
                            .weight(.5f)
                            .clickable { /*TODO*/ },
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(8.dp))
                rowCounter++
            }
        }
    }
}

@Composable
fun ScanFidle(
    viewModel: scanViewModel,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {}
) {
    //错误信息，从ViewModel中获取
    val errMessage by viewModel.errorMsg.collectAsState(initial = "")
    //cms\vda\序列号错误信息，从ViewModel中获取
    val cmsError by viewModel.cmsCodeError.collectAsState(initial = false)
    val vdaError by viewModel.vdaCodeError.collectAsState(initial = false)
    val serialError by viewModel.serialCodeError.collectAsState(initial = false)
    // 输入框数据
    val cmsMatCode by viewModel.cmsMat.collectAsState(initial = "")
    val vdaMatCode by viewModel.vdaMat.collectAsState(initial = "")
    val vdaSerialCode by viewModel.vdaSerialCode.collectAsState(initial = "")


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (cmsError) {
            TextField(
                value = cmsMatCode,
                enabled = false,
                onValueChange = {},
                label = { Text("扫码内部CMS物料号") },
                isError = true,
                modifier = Modifier.height(46.dp),
                singleLine = true,
                readOnly = true,
            )
        } else {
            TextField(
                value = cmsMatCode,
                enabled = false,
                onValueChange = {},
                label = { Text("扫码内部CMS物料号") },
                modifier = Modifier.height(46.dp),
                singleLine = true,
                readOnly = true,
            )
        }

        if (vdaError) {
            TextField(
                value = vdaMatCode,
                enabled = false,
                onValueChange = {},
                label = { Text("扫码客户VDA物料号") },
                isError = true,
                modifier = Modifier.height(46.dp),
                singleLine = true,
                readOnly = true
            )
        } else {
            TextField(
                value = vdaMatCode,
                enabled = false,
                onValueChange = {},
                label = { Text("扫码客户VDA物料号") },
                modifier = Modifier.height(46.dp),
                singleLine = true,
                readOnly = true,
            )
        }

        if (serialError) {
            TextField(
                value = vdaSerialCode,
                enabled = false,
                onValueChange = {},
                label = { Text("扫码客户VDA序列号") },
                isError = true,
                singleLine = true,
                readOnly = true,
                modifier = Modifier.height(46.dp),
            )
        } else {
            TextField(
                value = vdaSerialCode,
                enabled = false,
                onValueChange = {},
                label = { Text("扫码客户VDA序列号") },
                singleLine = true,
                readOnly = true,
                modifier = Modifier.height(46.dp),

            )


        // 下面扫码sideEffect

            val currentOnStart by rememberUpdatedState(onStart)
            val currentOnStop by rememberUpdatedState(onStop)
            // 广播使用的上下文
            val context = LocalContext.current



            // If `lifecycleOwner` changes, dispose and reset the effect
            DisposableEffect(lifecycleOwner) {
                // 广播
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        Log.i("Bc", "onReceive")
                        val scanData = intent.getStringExtra("eee")
                        scanData?.let { viewModel.getBCScanData(it) }
                    }
                }
                // Create an observer that triggers our remembered callbacks
                // for sending analytics events
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        Log.i("MainScreen", "ON_START")
                        currentOnStart()
                        context.registerReceiver(receiver, IntentFilter("com.v.b"))
                    } else if (event == Lifecycle.Event.ON_STOP) {
                        Log.i("MainScreen", "ON_STOP")
                        try {
                            context.unregisterReceiver(receiver)
                        } catch (e: IllegalArgumentException) {
                            Log.e("MainScreen-when stop", "unregisterReceiver error")
                        }
                        currentOnStop()
                    }
                }

                // Add the observer to the lifecycle
                lifecycleOwner.lifecycle.addObserver(observer)

                // When the effect leaves the Composition, remove the observer
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                    try {
                        context.unregisterReceiver(receiver)
                    } catch (e: IllegalArgumentException) {
                        Log.e("MainScreen-when Dispose", "unregisterReceiver error")
                    }
                    Log.i("MainScreen", "onDispose")
                }
            }

        }




        if (errMessage != ""){
            Text(text = errMessage, color = Color.Red, modifier = Modifier.scale(0.77f))
        }


        BottonRow(viewModel)


    }
}

@Composable
fun BottonRow(viewModel: scanViewModel) {

    val scanstepindex by viewModel.scanstepindex.collectAsState(initial = 0)



    // 按钮组，根据业务场景，常用的按钮会优先放置在右侧
    Row(
        Modifier
            .fillMaxWidth()
            //.align(Alignment.CenterHorizontally)
        ,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (scanstepindex == 3) {
            Button(
                onClick = { viewModel.submitScanData() },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                Text("提交")
            }

            Button(
                onClick = { viewModel.resetScanData() },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                Text("重置")
            }

            Button(
                onClick = { viewModel.backScanData() },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                Text("回退")
            }


        }

        if (scanstepindex < 3) {

            Button(
                onClick = { viewModel.resetScanData() },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                Text("重置本组数据")
            }

            Button(
                onClick = { viewModel.backScanData() },
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                Text("重扫当前步骤")
            }
        }
    }

}