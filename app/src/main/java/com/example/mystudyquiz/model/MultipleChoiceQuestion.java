package com.example.mystudyquiz.model;

public class MultipleChoiceQuestion extends Question {

    private Answer answerOption1;
    private Answer answerOption2;
    private Answer answerOption3;
    private Answer answerOption4;

    public MultipleChoiceQuestion(String text) {
        super(text);
    }

    public MultipleChoiceQuestion(String text, Answer correctAnswer) {
        super(text, correctAnswer);
    }

    public Answer getAnswerOption1() {
        return answerOption1;
    }

    public void setAnswerOption1(Answer answerOption1) {
        this.answerOption1 = answerOption1;
    }

    public Answer getAnswerOption2() {
        return answerOption2;
    }

    public void setAnswerOption2(Answer answerOption2) {
        this.answerOption2 = answerOption2;
    }

    public Answer getAnswerOption3() {
        return answerOption3;
    }

    public void setAnswerOption3(Answer answerOption3) {
        this.answerOption3 = answerOption3;
    }

    public Answer getAnswerOption4() {
        return answerOption4;
    }

    public void setAnswerOption4(Answer answerOption4) {
        this.answerOption4 = answerOption4;
    }

    @Override
    public Answer getAnswerByText(String text) {
        if(answerOption1.getText().equalsIgnoreCase(text))
            return answerOption1;
        if(answerOption2.getText().equalsIgnoreCase(text))
            return answerOption2;
        if(answerOption3.getText().equalsIgnoreCase(text))
            return answerOption3;
        if(answerOption4.getText().equalsIgnoreCase(text))
            return answerOption4;

        return null;
    }

    public void setAnswerList(AnswerList answerList) {
        if (answerList.getSize() > 0)
            answerOption1 = answerList.getAnswerList().get(0);

        if (answerList.getSize() > 1)
            answerOption2 = answerList.getAnswerList().get(1);

        if (answerList.getSize() > 2)
            answerOption3 = answerList.getAnswerList().get(2);

        if (answerList.getSize() > 3)
            answerOption4 = answerList.getAnswerList().get(3);
    }
}
