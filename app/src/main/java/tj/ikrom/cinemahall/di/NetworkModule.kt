package tj.ikrom.cinemahall.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import retrofit2.converter.gson.GsonConverterFactory
import tj.ikrom.cinemahall.data.network.api.Api
import tj.ikrom.cinemahall.data.network.service.NetworkService
import tj.ikrom.cinemahall.domain.common.ApiConstants.API_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkService(retrofit: Retrofit): NetworkService {
        return NetworkService(retrofit)
    }

    @Provides
    @Singleton
    fun provideChuckerCollector(@ApplicationContext context: Context): ChuckerCollector {
        return ChuckerCollector(
            context = context,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context, showNotification = true))
            .maxContentLength(250000L)
            .alwaysReadResponseBody(true)
            .createShortcut(true)
            .build()
    }


}