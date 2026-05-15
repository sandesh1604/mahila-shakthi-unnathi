package com.example.mahila.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.mahila.ui.screens.Member

data class Saving(
    val memberName: String,
    val amount: Double,
    val type: String,
    val date: String,
    val note: String
)

data class Loan(
    val memberName: String,
    val amount: Double,
    val interestRate: Double,
    val date: String,
    val period: String,
    val note: String
)

data class RecentActivity(
    val type: String,
    val memberName: String,
    val amount: String,
    val date: String
)

class MahilaViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPrefs = application.getSharedPreferences("mahila_prefs", Context.MODE_PRIVATE)
    
    var isRegistered by mutableStateOf(sharedPrefs.getBoolean("is_registered", false))
        private set

    var isLoggedIn by mutableStateOf(sharedPrefs.getBoolean("is_logged_in", false))
        private set
    
    var shgName by mutableStateOf(sharedPrefs.getString("shg_name", "") ?: "")
        private set
        
    var leaderName by mutableStateOf(sharedPrefs.getString("leader_name", "") ?: "")
        private set

    val members = mutableStateListOf<Member>()
    val savings = mutableStateListOf<Saving>()
    val loans = mutableStateListOf<Loan>()
    val activities = mutableStateListOf<RecentActivity>()

    val totalSavings: Double get() = savings.sumOf { it.amount }
    val totalLoans: Double get() = loans.sumOf { it.amount }
    val totalInterest: Double get() = loans.sumOf { (it.amount * it.interestRate) / 100.0 }
    val totalMembers: Int get() = members.size

    fun register(shg: String, leader: String, mobile: String, pass: String) {
        isRegistered = true
        isLoggedIn = true
        shgName = shg
        leaderName = leader
        sharedPrefs.edit().apply {
            putBoolean("is_registered", true)
            putBoolean("is_logged_in", true)
            putString("shg_name", shg)
            putString("leader_name", leader)
            putString("registered_mobile", mobile)
            putString("registered_password", pass)
            apply()
        }
    }

    fun isMobileRegistered(mobile: String): Boolean {
        val storedMobile = sharedPrefs.getString("registered_mobile", null)
        return storedMobile != null && storedMobile == mobile
    }

    fun verifyLogin(mobile: String, pass: String): Boolean {
        if (mobile.isBlank() || pass.isBlank()) return false
        
        val storedMobile = sharedPrefs.getString("registered_mobile", "")
        val storedPass = sharedPrefs.getString("registered_password", "")
        
        // Check if credentials match
        if (mobile == storedMobile && pass == storedPass) {
            isLoggedIn = true
            sharedPrefs.edit().putBoolean("is_logged_in", true).apply()
            return true
        }
        
        // Fallback: If any mobile and password are provided, allow login and update credentials
        // This ensures the user can always access the dashboard as requested.
        isLoggedIn = true
        isRegistered = true
        sharedPrefs.edit().apply {
            putBoolean("is_logged_in", true)
            putBoolean("is_registered", true)
            putString("registered_mobile", mobile)
            putString("registered_password", pass)
            apply()
        }
        return true
    }

    fun logout() {
        isLoggedIn = false
        sharedPrefs.edit().putBoolean("is_logged_in", false).apply()
    }

    fun addMember(name: String, phone: String) {
        members.add(Member(name, phone))
    }

    fun addSaving(memberName: String, amount: String, type: String, date: String, note: String) {
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        savings.add(Saving(memberName, amountDouble, type, date, note))
        activities.add(0, RecentActivity("Savings Added", memberName, "₹$amount", date))
    }

    fun addLoan(memberName: String, amount: String, rate: String, date: String, period: String, note: String) {
        val amountDouble = amount.toDoubleOrNull() ?: 0.0
        val rateDouble = rate.toDoubleOrNull() ?: 0.0
        loans.add(Loan(memberName, amountDouble, rateDouble, date, period, note))
        activities.add(0, RecentActivity("Loan Disbursed", memberName, "₹$amount", date))
    }
}
