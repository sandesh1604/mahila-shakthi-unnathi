package com.example.mahila.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahila.ui.theme.DeepPurple
import com.example.mahila.ui.viewmodel.MahilaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    viewModel: MahilaViewModel, 
    onBackClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val reportTypes = listOf("Savings Summary", "Loan Summary", "Member Wise Report", "Monthly Collection")
    var selectedReport by remember { mutableStateOf(reportTypes[0]) }
    var expandedReport by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val dateRangePickerState = rememberDateRangePickerState()
    var showDateRangePicker by remember { mutableStateOf(false) }
    
    val startDate = dateRangePickerState.selectedStartDateMillis?.let { dateFormatter.format(Date(it)) } ?: "Start Date"
    val endDate = dateRangePickerState.selectedEndDateMillis?.let { dateFormatter.format(Date(it)) } ?: "End Date"
    val dateRangeDisplay = if (dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null) {
        "$startDate - $endDate"
    } else {
        "Select Date Range"
    }

    if (showDateRangePicker) {
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("OK", color = DeepPurple)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) {
                    Text("Cancel", color = DeepPurple)
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                modifier = Modifier.height(400.dp),
                title = { Text("Select Date Range", modifier = Modifier.padding(16.dp)) },
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = DeepPurple,
                    todayContentColor = DeepPurple,
                    todayDateBorderColor = DeepPurple
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports", color = Color.White) },
                navigationIcon = {
                    if (onBackClick != {}) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = Color.White)
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
            Text(text = "Select Report", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Box {
                OutlinedTextField(
                    value = selectedReport,
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth().clickable { expandedReport = true },
                    readOnly = true,
                    trailingIcon = { 
                        IconButton(onClick = { expandedReport = true }) {
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
                    expanded = expandedReport,
                    onDismissRequest = { expandedReport = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    reportTypes.forEach { report ->
                        DropdownMenuItem(
                            text = { Text(report) },
                            onClick = {
                                selectedReport = report
                                expandedReport = false
                            }
                        )
                    }
                }
            }

            Text(text = "Date Range", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = dateRangeDisplay,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().clickable { showDateRangePicker = true },
                readOnly = true,
                trailingIcon = { 
                    IconButton(onClick = { showDateRangePicker = true }) {
                        Icon(Icons.Default.CalendarToday, null)
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ReportStatCard("Total Savings", "₹ ${viewModel.totalSavings}", Modifier.weight(1f))
                ReportStatCard("Total Interest", "₹ ${viewModel.totalInterest}", Modifier.weight(1f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ReportStatCard("Total Members", "${viewModel.totalMembers}", Modifier.weight(1f))
                ReportStatCard("Total Loans", "₹ ${viewModel.totalLoans}", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val reportText = """
                        *SHG Report: $selectedReport*
                        Period: $dateRangeDisplay
                        
                        Total Savings: ₹ ${viewModel.totalSavings}
                        Total Interest: ₹ ${viewModel.totalInterest}
                        Total Members: ${viewModel.totalMembers}
                        Total Loans: ₹ ${viewModel.totalLoans}
                        
                        Generated via Mahila SHG App
                    """.trimIndent()
                    
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, reportText)
                        type = "text/plain"
                        setPackage("com.whatsapp")
                    }
                    
                    try {
                        context.startActivity(sendIntent)
                    } catch (e: Exception) {
                        // If WhatsApp is not installed, use general share
                        val shareIntent = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, reportText)
                            type = "text/plain"
                        }, "Share Report")
                        context.startActivity(shareIntent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Export via WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReportStatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, color = Color.Gray, fontSize = 12.sp)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DeepPurple)
        }
    }
}
