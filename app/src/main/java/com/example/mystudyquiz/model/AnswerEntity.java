package com.example.mystudyquiz.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "answer_table",foreignKeys = {
        @ForeignKey(entity = QuestionEntity.class,parentColumns = "id",childColumns = "question_id",onDelete = CASCADE)
})
public class AnswerEntity {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "text")
    private String text;


    @ColumnInfo(name = "question_id")
    private String questionId;


    public AnswerEntity(String text) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
