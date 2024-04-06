package com.crtyiot.ept

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crtyiot.ept.ui.viewModel.newTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskScreen(
    viewModel: newTaskViewModel = hiltViewModel()
) {
    // 绑定员工号数据
    val empNo by viewModel.staffid.collectAsState(initial = "")
    // 下拉框物料数据
    val matlist by viewModel.mat.collectAsState(initial = emptyList())
    matlist.map { it.vdaMatCode }
    val options = matlist.map { it.vdaMatCode }

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember {
        mutableStateOf(if (options.isNotEmpty()) options[0] else "")
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {



        Spacer(Modifier.height(8.dp))

        TextField(
            value = empNo,
            onValueChange = {(viewModel.setStaffid(it))},
            label = {
                Text(
                    text = "请录入员工号：",
                    modifier = Modifier
                )
            },
            maxLines = 1,
        )

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {

            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                label = { Text("选择客户物料代码") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),

            shape = MaterialTheme.shapes.medium,

            onClick = { viewModel.registerTask(selectedOptionText )})
        {
            Text("注册新的防错任务")
        }

    }
}
