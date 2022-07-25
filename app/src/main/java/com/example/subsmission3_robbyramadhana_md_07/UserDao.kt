package com.example.subsmission3_robbyramadhana_md_07

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user ORDER BY username ASC")
    suspend fun getFavoriteListUser(): List<UserData>

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getFavoriteDetailUser(username: String): UserData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(user: UserData)

    @Delete
    suspend fun deleteFavoriteUser(user: UserData)
}