package com.example.kdmeudinheiro

import android.app.Activity
import com.example.kdmeudinheiro.model.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.util.concurrent.Executor

data class LogInModel(val observer: LogInLIstener, val mAuth: FirebaseAuth) {

    fun logIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).apply {
            if (this.isSuccessful) observer.logInSuccess(email, password)
            else observer.logInFailure(email, password)
        }
    }
}

@RunWith(JUnit4::class)
class FirebaseTest : LogInLIstener {
    private lateinit var successTask: Task<AuthResult>
    private lateinit var failureTask: Task<AuthResult>

    @Mock
    private lateinit var mAuth: FirebaseAuth
    private lateinit var logInModel: LogInModel

    private var logInResult = UNDEF

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        successTask = object : Task<AuthResult>() {
            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true

            // ...
            override fun addOnCompleteListener(
                executor: Executor,
                onCompleteListener: OnCompleteListener<AuthResult>
            ): Task<AuthResult> {
                onCompleteListener.onComplete(successTask)
                return successTask
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun getResult(): AuthResult? {
                return result
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                return result
            }

            override fun getException(): Exception? {
                return exception
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                return this

            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return this

            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return this
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                return this
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return this
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return this
            }
        }
        failureTask = object : Task<AuthResult>() {
            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = false

            // ...
            override fun addOnCompleteListener(
                executor: Executor,
                onCompleteListener: OnCompleteListener<AuthResult>
            ): Task<AuthResult> {
                onCompleteListener.onComplete(failureTask)
                return failureTask
            }

            override fun isCanceled(): Boolean {
                return false
            }

            override fun getResult(): AuthResult? {
                return result
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                return result
            }

            override fun getException(): Exception? {
                return exception
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                return this
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return this
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                return this
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                return this
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return this
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                return this
            }
        }
        logInModel = LogInModel(this, mAuth)
    }


    @Test
    fun logInSuccess_test() {
        val email = "janeDoe@gmail.com"
        val password = "DoeJane21"
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
        logInModel!!.logIn(email, password)
        assertThat(logInResult).isEqualTo(SUCCESS)
    }

    @Test
    fun logInFailure_test() {
        val email = "janeGmail.com"
        val password = "123_456"
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(failureTask)
        logInModel.logIn(email, password)
        assertThat(logInResult).isEqualTo(FAILURE)
    }

    override fun logInSuccess(email: String?, password: String?) {
        val existingUsers = listOf<UserModel>(
            UserModel("", "janeDoe@gmail.com", "DoeJane21", "Jane Doe", ""),
            UserModel("", "admin@gmail.com", "ADM123", "Admin Kaze Gi", ""),
            UserModel("", "janeGmail.com", "123_456", "", "")
        )


        val thisUserModel = UserModel("", email = email!!, password = password!!, "", "")
        if (existingUsers.contains(thisUserModel)) {
            logInResult = SUCCESS
        } else {
            logInResult = FAILURE
        }
    }

    override fun logInFailure(email: String?, password: String?) {
        val existingUsers = listOf<UserModel>(
            UserModel("", "janeDoe@gmail.com", "DoeJane21", "Jane Doe", ""),
            UserModel("", "admin@gmail.com", "ADM123", "Admin Kaze Gi", ""),
            UserModel("", "janeGmail.com", "ADM123", "", "")
        )

        val thisUserModel = UserModel("", email = email!!, password = password!!, "", "")
        if (existingUsers.contains(thisUserModel)) {
            logInResult = SUCCESS
        } else {
            logInResult = FAILURE
        }
    }


}