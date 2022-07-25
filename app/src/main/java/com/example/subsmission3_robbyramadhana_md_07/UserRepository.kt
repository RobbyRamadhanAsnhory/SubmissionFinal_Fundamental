package com.example.subsmission3_robbyramadhana_md_07

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val application: Application) {

    private val retrofit: ApiService
    private val dao: UserDao
    private val dataStore: ThemePreferences

    init {
        retrofit = RetrofitService.create()
        val database: UserDatabase = UserDatabase.getInstance(application)
        dao = database.userDao()
        dataStore = ThemePreferences.getInstance(application)
    }

    fun searchUser(query: String): LiveData<Desc<List<UserData>>> {
        val listUser = MutableLiveData<Desc<List<UserData>>>()

        listUser.postValue(Desc.Loading())
        retrofit.searchUsers(query).enqueue(object : Callback<SearchList> {
            override fun onResponse(
                call: Call<SearchList>,
                response: Response<SearchList>
            ) {
                val list = response.body()?.items
                if (list.isNullOrEmpty())
                    listUser.postValue(Desc.Error(null))
                else
                    listUser.postValue(Desc.Success(list))
            }

            override fun onFailure(call: Call<SearchList>, t: Throwable) {
                listUser.postValue(Desc.Error(t.message))
            }

        })
        return listUser
    }

    suspend fun getDetailUser(username: String): LiveData<Desc<UserData>> {

        val user = MutableLiveData<Desc<UserData>>()

        if (dao.getFavoriteDetailUser(username) != null) {
            //Access Database
            user.postValue(Desc.Success(dao.getFavoriteDetailUser(username)))
        } else {
            //Request ke API
            retrofit.getDetailUser(username).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    val result = response.body()
                    user.postValue(Desc.Success(result))
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {

                }
            })
        }

        return user
    }

    fun getUserFollowing(username: String): LiveData<Desc<List<UserData>>> {

        val listUser = MutableLiveData<Desc<List<UserData>>>()

        listUser.postValue(Desc.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(Desc.Error(null))
                else
                    listUser.postValue(Desc.Success(list))
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                listUser.postValue(Desc.Error(t.message))
            }
        })
        return listUser
    }

    fun getUserFollowers(username: String): LiveData<Desc<List<UserData>>> {

        val listUser = MutableLiveData<Desc<List<UserData>>>()

        listUser.postValue(Desc.Loading())
        retrofit.getUserFollowers(username).enqueue(object : Callback<List<UserData>> {
            override fun onResponse(
                call: Call<List<UserData>>,
                response: Response<List<UserData>>
            ) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(Desc.Error(null))
                else
                    listUser.postValue(Desc.Success(list))
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                listUser.postValue(Desc.Error(t.message))
            }
        })

        return listUser
    }

    suspend fun getFavoriteList(): LiveData<Desc<List<UserData>>> {

        val listFavorite = MutableLiveData<Desc<List<UserData>>>()
        listFavorite.postValue(Desc.Loading())

        if (dao.getFavoriteListUser().isNullOrEmpty())
            listFavorite.postValue(Desc.Error(null))
        else
            listFavorite.postValue(Desc.Success(dao.getFavoriteListUser()))

        return listFavorite
    }

    suspend fun insertFavoriteUser(user: UserData) = dao.insertFavoriteUser(user)

    suspend fun deleteFavoriteUser(user: UserData) = dao.deleteFavoriteUser(user)

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        dataStore.saveThemeSetting(isDarkModeActive)

    fun getThemeSetting() = dataStore.getThemeSetting()

}