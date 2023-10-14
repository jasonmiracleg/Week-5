package com.example.week5.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week5.R
import com.example.week5.model.StudentCourse
import com.example.week5.model.StudentInfo
import com.example.week5.viewmodel.StudentCourseViewModel
import java.text.DecimalFormat

@Composable
fun LayoutStudentCourses(studentCourseViewModel: StudentCourseViewModel = StudentCourseViewModel()) {
    val studentCourseUIState by studentCourseViewModel.data.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            PersonalInfo(studentCourseUIState)
        }
        item {
            InputSKS(studentCourseViewModel)
        }
        items(studentCourseUIState.courses.size) {
            DisplaySKS(studentCourseUIState.courses[it], studentCourseViewModel)
        }
        if (studentCourseUIState.courses.isEmpty())
            item {
                Text(
                    text = "No Course Data Available",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF495D91)
                )
            }

    }
}

@Composable
fun PersonalInfo(studentCourseUIState: StudentInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "Courses", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "Total SKS : ${studentCourseUIState.totalSKS}")
        Text(text = "IPK : ${DecimalFormat("0.00").format(studentCourseUIState.totalIPK)}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSKS(studentCourseViewModel: StudentCourseViewModel) {
    var SKSamount by rememberSaveable { mutableStateOf("") }
    var courseName by rememberSaveable { mutableStateOf("") }
    var score by rememberSaveable { mutableStateOf("") }
    var valid by rememberSaveable { mutableStateOf(true) }
    var checkInput by rememberSaveable { mutableStateOf(true) }
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = SKSamount,
                onValueChange = { SKSamount = it },
                label = { Text(text = "SKS") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFF495D91)
                ),
                modifier = Modifier
                    .weight(1f),
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = !valid
            )
            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text(text = "Score") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFF495D91)
                ),
                modifier = Modifier
                    .weight(1f),
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                isError = !valid.and(checkInput)
            )
        }
        if (!valid) {
            Text(
                text = "Please enter the valid number",
                color = Color.Red
            )
        } else if (!checkInput) {
            Text(
                text = "Please enter the number in range 0.00 - 4.00",
                color = Color.Red
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = courseName,
                onValueChange = { courseName = it },
                label = { Text(text = "Course") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color(0xFF495D91)
                ),
                keyboardOptions = KeyboardOptions().copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )
            Button(
                onClick = {
                    valid = studentCourseViewModel.checkInput(SKSamount)
                        .and(studentCourseViewModel.checkInput(score))
                    if (valid) {
                        checkInput = studentCourseViewModel.checkIPK(score.toDouble())
                        if (checkInput) {
                            studentCourseViewModel.addCourse(
                                courseName,
                                score.toDouble(),
                                SKSamount.toInt()
                            )
                            studentCourseViewModel.calculateSKS()
                            studentCourseViewModel.calculateIPK()
                        }
                    }

                }, modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                enabled = SKSamount.isNotBlank() && score.isNotBlank() && courseName.isNotBlank()
            ) {
                Text(text = "+", fontSize = 28.sp)
            }
        }
    }
}

@Composable
fun DisplaySKS(studentCourse: StudentCourse, studentCourseViewModel: StudentCourseViewModel) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Name : ${studentCourse.courseName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(text = "SKS : ${studentCourse.SKS}", fontSize = 16.sp)
                    Text(
                        text = "Score : ${DecimalFormat("0.00").format(studentCourse.score)}",
                        fontSize = 16.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            studentCourseViewModel.deleteCourse(studentCourse)
                            studentCourseViewModel.calculateSKS()
                            studentCourseViewModel.calculateIPK()
                        },
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_delete_24),
                        contentDescription = "Delete Course",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewStudentCourses() {
    LayoutStudentCourses()
}