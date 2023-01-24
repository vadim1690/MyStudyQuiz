package com.studyquiz.mystudyquiz.utils;

import java.util.List;

public class Utils {

    /**
     * Returns a List as a comma-separated String.
     *
     * @param list The List to convert.
     * @return The List as a String.
     */
    public static String listToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
