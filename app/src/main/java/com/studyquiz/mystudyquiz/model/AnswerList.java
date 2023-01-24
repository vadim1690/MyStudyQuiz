package com.studyquiz.mystudyquiz.model;

import java.util.List;

public class AnswerList {
    private List<Answer> answerList;

    public AnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public int getSize(){
        return answerList.size();
    }
}
