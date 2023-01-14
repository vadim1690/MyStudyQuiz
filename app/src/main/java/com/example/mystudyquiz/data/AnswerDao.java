package com.example.mystudyquiz.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mystudyquiz.data.entities.QuizWithQuestionAndAnswersEntity;
import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.Question;

import java.util.List;


@Dao
public interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAnswer(Answer answer);

//    @Transaction
//    @Query("SELECT * FROM quiz_table WHERE :quizId = quizId  ")
//    public LiveData<QuizWithQuestionAndAnswersEntity> getQuiz(String quizId);


    @Query("SELECT * FROM answer_table WHERE :questionId = questionId")
    public LiveData<List<Answer>> getAnswersForQuestion(String questionId);

}
