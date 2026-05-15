package com.example.mahila.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahila.ui.theme.DeepPurple
import com.example.mahila.ui.viewmodel.MahilaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanEntryScreen(viewModel: MahilaViewModel, onBackClick: () -> Unit) {
    val members = viewModel.members.map { it.name }.ifEmpty { listOf("No Members Added") }
    var selectedMember by remember { mutableStateOf(members[0]) }
    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var repaymentPeriod by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var expandedMember by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var showDatePicker by remember { mutableStateOf(false) }
    val dateString = dateFormatter.format(Date(datePickerState.selectedDateMillis ?: System.currentTimeMillis()))

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK", color = DeepPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = DeepPurple)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Loan", color = Color.White) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Select Member", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = selectedMember,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth().clickable { expandedMember = true },
                    readOnly = true,
                    trailingIcon = { 
                        IconButton(onClick = { expandedMember = true }) {
                            Icon(Icons.Default.KeyboardArrowDown, null)
                        }
                    },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                DropdownMenu(
                    expanded = expandedMember,
                    onDismissRequest = { expandedMember = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    members.forEach { member ->
                        DropdownMenuItem(
                            text = { Text(member) },
                            onClick = {
                                selectedMember = member
                                expandedMember = false
                            }
                        )
                    }
                }
            }

            Text(text = "Loan Amount (₹)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = loanAmount,
                onValueChange = { loanAmount = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = "Interest Rate (%)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = interestRate,
                onValueChange = { interestRate = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter rate") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = "Loan Date", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = dateString,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                readOnly = true,
                trailingIcon = { 
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, null)
                    }
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Text(text = "Repayment Period (Weeks)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = repaymentPeriod,
                onValueChange = { repaymentPeriod = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter weeks") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = "Note (Optional)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter note") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedMember != "No Members Added" && loanAmount.isNotBlank()) {
                        viewModel.addLoan(selectedMember, loanAmount, interestRate, dateString, repaymentPeriod, note)
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save Loan", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
