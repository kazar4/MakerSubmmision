import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javafx.animation.PathTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ServerCalculations extends ServerWindow implements Serializable {

    //Variables run the animation and behavior of enemies
    int DelayDuration;
    int StartY;
    static long startTime;
    static long time = 30;
    static int waveNumber = 0;
    static Random ran = new Random();

    //Holds variables to eventually be sent to other player
    ArrayList < int[][] > gameRandomValues = new ArrayList < int[][] > ();
    static int[][] combinedVals;
    static ArrayList < Integer > timeVals = new ArrayList < Integer > ();

    public void CalculateValues(int waveNumber) {

        //Amount of enemies that need to be spawned based on spawn rate
        int amt = spawnRate();

        int[] startYVals = new int[amt];
        int[] DelayDurationVals = new int[amt];

        for (int i = 0; i < amt; i++) {

            startYVals[i] = ranStartY();
            DelayDurationVals[i] = ranDelayDuration();

        }

        //Saves game state values
        combinedVals = new int[][] {
            startYVals,
            DelayDurationVals
        };
        gameRandomValues.add(waveNumber, combinedVals);

        //Caps out wave number at 20
        if (waveNumber < 20) {
            waveNumber++;
            CalculateValues(waveNumber);
        }
    }

    //Randomizes starting y position of enemy
    public int ranStartY() {
        this.StartY = ran.nextInt(480);
        return this.StartY;
    }

    //Randomizes starting delay of enemy
    public int ranDelayDuration() {
        this.DelayDuration = ran.nextInt(10000);
        return this.DelayDuration;
    }

    //Calculates the current time passed of the game
    public int getTime() {
        time = (((new Date().getTime() / 1000 - startTime)));
        System.out.println("You Grabbed " + time + " secounds");
        return (int) time;
    }

    //sometime implement a formula package to do this, so like New Formula
    //Logistic growth formula to represent number of enemies spawned
    public int spawnRate() {

        double spawnAmt = Math.round(((20) / (1 + (1 * Math.pow(Math.E, (-1.2 * getTime()) + 3.5)))));
        //System.out.println(spawnAmt);
        return (int) spawnAmt;
    }
}
