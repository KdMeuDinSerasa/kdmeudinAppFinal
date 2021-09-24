package com.example.kdmeudinheiro

import android.app.Activity
import com.example.kdmeudinheiro.interfaces.addObjectListener
import com.example.kdmeudinheiro.model.BillsModel
import com.example.kdmeudinheiro.model.IncomeModel
import com.example.kdmeudinheiro.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.CollectionReference
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executor

@RunWith(JUnit4::class)
class UserRepositoryFirestore: addObjectListener {

    @Mock
    private lateinit var successTask: Task<DocumentReference>
    @Mock
    private lateinit var failureTask: Task<DocumentReference>

    @Mock
    private lateinit var db: FirebaseFirestore

    @Mock
    private lateinit var collectionReference: CollectionReference


    private lateinit var mCreateUser: CreateUser
    private lateinit var mCreateBill: CreateBill
    private lateinit var mCreateIncome: CreateIncome

    private var result = DEFAULT

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = 2
        private const val DEFAULT = 0
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mCreateUser = CreateUser(this, db)
        mCreateBill = CreateBill(this, db)
        mCreateIncome = CreateIncome(this, db)
    }

    @Test
    fun addUserSucess_test() {
        val mUserModel = UserModel("", "", "", "", "")
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        Mockito.`when`(db.collection("table_user")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mUserModel)).thenReturn(successTask)
        mCreateUser.addUser(mUserModel)
        assertThat(result).isEqualTo(SUCCESS)
    }

    @Test
    fun addUserFailure_test() {
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        val mUserModel = UserModel("", "", "", "", "")
        Mockito.`when`(db.collection("table_user")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mUserModel)).thenReturn(failureTask)
        mCreateUser.addUser(mUserModel)
        assertThat(result).isEqualTo(FAILURE)
    }

    @Test
    fun addBillSuccess_test(){
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        val mBillsModel = BillsModel("", "", "", "", "", Calendar.getInstance().time, 0, )
        Mockito.`when`(db.collection("table_account")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mBillsModel)).thenReturn(successTask)
        mCreateBill.addBill(mBillsModel)
        assertThat(result).isEqualTo(SUCCESS)
    }

    @Test
    fun addBillFailure_test(){
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        val mBillsModel = BillsModel("", "", "", "", "", Calendar.getInstance().time, 0, )
        Mockito.`when`(db.collection("table_account")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mBillsModel)).thenReturn(failureTask)
        mCreateBill.addBill(mBillsModel)
        assertThat(result).isEqualTo(FAILURE)
    }

    @Test
    fun addIncomeSuccess_test(){
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        val mIncome = IncomeModel("", "", "")
        Mockito.`when`(db.collection("table_income")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mIncome)).thenReturn(successTask)
        mCreateIncome.addIncome(mIncome)
        assertThat(result).isEqualTo(SUCCESS)
    }

    @Test
    fun addIncomeFailure_test(){
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        val mIncome = IncomeModel("", "", "")
        Mockito.`when`(db.collection("table_income")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mIncome)).thenReturn(failureTask)
        mCreateIncome.addIncome(mIncome)
        assertThat(result).isEqualTo(FAILURE)
    }

    override fun addSucess(mObject: Any) {
        result = SUCCESS
    }

    override fun addFailure(mObject: Any) {
        result = FAILURE
    }
}


class CreateUser(val observer: addObjectListener, val db: FirebaseFirestore) {
    fun addUser(mUserModel: UserModel) {
        val task = db.collection("table_user").add(mUserModel)
        if (task.isSuccessful) observer.addSucess(mUserModel)
        else observer.addFailure(mUserModel)

    }

}

class CreateBill(val observer: addObjectListener, val db: FirebaseFirestore){
    fun addBill(mBillsModel: BillsModel){
        db.collection("table_account").add(mBillsModel).apply {
            if (this.isSuccessful) observer.addSucess(mBillsModel)
            else observer.addFailure(mBillsModel)
        }
    }
}

class CreateIncome(val observer: addObjectListener, val db: FirebaseFirestore){
    fun addIncome(mIncome: IncomeModel) {
        db.collection("table_income").add(mIncome).apply {
            if (this.isSuccessful) observer.addSucess(mIncome)
            else observer.addFailure(mIncome)
        }
    }

}

