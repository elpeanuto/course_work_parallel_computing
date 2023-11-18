package com.parallelcomputing.invertedindex;

import java.util.*;

public class InvertedIndex {

    private final Map<String, Set<String>> index = new HashMap<>();

    public void add(String text, String fileName) {
        String[] words = parseTextToWords(text);

        for (String word : words) {
            Set<String> val = index.computeIfAbsent(word, k -> new HashSet<>());
            val.add(fileName);
        }
    }

    public Set<String> get(String word) {
        return index.get(word.trim().toLowerCase(Locale.ROOT));
    }

    private String[] parseTextToWords(String str) {
        return str
                .trim()
                .replaceAll("[^A-Za-z\\s]", "")
                .toLowerCase(Locale.ROOT)
                .split("\\s+");
    }
}
