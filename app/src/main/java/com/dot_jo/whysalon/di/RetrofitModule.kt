package com.dot_jo.whysalon.di

import com.dot_jo.whysalon.base.NetworkResponseAdapterFactory
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.webService.ApiBase
import com.dot_jo.whysalon.data.webService.ApiInterface
import com.dot_jo.whysalon.util.Constants
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor

 import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    val interceptor = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("lang", PrefsHelper.getLanguage())
                .addHeader("token",  PrefsHelper.getToken()!!)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .addInterceptor(interceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    var gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideClient(): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiBase.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()


    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideRepository(
        api: ApiInterface
    ): Repository = Repository(api)

}
