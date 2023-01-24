package com.studyquiz.mystudyquiz.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.studyquiz.mystudyquiz.data.AnswerDao;
import com.studyquiz.mystudyquiz.data.AppDatabase;
import com.studyquiz.mystudyquiz.data.QuestionDao;
import com.studyquiz.mystudyquiz.data.QuizDao;
import com.studyquiz.mystudyquiz.data.entities.QuestionWithAnswersEntity;
import com.studyquiz.mystudyquiz.data.entities.QuizStatistic;
import com.studyquiz.mystudyquiz.model.Question;
import com.studyquiz.mystudyquiz.model.Quiz;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuizRepository {
    private MutableLiveData<List<Quiz>> quizzes;
    private List<Quiz> list;
    private QuizDao quizDao;
    private QuestionDao questionDao;
    private AnswerDao answerDao;
    private Context context;

    public QuizRepository(Context context) {
        this.context = context;
        quizzes = new MutableLiveData<>();
        quizDao = AppDatabase.getInstance(context).quizDao();
        questionDao = AppDatabase.getInstance(context).questionDao();
        answerDao = AppDatabase.getInstance(context).answerDao();

    }

    public LiveData<List<Quiz>> getQuizzes() {

        return quizDao.getAllQuizzes();
    }

    public void addNewQuiz(Quiz quiz) {
        AppDatabase.databaseExecutor.execute(() -> quizDao.insertQuiz(quiz));
    }

    public LiveData<List<Question>> getQuestionsForQuiz(String quizId) {
        return questionDao.getQuestionsForQuiz(quizId);
    }

    public void addNewQuestionToCurrentQuiz(Question question) {
        AppDatabase.databaseExecutor.execute(() -> {
            questionDao.insertQuestion(question);
            insertAnswers(question);
        });

    }

    private void insertAnswers(Question question) {
        question.getAnswerList().forEach(answer -> AppDatabase.databaseExecutor.execute(() -> answerDao.insertAnswer(answer)));

    }

    public void getAllQuestionsWithAnswersForQuiz(String quizId, List<Question> questions, QuizViewModel.QuestionsWithAnswersCallback questionsWithAnswersCallback) {
        Handler handler = new Handler(Looper.getMainLooper());
        AppDatabase.databaseExecutor.execute(() -> {
            List<Question> questionList = new ArrayList<>();
            List<QuestionWithAnswersEntity> list = questionDao.getQuestionWithAnswers(quizId);
            list.forEach(questionWithAnswersEntity -> {

                questionWithAnswersEntity.questionEntity.setAnswerList(questionWithAnswersEntity.answerList);

                questionWithAnswersEntity.
                        answerList.
                        stream().
                        filter(answer -> answer.getAnswerId().equalsIgnoreCase(questionWithAnswersEntity.questionEntity.getCorrectAnswerId())).
                        findFirst().ifPresent(oAnswer -> questionWithAnswersEntity.questionEntity.setCorrectAnswer(oAnswer));

                questionList.add(questionWithAnswersEntity.questionEntity);
            });
            handler.post(() -> questionsWithAnswersCallback.onAllQuestionsReady(questionList));
        });
    }

    public void deleteQuiz(Quiz quiz) {
        AppDatabase.databaseExecutor.execute(() -> quizDao.deleteQuiz(quiz));
    }

    public void deleteQuestion(Question question) {
        AppDatabase.databaseExecutor.execute(() -> questionDao.deleteQuestion(question));

    }

    public void updateQuiz(Quiz quiz) {
        AppDatabase.databaseExecutor.execute(() -> quizDao.updateQuiz(quiz));
    }


    public void getAllQuizStatistics(List<Quiz> quizzes, QuizViewModel.StatisticsCallback statisticsCallback) {
        Handler handler = new Handler(Looper.getMainLooper());
        AppDatabase.databaseExecutor.execute(() -> {
            List<QuizStatistic> quizStatisticList = new ArrayList<>();
            quizzes.forEach(quiz -> {
                QuizStatistic quizStatistic = getStatisticsForQuiz(quiz);
                quizStatisticList.add(quizStatistic);
            });
            handler.post(() -> statisticsCallback.onAllStatisticsCreated(quizStatisticList));
        });

    }

    private QuizStatistic getStatisticsForQuiz(Quiz quiz) {
        QuizStatistic quizStatistic = new QuizStatistic();
        quizStatistic.setQuizName(quiz.getName());
        quizStatistic.setTimesTaken(String.valueOf(quiz.getNumberOfTimesTaken()));
        quizStatistic.setAverage(String.format(Locale.getDefault(),"%,.2f", quiz.getAverage()));
        quizStatistic.setNumberOfCorrectTimes(String.valueOf(quiz.getNumberOfCorrectTimes()));
        quizStatistic.setNumberOfWrongTimes(String.valueOf(quiz.getNumberOfWrongTimes()));
        Question hardestQuestion = getQuestionById(quiz.getHardestQuestionId());
        Question easiestQuestion = getQuestionById(quiz.getEasiestQuestionId());
        if(hardestQuestion != null){
            quizStatistic.setHardestQuestion(hardestQuestion.getText());
            quizStatistic.setHardestQuestionCorrectTimes(String.valueOf(hardestQuestion.getNumberOfTimesCorrect()));
            quizStatistic.setHardestQuestionWrongTimes(String.valueOf(hardestQuestion.getNumberOfTimesWrong()));
        }
        if(easiestQuestion != null){
            quizStatistic.setEasiestQuestion(easiestQuestion.getText());
            quizStatistic.setEasiestQuestionCorrectTimes(String.valueOf(easiestQuestion.getNumberOfTimesCorrect()));
            quizStatistic.setEasiestQuestionWrongTimes(String.valueOf(easiestQuestion.getNumberOfTimesWrong()));
        }

        return quizStatistic;

    }

    private Question getQuestionById(String questionId) {
        return questionDao.getQuestionById(questionId);
    }

    public void updateQuizWithQuestions(Quiz selectedQuiz) {
        AppDatabase.databaseExecutor.execute(() -> selectedQuiz.getQuestions().getQuestionList().forEach(question -> {
            questionDao.updateQuestion(question);
        }));
        updateQuiz(selectedQuiz);
    }
}
