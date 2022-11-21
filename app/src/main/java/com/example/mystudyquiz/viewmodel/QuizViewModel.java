package com.example.mystudyquiz.viewmodel;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.AnswerReportType;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.repositories.QuizRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizViewModel extends ViewModel {
    private QuizRepository quizRepository;
    private MutableLiveData<List<Quiz>> quizzes;
    private Quiz selectedQuiz;
    private Map<Question, Answer> correctAnswers;
    private Map<Question, Answer> wrongAnswers;
    private AnswerReportType answersReportType;
    private Question currentQuestion;
    private int currentQuestionIndex;

    public QuizViewModel() {
        this.quizRepository = new QuizRepository();
        currentQuestionIndex = -1;
        correctAnswers = new HashMap<>();
        wrongAnswers = new HashMap<>();
    }

    public MutableLiveData<List<Quiz>> getQuizzes() {
        quizzes = quizRepository.getQuizzes();
        return quizzes;
    }

    public Map<Question, Answer> getCorrectAnswers() {
        return correctAnswers;
    }

    public Map<Question, Answer> getWrongAnswers() {
        return wrongAnswers;
    }

    public Quiz getSelectedQuiz() {
        return selectedQuiz;
    }

    public void setSelectedQuiz(Quiz selectedQuiz) {
        this.selectedQuiz = selectedQuiz;
    }

    public int getCorrectAnswersCounter() {
        return correctAnswers != null ? correctAnswers.size() : 0;
    }

    public int getWrongAnswersCounter() {
        return wrongAnswers != null ? wrongAnswers.size() : 0;
    }

    /**
     * return whether the quiz is finished or not.
     *
     * @return true if the quiz is finished, false if not.
     */
    public boolean isQuizFinished() {
        return currentQuestionIndex == selectedQuiz.getSize() - 1;
    }

    /**
     * returns the next question of the current quiz and increment the quiz question counter.
     *
     * @return the next question of the current quiz
     */

    public Question getNextQuestionForQuiz() {
        currentQuestionIndex++;
        currentQuestion = selectedQuiz.getQuestions().getQuestionList().get(currentQuestionIndex);
        return currentQuestion;
    }


    /**
     * Computes if the answer selected by the user is the right answer to the question and if it is than it increments the correct answers counter.
     */
    public void computeSelectedAnswer(Answer selectedAnswer) {
        if (selectedAnswer != null && currentQuestion.computeAnswer(selectedAnswer)) {
            correctAnswers.put(currentQuestion, selectedAnswer);
        } else {
            wrongAnswers.put(currentQuestion, selectedAnswer);
        }
    }

    public void currentQuizStartOver() {
        currentQuestionIndex = -1;
        correctAnswers = new HashMap<>();
        wrongAnswers = new HashMap<>();
    }

    public AnswerReportType getAnswersReportType() {
        return answersReportType;
    }

    public boolean setAnswersReportType(AnswerReportType answersReportType) {
        if (answersReportType == AnswerReportType.WRONG_ANSWERS && wrongAnswers.size() == 0)
            return false;

        if (answersReportType == AnswerReportType.CORRECT_ANSWERS && correctAnswers.size() == 0)
            return false;

        this.answersReportType = answersReportType;
        return true;
    }

    public Answer getAnswerByText(String text) {
        return currentQuestion.getAnswerByText(text);
    }

    public Answer getCurrentCorrectAnswer() {
        return currentQuestion.getCorrectAnswer();
    }

    public void addNewQuiz(Quiz quiz) {
        quizRepository.addNewQuiz(quiz);

    }
}
