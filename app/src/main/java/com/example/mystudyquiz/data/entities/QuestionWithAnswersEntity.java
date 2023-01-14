package com.example.mystudyquiz.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.Question;

import java.util.List;

public class QuestionWithAnswersEntity {
    @Embedded public Question questionEntity;
    @Relation(
            parentColumn = "questionId",
            entityColumn = "questionId"
    )
    public List<Answer> answerList;
}
