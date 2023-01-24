package com.studyquiz.mystudyquiz.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.studyquiz.mystudyquiz.model.Answer;
import com.studyquiz.mystudyquiz.model.Question;
import com.studyquiz.mystudyquiz.model.Quiz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Quiz.class, Question.class, Answer.class}, version = 7)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "myStudyQuizDatabase").
                            fallbackToDestructiveMigration().
                            build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract QuizDao quizDao();
    public abstract QuestionDao questionDao();
    public abstract AnswerDao answerDao();
}
