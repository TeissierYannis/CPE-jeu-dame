package controller.localController;

import controller.Mediator;
import gui.GuiConfig;
import gui.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.BoardGame;
import model.Coord;
import model.Model;
import nutsAndBolts.PieceSquareColor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.RunnableFuture;

public class ClientController extends Application {

    public static PieceSquareColor playerColor;
    private static Socket client;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;
    RunnableFuture<Void> future;
    private BoardGame<Coord> model;
    private EventHandler<MouseEvent> controller;
    private View view;

    public static void main(String[] args) throws IOException {

        try {
            client = new Socket("127.0.0.1", 8889);

            if (client.isConnected()) {
                dataOutputStream = new DataOutputStream(client.getOutputStream());
                dataInputStream = new DataInputStream(client.getInputStream());

                dataOutputStream.writeUTF("[CLIENT] Launching app...");

                while (true) {
                    String line = dataInputStream.readUTF();

                    if (line.contains("start")) {
                        System.out.println("[CLIENT] Launching client");
                        break;
                    } else if (line.contains("color")) {
                        playerColor = Objects.equals(line.split(" ")[1], "WHITE") ? PieceSquareColor.WHITE : PieceSquareColor.BLACK;
                        System.out.println("[CLIENT] Color received : " + playerColor);
                    }
                }

                ClientController.launch();

            } else {
                System.out.println("[CLIENT] Error connection");
            }
        } catch (UnknownHostException e) {
            System.out.println("[CLIENT] Error " + e.getMessage());
        }
    }

    @Override
    public void init() throws Exception {
        super.init();

        ///////////////////////////////////////////////////////////////////////////////////////
        // Objet qui g�re les aspects m�tier du jeu de dame :
        ///////////////////////////////////////////////////////////////////////////////////////

        this.model = new Model();

        ///////////////////////////////////////////////////////////////////////////////////////
        // Objet qui contr�le les actions de la vue et les transmet au model
        // il renvoie les r�ponses du model � la vue
        ///////////////////////////////////////////////////////////////////////////////////////

        this.controller = new Controller(dataOutputStream, playerColor);

        ///////////////////////////////////////////////////////////////////////////////////////
        // Fen�tre dans laquelle se dessine le damier est �cout�e par controller
        ///////////////////////////////////////////////////////////////////////////////////////

        this.view = new View(controller);

        // Listening broadcast from server and propagating it to the view to update the board
        future = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    while (true) {
                        String line = dataInputStream.readUTF();

                        if (line.contains("move")) {
                            System.out.println("[CLIENT MOVE] " + line);

                            String[] data = line.split(" ");
                            Platform.runLater(() -> {
                                try {
                                    ((Controller) controller).moveCapturePromote(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            System.out.println("[CLIENT INFO] " + line);
                        }
                    }
                } catch (SocketException e) {
                }
                return null;
            }
        };

        // Controller doit pouvoir invoquer les m�thodes du model
        // il enverra ensuite des instructions � view qui relaiera � son objet Board
        // En mode Client/Server
        // Les actions devront �tre propag�es sur les vues de chaque client et non pas seulement
        // sur celle qui a initi� l'action
        ((Mediator) controller).setView(view);
        ((Mediator) controller).setModel(model);
    }


    @Override
    public void start(Stage primaryStage) {

        // Listening communication from the server
        try {
            Thread thread = new Thread(future);
            thread.start();

        } catch (Exception e) {
            System.out.println("[CLIENT] Error thread  " + e.getMessage());
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(this.view, GuiConfig.HEIGHT, GuiConfig.HEIGHT));
        primaryStage.setTitle("Jeu de dames - " + playerColor);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        System.out.println("Stopping client ...");

        dataOutputStream.writeUTF("exit");
        dataOutputStream.flush();

        // Close our streams
        dataInputStream.close();
        dataOutputStream.close();
        client.close();
    }
}
