package server.controller.localController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerController extends AbstractController {

    private static final int MAX_CLIENTS = 2;

    private LinkedList<Socket> clients;
    private static int clientCount;

    private ServerSocket serverSocket;
    private boolean isRunning = false;

    public ServerController() {
        super();
        clientCount = 0;
        clients = new LinkedList<Socket>();
    }

    public static void main(String[] args) {
        ServerController server = new ServerController();
        server.startServer();
        server.stopServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("[SERVER] Server listening on 127.0.0.1:" + PORT);
            this.start();

        } catch (IOException e) {
            System.out.println("[SERVER] Error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        this.isRunning = true;

        System.out.println("[SERVER] Waiting for client... " + clientCount);
        while (isRunning) {
            try {
                if (clientCount == MAX_CLIENTS) {
                    System.out.println("[SERVER] Maximum number of clients reached");
                }
                // Wait for a client to connect
                Socket socket = serverSocket.accept();
                clientCount++;

                RequestHandler requestHandler = new RequestHandler(socket, this);
                requestHandler.start();

            } catch (IOException e) {
                System.out.println("[SERVER] Error " + e.getMessage() + " line : " + e.getStackTrace()[0].getLineNumber());
            }
        }
    }

    public void stopServer() {
        this.isRunning = false;
        this.interrupt();
    }

    public int getClientCount() {
        return clientCount;
    }

    public int getMaxClients() {
        return MAX_CLIENTS;
    }

    public LinkedList<Socket> getClients() {
        return clients;
    }

    public void addClient(Socket client) {
        clients.add(client);
        System.out.println("[SERVER] Client added to the player list");
    }

    public void removeClient(Socket client) {
        clientCount--;
        clients.remove(client);
        System.out.println("[SERVER] Client removed");
    }
}

