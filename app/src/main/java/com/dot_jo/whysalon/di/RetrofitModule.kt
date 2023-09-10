package com.dot_jo.whysalon.di

import com.dot_jo.whysalon.base.NetworkResponseAdapterFactory
import com.dot_jo.whysalon.data.PrefsHelper
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

    /*
    @Provides
    @Singleton
    fun provideRetrofit(): ApiInterface {
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(
            LoggingInterceptor.Builder().setLevel(Level.BASIC)
                .addHeader("Token" ,PrefsHelper.getToken()!!)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("lang",  PrefsHelper.getLanguage())
                .build()
        )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiBase.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiInterface::class.java)
    }*/

    @Provides
    @Singleton
    fun proideokHttpClient():OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
            .apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        return OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)
                    .addHeader("Token" ,PrefsHelper.getToken()!!)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("lang",  PrefsHelper.getLanguage())

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    }


    var gson = GsonBuilder()
        .setLenient().disableHtmlEscaping()
        .create()

    @Provides
    @Singleton
    @Named("Retrofit")
    fun provideClient(okHttpClient:OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiBase.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()


    @Provides
    @Singleton
    fun provideApiInterface( @Named("Retrofit") retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)


}