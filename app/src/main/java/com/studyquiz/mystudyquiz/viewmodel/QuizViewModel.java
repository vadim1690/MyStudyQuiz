package com.studyquiz.mystudyquiz.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.studyquiz.mystudyquiz.data.entities.QuizStatistic;
import com.studyquiz.mystudyquiz.model.Answer;
import com.studyquiz.mystudyquiz.model.AnswerReportType;
import com.studyquiz.mystudyquiz.model.Question;
import com.studyquiz.mystudyquiz.model.QuestionList;
import com.studyquiz.mystudyquiz.model.QuestionType;
import com.studyquiz.mystudyquiz.model.Quiz;
import com.studyquiz.mystudyquiz.repositories.QuizRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizViewModel extends AndroidViewModel {
    private QuizRepository quizRepository;
    private LiveData<List<Quiz>> quizzes;
    private Quiz selectedQuiz;
    private Map<Question, Answer> correctAnswers;
    private Map<Question, Answer> wrongAnswers;
    private AnswerReportType answersReportType;
    private Question currentQuestion;
    private Question newQuestionToAdd;
    private int currentQuestionIndex;
    private QuestionType newQuestionToAddType;
    private Answer selectedAnswer;
    private LiveData<List<Question>> questionsList;


    public interface QuizReadyCallback {
        void onReady();
    }

    public interface QuestionsWithAnswersCallback {
        void onAllQuestionsReady(List<Question> allQuestions);
    }

    public interface StatisticsCallback {
        void onAllStatisticsCreated(List<QuizStatistic> quizStatisticList);
    }

    public QuizViewModel(Application application) {
        super(application);
        this.quizRepository = new QuizRepository(application);
        currentQuestionIndex = -1;
        correctAnswers = new HashMap<>();
        wrongAnswers = new HashMap<>();
    }


    public void finishQuiz() {
        double newScore = ((double) getCorrectAnswersCounter() / (double) getSelectedQuiz().getSize()) * 100;
        int numberOfTimesTaken = selectedQuiz.getNumberOfTimesTaken();
        double oldAverage = selectedQuiz.getAverage();
        double newAverage = (oldAverage * numberOfTimesTaken + newScore) / (numberOfTimesTaken + 1);
        selectedQuiz.setAverage(newAverage);
        selectedQuiz.setNumberOfTimesTaken(selectedQuiz.getNumberOfTimesTaken() + 1);

        selectedQuiz.setNumberOfCorrectTimes(selectedQuiz.getNumberOfCorrectTimes() + correctAnswers.keySet().size());
        selectedQuiz.setNumberOfWrongTimes(selectedQuiz.getNumberOfWrongTimes() + wrongAnswers.keySet().size());
        correctAnswers.keySet().forEach(question -> {
            question.setNumberOfTimesCorrect(question.getNumberOfTimesCorrect() + 1);
        });
        wrongAnswers.keySet().forEach(question -> {
            question.setNumberOfTimesWrong(question.getNumberOfTimesWrong() + 1);
        });

        List<Question> currentQuizQuestions = selectedQuiz.getQuestions().getQuestionList();
        if (currentQuizQuestions != null) {
            Question mostCorrect = currentQuizQuestions.stream().max((q1, q2) -> (int) (q1.getNumberOfTimesCorrect() - q2.getNumberOfTimesCorrect())).orElse(null);
            Question mostWrong = currentQuizQuestions.stream().max((q1, q2) -> (int) (q1.getNumberOfTimesWrong() - q2.getNumberOfTimesWrong())).orElse(null);

            if (mostCorrect != null)
                selectedQuiz.setEasiestQuestionId(mostCorrect.getQuestionId());
            if (mostWrong != null)
                selectedQuiz.setHardestQuestionId(mostWrong.getQuestionId());

        }

        quizRepository.updateQuizWithQuestions(selectedQuiz);
    }


    public void getAllStatistics(StatisticsCallback statisticsCallback) {
        quizRepository.getAllQuizStatistics(quizzes.getValue(), statisticsCallback);
    }


    public void startCurrentQuiz(QuizReadyCallback quizReadyCallback) {

        quizRepository.getAllQuestionsWithAnswersForQuiz(selectedQuiz.getQuizId(), questionsList.getValue(), allQuestions -> {
            selectedQuiz.setQuestions(new QuestionList(allQuestions));
            quizReadyCallback.onReady();
        });
    }

    public void deleteQuiz(Quiz quiz) {
        quizRepository.deleteQuiz(quiz);
    }

    public void deleteQuestionFromCurrentQuiz(Question question) {
        selectedQuiz.setSize(selectedQuiz.getSize() - 1);
        quizRepository.deleteQuestion(question);
        quizRepository.updateQuiz(selectedQuiz);
    }

    public void addToCurrentQuizCapacity(int capacityToAdd) {
        selectedQuiz.setCapacity(selectedQuiz.getCapacity() + capacityToAdd);
        quizRepository.updateQuiz(selectedQuiz);
    }

    public void sortCurrentQuestionAnswersRandomly() {
        if (currentQuestion.getQuestionType() == QuestionType.MULTIPLE_CHOICE) {
            Collections.shuffle(currentQuestion.getAnswerList());
        }
    }


    public LiveData<List<Quiz>> getQuizzes() {
        quizzes = quizRepository.getQuizzes();
        return quizzes;
    }


    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
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
        return currentQuestionIndex >= selectedQuiz.getSize() - 1;
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
    public void computeSelectedAnswer() {
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

    public void clearViewModel() {
        currentQuizStartOver();
        quizzes = null;
        selectedQuiz = null;
        answersReportType = null;
        currentQuestion = null;
        newQuestionToAdd = null;
        newQuestionToAddType = null;
        selectedAnswer = null;
        questionsList = null;
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

    public void setNewQuestionToAdd(Question question) {
        newQuestionToAdd = question;
    }

    public Question getNewQuestionToAdd() {
        return newQuestionToAdd;
    }

    public void addNewQuestionToCurrentQuiz(Question question) {
        selectedQuiz.setSize(selectedQuiz.getSize() + 1);
        question.setQuizId(selectedQuiz.getQuizId());
        quizRepository.addNewQuestionToCurrentQuiz(question);
        quizRepository.updateQuiz(selectedQuiz);

    }

    public void setNewQuestionToAddType(QuestionType questionType) {
        newQuestionToAddType = questionType;
    }

    public QuestionType getNewQuestionToAddType() {
        return newQuestionToAddType;
    }

    public void setSelectedAnswer(Answer selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public LiveData<List<Question>> getQuestionsForQuiz(String quizId) {
        questionsList = quizRepository.getQuestionsForQuiz(quizId);
        return questionsList;
    }
}


