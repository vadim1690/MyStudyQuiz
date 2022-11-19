package com.example.mystudyquiz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuestionList {
    private List<Question> questionList;

    public QuestionList() {
        this.questionList = new ArrayList<>();

    }
    public QuestionList(List<Question> questionList) {
        this.questionList = questionList;

    }

    public QuestionList(Set<Question> questionSet) {
        questionList = new ArrayList<>(questionSet);

    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public int getSize() {
        return questionList.size();
    }

    public void addQuestion(Question question){
        questionList.add(question);
    }
}
