package com.example.thechristianapp.di

import com.example.thechristianapp.data.remote.GitService
import com.example.thechristianapp.data.remote.GitServiceImpl
import com.example.thechristianapp.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindBibleRepository(
        bibleRepositoryImpl: BibleRepositoryImpl
    ): BibleRepository
    
    @Binds
    abstract fun bindGitService(
        gitServiceImpl: GitServiceImpl
    ): GitService
    
    @Binds
    abstract fun bindPrayerRepository(
        prayerRepositoryImpl: PrayerRepositoryImpl
    ): PrayerRepository
    
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
