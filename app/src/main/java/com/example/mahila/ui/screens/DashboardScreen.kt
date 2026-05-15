package com.example.mahila.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun DashboardScreen(
    viewModel: MahilaViewModel,
    onAddSavingsClick: () -> Unit = {},
    onInterestCalcClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepPurple),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(DeepPurple)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Welcome ${viewModel.leaderName}".trim(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(text = "SHG: ${viewModel.shgName}", color = Color.Gray, fontSize = 14.sp)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(text = "Group Overview", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OverviewItem("Total Savings", "₹ ${viewModel.totalSavings}", Modifier.weight(1f))
                        OverviewItem("Total Members", "${viewModel.totalMembers}", Modifier.weight(1f))
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OverviewItem("Total Loans", "₹ ${viewModel.totalLoans}", Modifier.weight(1f))
                        OverviewItem("Total Interest", "₹ ${viewModel.totalInterest}", Modifier.weight(1f))
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Recent Activity", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        TextButton(onClick = onAddSavingsClick) {
                            Text("+ Add New", color = DeepPurple, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (viewModel.activities.isEmpty()) {
                        Text(
                            text = "No recent activities",
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    } else {
                        viewModel.activities.forEachIndexed { index, activity ->
                            ActivityItem(activity.type, activity.memberName, activity.amount, activity.date)
                            if (index < viewModel.activities.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = onInterestCalcClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPurple.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Calculate Interest", color = DeepPurple, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun OverviewItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DeepPurple)
    }
}

@Composable
fun ActivityItem(type: String, name: String, amount: String, date: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = type, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = name, color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2E7D32))
            Text(text = date, color = Color.Gray, fontSize = 12.sp)
        }
    }
}
