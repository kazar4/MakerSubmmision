package gameFiles;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import gameFiles.UTTTExtra;

public class UltimateTicTacToe extends Application {

    Scene scene;
    static String turn = "X"; //Player X starts the game
    static ArrayList < TilePane > gameBox = new ArrayList < TilePane > (); //array to hold each board
    static TilePane master = new TilePane(); //Holds all of the game elements
    static ArrayList < Button > buttonBox = new ArrayList < Button > (); //holds buttons used to play game (X or O)
    static Button loadButton, hostButton, connectButton, clearButton, saveButton, cycleBg2; //Other GUI buttons
    static int drawCounter; //When number reaches max plays and game ties
    static boolean foundWinner = false; //boolean for when a winner is found on the board
    static int openBoard = 81; //81 is an arbitaury num representing that any board is playable
    static int selectedBoard; //Board that is clicked (Board is a set of 3x3 buttons)
    static int selectedButton; //Button that is clicked
    static int HoverSelectedBoard; //Board that mouse of hovering over
    static int hoverButton = 0; //Button that mouse is hovering over
    static Stage window; //New stage to change name
    UTTTExtra extraMethods; //Class for all extra functionality like online play
    static String[][] gameBoardVals = new String[9][9]; //Array representing all values on the board (Blank, X or O)
    static String[][] winBoardVals = new String[1][9]; //Array showing all the boards that have been won already
    static boolean chooseBoard = true; //If the next board is already won, player can now choose next board
    static boolean DoesThisMoveWork; //Boolean for whether or not a move is legal
    static int lastBoard; //last board clicked on
    static int lastButton; //last button clicked on
    static Background background; //background of entire game
    static int bgCounter = 0; //Counter variable for background cycle

    //Launches javafx GUI
    public static void main(String[] args) {
        launch(args);
    }

		//Handles addition of GUI elements for start screen
    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;

        primaryStage.setTitle("Ultimate TicTacToe"); //Title of game

