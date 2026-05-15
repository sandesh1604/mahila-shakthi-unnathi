package com.example.mahila.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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

data class Member(val name: String, val phone: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDirectoryScreen(viewModel: MahilaViewModel, onMemberClick: (String) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddMemberDialog(
            onDismiss = { showDialog = false },
            onConfirm = { name, phone ->
                viewModel.addMember(name, phone)
                showDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Members", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepPurple),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = DeepPurple,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Member")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search members...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = DeepPurple
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (viewModel.members.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No members added yet.", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(viewModel.members.filter { it.name.contains(searchQuery, ignoreCase = true) }) { member ->
                        MemberRow(member, onClick = { onMemberClick(member.name) })
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddMemberDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add New Member", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank() && phone.isNotBlank()) onConfirm(name, phone) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurple)
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = DeepPurple)
            }
        }
    )
}

@Composable
fun MemberRow(member: Member, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = Color(0xFFE8EAF6)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = member.name.take(1),
                    color = DeepPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = member.phone, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
