

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;


import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Wave extends Client {

	int TransFinished = 0;
	int DelayDuration;
	int StartY;
	static long startTime;
	static int[][] combinedVals;
	static ArrayList<Integer> timeVals = new ArrayList<Integer>();
	static long time;
	
	
	Wave(int waveNum) {
		
		
		if (waveNum == 0 ){
	
		startTime = System.currentTimeMillis()/1000;
		}
		
		waveNumber = waveNum;	
		
		System.out.println(gameRandomValues.size());
		
	if (waveNum >= (gameRandomValues.size())) {
		spawnBaddies();
	} else {
		runWave(waveNum);
	}
		//startY = gameRandomValues.get(waveNum)[][]
		//DelayDuraton = gameRandomValues.get(waveNum)[][]
	}
	//2 arrays size amt
	//Each rep, store random values in array
	//Put array in ArrayList for each wave

	public int spawnRate() {
			
			double spawnAmt = Math.round(((20)/(1+(1*Math.pow(Math.E,(-1.2*getTime())+3.5)))));
			//System.out.println(spawnAmt);
			return (int) spawnAmt;
		}
	public void spawnBaddies() {
		
			//int amt = spawnRate();

			runWave(getWaveNumber());
		}
	
	public void runWave(int waveNum) {

		System.out.println(waveNum + " Wave");
		int amt = spawnRate();
		System.out.println(amt  + " amt that will be spawned");
		
		
		for (int i = 0; i < (gameRandomValues.get(waveNum))[0].length; i++) {
		
			int index = i;
			
			
			enemies.add(i,new Rectangle(50,25,Color.MEDIUMVIOLETRED));
			enemies.get(i).setX(800);
			enemies.get(i).setY((gameRandomValues.get(waveNum))[0][i]);
			root.getChildren().add(enemies.get(i));
		
			PathTransition eneTrans = new PathTransition();
			eneTrans.setDuration(Duration.millis(2000));
			eneTrans.setNode(enemies.get(i));
			eneTrans.setDelay(Duration.millis((gameRandomValues.get(waveNum))[1][i]));
			eneTrans.setCycleCount(1);
			eneTrans.setAutoReverse(false);
			eneTrans.setPath(new Line(enemies.get(i).getX(), enemies.get(i).getY(), -100, enemies.get(i).getY()));
			eneTrans.play();
			
			
			eneTrans.setOnFinished(e -> {
				eneTrans.stop();
				TransFinished++;
				//System.out.println("One Trans finished");
				if (TransFinished >= enemies.size()) {
					enemies.removeAll(enemies);
					System.out.println("All Enemies Gone");
					TransFinished = 0;
					timeVals.add(getTime());
			
					setWaveNumber((getWaveNumber()) + 1);
				
					new Wave(super.getWaveNumber());
				}
			});
		}
	}
	
	public void eneTransOnStop(PathTransition eneTrans) {
		eneTrans.stop();
		TransFinished++;
		//System.out.println("One Trans finished");
		if (TransFinished >= enemies.size()) {
			enemies.removeAll(enemies);
			System.out.println("All Enemies Gone");
			TransFinished = 0;
			timeVals.add(getTime());
	
			setWaveNumber((getWaveNumber()) + 1);
		
			new Wave(super.getWaveNumber());
		}
	}
	public int ranStartY() {
		this.StartY = ran.nextInt(480);
		return this.StartY;
	}
	public int ranDelayDuration() {
		this.DelayDuration = ran.nextInt(10000);
		return this.DelayDuration;
	}
	public void moveStartTime(long offset){
		startTime = startTime + (offset);
	}
	
	public int getTime() {
		time = (((new Date().getTime()/1000 - startTime)));
		System.out.println("You Grabbed " + time + " secounds");
		return (int) time;
	}
/*	
	public int getTime(int waveNum) {
		time = (((new Date().getTime() - startTime + timeVals.get(waveNum)))/1000);
		return (int) time;
	}
	*/
}
