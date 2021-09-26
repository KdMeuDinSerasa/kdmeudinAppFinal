package com.example.kdmeudinheiro.repository

import android.app.Activity
import com.example.kdmeudinheiro.interfaces.getObjectListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.util.concurrent.Executor

@RunWith(JUnit4::class)
class getModelsTest: getObjectListener {
    @Mock
    private lateinit var successTask: Task<QuerySnapshot>
    @Mock
    private lateinit var failureTask: Task<QuerySnapshot>

    @Mock
    private lateinit var db: FirebaseFirestore

    @Mock
    private lateinit var collectionReference: CollectionReference

    private lateinit var mDbFirestore: DbFirestore

    private var result = DEFAULT
    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val DEFAULT = 0
    }


    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        mDbFirestore = DbFirestore(this, db)
    }
    

    @Test
    fun getListOfUsersSucess(){
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        Mockito.`when`(db.collection("table_user")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.get()).thenReturn(successTask)
        mDbFirestore.getUsers()
        assertThat(result).isEqualTo(SUCCESS)

    }

    @Test
    fun getListOfUsersFailure(){
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        Mockito.`when`(db.collection("table_user")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.get()).thenReturn(failureTask)
        mDbFirestore.getUsers()
        assertThat(result).isEqualTo(FAILURE)
    }

    @Test
    fun getListOfBillsSuccess(){
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        Mockito.`when`(db.collection("table_account")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.get()).thenReturn(successTask)
        mDbFirestore.getBills()
        assertThat(result).isEqualTo(SUCCESS)
    }
    @Test
    fun getListOfBillsFailure(){
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        Mockito.`when`(db.collection("table_account")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.get()).thenReturn(failureTask)
        mDbFirestore.getBills()
        assertThat(result).isEqualTo(FAILURE)
    }
    @Test
    fun getListOfIncomeSuccess(){
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        Mockito.`when`(db.collection("table_income")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.get()).thenReturn(successTask)
        mDbFirestore.getIncome()
        assertThat(result).isEqualTo(SUCCESS)
    }
    @Test
    fun getListOfIncomeFailure(){
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        Mockito.`when`(db.collection("table_income")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.get()).thenReturn(failureTask)
        mDbFirestore.getIncome()
        assertThat(result).isEqualTo(FAILURE)
    }


    override fun getSucess() {
        result = SUCCESS
    }

    override fun getFailure() {
        result = FAILURE
    }
}


class DbFirestore(val observer: getObjectListener, val db: FirebaseFirestore){
    fun getUsers(){
        db.collection("table_user").get().apply { 
            if (this.isSuccessful) observer.getSucess()
            else observer.getFailure()
        }
    }

    fun getBills(){
        db.collection("table_account").get().apply {
            if (this.isSuccessful) observer.getSucess()
            else observer.getFailure()
        }
    }

    fun getIncome(){
        db.collection("table_income").get().apply {
            if (this.isSuccessful) observer.getSucess()
            else observer.getFailure()
        }
    }
}