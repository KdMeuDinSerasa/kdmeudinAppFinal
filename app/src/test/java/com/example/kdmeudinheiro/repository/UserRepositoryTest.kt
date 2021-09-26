package com.example.kdmeudinheiro

import android.app.Activity
import com.example.kdmeudinheiro.interfaces.LogInLIstener
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
    @Mock
    private lateinit var successTask: Task<AuthResult>
    @Mock
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
        logInModel = LogInModel(this, mAuth)
    }


    @Test
    fun logInSuccess_test() {
        val email = "janeDoe@gmail.com"
        val password = "DoeJane21"
        Mockito.`when`(successTask.isSuccessful).thenReturn(true)
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
        logInModel!!.logIn(email, password)
        assertThat(logInResult).isEqualTo(SUCCESS)
    }

    @Test
    fun logInFailure_test() {
        val email = "janeGmail.com"
        val password = "123_456"
        Mockito.`when`(failureTask.isSuccessful).thenReturn(false)
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(failureTask)
        logInModel.logIn(email, password)
        assertThat(logInResult).isEqualTo(FAILURE)
    }

    override fun logInSuccess(email: String?, password: String?) {
            logInResult = SUCCESS
    }

    override fun logInFailure(email: String?, password: String?) {
            logInResult = FAILURE
    }


}