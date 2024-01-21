package com.parallelcomputing.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Client {

    private final String HOST;
    private final int PORT;
    private static final String[] WORDS_ARRAY = {
            "this", "features", "interesting", "movie", "desperate", "need",
            "apple", "banana", "orange", "grape", "kiwi", "melon",
            "cat", "dog", "bird", "fish", "rabbit", "turtle",
            "sun", "moon", "star", "cloud", "rain", "wind",
            "happy", "sad", "angry", "excited", "calm", "bored"
    };
    private final Random random = new Random();

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
    }

    public void sendMessage() {
        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            String word = WORDS_ARRAY[random.nextInt(WORDS_ARRAY.length)];

            out.writeUTF(word);

            System.out.println("\tData to sent: " + word);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}