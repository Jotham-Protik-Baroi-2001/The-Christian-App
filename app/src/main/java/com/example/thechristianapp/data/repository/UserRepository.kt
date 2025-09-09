package com.example.thechristianapp.data.repository

import com.example.thechristianapp.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun signInUser(user: User)
    suspend fun signOut()
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)
}
