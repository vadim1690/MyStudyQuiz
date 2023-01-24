package com.studyquiz.mystudyquiz.data;

import androidx.room.TypeConverter;

import com.studyquiz.mystudyquiz.model.QuestionType;

public class Converters {
    @TypeConverter
    public static String questionTypeToString(QuestionType value) {
        return value == null ? null : value.name();
    }

    @TypeConverter
    public static QuestionType stringToQuestionType(String value) {
        return value == null ? null : QuestionType.valueOf(value);
    }
}
