package com.example.mystudyquiz.model;

import java.io.Serializable;


public class Quiz implements Serializable {
    private String name;
    private QuestionList questions;

    public Quiz(String name) {
        this.name = name;
        questions = new QuestionList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return questions.getSize();
    }

    public QuestionList getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionList questions) {
        this.questions = questions;
    }
}
