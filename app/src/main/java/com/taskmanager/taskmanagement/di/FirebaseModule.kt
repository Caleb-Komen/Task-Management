package com.taskmanager.taskmanagement.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteDataSource
import com.taskmanager.taskmanagement.data.remote.ProjectRemoteSourceImpl
import com.taskmanager.taskmanagement.data.remote.UserRemoteDataSource
import com.taskmanager.taskmanagement.data.remote.UserRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {
    @Singleton
    @Provides
    fun provideProjectRemoteDataSource(
        firestore: FirebaseFirestore
    ): ProjectRemoteDataSource {
        return ProjectRemoteSourceImpl(firestore)
    }

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(firestore, firebaseAuth)
    }
}