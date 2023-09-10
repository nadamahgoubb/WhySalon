package com.dot_jo.whysalon.di


import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.webService.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModul {

    @Singleton
    @Provides
    fun provideRepository(
        api: ApiInterface
    ): Repository = Repository(api)

}