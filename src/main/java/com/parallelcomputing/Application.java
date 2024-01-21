package com.parallelcomputing;

import com.parallelcomputing.client.Client;
import com.parallelcomputing.invertedindex.InvertedIndex;
import com.parallelcomputing.server.Server;

import java.util.Scanner;
import java.util.Set;

public class Application {
    private static final int PORT = 1234;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try {
            Server TCPServer = new Server(PORT);
            Client TCPClient = new Client(HOST, PORT);

            serverTest(TCPServer, TCPClient);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void serverTest(Server server, Client client) throws InterruptedException {
        InvertedIndex invertedIndex = InvertedIndex.getInstance();
        System.out.println("Index ready!");

        Thread serverThread = new Thread(server::startServer);
        serverThread.start();

        Thread.sleep(1000);

        client.sendMessage();
        client.sendMessage();
        client.sendMessage();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your query: ");
        String userInput = scanner.nextLine();
        Set<String> result = invertedIndex.get(userInput);
        System.out.println("Search result: " + result);
        scanner.close();

        Thread.sleep(20_000);

        server.stopServer();
        serverThread.join();
    }
}
