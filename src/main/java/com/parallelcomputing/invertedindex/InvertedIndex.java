package com.parallelcomputing.invertedindex;

import java.nio.file.Path;
import java.util.*;

public class InvertedIndex {

    private static InvertedIndex instance;
    private final Map<String, Set<String>> index = new HashMap<>();

    private InvertedIndex() {
        List<Path> list = List.of(Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/test/neg"),
                Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/test/pos"),
                Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/train/neg"),
                Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/train/pos"));

        builder(list, 1500, 1750);
        builder(List.of(Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/train/unsup")), 1500, 2500);
    }

    private void builder(List<Path> dirList, int startIndex, int endIndex) {
        for (Path dir:dirList) {
            List<Path> pathList = FileUtil.readFilesInRange(dir, startIndex, endIndex);

            if (pathList == null) throw new RuntimeException();

            for (Path path : pathList) {
                add(FileUtil.fileToSting(path), String.valueOf(path.getFileName()));
            }
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

    public Set<String> get(String inputWords) {
        String[] words = parseTextToWords(inputWords);

        Set<String> result = null;
        for (String word : words) {
            Set<String> currentSet = index.get(word);

            if (currentSet == null) {
                return null;
            }

            if (result == null) {
                result = new HashSet<>(currentSet);
            } else {
                result.retainAll(currentSet);
            }
        }

        return result;
    }


    private String[] parseTextToWords(String str) {
        String cleanedText = str.replaceAll("<[^>]*>", " ").trim();

        return Arrays.stream(cleanedText
                        .replaceAll("[^A-Za-z0-9\\s']", " ")
                        .replaceAll("'\\S*", "")
                        .toLowerCase(Locale.ROOT)
                        .split("\\s+"))
                .distinct()
                .toArray(String[]::new);
    }
}
