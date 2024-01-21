package com.parallelcomputing.server;

import com.parallelcomputing.invertedindex.InvertedIndex;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {

    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            System.out.println("Client " + socket.getInetAddress() + " connected" + "\n");

            String receivedString = in.readUTF();

            System.out.println("Received string:\n" + receivedString);

            System.out.println(InvertedIndex.getInstance().get(receivedString));

        } catch (SocketException e) {
            System.out.println("Connection with: " + this.socket.getInetAddress() + " abandoned!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopHandler() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}