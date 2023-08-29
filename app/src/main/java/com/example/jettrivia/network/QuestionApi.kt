package com.example.jettrivia.network

import com.example.jettrivia.model.QuestionItem
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {
    @GET("world.json")
    suspend fun getQuestionList(): ArrayList<QuestionItem>
}