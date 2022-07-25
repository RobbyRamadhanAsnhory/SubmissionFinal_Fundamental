package com.example.subsmission3_robbyramadhana_md_07

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class FollowingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun getUserFollowing(username: String) = repository.getUserFollowing(username)
}