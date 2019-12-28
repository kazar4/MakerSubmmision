package newStuff;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JButton;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class TicTacToeFour extends Application {
	
	Scene scene;
	static String turn = "X";
	static ArrayList<Button> buttonBox = new ArrayList<Button>();
	static Button button1, button2, button3, button4, button5, button6, button7, button8, button9, clearButton, saveButton,
			loadButton, hostButton, connectButton;
	static int drawCounter;
	static boolean foundWinner = false;
	static Stage window;
	static boolean needToUpdate = true;
//Used for wifi connection
	TTTFourExtra extraMethods;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		window = primaryStage;

		primaryStage.setTitle("TicTacToe 4");

		Button button = new Button("Click To Start");
		button.setOnAction(e -> gameScreen(scene, primaryStage));

		BorderPane layout = new BorderPane();

		layout.setBottom(button);

		Scene scene = new Scene(layout, 300, 350);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void gameScreen(Scene scene, Stage stage) {

		Insets inset = new Insets(5, 25, 5, 25);
		FlowPane flow = new FlowPane();
		flow.setPadding(inset);
		flow.setVgap(4);
		flow.setHgap(4);

		/*
		 * //flow.setPrefColumns(3); //flow.setPrefRows(3); TilePane tile = new
		 * TilePane(); tile.setPadding(inset); tile.setVgap(4); tile.setHgap(4);
		 * tile.setPrefColumns(3); tile.setPrefRows(3);
		 */

		Dimension d = new Dimension(80, 80);

		button1 = new Button("");
		button1.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button1);
		button1.setOnAction(e -> makeMove(button1));

		button2 = new Button("");
		button2.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button2);
		button2.setOnAction(e -> makeMove(button2));

		button3 = new Button("");
		button3.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button3);
		button3.setOnAction(e -> makeMove(button3));

		button4 = new Button("");
		button4.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button4);
		button4.setOnAction(e -> makeMove(button4));

		button5 = new Button("");
		button5.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button5);
		button5.setOnAction(e -> makeMove(button5));

		button6 = new Button("");
		button6.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button6);
		button6.setOnAction(e -> makeMove(button6));

		button7 = new Button("");
		button7.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button7);
		button7.setOnAction(e -> makeMove(button7));

		button8 = new Button("");
		button8.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button8);
		button8.setOnAction(e -> makeMove(button8));

		button9 = new Button("");
		button9.setPrefSize(d.getWidth(), d.getHeight());
		buttonBox.add(button9);
		button9.setOnAction(e -> makeMove(button9));

		clearButton = new Button("Clear");
		clearButton.setOnAction(e -> clearGame());
		
		extraMethods = new TTTFourExtra();
		
		saveButton = new Button("Save");
		saveButton.setOnAction(e -> extraMethods.saveGame());
		
		loadButton = new Button("Load");
		loadButton.setOnAction(e -> extraMethods.loadGame());
		
		hostButton = new Button("Host");
		hostButton.setOnAction(e -> extraMethods.hostGame());
		
		connectButton = new Button("Connect");
		connectButton.setOnAction(e -> extraMethods.connectGame());

		flow.getChildren().addAll(button1, button2, button3, button4, button5, button6, button7, button8, button9);
		flow.getChildren().addAll(clearButton, saveButton, loadButton, hostButton, connectButton);

		scene = new Scene(flow, 300, 350);
		stage.setScene(scene);
		stage.show();

	}

	public void makeMove(Button button) {

		if (button.getText() == "") {
			button.setText(turn);
			drawCounter++;

			if (turn == "X") {
				turn = "O";

			} else if (turn == "O") {
				turn = "X";
			}
			
			if (TTTFourExtra.connection.isConnected()) {

				extraMethods.sendData();
			}

			updateGame();

		}

	}

	public static void updateGame() {

		if (foundWinner == false) {

			window.setTitle("It is Player " + turn + " turn");
		}

		if (button1.getText().equals(button2.getText()) && button1.getText().equals(button3.getText())
				&& foundWinner == false) {
			if (button1.getText() != "") {
				window.setTitle("Player " + button1.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button1.getText().equals(button4.getText()) && button1.getText().equals(button7.getText())
				&& foundWinner == false) {
			if (button1.getText() != "") {
				window.setTitle("Player " + button1.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button7.getText().equals(button8.getText()) && button7.getText().equals(button9.getText())
				&& foundWinner == false) {
			if (button7.getText() != "") {
				window.setTitle("Player " + button7.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button4.getText().equals(button5.getText()) && button4.getText().equals(button6.getText())
				&& foundWinner == false) {
			if (button4.getText() != "") {
				window.setTitle("Player " + button4.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button2.getText().equals(button5.getText()) && button2.getText().equals(button8.getText())
				&& foundWinner == false) {
			if (button2.getText() != "") {
				window.setTitle("Player " + button2.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button3.getText().equals(button6.getText()) && button3.getText().equals(button9.getText())
				&& foundWinner == false) {
			if (button3.getText() != "") {
				window.setTitle("Player " + button3.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button3.getText().equals(button5.getText()) && button3.getText().equals(button7.getText())
				&& foundWinner == false) {
			if (button3.getText() != "") {
				window.setTitle("Player " + button3.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (button1.getText().equals(button5.getText()) && button1.getText().equals(button9.getText())
				&& foundWinner == false) {
			if (button1.getText() != "") {
				window.setTitle("Player " + button1.getText() + " Has Won!");
				foundWinner = true;
			}
		}
		if (drawCounter == 9 && window.getTitle() != "Player X Has Won!" && window.getTitle() != "Player O Has Won!"
				&& foundWinner == false) {
			window.setTitle("Draw! Nobody Wins");
		}
	}
	public void clearGame() {
		
		turn = "X";
		button1.setText("");
		button2.setText("");
		button3.setText("");
		button4.setText("");
		button5.setText("");
		button6.setText("");
		button7.setText("");
		button8.setText("");
		button9.setText("");

		window.setTitle("Player " + turn + "'s Turn");
		drawCounter = 0;
		foundWinner = false;
	}
}
