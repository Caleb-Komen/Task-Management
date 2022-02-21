package com.taskmanager.taskmanagement.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.taskmanager.taskmanagement.data.local.ProjectDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class]
)
@Module
object TestModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth{
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.useEmulator("10.0.2.2", 9099)
        return firebaseAuth
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestoreSettings(): FirebaseFirestoreSettings {
        return FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(settings: FirebaseFirestoreSettings): FirebaseFirestore{
        val firestore = FirebaseFirestore.getInstance()
        firestore.useEmulator("10.0.2.2", 8080)
        firestore.firestoreSettings = settings
        return firestore
    }

    @Singleton
    @Provides
    fun provideProjectDataBase(
        @ApplicationContext context: Context
    ): ProjectDatabase{
        return Room.inMemoryDatabaseBuilder(
            context,
            ProjectDatabase::class.java
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

}