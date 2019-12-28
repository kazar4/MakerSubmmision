import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.stage.Stage;


public class Client extends GameWindow {

    //Used for connecting to server
    static ObjectOutputStream output;
    static ObjectInputStream input;
    static ServerSocket server;
    static Socket connection;

    //Holds positions of player 1 and 2
    static Dimension posOne = new Dimension(0, 0);
    static Dimension posTwo = new Dimension(0, 0);


    static Stage stage;
    int numberConnection;
    static ArrayList < int[][] > gameRandomValues = new ArrayList < int[][] > ();

    Client() {

    }

    //Connects to server, currently uses home ip, but can use others with port forward
    Client(Stage primaryStage) {

        stage = primaryStage;

        try {
            //IP and port --> ("127.0.0.1"), 7000);
            String textBoxInput = textbox.getText();
            if (textbox.getText().contains("Put IP in Here")) {
                textBoxInput = "127.0.0.1";
            }
            connection = new Socket(InetAddress.getByName(textBoxInput), 7000);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            Thread clientConnection = new Thread(new clientConnection());
            clientConnection.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//Thread for checking positions of players
class clientConnection extends Client implements Runnable {

    @Override
    public void run() {
        while (true) {

            try {
                gameRandomValues = (ArrayList < int[][] > ) input.readObject();
                System.out.println((gameRandomValues.get(0))[1].length);
                input.readObject(); //gives time in long type variable

                int numberConnection = input.readInt();
                System.out.println("NumConnection is " + numberConnection);


                while ((boolean) input.readObject() == false) {
                    System.out.println("Not connected to Two");
                }

                Wave wave = new Wave(waveNumber);
                System.out.println("Reached Stage Opening");
                Platform.runLater(() - > openMainWindow(stage));

                if (numberConnection == 1) {
                    while (true) {
                        posOne = new Dimension(mouseX, mouseY);
                        output.writeObject(posOne);
                        posTwo = (Dimension) input.readObject();
                        try {
                            Platform.runLater(() - > movement.otherPlayerMovement((int) posTwo.getWidth(), (int) posTwo.getHeight()));
                        } catch (NullPointerException e) {

                        }
                    }
                } else if (numberConnection == 2) {
                    while (true) {
                        posTwo = new Dimension(mouseX, mouseY);
                        output.writeObject(posTwo);
                        posOne = (Dimension) input.readObject();
                        try {
                            Platform.runLater(() - > movement.otherPlayerMovement((int) posOne.getWidth(), (int) posOne.getHeight()));
                        } catch (NullPointerException e) {

                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
