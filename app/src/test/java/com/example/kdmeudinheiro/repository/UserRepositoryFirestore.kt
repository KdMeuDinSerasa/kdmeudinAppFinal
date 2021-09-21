package com.example.kdmeudinheiro

import android.app.Activity
import com.example.kdmeudinheiro.interfaces.addObjectListener
import com.example.kdmeudinheiro.model.BillsModel
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


    private lateinit var successTask: Task<DocumentReference>
    private lateinit var failureTask: Task<DocumentReference>

    @Mock
    private lateinit var db: FirebaseFirestore

    @Mock
    private lateinit var collectionReference: CollectionReference


    private lateinit var mCreateUser: CreateUser
    private lateinit var mCreateBill: CreateBill

    private var result = DEFAULT

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = 2
        private const val DEFAULT = 0
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        successTask = object : Task<DocumentReference>() {
            override fun addOnCompleteListener(
                p0: Executor,
                p1: OnCompleteListener<DocumentReference>
            ): Task<DocumentReference> {
                p1.onComplete(successTask)
                return successTask
            }

            override fun isComplete(): Boolean {
                return true
            }

            override fun isSuccessful(): Boolean {
                return true
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun getResult(): DocumentReference? {
                return result
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): DocumentReference? {
                return result
            }

            override fun getException(): Exception? {
                return exception
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in DocumentReference>): Task<DocumentReference> {
                return this
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in DocumentReference>
            ): Task<DocumentReference> {
                return this
            }


            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in DocumentReference>
            ): Task<DocumentReference> {
                return this
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<DocumentReference> {
                return this
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<DocumentReference> {
                return this
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<DocumentReference> {
                return this
            }

        }
        failureTask = object : Task<DocumentReference>() {
            override fun addOnCompleteListener(
                p0: Executor,
                p1: OnCompleteListener<DocumentReference>
            ): Task<DocumentReference> {
                p1.onComplete(failureTask)
                return failureTask
            }

            override fun isComplete(): Boolean {
                return true
            }

            override fun isSuccessful(): Boolean {
                return false
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun getResult(): DocumentReference? {
                return result
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): DocumentReference? {
                return result
            }

            override fun getException(): Exception? {
                return exception
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in DocumentReference>): Task<DocumentReference> {
                return this
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in DocumentReference>
            ): Task<DocumentReference> {
                return this
            }


            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in DocumentReference>
            ): Task<DocumentReference> {
                return this
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<DocumentReference> {
                return this
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<DocumentReference> {
                return this
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<DocumentReference> {
                return this
            }

        }
        mCreateUser = CreateUser(this, db)
        mCreateBill = CreateBill(this, db)
    }

    @Test
    fun addUserSucess_test() {
        val mUserModel = UserModel("", "", "", "", "")
        Mockito.`when`(db.collection("table_user")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mUserModel)).thenReturn(successTask)
        mCreateUser.addUser(mUserModel)
        assertThat(result).isEqualTo(SUCCESS)
    }

    @Test
    fun addUserFailure_test() {
        val mUserModel = UserModel("", "", "", "", "")
        Mockito.`when`(db.collection("table_user")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mUserModel)).thenReturn(failureTask)
        mCreateUser.addUser(mUserModel)
        assertThat(result).isEqualTo(FAILURE)
    }

    @Test
    fun addBillSuccess_test(){
        val mBillsModel = BillsModel("", "", "", "", "", Calendar.getInstance().time, 0, )
        Mockito.`when`(db.collection("table_account")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mBillsModel)).thenReturn(successTask)
        mCreateBill.addBill(mBillsModel)
        assertThat(result).isEqualTo(SUCCESS)
    }

    @Test
    fun addBillFailure_test(){
        val mBillsModel = BillsModel("", "", "", "", "", Calendar.getInstance().time, 0, )
        Mockito.`when`(db.collection("table_account")).thenReturn(collectionReference)
        Mockito.`when`(collectionReference.add(mBillsModel)).thenReturn(failureTask)
        mCreateBill.addBill(mBillsModel)
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

