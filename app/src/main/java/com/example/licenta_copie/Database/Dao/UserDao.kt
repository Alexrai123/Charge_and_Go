package com.example.licenta_copie.Database.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.licenta_copie.Database.Entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT EXISTS(SELECT 1 FROM User WHERE email = :email AND password = :password)")
    suspend fun userExists(email: String, password: String): Boolean
    @Query("SELECT password FROM User WHERE id = :id")
    fun getPasswordById(id: Int): Flow<String?>
    @Query("SELECT password FROM User WHERE email = :email")
    fun getPasswordByEmail(email: String): Flow<String?>
    @Query("SELECT * FROM User WHERE (email = :email AND password = :password)")
    fun getUserByEmailAndPassword(email: String, password: String): Flow<User?>
    @Query("SELECT id FROM User WHERE (email = :email AND password = :password)")
    fun getUserIDByEmailAndPassword(email: String, password: String): Flow<Int?>
    @Query("SELECT * FROM User WHERE (email = :email)")
    fun getUserByEmail(email: String): Flow<User?>
    @Query("SELECT * from User")
    fun getAllUsers(): Flow<List<User>>
    @Query("SELECT * from User WHERE id = :id")
    fun getUserById(id: Int): Flow<User?>
    @Query("DELETE FROM User WHERE id = :id")
    suspend fun deleteUserById(id: Int)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)
    @Update
    suspend fun update(user: User)
    @Delete
    suspend fun delete(user: User)
}