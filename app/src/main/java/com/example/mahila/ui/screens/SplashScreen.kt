package com.example.mahila.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mahila.ui.theme.DeepPurple

@Composable
fun SplashScreen(onGetStartedClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepPurple)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        Surface(
            modifier = Modifier.size(150.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.2f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Placeholder for logo
                Icon(
                    painter = painterResource(android.R.drawable.ic_menu_gallery),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Mahila-Shakti\nUnnati",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Empowering Women\nThrough Savings",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Get Started", color = DeepPurple, fontWeight = FontWeight.Bold)
        }
    }
}
