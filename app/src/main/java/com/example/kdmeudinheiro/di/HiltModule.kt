package com.example.kdmeudinheiro.di

import android.content.Context
import com.example.kdmeudinheiro.repository.*
import com.example.kdmeudinheiro.services.NewsLetterService
import com.example.kdmeudinheiro.services.NotificationHandler
import com.example.kdmeudinheiro.singletons.RetrofitBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun getBillsRepository(db: FirebaseFirestore): BillsRepository = BillsRepository(db)

    @Provides
    fun getIncomeRepository(db: FirebaseFirestore): IncomeRepository = IncomeRepository(db)

    @Provides
    fun getArticlesRepository(db: FirebaseFirestore): ArticlesRepository = ArticlesRepository(db)

    @Provides
    fun getRepositoryNewsLetter(services: NewsLetterService): NewsLetterRepository =
        NewsLetterRepository(services)

    @Provides
    fun getUserRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        firebaseStorage: FirebaseStorage
    ): UserRepository = UserRepository(db, auth, firebaseStorage)

    @Provides
    fun getDatabase(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun getFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun getNotificationHandler(@ApplicationContext context: Context): NotificationHandler =
        NotificationHandler(context)

    @Provides
    fun providesApi(): NewsLetterService =
        RetrofitBuilder.buildRetrofit().create(NewsLetterService::class.java)

}