package com.example.mahila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahila.ui.theme.DeepPurple

@Composable
fun LoginScreen(
    showSignUp: Boolean = true,
    onLoginAttempt: (String, String) -> Boolean,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit
) {
    var mobileNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Welcome Header
            Text(
                text = "Welcome",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Digital Ledger for\nWomen's SHGs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DeepPurple,
                lineHeight = 28.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Logo Section
            // NOTE: After you add 'logo.png' to res/drawable, uncomment the Image block below 
            // and remove the Placeholder Box.
            
            /*
            Image(
                painter = painterResource(id = com.example.mahila.R.drawable.logo),
                contentDescription = "Mahila Shakti Logo",
                modifier = Modifier.size(240.dp)
            )
            */

            // Placeholder Box (Prevents build error until logo is added)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = DeepPurple.copy(alpha = 0.1f)
                )
                Text(
                    text = "Add logo.png to\nres/drawable",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Mobile Number Field
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                placeholder = { Text("Mobile Number", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedBorderColor = DeepPurple,
                    unfocusedContainerColor = Color(0xFFFBFBFB),
                    focusedContainerColor = Color(0xFFFBFBFB)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFEEEEEE),
                    focusedBorderColor = DeepPurple,
                    unfocusedContainerColor = Color(0xFFFBFBFB),
                    focusedContainerColor = Color(0xFFFBFBFB)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = { /* Handle Forgot Password */ },
                modifier = Modifier.align(Alignment.Start),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = DeepPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Login Button
            Button(
                onClick = {
                    if (mobileNumber.isNotBlank() && password.isNotBlank()) {
                        onLoginAttempt(mobileNumber, password)
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            Row(
                modifier = Modifier.padding(bottom = 40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
                TextButton(
                    onClick = onSignUpClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        color = DeepPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
