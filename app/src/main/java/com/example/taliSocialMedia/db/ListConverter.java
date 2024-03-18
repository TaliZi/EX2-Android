package com.example.taliSocialMedia.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<String> fromJsonString(String value) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String toJsonString(List<String> list) {
        return gson.toJson(list);
    }
}
