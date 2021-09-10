package com.example.kdmeudinheiro.di

import com.example.kdmeudinheiro.repository.BillsRepository
import com.example.kdmeudinheiro.repository.IncomeRepository
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun getBillsRepository(db: FirebaseFirestore): BillsRepository = BillsRepository(db)

    @Provides
    fun getIncomeRepository(db: FirebaseFirestore): IncomeRepository = IncomeRepository(db)

    @Provides
    fun getUserRepository(db: FirebaseFirestore, auth: FirebaseAuth): UserRepository = UserRepository(db, auth)

    @Provides
    fun getDatabase(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


}