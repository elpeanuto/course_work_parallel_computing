package com.parallelcomputing.invertedindex;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {

    private static InvertedIndex instance;
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();
    private final List<Path> allFiles;

    private InvertedIndex(int numOfThreads) {
        allFiles = getAllPaths();
        distributeFilesToThreads(numOfThreads);
    }

    private void distributeFilesToThreads(int numOfThreads) {
        int batchSize = allFiles.size() / numOfThreads;
        int startIndex = 0;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numOfThreads; i++) {
            int endIndex = Math.min(startIndex + batchSize, allFiles.size());
            List<Path> filesForThread = allFiles.subList(startIndex, endIndex);

            Thread thread = new Thread(() -> processFiles(filesForThread));
            threads.add(thread);
            thread.start();

            startIndex = endIndex;
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processFiles(List<Path> files) {
        for (Path path : files) {
            add(FileUtil.fileToSting(path), String.valueOf(path.getFileName()));
        }
    }

    public List<Path> getAllPaths() {
        List<Path> list = new ArrayList<>(Objects.requireNonNull(
                FileUtil.readFilesInRange(Path.of("C:/Users/elpea/OneDrive/Desktop/aclImdb/train/unsup"), 1500, 2500)));

        String[] directories = {
                "C:/Users/elpea/OneDrive/Desktop/aclImdb/test/neg",
                "C:/Users/elpea/OneDrive/Desktop/aclImdb/test/pos",
                "C:/Users/elpea/OneDrive/Desktop/aclImdb/train/neg",
                "C:/Users/elpea/OneDrive/Desktop/aclImdb/train/pos"
        };

        for (String directory : directories) {
            list.addAll(Objects.requireNonNull(FileUtil.readFilesInRange(Path.of(directory), 1500, 1750)));
        }

        return list;
    }

    public static InvertedIndex getInstance(int numOfThreads) {
        if (instance == null) {
            instance = new InvertedIndex(numOfThreads);
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
