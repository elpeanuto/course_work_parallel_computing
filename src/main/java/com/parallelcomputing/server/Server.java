package com.parallelcomputing.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private ServerSocket serverSocket;
    private final int port;
    private final List<ClientHandler> clientHandlerThreads = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);

            while (!Thread.currentThread().isInterrupted()) {
                ClientHandler handler = new ClientHandler(serverSocket.accept());
                clientHandlerThreads.add(handler);
                new Thread(handler).start();
            }
        } catch (SocketException e) {
            System.out.println("Server stopped!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        try {
            for (ClientHandler clientThread : clientHandlerThreads) {
                clientThread.stopHandler();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
