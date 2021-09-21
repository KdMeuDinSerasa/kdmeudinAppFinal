import com.example.kdmeudinheiro.LogInLIstener
import com.example.kdmeudinheiro.model.NewsLetter
import com.example.kdmeudinheiro.model.ReciviedArticles
import com.example.kdmeudinheiro.model.Source
import com.example.kdmeudinheiro.repository.NewsLetterRepository
import com.example.kdmeudinheiro.services.NewsLetterService
import com.example.kdmeudinheiro.viewModel.RetrofitListener
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response


data class FakeRepo(val observer: RetrofitListener, val service: NewsLetterService) {

    suspend fun callapi() {
        service.callAPINewsLetter().apply {
            if (this.isSuccessful) observer.callSuccess()
            else observer.callFailure()
        }
    }
}


@RunWith(JUnit4::class)
class RetrofitTest : RetrofitListener {

    private lateinit var successResult: Response<ReciviedArticles>
    private lateinit var failureResult: Response<ReciviedArticles>
    private lateinit var repo: FakeRepo

    @Mock
    lateinit var api: NewsLetterService

    private var RESULT = UNDEFINED

    companion object {
        private const val SUCCESS = 1
        private const val FAILURE = -1
        private const val UNDEFINED = 0
    }

    @Before
    fun setUp() {
        val source = Source("tireido")
        val news = mutableListOf<NewsLetter>()
        news.add(NewsLetter("", "", "", "", "", source))
        news.add(NewsLetter("", "", "", "", "", source))
        news.add(NewsLetter("", "", "", "", "", source))
        news.add(NewsLetter("", "", "", "", "", source))
        MockitoAnnotations.openMocks(this)
        successResult = Response.success(200, ReciviedArticles(news))
        failureResult = Response.error(500, ResponseBody.create(null, "content"))
        repo = FakeRepo(this, api)
    }

    @Test
    fun `fetch user should return a success response`() = runBlocking {
        Mockito.`when`(api.callAPINewsLetter())
            .thenReturn(successResult)
        repo.callapi()
        assertThat(RESULT).isEqualTo(SUCCESS)
    }

    @Test
    fun `fetch user should return a failure response`() = runBlocking {
        Mockito.`when`(api.callAPINewsLetter())
            .thenReturn(failureResult)
        repo.callapi()
        assertThat(RESULT).isEqualTo(FAILURE)
    }

    override fun callSuccess() {
        RESULT = SUCCESS
    }

    override fun callFailure() {
        RESULT = FAILURE
    }


}