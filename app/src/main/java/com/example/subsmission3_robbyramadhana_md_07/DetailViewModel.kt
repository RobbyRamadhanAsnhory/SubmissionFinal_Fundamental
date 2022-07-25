package com.example.subsmission3_robbyramadhana_md_07

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    val repository = UserRepository(application)

    suspend fun getDetailUser(username: String) = repository.getDetailUser(username)

    fun insertFavoriteUser(user: UserData) = viewModelScope.launch {
        repository.insertFavoriteUser(user)
    }

    fun deleteFavoriteUser(user: UserData) = viewModelScope.launch {
        repository.deleteFavoriteUser(user)
    }
}