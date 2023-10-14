package com.example.week5.model

data class GameUIState(
    var chances:Int = 0,
    var guessNumber:Int = 0,
    var score:Int = 0,
    var restart:Boolean = false
)
