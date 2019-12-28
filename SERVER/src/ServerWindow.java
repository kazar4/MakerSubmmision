import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ServerWindow extends Application {

    //Variables for connection creation
    ServerSocket server;
    Socket connection;

    boolean gameStarted = false; //has the game started
    Text DisplayText; //Text displayed on screen

    static ServerCalculations SerCalc = new ServerCalculations(); //Class for server calculations

    //Holds x and y positions of player 1 and 2
    static Dimension posOne = new Dimension(0, 0);
    static Dimension posTwo = new Dimension(0, 0);


    static int connectionNumber = 0; //Player 1 (1) or 2 (2), 0 is initial value

    static boolean connectedToTwo = false; //Boolean if connected to 2 players

    final int PORT = 7000; //Port for connection

    //Sets up GUI for server screen
    public void start(Stage primaryStage) throws IOException {

        primaryStage = new Stage();

        BorderPane Bpane = new BorderPane();
        DisplayText = new Text(" Welcome to my Game \n Please Wait until \n Two Players Join \n Port: " + PORT);
        Button HostButton = new Button("Click to Host");
        HBox Box = new HBox();
        Bpane.setBottom(HostButton);
        Bpane.setTop(DisplayText);
        Scene scene2 = new Scene(Bpane, 200, 100);

        primaryStage.setScene(scene2);
        primaryStage.show();

        //When host button is clicked it starts to wait for 2 connections
        //Assigns player number to each
        HostButton.setOnMouseClicked(e - > {
            try {
                SerCalc.CalculateValues(0);
                server = new ServerSocket(PORT, 100);

                System.out.println("Waiting on connection 1");
                Thread connectionThread = new Thread(new connectionThread(server.accept(), 1));
                System.out.println("connected to 1");
                setConnection(connection);
                connectionThread.start();

                System.out.println("Waiting on connection 2");
                Thread connectionThread2 = new Thread(new connectionThread(server.accept(), 2));
                System.out.println("connected to 2");
                setConnection(connection);
                connectionThread2.start();

                connectedToTwo = true;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    }

    //Launches JavaFX GUI
    public static void main(String[] args) {
        launch(args);
    }

    //Setter for the connection variable
    public void setConnection(Socket connect) {
        connection = connect;
    }

    //Getter for the connection variable
    public Socket getConnection() {
        return connection;
    }
}

//Thread that constantly sends player positions to both clients
class connectionThread extends ServerWindow implements Runnable {

    private int numConnection;


    connectionThread(Socket connectionVal, int connectNum) {

        connection = connectionVal;
        numConnection = connectNum;

    }


    @Override
    public void run() {

        try {

            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
            output.writeObject(SerCalc.gameRandomValues);
            output.writeObject(SerCalc.time);
            output.writeInt(numConnection);

            while (!connectedToTwo) {
                System.out.println("Not connected to Two");
            }
            output.writeObject(connectedToTwo);


            while (true) {

                //If data is from client 1, send to client 2
                //And vice-versa for client 2
                if (numConnection == 1) {
                    posOne = (Dimension) input.readObject();
                    output.writeObject(posTwo);

                } else if (numConnection == 2) {

                    posTwo = (Dimension) input.readObject();
                    output.writeObject(posOne);
                }
            }

        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
