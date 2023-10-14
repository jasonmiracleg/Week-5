package com.example.week5.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week5.viewmodel.NumberGuesserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutNumberGuesser(
    numberGuesserViewModel: NumberGuesserViewModel // Calling the view model - using the function logic
) {
    val numberGuesserUIState by numberGuesserViewModel.uiState.collectAsState() // Calling the model - retrieving the model's attributee
    var input by rememberSaveable {
        mutableStateOf("")
    }
    var valid by rememberSaveable {
        mutableStateOf(true)
    }
    var isDialogActive by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Guess The Number", fontSize = 24.sp)
        Card(
            colors = CardDefaults.cardColors(Color(0xFFE2E0EB)),
            modifier = Modifier.padding(24.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Number of Guesses: ${numberGuesserUIState.chances}",
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 16.dp, end = 16.dp, bottom = 8.dp)
                            .background(Color(0xFF4355B8), RoundedCornerShape(30))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = "${numberGuesserUIState.guessNumber}",
                    fontSize = 54.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "From 1 to 10 Guess the Number",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Score: ${numberGuesserUIState.score}",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(30),
                    label = { Text(text = "Enter your word", color = Color(0xFF4355B8)) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color(0xFF4355B8),
                        containerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions().copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    isError = !valid
                )
                if (!valid) {
                    Text(
                        text = "Please enter a number",
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
                    )
                }
            }
        }
        Button(
            onClick = {
                valid = numberGuesserViewModel.checkInput(input)
                if (valid) {
                    numberGuesserViewModel.checkGuess(input.toInt())
                    isDialogActive = numberGuesserViewModel.checkGame()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF4355B8))
        ) {
            Text(
                text = "Guess",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        if (isDialogActive) {
            AlertDialog(onDismissRequest = {
                isDialogActive = false
            }, title = {
                Text(
                    text = "Game Over!",
                )

            }, text = {

                Text(
                    text = "You scored: ${numberGuesserUIState.score}\n"
                )

            }, confirmButton = {
                Button(onClick = {
                    numberGuesserViewModel.resetGame()
                    isDialogActive = false
                }) {
                    Text("Play Again")
                }
            }, dismissButton = {
                Button(onClick = {
                    System.exit(0)
                    isDialogActive = false
                }) {
                    Text("Exit")
                }
            })
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNumberGuesser() {
    LayoutNumberGuesser(NumberGuesserViewModel())
}