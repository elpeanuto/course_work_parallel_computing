package com.parallelcomputing;

import com.parallelcomputing.invertedindex.InvertedIndex;

import java.util.Scanner;
import java.util.Set;

public class Application {

    public static void main(String[] args) {
        InvertedIndex invertedIndex = InvertedIndex.getInstance();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter your query (type 'exit' to quit): ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Program terminated.");
                break;
            }

            Set<String> result = invertedIndex.get(userInput);
            System.out.println("Search result: " + result);
        }

        scanner.close();
    }
}
