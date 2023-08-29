package com.example.jettrivia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
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
import com.example.jettrivia.ui.theme.Purple40
import com.example.jettrivia.ui.theme.PurpleGrey80
import com.example.jettrivia.ui.viewmodels.QuestionsViewModel

@Composable
fun Question(viewModel: QuestionsViewModel) {
    if (viewModel.isLoading.value) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
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
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp),
        color = Color.DarkGray
    ) {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top){
            val questionsList = viewModel.questions.value
            val index = viewModel.currentQuestionIndex.value
            val currentQuestion = questionsList[index]
            QuestionTracker(currentCount = index+1, outOf = questionsList.size)
            ScoreTracker(score = viewModel.score)
            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(2.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                color = PurpleGrey80
            )
            QuestionText(text = currentQuestion.question)

            AnswerRadioButtons(choices = currentQuestion.choices, viewModel = viewModel)

            // This is shown when user has answered the question correctly
            if (viewModel.isNextButtonVisible.value) {
                NextQuestionButton(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    viewModel.handleNextButtonClick()
                }
            }
        }
    }
}

@Composable
fun AnswerRadioButtons(choices: List<String>, viewModel: QuestionsViewModel) {
    val optionHeight = 50
    val optionPadding = 10
    val totalHeight = choices.size * optionHeight
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        // Set fixed height for scrolling parent Column
        .height(totalHeight.dp),
        userScrollEnabled = false) {
        itemsIndexed(choices) { index, choiceString ->
            Row(modifier = Modifier
                .height(optionHeight.dp)
                .padding(top = optionPadding.dp, bottom = optionPadding.dp)
                .clickable(enabled = !viewModel.isChoiceSelected()) { viewModel.handleAnswerClick(index) },
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
                    selected = viewModel.selectedIndex.value == index,
                    enabled = !viewModel.isChoiceSelected(),
                    onClick = { viewModel.handleAnswerClick(index) }
                )
                Text(
                    text = choiceString,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = PurpleGrey80
                )
                if (viewModel.selectedIndex.value == index) {
                    val iconModifier = Modifier.padding(start = 6.dp, end = 6.dp)
                        .align(Alignment.CenterVertically)
                    if (viewModel.isCorrectAnswerSelected()) {
                        Icon(
                            modifier = iconModifier,
                            imageVector = Icons.Default.Check,
                            tint = Color.Green,
                            contentDescription = "Tick for correct answer"
                        )
                    }
                    else {
                        Icon(modifier = iconModifier,
                            imageVector = Icons.Default.Clear,
                            tint = Color.Red,
                            contentDescription = "X for wrong answer")
                    }
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
            withStyle(
                SpanStyle(
                color = Purple40,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold)
            ) {
                append("Question $currentCount/")
            }
            withStyle(
                SpanStyle(
                color = PurpleGrey80,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium)
            ) {
                append("$outOf")
            }
        }
    }
    Text(modifier = Modifier.padding(16.dp),
        text = progressText)
}

@Composable
fun ScoreTracker(score: MutableState<Int>) {
    val progressText = buildAnnotatedString {
        withStyle(ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                SpanStyle(
                color = Purple40,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold)
            ) {
                append("Score ")
            }
            withStyle(
                SpanStyle(
                color = PurpleGrey80,
                fontSize = 27.sp,
                fontWeight = FontWeight.Medium)
            ) {
                append("${score.value}")
            }
        }
    }
    Text(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        text = progressText)
}