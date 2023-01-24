package com.studyquiz.mystudyquiz.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.studyquiz.mystudyquiz.data.entities.QuestionWithAnswersEntity;
import com.studyquiz.mystudyquiz.model.Question;

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
