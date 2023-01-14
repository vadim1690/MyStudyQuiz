package com.example.mystudyquiz.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mystudyquiz.data.entities.QuestionWithAnswersEntity;
import com.example.mystudyquiz.data.entities.QuizWithQuestionAndAnswersEntity;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.Quiz;

import java.util.List;


@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertQuestion(Question question);

    @Transaction
    @Query("SELECT * FROM question_table WHERE :quizId = quizId  ")
    List<QuestionWithAnswersEntity> getQuestionWithAnswers(String quizId);


    @Query("SELECT * FROM question_table WHERE :quizId = quizId")
    LiveData<List<Question>> getQuestionsForQuiz(String quizId);

    @Delete
    void deleteQuestion(Question question);

    @Query("SELECT * FROM question_table WHERE :questionId = questionId")
    Question getQuestionById(String questionId);

    @Update
    void updateQuestion(Question question);
}
