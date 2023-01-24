package com.studyquiz.mystudyquiz.data.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.studyquiz.mystudyquiz.model.Answer;
import com.studyquiz.mystudyquiz.model.Question;

import java.util.List;

public class QuestionWithAnswersEntity {
    @Embedded public Question questionEntity;
    @Relation(
            parentColumn = "questionId",
            entityColumn = "questionId"
    )
    public List<Answer> answerList;
}
