package com.example.jettrivia.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettrivia.data.QuestionRepository
import com.example.jettrivia.model.QuestionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repository: QuestionRepository): ViewModel() {
    val currentQuestionIndex: MutableState<Int> = mutableStateOf(0)
    val selectedIndex: MutableState<Int> = mutableStateOf(-1)
    val score: MutableState<Int> = mutableStateOf(0)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val questions: MutableState<List<QuestionItem>> = mutableStateOf(listOf())
    val isNextButtonVisible: MutableState<Boolean> = mutableStateOf(false)

    val finalAnswers: MutableList<Int> = mutableListOf()

    init {
        fetchAllQuestions()
    }

    fun isChoiceSelected(): Boolean = selectedIndex.value != -1

    fun isCorrectAnswerSelected(): Boolean {
        val questionIndex = currentQuestionIndex.value
        val currentQuestion = questions.value[questionIndex]
        val selectedAnswerText = currentQuestion.choices.getOrNull(selectedIndex.value)
        return selectedAnswerText == currentQuestion.answer
    }

    fun handleNextButtonClick() {
        // Reset state
        selectedIndex.value = -1
        isNextButtonVisible.value = false
        
        currentQuestionIndex.value += 1
    }

    fun handleAnswerClick(index: Int) {
        selectedIndex.value = index
        isNextButtonVisible.value = true
    }

    private fun fetchAllQuestions() {
        viewModelScope.launch {
            isLoading.value = true
            questions.value = repository.getListOfQuestions().shuffled()
            isLoading.value = false
        }
    }
}