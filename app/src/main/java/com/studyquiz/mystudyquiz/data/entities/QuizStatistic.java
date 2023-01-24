package com.studyquiz.mystudyquiz.data.entities;

public class QuizStatistic {

    private String quizName;
    private String timesTaken;
    private String average;
    private String numberOfCorrectTimes;
    private String numberOfWrongTimes;
    private String hardestQuestion;
    private String hardestQuestionCorrectTimes;
    private String hardestQuestionWrongTimes;
    private String easiestQuestion;
    private String easiestQuestionCorrectTimes;
    private String easiestQuestionWrongTimes;



    public QuizStatistic() {
    }

    public String getEasiestQuestion() {
        return easiestQuestion;
    }

    public void setEasiestQuestion(String easiestQuestion) {
        this.easiestQuestion = easiestQuestion;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getNumberOfCorrectTimes() {
        return numberOfCorrectTimes;
    }

    public void setNumberOfCorrectTimes(String numberOfCorrectTimes) {
        this.numberOfCorrectTimes = numberOfCorrectTimes;
    }

    public String getNumberOfWrongTimes() {
        return numberOfWrongTimes;
    }

    public void setNumberOfWrongTimes(String numberOfWrongTimes) {
        this.numberOfWrongTimes = numberOfWrongTimes;
    }

    public String getHardestQuestion() {
        return hardestQuestion;
    }

    public void setHardestQuestion(String hardestQuestion) {
        this.hardestQuestion = hardestQuestion;
    }

    public String getHardestQuestionCorrectTimes() {
        return hardestQuestionCorrectTimes;
    }

    public void setHardestQuestionCorrectTimes(String hardestQuestionCorrectTimes) {
        this.hardestQuestionCorrectTimes = hardestQuestionCorrectTimes;
    }

    public String getHardestQuestionWrongTimes() {
        return hardestQuestionWrongTimes;
    }

    public void setHardestQuestionWrongTimes(String hardestQuestionWrongTimes) {
        this.hardestQuestionWrongTimes = hardestQuestionWrongTimes;
    }

    public String getEasiestQuestionCorrectTimes() {
        return easiestQuestionCorrectTimes;
    }

    public void setEasiestQuestionCorrectTimes(String easiestQuestionCorrectTimes) {
        this.easiestQuestionCorrectTimes = easiestQuestionCorrectTimes;
    }

    public String getEasiestQuestionWrongTimes() {
        return easiestQuestionWrongTimes;
    }

    public void setEasiestQuestionWrongTimes(String easiestQuestionWrongTimes) {
        this.easiestQuestionWrongTimes = easiestQuestionWrongTimes;
    }

    public String getTimesTaken() {
        return timesTaken;
    }

    public void setTimesTaken(String timesTaken) {
        this.timesTaken = timesTaken;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
