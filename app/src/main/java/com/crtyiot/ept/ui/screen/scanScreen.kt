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
import com.crtyiot.ept.ui.viewModel.indexViewModel
import com.crtyiot.ept.ui.viewModel.scanViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
    val scanTaskInfo = viewModel.scanningTaskId.collectAsState(initial = "")
    var xxx = scanTaskInfo.value

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
                                append("删除")
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
fun ScanFidle(viewModel: viewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = "d",
            onValueChange = {},
            label = { Text("扫码内部CMS物料号") },
            isError = true,
            modifier = Modifier.height(46.dp),
            singleLine = true,
            readOnly = true,
        )
        TextField(
            value = "d",
            onValueChange = {},
            label = { Text("扫码客户VDA物料号") },
            isError = true,
            modifier = Modifier.height(46.dp),
            singleLine = true,
            readOnly = true
            )
        TextField(
            value = "d",
            onValueChange = {},
            label = { Text("扫码客户VDA序列号") },
            isError = true,
            singleLine = true,
            readOnly = true,
            modifier = Modifier.height(68.dp),
            supportingText = { Text("支持扫码或手动输入", modifier = Modifier.scale(0.87f)) },
        )

        BottonRow(viewModel)


    }
}

@Composable
fun BottonRow(viewModel: ScanViewModel) {
    if (scanstepindex == 0 ) {
        CmsMatField(viewModel = viewModel)
    } else if (scanstepindex == 1) {
        VdaMatField(viewModel = viewModel)
    } else if (scanstepindex == 2) {
        VdaPkgField(viewModel = viewModel)
    }

    // 按钮组，根据业务场景，常用的按钮会优先放置在右侧
    Row(
        Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
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