package com.example.week5.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week5.model.StudentCourse
import com.example.week5.model.StudentInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern

class StudentCourseViewModel : ViewModel() {
    private val currentData = MutableStateFlow(StudentInfo())
    val data: StateFlow<StudentInfo> = currentData.asStateFlow()

    fun addCourse(name: String, score: Double, sks: Int) {
        val courseList = currentData.value.courses
        courseList.add(StudentCourse(courseName = name, score = score, SKS = sks))
        currentData.update { currentState ->
            currentState.copy(courses = courseList)
        }
    }

    fun deleteCourse(course: StudentCourse) {
        val courseList = currentData.value.courses
        courseList.remove(course)
        currentData.update { currentState ->
            currentState.copy(
                courses = courseList
            )
        }
    }

    fun calculateSKS() {
        val courseList = currentData.value.courses
        currentData.update { currentState ->
            currentState.copy(
                totalSKS = courseList.sumOf { studentCourse: StudentCourse -> studentCourse.SKS }
            )
        }
    }

    fun calculateIPK() {
        if (currentData.value.courses.size != 0) {
            val courseList = currentData.value.courses
            var totalScore = 0.00
            for (course in courseList) {
                totalScore += course.SKS * course.score
            }
            currentData.update {
                it.copy(
                    totalIPK = totalScore / currentData.value.totalSKS
                )
            }
        } else {
            currentData.update {
                it.copy(
                    totalIPK = 0.00
                )
            }
        }
    }

    fun checkInput(input: String): Boolean {
        val numberFormat = Pattern.compile(".*\\d+.*")
        return numberFormat.matcher(input).matches()
    }

    fun checkIPK(ipk: Double):Boolean {
        if (ipk < 0.00 || ipk > 4.00) return false
        return true
    }
}