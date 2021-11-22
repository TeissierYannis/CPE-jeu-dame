package controller.localController;

import controller.Mediator;
import gui.GuiConfig;
import gui.View;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.BoardGame;
import model.Coord;
import model.Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.RunnableFuture;

public class ClientController extends Application {

    private BoardGame<Coord> model;
    private EventHandler<MouseEvent> controller;
    private View view;

    private static Socket client;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    RunnableFuture<Void> future;

    public static void main(String[] args) throws IOException {

        try {
            client = new Socket("127.0.0.1", 8889);

            dataOutputStream = new DataOutputStream(client.getOutputStream());
            dataInputStream = new DataInputStream(client.getInputStream());

            dataOutputStream.writeUTF("[CLIENT] Launching app...");

            ClientController.launch();

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

        this.controller = new Controller(dataOutputStream, dataInputStream);

        ///////////////////////////////////////////////////////////////////////////////////////
        // Fen�tre dans laquelle se dessine le damier est �cout�e par controller
        ///////////////////////////////////////////////////////////////////////////////////////

        this.view = new View(controller);

        future = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                /*
                String line = dataInputStream.readUTF();
                System.out.println("[CLIENT] " + line);

                        /*
                         0 = cmd name
                         1 = toMovePieceIndex
                         2 = targetSquareIndex
                         3 = capturedPieceIndex
                         4 = promotedPieceIndex
                         5 = promotedPieceColor
                         */
                /*
                String[] data = line.split(" ");
                InputViewData<Integer> inputViewData = new InputViewData<Integer>(
                        Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]),
                        Integer.parseInt(data[4]),
                        Objects.equals(data[5], "WHITE") ? PieceSquareColor.WHITE
                        : (Objects.equals(data[5], "BLACK") ? PieceSquareColor.BLACK
                        : null)
                );

                System.out.println("[CLIENT] " + inputViewData);

                view.actionOnGui(inputViewData);*/
                    String line = dataInputStream.readUTF();

                    if (line.contains("move")) {
                        System.out.println("[CLIENT MOVE] " + line);

                        String[] data = line.split(" ");
                        ((Controller) controller).moveCapturePromote(Integer.parseInt(data[1]), Integer.parseInt(data[2]));

                    } else {
                        System.out.println("[CLIENT INFO] " + line);
                    }
                }
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
        Thread thread = new Thread(future);
        thread.start();

        primaryStage.setScene(new Scene(this.view, GuiConfig.HEIGHT, GuiConfig.HEIGHT));
        primaryStage.setTitle("Jeu de dames");
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
