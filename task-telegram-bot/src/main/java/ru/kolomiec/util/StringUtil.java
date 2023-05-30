package ru.kolomiec.util;

public class StringUtil {


    public static String longTaskIdToPermittedStringForCallbackData(Long taskId) {
        return String.format("TaskId:%s", taskId);
    }

    public static Long getTaskIdFromCallbackData(String callbackData) {
        return Long.valueOf(callbackData.split(":")[1]);
    }
}
