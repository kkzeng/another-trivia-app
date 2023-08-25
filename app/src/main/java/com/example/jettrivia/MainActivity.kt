package com.example.jettrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettrivia.ui.theme.JetTriviaTheme
import com.example.jettrivia.ui.viewmodels.QuestionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTriviaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TriviaHome()
                }
            }
        }
    }
}

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel()) {
    Question(viewModel = viewModel)
}

@Composable
fun Question(viewModel: QuestionsViewModel) {
    // ArrayList must be converted to MutableList for Composition
    // TODO: Try to remove toMutableList() to see if Composition occurs properly
    val questions = viewModel.data.value.data?.toMutableList()
    if (viewModel.data.value.isLoading) {
        // Something
    }
    else {

    }
}