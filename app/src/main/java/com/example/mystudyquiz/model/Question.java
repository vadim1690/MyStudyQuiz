package com.example.mystudyquiz.model;

public class Question {
    private String text;
    private Answer correctAnswer;
    private boolean isCorrect;

    public Question(String text) {
        this.text = text;
    }

    public Question(String text, Answer correctAnswer) {
        this(text);
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    public boolean computeAnswer(Answer answer) {
        isCorrect = answer.getText().equalsIgnoreCase(correctAnswer.getText());
        return isCorrect;
    }

    public Answer getAnswerByText(String text) {
        if (correctAnswer.getText().equalsIgnoreCase(text))
            return correctAnswer;
        else return null;
    }
}
