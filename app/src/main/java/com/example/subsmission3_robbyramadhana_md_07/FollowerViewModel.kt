package com.example.subsmission3_robbyramadhana_md_07

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class FollowerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun getUserFollowers(username: String) = repository.getUserFollowers(username)
}