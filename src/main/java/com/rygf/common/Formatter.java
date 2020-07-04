package com.rygf.common;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Formatter {
    
    public static String formatString(String str, int maxWord) {
        String[] split = str.split("\\s");
        if(split.length <= maxWord)
            return str;
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < maxWord; i++) {
            builder.append(split[i] + " ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("...");
        
        return builder.toString();
    }
    
    public static String convertStrToHashtag(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String s = pattern.matcher(temp)
                .replaceAll("")
                .replaceAll("đ", "d")
                .toLowerCase();
            String[] split = s.split("\\s");
            List<String> words = Arrays.asList(split).stream()
                .map(word ->
                    word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.toList());
//            words.add(0, "#");
            return String.join("", words);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public static String convertStrToUrl(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}
