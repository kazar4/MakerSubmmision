package newStuff;

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

import javax.swing.JButton;

import javafx.application.Platform;
import javafx.scene.control.Button;


public class TTTFourExtra extends TicTacToeFour {

	static FileWriter fw;
	static BufferedWriter bw;
	static PrintWriter pw;
	// static File file;

	static BufferedReader bis = null;
	static FileReader fis = null;

	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	private static ServerSocket server;
	static Socket connection;

	CharSequence csE = "E";

	String serverName = "74.97.190.170";

	// File file = new
	// File(TicTacToeFour.class.getResource("/res/gameSave.txt").getFile());
	File file = new File("/Users/kazengallman/Desktop/newGame.txt");

	public void saveGame() {

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);

			for (Button i : buttonBox) {
				pw.println(i.getText());
			}

			pw.println(drawCounter);
			pw.println(turn);
			pw.println(foundWinner);
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

	public void loadGame() {

		clearGame();

		int i = 0;
		String contentLine = null;
		CharSequence csO = "O";
		CharSequence csX = "X";

		try {

			ClassLoader classLoader = getClass().getClassLoader();

			// bis = new BufferedReader(new
			// FileReader("/Users/kazengallman/Desktop/newfile.txt"));

			bis = new BufferedReader(new FileReader(file));

			while (i < 8) {

				contentLine = bis.readLine();

				if (contentLine.contains(csO) == false && contentLine.contains(csX) == false) {

					contentLine = "";
				}

				buttonBox.get(i).setText(contentLine);

				// buttonBox.get(i).getText().contains(csO)

				if (buttonBox.get(i).getText().contains(csO)) {
				}

				// buttonBox.get(i).getText().contains(csX)
				if (buttonBox.get(i).getText().contains(csX)) {
				}

				i++;

			}

			bis.readLine();
			drawCounter = Integer.parseInt(bis.readLine());
			// System.out.println(drawCounter);

			turn = bis.readLine();
			if (turn.contains(csX)) {
				turn = "X";
			} else if (turn.contains(csO)) {
				turn = "O";
			}

			// System.out.println(turn);
			foundWinner = Boolean.valueOf(bis.readLine());
			// System.out.println(foundWinner);

			if (foundWinner == true) {
				foundWinner = false;
			}
			updateGame();
		}

		catch (IOException e) {

			e.printStackTrace();
			window.setTitle("Error Loading Save");
		} finally {
			try {
				bis.close();
			} catch (IOException e) {

				e.printStackTrace();

			}
		}
		try {
			if (connection.isConnected()) {
				sendData();
			}
		} catch (NullPointerException npe) {

		}

	}

	 public void sendData() {

		try {

			for (Button i : buttonBox) {
				output.writeObject(i.getText());
				output.flush();

			}
			output.writeObject(drawCounter);
			output.flush();
			output.writeObject(turn);
			output.flush();
			output.writeObject(foundWinner);
			output.flush();

		} catch (IOException ioException) {
		}
	}

	public void readData() throws ClassNotFoundException {

		int i = 0;
		CharSequence csO = "O";
		CharSequence csX = "X";
		String contentLine = null;

		while (i < 9) {

			try {

				try {
					contentLine = (String) input.readObject();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					contentLine = "";
				}

				if (contentLine.contains(csO) == false && contentLine.contains(csX) == false) {

					contentLine = "";
				}

				buttonBox.get(i).setText(contentLine);
				//System.out.println(buttonBox.get(i));

				// buttonBox.get(i).getText().contains(csO)

				if (buttonBox.get(i).getText().contains(csO)) {
					// buttonBox.get(i).setOpaque(true);
					// buttonBox.get(i).setBackground(Color.RED);
				}

				// buttonBox.get(i).getText().contains(csX)
				if (buttonBox.get(i).getText().contains(csX)) {
					// buttonBox.get(i).setOpaque(true);
					// buttonBox.get(i).setBackground(Color.BLUE);
				}

				if (buttonBox.get(i).getText().contains(csO) == false
						&& buttonBox.get(i).getText().contains(csX) == false) {

					// buttonBox.get(i).setOpaque(false);

				}

				i++;

			} catch (IOException ioException) {

			}
		}

		try {

			try {
				drawCounter = (Integer) input.readObject();
			} catch (NullPointerException nullpointerException) {

				drawCounter = 0;
			}

			// System.out.println(drawCounter);
			try {
				turn = input.readObject().toString();
			} catch (NullPointerException nullpointerException) {

			}
			if (turn.contains(csX)) {
				turn = "X";
			} else if (turn.contains(csO)) {
				turn = "O";
			}
			try {
				// System.out.println(turn);
				foundWinner = Boolean.getBoolean(input.readObject().toString());
				// System.out.println(foundWinner);
			} catch (NullPointerException nullpointerException) {

			}
			if (foundWinner == true) {
				foundWinner = false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public class TTT_Four_Thread implements Runnable {

		@Override
		public void run() {

			while (true) {

				try {
					readData();
					TicTacToeFour.updateGame();

				} catch (NumberFormatException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Found Issue");
				}

			}

		}

	}
	
	public void hostGame() {
		
		try{

			server = new ServerSocket(65535, 100);
			connection = server.accept();
			System.out.println("Server Found Connection");

			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();

			input = new ObjectInputStream(connection.getInputStream());


			}catch (IOException ioException) {

			}

		Thread Thread1 = new Thread(new TTT_Four_Thread());
		Thread1.start();
		
	}
	
	public void connectGame() {
		
		

			  try {
					connection = new Socket(InetAddress.getByName(serverName), 65535);

					output = new ObjectOutputStream(connection.getOutputStream());
					output.flush();
					input = new ObjectInputStream(connection.getInputStream());


					} catch (IOException ioException) {

				}

			  Thread Thread1 = new Thread(new TTT_Four_Thread());
			  Thread1.start();

		  }
/*	
	
public void doRunLater() {
					 Platform.runLater(new Runnable() {
				            @Override
				            public void run() {
				            
				            	while(true) {
				    				try {
										readData();
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				    				
				    				updateGame();
				            	}
				            	
				            }
				          });       

			}
}
//Work in progress
		*/

	}