        //Loads the logo of the game and displays it
        Image image = null;
        try {
            URL url = UltimateTicTacToe.class.getResource("/Untitled-1.png");
            image = new Image(new FileInputStream(url.getPath()));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ImageView imageView = new ImageView(image);


        //Setting the position of the logo
        imageView.setX(50);
        imageView.setY(25);

        //setting the fit height and width of the logo
        imageView.setFitHeight(420);
        imageView.setFitWidth(475);

        //Setting the preserve ratio of the logo
        imageView.setPreserveRatio(true);


        //Intilizes background for game, loads background, and displays it
        URL url3 = null;
        url3 = UltimateTicTacToe.class.getResource("/bgForGame.png");
        Image image2 = null;
        try {
            image2 = new Image(new FileInputStream(url3.getPath()));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        BackgroundImage backgroundimage = new BackgroundImage(image2,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
        background = new Background(backgroundimage);

        //Creates pane to hold start screen elements
        BorderPane layout = new BorderPane();

        //Sets start button text and location
        Button button = new Button("Click To Start");
        button.setOnAction(e - > gameScreen(scene, primaryStage));
        button.setTranslateY(-150);

        //Sets change background text, sets method to cycle throughout all images
        Button changeBg = new Button("Cycle Backgrounds");
        changeBg.setOnAction(e - > cycleBg(layout));
        layout.setBackground(background);

        //Puts buttons into the pane
        layout.setCenter(button);
        layout.setTop(imageView);
        layout.setBottom(changeBg);

        //Creates the actual GUI with all the previous inputs
        Scene scene = new Scene(layout, 475, 625);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

		//Hanldes addition of GUI elements for game screen
    public void gameScreen(Scene scene, Stage stage) {

        window.setTitle("It is Player " + turn + " turn"); //Sets the intial turn of the game

        //Resets all the button states to "", signifying that neither X nor O been played
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                gameBoardVals[i][j] = "";
                //System.out.println(i + "-" + j);
            }
        }

        //Resets all of board states to "", signifying that no boards have been one for either X nor O
        for (int i = 0; i < 9; i++) {
            winBoardVals[0][i] = "";
        }

        //Sets the button properties for all of the game boards
        for (int i = 0; i < 9; i++) {
            int index1 = i;
            Insets inset = new Insets(5, 5, 5, 5);
            gameBox.add(new TilePane());
            gameBox.get(index1).setPadding(inset);
            gameBox.get(index1).setVgap(5);
            gameBox.get(index1).setHgap(5);
            gameBox.get(index1).setPrefColumns(3);
            gameBox.get(index1).setPrefRows(3);

            Dimension d = new Dimension(40, 40);

            //Adds a method or function to each button press
            //Adds all buttons to an array
            //Sets size of buttons
            //Sets behavior for when mouse hovers over a button
            for (int j = 0; j < 9; j++) {
                Button button = new Button();

                int index2 = j;
                //index1 + ":" + index2);
                button.setOnAction(e - > makeMove(button));
                button.setPrefSize(d.getWidth(), d.getHeight());
                gameBox.get(index1).getChildren().add(button);
                buttonBox.add(button);

                button.setOnMouseEntered(e - > glowSpots((Button) e.getSource(), 0.8));
                button.setOnMouseExited(e - > glowSpots((Button) e.getSource(), 0.0));
            }
        }

        //Initializes class of extra functions
        extraMethods = new UTTTExtra();

        //Dimension d2 = new Dimension(60, 20);

        //Sets function and name of clear game button
        clearButton = new Button("Clear");
        clearButton.setOnAction(e - > clearGame());

        //Sets function and name of save game button
        saveButton = new Button("Save");
        saveButton.setOnAction(e - > extraMethods.saveGame());

        //Sets function and name of load game button
        loadButton = new Button("Load");
        loadButton.setOnAction(e - > extraMethods.loadGame());

        //Sets function and name of host game button
        hostButton = new Button("Host");
        hostButton.setOnAction(e - > extraMethods.hostGame());

        //Sets function and name of connect button
        connectButton = new Button("Connect");
        connectButton.setOnAction(e - > extraMethods.connectGame());

        //Sets function and name of cycle background button
        cycleBg2 = new Button("Cycle Background");
        cycleBg2.setOnAction(e - > cycleBg(master));

        //Pane that includes all the buttons when game is played
        master.setPadding(new Insets(10));
        master.setPrefColumns(3);
        master.setPrefRows(3);
        master.getChildren().addAll(gameBox);
        master.setVgap(10);
        master.setHgap(10);

        //Creates a Vbox to put inside master pane
        VBox VerBox = new VBox();
        VerBox.getChildren().addAll(clearButton, saveButton, loadButton, hostButton, connectButton);
        master.getChildren().add(VerBox);

        //When the game closes, if a wireless connection is used, close the sockets
        window.setOnCloseRequest(e - > {
            if (extraMethods.usingConnection.get()) {
                extraMethods.usingConnection.set(false);
                try {
                    extraMethods.closeServer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        //Sets the background to whatever background was set in the start screen
        master.setBackground(background);

        //Sets screen size, and finally displays the GUI with the previously written code
        scene = new Scene(master, 475, 625);
        stage.setScene(scene);
        stage.show();

    }

    //Code is run when button is clicked
		//Handles the logic of progressing the game
    public void makeMove(Button button) {

        //If the move works, it sets the text of the button
        //Then checks if a winner is found
        //moves the game to the next turn, and saves any prior variables
        if (DoesThisMoveWork(button)) {

            button.setText(turn);
            drawCounter++;

            gameBoardVals[selectedBoard][selectedButton] = turn;

            checkWinnerLogic();

            lastBoard = selectedBoard;
            lastButton = selectedButton;

            nextMoveSetUp();

            nextTurn();

            addColor();

            //If a wireless connection is used, send game state to other player
            if (extraMethods.usingConnection.get()) {
                if (extraMethods.connection.isConnected()) {
                    extraMethods.sendData();
                }
            }

        }
    }

    //Checks to see if a winner has been found
    //By using a multidimensional array, the possible win conditions
    //Ex: diagonal, across (in total there are 9 ways to win tictactoe)
    //The parameters were made in this form so that each boards buttons can be checked
    //Along with the board as a whole
    //Ex: can check if 3 buttons win a board, and if 3 boards win a game
    public static Pair <Boolean, String> checkWinner(String[][] gBVals, int bd) {


        if (gBVals[bd][0].equals(gBVals[bd][1]) && gBVals[bd][0].equals(gBVals[bd][2]) &&
            foundWinner == false) {
            if (gBVals[bd][0] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][0]);
            }
        }
        if (gBVals[bd][0].equals(gBVals[bd][3]) && gBVals[bd][0].equals(gBVals[bd][6]) &&
            foundWinner == false) {
            if (gBVals[bd][0] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][0]);
            }
        }
        if (gBVals[bd][6].equals(gBVals[bd][7]) && gBVals[bd][6].equals(gBVals[bd][8]) &&
            foundWinner == false) {
            if (gBVals[bd][6] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][6]);
            }
        }
        if (gBVals[bd][3].equals(gBVals[bd][4]) && gBVals[bd][3].equals(gBVals[bd][5]) &&
            foundWinner == false) {
            if (gBVals[bd][3] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][3]);
            }
        }
        if (gBVals[bd][1].equals(gBVals[bd][4]) && gBVals[bd][1].equals(gBVals[bd][7]) &&
            foundWinner == false) {
            if (gBVals[bd][1] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][1]);
            }
        }
        if (gBVals[bd][2].equals(gBVals[bd][5]) && gBVals[bd][2].equals(gBVals[bd][8]) &&
            foundWinner == false) {
            if (gBVals[bd][2] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][2]);
            }
        }
        if (gBVals[bd][2].equals(gBVals[bd][4]) && gBVals[bd][2].equals(gBVals[bd][6]) &&
            foundWinner == false) {
            if (gBVals[bd][2] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][2]);
            }
        }
        if (gBVals[bd][0].equals(gBVals[bd][4]) && gBVals[bd][0].equals(gBVals[bd][8]) &&
            foundWinner == false) {
            if (gBVals[bd][0] != "") {
                return new Pair < Boolean, String > (true, gBVals[bd][0]);
            }
        }

        return new Pair < Boolean, String > (false, "");
    }

    //Clears the game, setting all buttons to initial state
    //Brings all variables back to initial state
    public void clearGame() {

        turn = "X";
        for (Button button: buttonBox) {
            button.setText("");
            button.setStyle("-fx-background-color: ");
        }

        for (int i = 0; i < 9; i++) {
            winBoardVals[0][i] = "";
            for (int j = 0; j < 9; j++) {
                gameBoardVals[i][j] = "";
            }
        }

        drawCounter = 0;
        foundWinner = false;
        chooseBoard = true;
        openBoard = 81;
        lastBoard = 0;
        lastButton = 0;

        addColor();

        window.setTitle("Player " + turn + "'s Turn");
    }

    /*
		Algorithm calculates the index of the clicked button with
		help of the array created using index it is able to set
		the correct board to glow. The board glow indicates the next playable
		board based on if the hovered button is clicked
		*/
    public void glowSpots(Button button, double glowNum) {
        for (Button button1: buttonBox) {

            int hoverButtonIndex = 0;

            if (button1 == button) {
                if (buttonBox.indexOf(button1) > 8) {

                    int subtractionIndex = buttonBox.indexOf(button1) / 9;

                    DecimalFormat df = new DecimalFormat();
                    df.setRoundingMode(RoundingMode.DOWN);
                    String number = df.format(subtractionIndex);
                    subtractionIndex = Integer.parseInt(number);

                    int hoverButton = buttonBox.indexOf(button1) - (9 * subtractionIndex);
                    hoverButtonIndex = hoverButton;
                } else {
                    int hoverButton = buttonBox.indexOf(button1);
                    hoverButtonIndex = hoverButton;
                }

                gameBox.get(hoverButtonIndex).setEffect(new Glow(glowNum));

            }
        }
    }

    //Finds the index of the board (0 to 9) and button (0 to 9 of each board)
    public static int[] findLocation(Button button) {

        int selBoard = 0;
        int selButton = 0;

        for (TilePane tile: gameBox) {
            if (tile == (TilePane) button.getParent()) {
                selBoard = gameBox.indexOf(tile);

                for (Button button1: buttonBox) {
                    if (button == button1) {
                        selButton = buttonBox.indexOf(button) - (9 * (selBoard));
                    }
                }
            }
        }
        int[] values = new int[2];
        values[0] = selBoard;
        values[1] = selButton;

        return values;
    }

    //Adds color to buttons and board based on whatever player controls it
    public static void addColor() {

        CharSequence csX = "X";
        CharSequence csO = "O";

        for (int i = 0; i < 9; i++) {
            if (openBoard == i) {
                gameBox.get(openBoard).setStyle("-fx-border-color: black");
            } else {
                gameBox.get(i).setStyle("-fx-border-color:");
            }
        }

        for (Button button: buttonBox) {
            if (button.getText().contains(csX)) {
                button.setStyle("-fx-background-color: #F86B69; ");
            } else if (button.getText().contains(csO)) {
                button.setStyle("-fx-background-color: #B2CBE5; ");
            }
        }

        for (TilePane tile: gameBox) {
            for (Node node: tile.getChildren()) {
                if (winBoardVals[0][findLocation((Button) node)[0]] != "") {
                    if (winBoardVals[0][findLocation((Button) node)[0]].contains(csX)) {
                        node.setStyle("-fx-background-color: #F86B69; ");
                    } else if (winBoardVals[0][findLocation((Button) node)[0]].contains(csO)) {
                        node.setStyle("-fx-background-color: #B2CBE5; ");
                    }
                }
            }
        }
    }

    //Using logic from the checkWinner() method, displays the winner of the game
    public static void checkWinnerLogic() {

        for (int i = 0; i < 9; i++) {
            if (checkWinner(gameBoardVals, i).getKey() == true) {
                winBoardVals[0][i] = checkWinner(gameBoardVals, i).getValue();
            }
        }

        if (drawCounter == 81 && window.getTitle() != "Player X Has Won!" && window.getTitle() != "Player O Has Won!") {
            foundWinner = true;
            window.setTitle("Draw! Nobody Wins");
        }

        if (checkWinner(winBoardVals, 0).getKey() == true) {
            foundWinner = true;
            window.setTitle("Player " + checkWinner(winBoardVals, 0).getKey() + " Has Won!");
        }

    }

    //The board is playable if it contains neither X or O
    public static boolean DoesThisMoveWork(Button button) {

        selectedBoard = findLocation(button)[0];
        selectedButton = findLocation(button)[1];
        //System.out.println(selectedBoard + "-" + selectedButton);

        if ((openBoard == selectedBoard || chooseBoard == true) && foundWinner == false) {
            if (winBoardVals[0][selectedBoard] == "") {
                if (button.getText() == "") {
                    return true;
                }
            }
        }
        return false;
    }

    //Sets up the next move by choosing the next playable board
    public static void nextMoveSetUp() {

        openBoard = lastButton;

        chooseBoard = false;

        if (winBoardVals[0][openBoard] != "") {
            chooseBoard = true;
        }

        boolean FullBoard = false;
        int count = 0;

        for (int i = 0; i < 9; i++) {
            if (gameBoardVals[openBoard][i] != "") {
                count++;
            }
        }

        if (count == 9) {
            FullBoard = true;
            chooseBoard = true;
        }
    }

    //Starts the next turn by changing the title text to X or O
    //Uses the drawCounter which counts each turn to figure out the turn it should be on
    public static void nextTurn() {
        if (foundWinner == false) {
            if (drawCounter % 2 == 1) {
                turn = "O";
                window.setTitle("It is Player " + turn + "'s turn");
            } else if (drawCounter % 2 == 0) {
                turn = "X";
                window.setTitle("It is Player " + turn + "'s turn");
            }
        }

    }

    //Cycles through background images in the res folder to change background
    public static void cycleBg(Pane pane) {

        String[] bgNames = new String[] {
            "bgForGame.png",
            "bgForGame2.png",
            "bgForGame3.png",
            "bgForGame4.png",
            "bgForGame5.png"
        };
        bgCounter++;
        if (bgCounter > bgNames.length - 1) {
            bgCounter = 0;
        }

        URL url4 = UltimateTicTacToe.class.getResource("/" + bgNames[bgCounter]);
        Image bg = null;
        try {
            bg = new Image(new FileInputStream(url4.getPath()));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        BackgroundImage backgroundimage = new BackgroundImage(bg,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

        background = new Background(backgroundimage);

        pane.setBackground(background);
    }
}
