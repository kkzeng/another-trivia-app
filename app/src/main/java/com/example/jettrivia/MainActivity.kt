package com.example.jettrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettrivia.ui.theme.JetTriviaTheme
import com.example.jettrivia.ui.theme.Purple40
import com.example.jettrivia.ui.theme.PurpleGrey80
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
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    color = Color.LightGray
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
        CircularProgressIndicator()
    }
    else {
        QuestionDisplay(viewModel)
    }
}

@Composable
fun QuestionText(text: String) {
    Text(modifier = Modifier.padding(16.dp),
        color = PurpleGrey80,
        fontSize = 24.sp,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        text = text)
}

@Composable
fun QuestionDisplay(viewModel: QuestionsViewModel) {
    val nextQuestionButtonVisible = remember {
        mutableStateOf(false)
    }

    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp),
        color = Color.DarkGray
    ) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top){
            val questionsList = viewModel.data.value.data!!
            val index = viewModel.index.value
            val currentQuestion = questionsList[index]
            QuestionTracker(currentCount = index+1, outOf = questionsList.size)
            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(2.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                color = PurpleGrey80)
            QuestionText(text = currentQuestion.question)

            val selectedIndex: MutableState<Int> = remember {
                mutableStateOf(-1)
            }

            val correctAnswerSelected: MutableState<Boolean> = remember {
                mutableStateOf(false)
            }

            AnswerRadioButtons(
                choices = currentQuestion.choices,
                answer = currentQuestion.answer,
                selectedIndex = selectedIndex,
                correctAnswerSelected = correctAnswerSelected,
                isNextButtonVisible = nextQuestionButtonVisible
            )

            // This is shown when user has answered the question correctly
            if (nextQuestionButtonVisible.value) {
                NextQuestionButton(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    viewModel.index.value = index + 1

                    // Reset state
                    selectedIndex.value = -1
                    correctAnswerSelected.value = false
                    nextQuestionButtonVisible.value = false
                }
            }
        }
    }
}

@Composable
fun AnswerRadioButtons(choices: List<String>, answer: String, selectedIndex: MutableState<Int>, correctAnswerSelected: MutableState<Boolean>, isNextButtonVisible: MutableState<Boolean>) {
    val optionHeight = 30
    val optionPadding = 10
    val totalHeight = choices.size * (optionHeight + optionPadding)
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .height(totalHeight.dp),
        userScrollEnabled = false) {
        itemsIndexed(choices) { index, choiceString ->
            Row(modifier = Modifier
                .height(optionHeight.dp)
                .padding(top = optionPadding.dp)
                .clickable(enabled = !correctAnswerSelected.value) {
                    selectedIndex.value = index
                    correctAnswerSelected.value = choices[selectedIndex.value] == answer
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {
                RadioButton(
                    modifier = Modifier.fillMaxHeight(),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.White,
                        unselectedColor = Color.White,
                        disabledSelectedColor = Color.White,
                        disabledUnselectedColor = Color.White
                    ),
                    selected = selectedIndex.value == index,
                    enabled = !correctAnswerSelected.value,
                    onClick = {
                    selectedIndex.value = index
                    correctAnswerSelected.value = choices[selectedIndex.value] == answer
                })
                Text(
                    text = choiceString,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = PurpleGrey80
                )
                if (correctAnswerSelected.value && selectedIndex.value == index) {
                    Icon(modifier = Modifier.padding(start = 6.dp, end = 6.dp), imageVector = Icons.Default.CheckCircle, tint = Color.Green, contentDescription = "Tick for correct answer")
                    isNextButtonVisible.value = true
                }
            }
        }
    }
}

@Composable
fun NextQuestionButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier
            .fillMaxWidth(0.7f)
            .padding(10.dp),
        onClick = onClick) {
        Text(
            text = "Next Question",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun QuestionTracker(currentCount: Int = 1, outOf: Int = 1000) {
    val progressText = buildAnnotatedString {
        withStyle(ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(SpanStyle(
                color = Purple40,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold)) {
                append("Question $currentCount/")
            }
            withStyle(SpanStyle(
                color = PurpleGrey80,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium)) {
                append("$outOf")
            }
        }
    }
    Text(modifier = Modifier.padding(16.dp),
        text = progressText)
}