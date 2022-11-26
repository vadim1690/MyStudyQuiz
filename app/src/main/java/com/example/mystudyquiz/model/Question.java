package com.example.mystudyquiz.model;

public abstract class Question {
    private String text;
    protected Answer correctAnswer;
    private QuestionType questionType;

    public Question(String text,QuestionType questionType) {
        this.text = text;
        this.questionType = questionType;
    }

    public Question(String text,QuestionType questionType, Answer correctAnswer) {
        this(text,questionType);
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

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public boolean computeAnswer(Answer answer) {
        return answer.getText().equalsIgnoreCase(correctAnswer.getText());
    }

    public Answer getAnswerByText(String text) {
        if (correctAnswer.getText().equalsIgnoreCase(text))
            return correctAnswer;
        else return null;
    }
}
