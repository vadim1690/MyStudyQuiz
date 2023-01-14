package com.example.mystudyquiz.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "answer_table",foreignKeys = {@ForeignKey(entity = Question.class,
        parentColumns = "questionId",
        childColumns = "questionId",
        onDelete = ForeignKey.CASCADE)
})
public class Answer {

    @PrimaryKey()
    @NonNull
    private String answerId;


    private String text;


    private String questionId;

    public Answer(@NonNull String answerId){
        this.answerId = answerId;
    }
    @Ignore
    public Answer(String text,String questionId) {
        answerId = UUID.randomUUID().toString();
        this.text = text;
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(@NonNull String answerId) {
        this.answerId = answerId;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
