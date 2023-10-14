package com.example.week5.model

data class StudentInfo(
    var totalSKS: Int = 0,
    var totalIPK: Double = 0.00,
    val courses: MutableList<StudentCourse> = mutableListOf()
)
