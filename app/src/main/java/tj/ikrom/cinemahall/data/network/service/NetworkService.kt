package tj.ikrom.cinemahall.data.network.service

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import tj.ikrom.cinemahall.data.network.api.Api
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@Singleton
class NetworkService @Inject constructor(
    private val retrofit: Retrofit
) {
    val api: Api
        get() = retrofit.create(Api::class.java)

    companion object {
        private const val EXCEPTION = "API returned nothing"
        private const val LOG_TAG = "NetworkService"

        /**
         * Обрабатывает вызовы Retrofit в корутинах.
         */
        suspend fun <T> handleCall(call: Call<T>): T? = withContext(Dispatchers.IO) {
            try {
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw IllegalArgumentException(EXCEPTION)
                }
            } catch (exception: Exception) {
                Log.e(LOG_TAG, "Error executing request: ${exception.localizedMessage}")
                null
            }
        }
    }
}