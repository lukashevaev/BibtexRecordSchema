package com.ols.record;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class PatternFactory {

    private Map<String, String> patterns = new HashMap<String, String>();

    public PatternFactory(String xml) {
        Pattern patternForFindingTags = Pattern.compile("<\\w*>");
        Matcher matcherForFindingTags = patternForFindingTags.matcher(xml);
        String foundFieldParameter = "";
        while (matcherForFindingTags.find()) {
            String foundPattern = matcherForFindingTags.group();
            StringBuffer currentPatternBuilder = new StringBuffer();
            currentPatternBuilder // creating a pattern like "<pattern>.*</pattern>"
                    .append(foundPattern)
                    .append(".*")
                    .append(foundPattern.replace("<", "</"));
                Pattern currentPattern = Pattern.compile(currentPatternBuilder.toString());
                Matcher currentMatcher = currentPattern.matcher(xml);
                if (currentMatcher.find()) foundFieldParameter = currentMatcher.group()
                                                        .replace(foundPattern, "")
                                                        .replace(foundPattern.replace("<", "</"), "");
                if (!foundFieldParameter.equals("")) {
                    //put parameter into map of found parameters
                    patterns.put(foundPattern.replace("<", "")
                                    .replace(">", ""),
                            foundFieldParameter);
                }
        }
    }
    public Map<String, String> getPatterns() {
        return patterns;
    }

}
