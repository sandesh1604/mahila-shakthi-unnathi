package com.example.mahila.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.mahila.ui.screens.Member

class MemberViewModel : ViewModel() {
    private val _members = mutableStateListOf(
        Member("Sita Devi", "9876543210"),
        Member("Kamla Devi", "9876543211"),
        Member("Pooja Devi", "9876543212"),
        Member("Rekha Devi", "9876543213"),
        Member("Anita Devi", "9876543214")
    )
    val members: List<Member> get() = _members

    fun addMember(name: String, phone: String) {
        _members.add(Member(name, phone))
    }
}
