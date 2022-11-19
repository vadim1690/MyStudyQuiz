package com.example.mystudyquiz.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.AnswerList;
import com.example.mystudyquiz.model.MultipleChoiceQuestion;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.QuestionList;
import com.example.mystudyquiz.model.Quiz;

import java.util.Arrays;
import java.util.List;

public class QuizRepository {
    private MutableLiveData<List<Quiz>> quizzes;

    public QuizRepository() {
        quizzes = new MutableLiveData<>();
        setData();
    }

    public MutableLiveData<List<Quiz>> getQuizzes() {
        return quizzes;
    }

    private void setData() {
        MultipleChoiceQuestion question1 = new MultipleChoiceQuestion("What country has the highest life expectancy?", new Answer("Hong Kong"));
        question1.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Hong Kong"),
                new Answer("Tel-Aviv"),
                new Answer("Paris"),
                new Answer("USA")
        )));
        MultipleChoiceQuestion question2 = new MultipleChoiceQuestion("Where would you be if you were standing on the Spanish Steps?", new Answer("Rome"));
        question2.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Hong Kong"),
                new Answer("Tel-Aviv"),
                new Answer("Russia"),
                new Answer("USA")
        )));
        MultipleChoiceQuestion question3 = new MultipleChoiceQuestion("Which language has the more native speakers: English or Spanish?", new Answer("Spanish"));
        question3.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Spanish"),
                new Answer("English"),
                new Answer("German"),
                new Answer("Chinese")
        )));
        MultipleChoiceQuestion question4 = new MultipleChoiceQuestion("What is the most common surname in the United States?", new Answer("Smith"));
        question4.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Hank"),
                new Answer("David"),
                new Answer("Gorge"),
                new Answer("Smith")
        )));

        QuestionList questionList1 = new QuestionList();
        questionList1.addQuestion(question1);
        questionList1.addQuestion(question2);
        questionList1.addQuestion(question3);
        questionList1.addQuestion(question4);


        Quiz quiz1 = new Quiz("General Knowledge");
        quiz1.setQuestions(questionList1);


        MultipleChoiceQuestion question5 = new MultipleChoiceQuestion("What is the highest common factor of the numbers 30 and 132?", new Answer("6"));
        question5.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("6"),
                new Answer("14"),
                new Answer("18"),
                new Answer("22")
        )));
        MultipleChoiceQuestion question6 = new MultipleChoiceQuestion("From the number 0 to the number 1000, the letter “A” appears only in?", new Answer("1000"));
        question6.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("1000"),
                new Answer("2"),
                new Answer("5"),
                new Answer("38")
        )));
        MultipleChoiceQuestion question7 = new MultipleChoiceQuestion("What is next in the following number series: 256, 289, 324, 361 . . . ?", new Answer("400"));
        question7.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("363"),
                new Answer("362"),
                new Answer("521"),
                new Answer("Smith")
        )));
        MultipleChoiceQuestion question8 = new MultipleChoiceQuestion("What is the value of Pi to four individual decimal places?", new Answer("3.1416"));
        question8.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("4.1416"),
                new Answer("3.2416"),
                new Answer("3.1415"),
                new Answer("3.1416")
        )));

        QuestionList questionList2 = new QuestionList();
        questionList2.addQuestion(question5);
        questionList2.addQuestion(question6);
        questionList2.addQuestion(question7);
        questionList2.addQuestion(question8);

        Quiz quiz2 = new Quiz("Math");
        quiz2.setQuestions(questionList2);


        MultipleChoiceQuestion question9 = new MultipleChoiceQuestion("Cacio & Pepe is a staple of what Italian city’s cuisine? ", new Answer("Rome"));
        question9.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Hong Kong"),
                new Answer("Tel-Aviv"),
                new Answer("Rome"),
                new Answer("USA")
        )));
        MultipleChoiceQuestion question10 = new MultipleChoiceQuestion("Where did sushi originate?", new Answer("China"));
        question10.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Hong Kong"),
                new Answer("Tel-Aviv"),
                new Answer("Rome"),
                new Answer("China")
        )));
        MultipleChoiceQuestion question11 = new MultipleChoiceQuestion("What is a Beaujolais?", new Answer("A type of red wine"));
        question11.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("A type of red wine"),
                new Answer("A dress"),
                new Answer("Country"),
                new Answer("Lake")
        )));
        MultipleChoiceQuestion question12 = new MultipleChoiceQuestion("What is the world’s best-selling stout beer? ", new Answer("Guinness"));
        question12.setAnswerList(new AnswerList(Arrays.asList(
                new Answer("Guinness"),
                new Answer("Corona"),
                new Answer("Brew-dog"),
                new Answer("Malka")
        )));

        QuestionList questionList3 = new QuestionList();
        questionList3.addQuestion(question9);
        questionList3.addQuestion(question10);
        questionList3.addQuestion(question11);
        questionList3.addQuestion(question12);

        Quiz quiz3 = new Quiz("Food & Drink");
        quiz3.setQuestions(questionList3);


        quizzes.setValue(Arrays.asList(
            quiz1,quiz2,quiz3
        ));
    }
}
