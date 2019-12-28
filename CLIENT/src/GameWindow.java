import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class GameWindow extends Application {

    static String player = "";
    static int mouseX; //X position of mouse on screen
    static int mouseY; //Y position of mouse on screen
    static Scene scene;
    static Rectangle rect = new Rectangle(0, 0, 75, 50); //Player 1
    static Group root = new Group(); //Holds player object
    static Stage window;
    static Random ran = new Random(); //For random number generation
    int waveNumber = 0; //represents current wave of enemies
    static Stage secondStage; //connection screen
    static Engine engine = new Engine(); //Class which represents game engine
    static ArrayList < Rectangle > enemies = new ArrayList < Rectangle > (); //Array holding enemies
    static Button button2, button3; //Join and Play buttons
    static MouseEvent eventMouse; //event for when mouse is used
    static Rectangle rect2; //player 2
    static Movement movement; //Class for movement of characters
    static TextArea textbox; //Where IP address is written
    static int time; //Time passed of the current game
    static int lives = 3; //Lives
    static long highscore; //Highscore variable (seconds survived)
    static boolean canDie = true; //Is player invulnerable
    static long startTime = System.currentTimeMillis(); //Time from which game started

    //Loads the connction screen and allows game to start
    public void start(Stage primaryStage) {

        secondStage = new Stage();
        Scene scene2;
        BorderPane Bpane = new BorderPane();
        button2 = new Button("Join");
        button2.setMinWidth(30);
        button3 = new Button("Play");
        button3.setMinWidth(30);
        textbox = new TextArea("Put IP in Here");
        textbox.setPrefSize(50, 50);
        HBox Box = new HBox();
        //Group Box2 = new Group();
        //Box2.getChildren().add(textbox);
        Bpane.setBottom(textbox);
        Bpane.setTop(Box);
        scene2 = new Scene(Bpane, 200, 100);
        Box.getChildren().addAll(button2, button3);
        Box.setTranslateX(scene2.getWidth() / 7);
        secondStage.setScene(scene2);
        secondStage.show();

        button2.setOnMouseClicked(e - > {
            Client client = new Client(primaryStage);
        });
    }

    //Launches GUI
    public static void main(String[] args) {
        launch(args);
    }

    //Setter for mouse X position
    public void setMouseX(int X) {

        mouseX = X;
    }

    //Setter for mouse Y position
    public void setMouseY(int Y) {


        mouseY = Y;
    }

    //Setter for wave number
    public void setWaveNumber(int waveNum) {
        this.waveNumber = waveNum;
    }

    //Getter for wave number
    public int getWaveNumber() {
        return this.waveNumber;
    }

    //Logic & GUI elements for game screen
    public void openMainWindow(Stage primaryStage) {
        secondStage.close();

        startTime = System.currentTimeMillis();

        window = primaryStage;

        root.getChildren().addAll(rect);
        scene = new Scene(root, 720, 480);
        primaryStage.setScene(scene);


        rect2 = new Rectangle(0, 0, 75, 50);
        rect2.setFill(Color.RED); //Color of player 2
        root.getChildren().add(rect2);

        primaryStage.show();
        movement = new Movement();

        //Logic for when mouse of moved
        scene.setOnMouseMoved(e - > {
            movement.move(e);
            if (canDie == true) {
                if (engine.CheckCollision(enemies)) {
                    canDie = false;
                    lives--;
                    engine.playerDied();
                    if (lives <= 0) {
                        highscore = time;
                    }
                }
            }
            //Live logic
            if (lives > 0) {
                time = (int)((new Date().getTime() - startTime) / 1000);
                primaryStage.setTitle(Integer.toString(time) + " secounds survived (" + Integer.toString(lives) + " lives)");
            } else {
                primaryStage.setTitle("You Died, Max Time: " + highscore + " secounds survived");
            }
        });
        engine.EngineRun();
    }
}
