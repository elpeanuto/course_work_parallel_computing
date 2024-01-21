package com.parallelcomputing.invertedindex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil {

    public static List<Path> readFilesInRange(Path directoryPath, int startIndex, int endIndex) {
        try (Stream<Path> pathStream = Files.list(directoryPath)) {
            List<Path> files = pathStream
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparingInt(FileUtil::extractNumberFromFileName))
                    .toList();

            return files.subList(Math.max(0, startIndex), Math.min(endIndex + 1, files.size()));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fileToSting(Path path) {
        String text;

        try {
            text = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return text;
    }

    private static int extractNumberFromFileName(Path filePath) {
        String fileName = filePath.getFileName().toString().replaceAll("[^0-9]", "");
        return fileName.isEmpty() ? 0 : Integer.parseInt(fileName);
    }
}

