package com.example.mahila.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahila.ui.theme.DeepPurple
import com.example.mahila.ui.viewmodel.MahilaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailsScreen(
    memberName: String,
    viewModel: MahilaViewModel,
    onBackClick: () -> Unit
) {
    val memberSavings = viewModel.savings.filter { it.memberName == memberName }
    val memberLoans = viewModel.loans.filter { it.memberName == memberName }
    
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Savings", "Loans")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(memberName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepPurple)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab, containerColor = Color.White, contentColor = DeepPurple) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            if (selectedTab == 0) {
                if (memberSavings.isEmpty()) {
                    EmptyHistory("No savings recorded.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        items(memberSavings) { saving ->
                            HistoryItem(
                                title = "Saving: ₹${saving.amount}",
                                subtitle = "Type: ${saving.type}",
                                date = saving.date,
                                color = Color(0xFF2E7D32)
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            } else {
                if (memberLoans.isEmpty()) {
                    EmptyHistory("No loans disbursed.")
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        items(memberLoans) { loan ->
                            HistoryItem(
                                title = "Loan: ₹${loan.amount}",
                                subtitle = "Rate: ${loan.interestRate}% | ${loan.period} weeks",
                                date = loan.date,
                                color = Color.Red
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyHistory(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message, color = Color.Gray)
    }
}

@Composable
fun HistoryItem(title: String, subtitle: String, date: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
            Text(text = subtitle, color = Color.Gray, fontSize = 14.sp)
        }
        Text(text = date, color = Color.Gray, fontSize = 12.sp)
    }
}
