package com.example.jettrivia.network

import com.example.jettrivia.model.QuestionList
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("world.json")
    suspend fun getQuestionList(): QuestionList
}