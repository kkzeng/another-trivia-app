package com.example.jettrivia.data

import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {
    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Exception>()
    suspend fun getListOfQuestions(): DataOrException<ArrayList<QuestionItem>, Exception> {
        try {
            dataOrException.isLoading = true
            dataOrException.data = api.getQuestionList()
            if (dataOrException.data.toString().isNotEmpty()) {
                dataOrException.isLoading = false
            }
        }
        catch (e: Exception) {
            dataOrException.exception = e
        }
        return dataOrException
    }
}