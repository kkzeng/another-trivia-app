package com.example.jettrivia.di

import com.example.jettrivia.constants.Constants
import com.example.jettrivia.data.QuestionRepository
import com.example.jettrivia.network.QuestionApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideQuestionRepository(questionApi: QuestionApi): QuestionRepository = QuestionRepository(questionApi)

    @Singleton
    @Provides
    fun provideWorldQuestionsApi(): QuestionApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApi::class.java)
    }
}