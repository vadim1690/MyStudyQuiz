package com.example.mystudyquiz.model;


import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "question_table",foreignKeys = {
        @ForeignKey(entity = QuizEntity.class,parentColumns = "id",childColumns = "quiz_id",onDelete = CASCADE),
        @ForeignKey(entity = AnswerEntity.class,parentColumns = "id",childColumns = "correct_answer_id",onDelete = CASCADE)
})
public class QuestionEntity  {


    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "id")
    private String id;


    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "correct_answer_id")
    private String correctAnswerId;

    @ColumnInfo(name = "quiz_id")
    private String quizId;

    @ColumnInfo(name = "question_type")
    private QuestionType questionType;

    public QuestionEntity(String quizId,String text, QuestionType questionType) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.questionType = questionType;
        this.quizId = quizId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    public void setCorrectAnswerId(String correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
