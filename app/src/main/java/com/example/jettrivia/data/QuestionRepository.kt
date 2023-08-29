package com.example.jettrivia.data

import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    suspend fun getListOfQuestions(): List<QuestionItem> {
        return try {
            api.getQuestionList()
        }
        catch (e: Exception) {
            emptyList()
        }
    }
}