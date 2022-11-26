package com.example.mystudyquiz.model;

public class BooleanQuestion extends Question{
    private Answer wrongBooleanAnswer;

    public BooleanQuestion(String text, Answer correctAnswer) {
        super(text,QuestionType.BOOLEAN, correctAnswer);
    }

    public BooleanQuestion(String text) {
        super(text,QuestionType.BOOLEAN);
    }

    public Answer getWrongBooleanAnswer() {
        return wrongBooleanAnswer;
    }

    public void setWrongBooleanAnswer(Answer wrongBooleanAnswer) {
        this.wrongBooleanAnswer = wrongBooleanAnswer;
    }

    @Override
    public Answer getAnswerByText(String text) {
        if (correctAnswer.getText().equalsIgnoreCase(text))
            return correctAnswer;
        if(wrongBooleanAnswer.getText().equalsIgnoreCase(text))
            return wrongBooleanAnswer;
        return null;
    }
}
