package com.studyquiz.mystudyquiz.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.studyquiz.mystudyquiz.model.Question;
import com.studyquiz.mystudyquiz.model.Quiz;

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
