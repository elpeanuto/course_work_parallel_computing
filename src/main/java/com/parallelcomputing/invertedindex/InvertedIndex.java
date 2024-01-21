package com.parallelcomputing.invertedindex;

import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;

public class InvertedIndex {

    private static InvertedIndex instance;
    private final Map<String, Set<String>> index = new HashMap<>();

    private InvertedIndex() {
        Path dirPath = Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/test/neg");
        int startIndex = 1500;
        int endIndex = 1750;

        List<Path> pathList = FileUtil.readFilesInRange(dirPath, startIndex, endIndex);

        if (pathList == null) throw new RuntimeException();

        for (Path path : pathList) {
            add(FileUtil.fileToSting(path), String.valueOf(path.getFileName()));
        }
    }

    public static InvertedIndex getInstance() {
        if (instance == null) {
            instance = new InvertedIndex();
        }
        return instance;
    }

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
