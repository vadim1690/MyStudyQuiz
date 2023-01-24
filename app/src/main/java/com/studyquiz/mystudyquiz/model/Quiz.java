package com.studyquiz.mystudyquiz.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.studyquiz.mystudyquiz.Constants;

import java.util.UUID;

@Entity(tableName = "quiz_table")
public class Quiz {


    private String name;

    @PrimaryKey()
    @NonNull
    private String quizId;


    private int backgroundColor;

    private int size;

    private int capacity;

    private int numberOfTimesTaken;

    private double average;

    private long numberOfCorrectTimes;
    private long numberOfWrongTimes;

    private String hardestQuestionId;
    private String easiestQuestionId;

    @Ignore
    private QuestionList questions;

    public Quiz() {

    }

    @Ignore
    public Quiz(String name) {
        quizId = UUID.randomUUID().toString();
        this.name = name;
        capacity = Constants.QUIZ_QUESTIONS_FREE_MAX_CAPACITY;
        questions = new QuestionList();
    }

    @Ignore
    public Quiz(String name, int color) {
        this(name);
        this.backgroundColor = color;
    }

    @NonNull
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(@NonNull String quizId) {
        this.quizId = quizId;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public QuestionList getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionList questions) {
        this.questions = questions;
    }

    public long getNumberOfCorrectTimes() {
        return numberOfCorrectTimes;
    }


    public long getNumberOfWrongTimes() {
        return numberOfWrongTimes;
    }

    public void setNumberOfWrongTimes(long numberOfWrongTimes) {
        this.numberOfWrongTimes = numberOfWrongTimes;
    }

    public String getHardestQuestionId() {
        return hardestQuestionId;
    }

    public void setHardestQuestionId(String hardestQuestionId) {
        this.hardestQuestionId = hardestQuestionId;
    }

    public String getEasiestQuestionId() {
        return easiestQuestionId;
    }

    public void setEasiestQuestionId(String easiestQuestionId) {
        this.easiestQuestionId = easiestQuestionId;
    }

    public int getNumberOfTimesTaken() {
        return numberOfTimesTaken;
    }

    public void setNumberOfTimesTaken(int numberOfTimesTaken) {
        this.numberOfTimesTaken = numberOfTimesTaken;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public void setNumberOfCorrectTimes(long numberOfCorrectTimes) {
        this.numberOfCorrectTimes = numberOfCorrectTimes;
    }
}
