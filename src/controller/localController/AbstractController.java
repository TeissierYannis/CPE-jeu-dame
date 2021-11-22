package controller.localController;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractController extends Thread {

    // Server PORT
    protected static final int PORT = 8889;
    protected static final String IP = "127.0.0.1";
}
