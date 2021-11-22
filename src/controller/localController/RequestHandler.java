package controller.localController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Thread {

    private final ServerController serverController;
    private Socket socket;

    private boolean isRunning = false;

    public RequestHandler(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
    }

    @Override
    public void run() {

        this.isRunning = true;

        serverController.addClient(socket);

        while (serverController.getClientCount() != serverController.getMaxClients()) {
            try {
                Thread.sleep(1000);
                System.out.println(serverController.getClientCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            PrintWriter out = new PrintWriter(this.socket.getOutputStream());
            DataInputStream in = new DataInputStream(this.socket.getInputStream());

            while (this.isRunning) {
                String line = in.readUTF();
                // Receive exit command from client
                if (line.equals("exit")) {
                    System.out.println("[SERVER] Connexion closed by client [1/2]");
                    this.isRunning = false;
                    break;
                } else if (line.contains("moveto")) {
                    System.out.println("[MOVETO] : " + line);
                    String[] split = line.split(" ");
                    System.out.println("[SERVER] Move to " + split[1] + " to " + split[2]);
                } else if (line.contains(("refresh"))) {
                    System.out.println("[REFRESH] : " + line);
                    System.out.println("[REFRESH] : " + serverController.getClients());
                    for (Socket client : serverController.getClients()) {
                        DataOutputStream broadcast = new DataOutputStream(client.getOutputStream());
                        broadcast.writeUTF(line);
                        broadcast.flush();
                    }
                } else if (line.contains("move")) {
                    System.out.println("[MOVE] : " + line);
                    System.out.println("[MOVE] : " + serverController.getClients());
                    for (Socket client : serverController.getClients()) {
                        DataOutputStream broadcast = new DataOutputStream(client.getOutputStream());
                        broadcast.writeUTF(line);
                        broadcast.flush();
                    }
                }
                System.out.println("[Listening] " + line);
            }

            System.out.println("[SERVER] Connection closed by client [2/2]");

            out.close();
            in.close();
            this.socket.close();
            serverController.removeClient(socket);

        } catch (IOException e) {
            System.out.println("[SERVER] Error " + e.getMessage() + " line : " + e.getStackTrace()[0].getLineNumber());
            e.printStackTrace();
        }
    }
}

