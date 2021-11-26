package controller.localController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class RequestHandler extends Thread {

    private final ServerController serverController;
    private Socket socket;

    private DataOutputStream out;
    private DataInputStream in;

    public RequestHandler(Socket socket, ServerController serverController) {
        this.socket = socket;
        this.serverController = serverController;
    }

    @Override
    public void run() {

        try {
            boolean isRunning = true;

            serverController.addClient(socket);

            out = new DataOutputStream(this.socket.getOutputStream());
            in = new DataInputStream(this.socket.getInputStream());

            // TODO RETURN ID OF CLIENT AND SEND IT TO CLIENT TO ASSIGN IT IN CLIENT CONTROLLER
            if (serverController.getClientCount() == 1) {
                out.writeUTF("color WHITE");
            } else {
                out.writeUTF("color BLACK");
            }

            while (serverController.getClientCount() != serverController.getMaxClients()) {
                try {
                    out.writeUTF("ping");
                } catch(IOException e) {
                    isRunning = false;
                    serverController.removeClient(socket);
                    out.close();
                    in.close();
                    this.socket.close();
                    break;
                }
            }

            if (isRunning) broadcast("start");

            while (isRunning) {
                String line = in.readUTF();
                // Receive exit command from client
                if (line.equals("exit")) {
                    System.out.println("[SERVER] Connexion closed by client [1/2]");
                    break;
                } else if (line.contains("move")) {
                    System.out.println("[MOVE] : " + line);
                    System.out.println("[MOVE] : " + serverController.getClients());
                    broadcast(line);
                }
                System.out.println("[Listening] " + line);
            }

            System.out.println("[SERVER] Connection closed by client [2/2]");

            out.close();
            in.close();
            this.socket.close();
            serverController.removeClient(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all clients
     * @param message
     */
    private void broadcast(String message) {
        try {
            for (Socket client : serverController.getClients()) {
                DataOutputStream broadcast = new DataOutputStream(client.getOutputStream());
                broadcast.writeUTF(message);
                broadcast.flush();
            }
        }catch (IOException e){
            System.out.println("[SERVER] Error " + e.getMessage() + " line : " + e.getStackTrace()[0].getLineNumber());
        }
    }
}

