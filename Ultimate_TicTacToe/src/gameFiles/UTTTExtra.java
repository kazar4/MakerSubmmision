package gameFiles;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;

import javafx.application.Platform;
import javafx.scene.control.Button;


public class UTTTExtra extends UltimateTicTacToe {

		//variables allow game data to be written to a .txt file
    static FileWriter fw;
    static BufferedWriter bw;
    static PrintWriter pw;

		//Allow game data to be read from a .txt file
    static BufferedReader bis = null;
    static FileReader fis = null;

		//Allow game data to be recevied and sent to other players
    static ObjectOutputStream output;
    static ObjectInputStream input;

		//Creates the server connection between the other game
    static ServerSocket server;
    static Socket connection;

		/*
		Currently uses home network
		Has been tested with actual home IP, and works after network
		is port forwarded
		*/
    String serverName = "127.0.0.1";
    int port = 42069;

		//A new thread that is constantly reading potential data from other player
    Thread Thread1 = new Thread(new TTT_Four_Thread());

    //Using this type of boolean because it is thread safe
    static AtomicBoolean usingConnection = new AtomicBoolean(false);

		//Saves game status at this location
    URL url2 = UltimateTicTacToe.class.getResource("/newGame.txt");

		//creates a new file variable based on the path url
    File file = new File(url2.getPath());

		//Method to handle saving game state to a txt file
    public void saveGame() {

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            for (Button i: buttonBox) {
                pw.println(i.getText());
            }

            pw.println(drawCounter);
            pw.println(turn);
            pw.println(foundWinner);
            pw.println(chooseBoard);
            pw.println(openBoard);
            pw.println(lastBoard);
            pw.println(lastButton);
            pw.flush();

        } catch (IOException ioe) {
            ioe.printStackTrace();
            window.setTitle("Error Saving Game");
        } finally {
            try {
                fw.close();
                bw.close();
                pw.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

		//Method to handle loading game state from a txt file
		public void loadGame() {

				//Resets game before loading save
        clearGame();

        int i = 0;
        String contentLine = null;
        CharSequence csO = "O";
        CharSequence csX = "X";

        try {

            ClassLoader classLoader = getClass().getClassLoader();

            bis = new BufferedReader(new FileReader(file));

            for (Button button: buttonBox) {

                contentLine = bis.readLine();

                if (contentLine.contains(csX) == false && contentLine.contains(csO) == false) {

                    contentLine = "";
                }

								//Sets text of each button along with array value at that index
                if (contentLine.contains(csX)) {
                    gameBoardVals[findLocation(button)[0]][findLocation(button)[1]] = "X";
                    button.setText("X");
                }
                if (contentLine.contains(csO)) {
                    gameBoardVals[findLocation(button)[0]][findLocation(button)[1]] = "O";
                    button.setText("O");
                }
            }

            drawCounter = Integer.parseInt(bis.readLine());

            turn = bis.readLine();

            foundWinner = Boolean.valueOf(bis.readLine());

            if (foundWinner == true) {
                foundWinner = false;
            }

            chooseBoard = Boolean.valueOf(bis.readLine());

            openBoard = Integer.parseInt(bis.readLine());

            lastBoard = Integer.parseInt(bis.readLine());

            lastButton = Integer.parseInt(bis.readLine());

            bis.close();

            checkWinnerLogic();
            nextMoveSetUp();
            addColor();

        } catch (IOException e) {
            e.printStackTrace();
            window.setTitle("Error Loading Save");
        }

				//If servo is being used, send new game state to other player
        try {
            if (connection.isConnected()) {
                sendData();
            }
        } catch (NullPointerException npe) {

				}
    }

		//Method to handle sending game state to other player
    public void sendData() {

        try {

            for (Button i: buttonBox) {
                output.writeObject(i.getText());
                output.flush();

            }

						/*
						The repeated .flush() is to ensure that each data
						value is sent sepradtedly and each .readObject()
						has distinct bytes to read. May be redudant, but
						testing has worked well using it this way
						*/
            output.writeObject(drawCounter);
            output.flush();
            output.writeObject(turn);
            output.flush();
            output.writeObject(foundWinner);
            output.flush();
            output.writeObject(chooseBoard);
            output.flush();
            output.writeObject(openBoard);
            output.flush();
            output.writeObject(lastBoard);
            output.flush();
            output.writeObject(lastButton);
            output.flush();

        } catch (IOException ioException) {}
					e.printStackTrace();
    }

		//Method to handle reading game state sent from other player
    public void readData() throws ClassNotFoundException, IOException {

        CharSequence csO = "O";
        CharSequence csX = "X";
        String contentLine = null;

				//Reads all button values and puts them into array
        for (Button button: buttonBox) {

            try {
                contentLine = (String) input.readObject();
            } catch (NullPointerException e) {
                contentLine = "";
            }

            if (contentLine.contains(csX) == false && contentLine.contains(csO) == false) {
                contentLine = "";
            }

            if (contentLine.contains(csX)) {
                gameBoardVals[findLocation(button)[0]][findLocation(button)[1]] = "X";
                button.setText("X");
            }

            if (contentLine.contains(csO)) {
                gameBoardVals[findLocation(button)[0]][findLocation(button)[1]] = "O";
                button.setText("O");
            }
        }

				//Reads other game state variables
        try {
            drawCounter = (Integer) input.readObject();
        } catch (NullPointerException e) {
            drawCounter = 0;
        }

        try {
            turn = input.readObject().toString();
						foundWinner = Boolean.getBoolean(input.readObject().toString());
						chooseBoard = Boolean.getBoolean(input.readObject().toString());
						openBoard = (Integer) input.readObject();
						lastBoard = (Integer) input.readObject();
						lastButton = (Integer) input.readObject();
        } catch (NullPointerException nullpointerException) {
						e.printStackTrace();
        }

        checkWinnerLogic();
        nextMoveSetUp();
        addColor();
    }

		//Thread that constantly reads for new game states being sent
    public class TTT_Four_Thread implements Runnable {

        @Override
        public void run() {

            while (usingConnection.get()) {
                try {
                    readData();

                } catch (NumberFormatException | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
						//When connection is closed the thread is stopped
            Thread.currentThread().interrupt();
        }

    }

		//Method that opens a socket for the other player to connect to
    public void hostGame() {

        try {

						//Creates server at port with 100 secound wait timer
            server = new ServerSocket(port, 100);
            connection = server.accept();

            usingConnection.set(true);

            System.out.println("Server Found Connection");

            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();

            input = new ObjectInputStream(connection.getInputStream());


        } catch (IOException ioException) {
            System.out.println("No Connection");
        }

        Thread1.start();

    }

		//Method taht connects to a socket another player created
    public void connectGame() {

        usingConnection.set(true);

        try {
            connection = new Socket(InetAddress.getByName(serverName), port);

            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());


        } catch (IOException ioException) {
						System.out.println("Connection Error")
        }
        Thread1.start();
    }

		//Method that closes all server connections
    public void closeServer() throws IOException {

        extraMethods.output.close();
        extraMethods.input.close();
        extraMethods.connection.close();

    }
}
