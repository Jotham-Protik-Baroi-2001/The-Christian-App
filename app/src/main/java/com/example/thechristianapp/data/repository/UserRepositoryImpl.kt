package com.example.thechristianapp.data.repository

import com.example.thechristianapp.data.database.BibleDatabase
import com.example.thechristianapp.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val database: BibleDatabase
) : UserRepository {
    
    override fun getCurrentUser(): Flow<User?> {
        return database.userDao().getCurrentUser()
    }
    
    override suspend fun signInUser(user: User) {
        // Sign out all existing users first
        database.userDao().signOutAllUsers()
        
        // Insert the new user
        database.userDao().insertUser(user)
    }
    
    override suspend fun signOut() {
        database.userDao().signOutAllUsers()
    }
    
    override suspend fun updateUser(user: User) {
        database.userDao().updateUser(user)
    }
    
    override suspend fun deleteUser(userId: String) {
        val user = database.userDao().getUserById(userId)
        user?.let {
            database.userDao().deleteUser(it)
        }
    }
}
