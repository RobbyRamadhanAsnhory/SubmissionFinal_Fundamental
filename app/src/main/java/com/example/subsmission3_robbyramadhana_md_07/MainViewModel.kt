package com.example.subsmission3_robbyramadhana_md_07

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun searchUser(query: String) = repository.searchUser(query)
}