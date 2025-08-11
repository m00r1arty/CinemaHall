package tj.ikrom.cinemahall.data.network.service

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
import kotlin.coroutines.resumeWithException

class NetworkService private constructor(
    private val retrofit: Retrofit,
) {
    val api: Api
        get() = retrofit.create(Api::class.java)

    companion object {
        private var instance: NetworkService? = null

        fun getInstance(retrofit: Retrofit): NetworkService {
            return instance ?: synchronized(this) {
                instance ?: NetworkService(retrofit).also { instance = it }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun <T> handleCall(call: Call<T>): Result<T> = withContext(Dispatchers.IO) {
            try {
                val result: T = suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            if (response.isSuccessful) {
                                val body = response.body() ?: throw NullPointerException("Response body is null")
                                continuation.resume(body, null)
                            } else {
                                continuation.resumeWithException(HttpException(response))
                            }
                        }

                        override fun onFailure(call: Call<T>, t: Throwable) {
                            if (continuation.isActive) {
                                continuation.resumeWithException(t)
                            }
                        }
                    })
                }
                Result.success(result)
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
    }
}
