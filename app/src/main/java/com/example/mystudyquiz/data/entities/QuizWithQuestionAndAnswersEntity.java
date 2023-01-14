package com.example.mystudyquiz.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.Quiz;

import java.util.List;

public class QuizWithQuestionAndAnswersEntity {
    @Embedded public Quiz quiz;
    @Relation(
            entity = Question.class,
            parentColumn = "quizId",
            entityColumn = "quizId"
    )
    public List<QuestionWithAnswersEntity> questions;
}
