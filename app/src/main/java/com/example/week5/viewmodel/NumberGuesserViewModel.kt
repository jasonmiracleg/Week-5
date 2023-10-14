package com.example.week5.viewmodel

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.week5.model.GameUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.regex.Pattern
import kotlin.random.Random

class NumberGuesserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    init {
        generateNumberGuess()
    }

    private fun generateNumberGuess() {
        val guessNumber = Random.nextInt(1, 10)
        _uiState.update { currentState ->
            currentState.copy(guessNumber = guessNumber)
        }
    }

    fun checkInput(input: String): Boolean {
        val numberFormat = Pattern.compile(".*\\d+.*")
        return numberFormat.matcher(input).matches()
//        return input.isDigitsOnly()
    }

    fun checkGuess(number: Int) {
        if (_uiState.value.score != 3 && _uiState.value.chances != 3) {
            if (number == _uiState.value.guessNumber) {
                _uiState.update { currentState ->
                    currentState.copy(score = _uiState.value.score + 1)
                }
                generateNumberGuess()
            }
            _uiState.update { currentState ->
                currentState.copy(chances = _uiState.value.chances + 1)
            }
        }
    }

    fun checkGame(): Boolean {
        if (_uiState.value.score == 3 || _uiState.value.chances == 3) {
            return true
        }
        return false
    }

    fun resetGame() {
        generateNumberGuess()
        _uiState.update { currentState ->
            currentState.copy(score = 0)
        }
        _uiState.update { currentState ->
            currentState.copy(chances = 0)
        }
    }
}