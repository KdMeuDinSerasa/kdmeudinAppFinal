import android.app.Activity
import com.example.kdmeudinheiro.repository.UserRepository
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.util.concurrent.Executor
import javax.inject.Inject

@RunWith(JUnit4::class)
class UserRepositoryTest {


    private lateinit var successTask: Task<AuthResult>
    private lateinit var failureTask: Task<AuthResult>

    @Mock
    private lateinit var mAuth: FirebaseAuth
    @Mock
    private lateinit var mFireStore: FirebaseFirestore
    @Mock
    private lateinit var mFireStorage: FirebaseStorage
//    @Mock
//    private lateinit var logInModel: UserRepository
    val email = "janeDoe@gmail.com"
    val password = "DoeJane21"

    private var logInResult = UNDEF

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEF = 0
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        successTask = object : Task<AuthResult>() {
            override fun isComplete(): Boolean = true

            override fun isSuccessful(): Boolean = true
            // ...
            override fun addOnCompleteListener(executor: Executor,
                                               onCompleteListener: OnCompleteListener<AuthResult>): Task<AuthResult> {
                onCompleteListener.onComplete(successTask)
                return successTask
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
                TODO("Not yet implemented")
            }

            override fun getResult(): AuthResult? {
                TODO("Not yet implemented")
            }

            override fun <X : Throwable?> getResult(p0: Class<X>): AuthResult? {
                TODO("Not yet implemented")
            }

            override fun getException(): Exception? {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(p0: OnSuccessListener<in AuthResult>): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Executor,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnSuccessListener(
                p0: Activity,
                p1: OnSuccessListener<in AuthResult>
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(p0: OnFailureListener): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Executor,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }

            override fun addOnFailureListener(
                p0: Activity,
                p1: OnFailureListener
            ): Task<AuthResult> {
                TODO("Not yet implemented")
            }
        }
//        logInModel = UserRepository(mFireStore, mAuth, mFireStorage)
    }

    @Test
    fun logInSuccess_test() {
        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
            .thenReturn(successTask)
//        logInModel!!.loginWithEmailPassword(email, password) { response, e ->
//            if (response != null) {
//                logInResult == SUCCESS
//            } else {
//                logInResult == FAILURE
//                println(e)
//            }
//        }
        assert(logInResult == SUCCESS)
    }


//    @Test
//    fun logInFailure_test() {
//        val email = "cool@cool.com"
//        val password = "123_456"
//        Mockito.`when`(mAuth!!.signInWithEmailAndPassword(email, password))
//            .thenReturn(failureTask)
//        accountModel!!.logIn(email, password)
//        assert(logInResult == FAILURE)
//    }
//    @Test
//    fun someTest() {
//          val mockFirestore: FirebaseFirestore = Mockito.mock(FirebaseFirestore::class.java)
//                Mockito.when(mockFirestore.collection("table_user")){ a, b, c ->
//
//                }
//
//      val  interactor: UserRepository = UserRepository(mockFirestore, mAuth, mFireStorage)
//
//    interactor.loginWithEmailPassword(email, password){ resp
//
//    }
//
//        // some assertion or verification
//    }



}