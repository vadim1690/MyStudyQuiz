package com.example.mystudyquiz.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mystudyquiz.data.entities.QuizWithQuestionAndAnswersEntity;
import com.example.mystudyquiz.model.Quiz;

import java.util.List;


@Dao
public interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertQuiz(Quiz quizEntity);

    @Transaction
    @Query("SELECT * FROM quiz_table WHERE :quizId = quizId  ")
    public LiveData<QuizWithQuestionAndAnswersEntity> getQuiz(String quizId);


    @Query("SELECT * FROM quiz_table")
    public LiveData<List<Quiz>> getAllQuizzes();

    @Update
    void updateQuiz(Quiz quiz);
    @Delete
    void deleteQuiz(Quiz quiz);

}
