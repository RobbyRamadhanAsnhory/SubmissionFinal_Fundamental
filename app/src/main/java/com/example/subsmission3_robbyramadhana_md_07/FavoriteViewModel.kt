package com.example.subsmission3_robbyramadhana_md_07

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    suspend fun getFavoriteList() = repository.getFavoriteList()
}
