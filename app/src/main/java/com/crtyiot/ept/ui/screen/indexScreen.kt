package com.crtyiot.ept.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crtyiot.ept.ui.viewModel.indexViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun indexScreen(viewModel: indexViewModel = hiltViewModel()) {
    val taskDH by viewModel.taskDash.collectAsState(initial = emptyList())
    var rowCounter = 0

    Surface(modifier = Modifier.fillMaxWidth()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(text = "法雷奥防错扫描-已注册任务")
                    },
                )
            },

            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.offset(x = (-45).dp, y = (-10).dp),
                    onClick = {
                        // Handle FAB click
                    }
                ) {
                    Column (verticalArrangement = Arrangement.Bottom) {
                        Icon(
                            imageVector = Icons.Filled.Create,
                            contentDescription = "注册整托扫码")
                        Text(text = "注册新托")
                    }

                }
            }

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {


                // Header
                Spacer(Modifier.height(5.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "扫描员工", modifier = Modifier.weight(1f))
                    Text(text = "原材料号", modifier = Modifier.weight(1f))
                    Text(text = "目标数量", modifier = Modifier.weight(1f))
                    Text(text = "已扫数量", modifier = Modifier.weight(1f))
                    Text(text = "任务操作", modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(4.dp))

                // Data
                LazyColumn {
                    items(taskDH) { task ->
                        val backgroundColor = if (rowCounter % 2 == 0) Color.LightGray else Color.White
                        Row(modifier = Modifier
                            .background(backgroundColor)
                            .fillMaxWidth()) {
                            Text(text = task.staff, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = task.vdaMatId, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = task.targetQty.toString(), modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = task.scannedCount.toString(), modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                                        append("查看任务")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { /* Handle navigation here */ },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        rowCounter++
                    }
                }
            }
        }
    }
}