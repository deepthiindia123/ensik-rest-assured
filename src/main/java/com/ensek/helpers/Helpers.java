package com.ensek.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helpers {

    public static String extractOrderID(String responseMsg) {
        String UUID_REGEX = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";
        String extractedUUID = null;
        Pattern pairRegex = Pattern.compile(UUID_REGEX);
        Matcher matcher = pairRegex.matcher(responseMsg);
        while (matcher.find()) {
            extractedUUID = matcher.group(0);
        }
        return extractedUUID;
    }
}
