package com.example.mahila.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahila.ui.theme.DeepPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestCalculatorScreen(onBackClick: () -> Unit) {
    var principal by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var weeks by remember { mutableStateOf("") }
    var totalInterest by remember { mutableStateOf("0") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interest Calculator", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepPurple)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Principal (₹)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = principal,
                onValueChange = { principal = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = "Interest Rate (% per week)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = interestRate,
                onValueChange = { interestRate = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter rate") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = "Number of Weeks", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = weeks,
                onValueChange = { weeks = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter weeks") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total Interest", color = Color.Gray, fontSize = 14.sp)
                    Text(text = "₹ $totalInterest", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = DeepPurple)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val p = principal.toDoubleOrNull() ?: 0.0
                    val r = interestRate.toDoubleOrNull() ?: 0.0
                    val w = weeks.toDoubleOrNull() ?: 0.0
                    totalInterest = (p * r * w / 100).toString()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Calculate", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
