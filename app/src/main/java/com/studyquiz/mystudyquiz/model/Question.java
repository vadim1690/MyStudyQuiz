package com.studyquiz.mystudyquiz.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.UUID;

@Entity(tableName = "question_table",foreignKeys = {@ForeignKey(entity = Quiz.class,
        parentColumns = "quizId",
        childColumns = "quizId",
        onDelete = ForeignKey.CASCADE)
})
public class Question {

    @PrimaryKey()
    @NonNull
    private String questionId;

    private String text;
    @Ignore
    protected Answer correctAnswer;

    @Ignore
    private List<Answer> answerList;

    private QuestionType questionType;

    private String correctAnswerId;
    private String quizId;

    private long numberOfTimesWrong;
    private long numberOfTimesCorrect;

    public Question(){

    }

    @Ignore
    public Question(String text,QuestionType questionType) {
        questionId = UUID.randomUUID().toString();
        this.text = text;
        this.questionType = questionType;
    }
    @Ignore
    public Question(String text,QuestionType questionType, Answer correctAnswer) {
        this(text,questionType);
        setCorrectAnswer(correctAnswer);
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public String getText() {
        return text;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(@NonNull String questionId) {
        this.questionId = questionId;
    }

    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    public void setCorrectAnswerId(String correctAnswerId) {
        this.correctAnswerId = correctAnswerId;
    }


    public Answer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Answer correctAnswer) {
        this.correctAnswer = correctAnswer;
        this.correctAnswer.setQuestionId(getQuestionId());
        setCorrectAnswerId(correctAnswer.getAnswerId());
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
        return answerList.
                stream().
                filter(answer -> answer.getText().equalsIgnoreCase(text)).
                findFirst().
                orElse(null);

    }

    public long getNumberOfTimesCorrect() {
        return numberOfTimesCorrect;
    }

    public void setNumberOfTimesCorrect(long numberOfTimesCorrect) {
        this.numberOfTimesCorrect = numberOfTimesCorrect;
    }

    public long getNumberOfTimesWrong() {
        return numberOfTimesWrong;
    }

    public void setNumberOfTimesWrong(long numberOfTimesWrong) {
        this.numberOfTimesWrong = numberOfTimesWrong;
    }
}
