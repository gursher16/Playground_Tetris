package com.playground.tetris;

import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.JFrame;

/**
 * Class that sets up the game environment and starts the game
 * 
 * @author Gursher
 *
 */
public class Tetris {

	public static void main(String args[]) {

		try {
			int difficulty = 0;
			boolean isValid = false;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println(
					"===Welcome to Tetris!===\n" + "Select Difficulty:\n" + "1. Easy\n" + "2. Medium\n" + "3. Hard\n");

			while (!isValid) {
				try {
					difficulty = Integer.parseInt(br.readLine());
					isValid = (difficulty <= 3 && difficulty > 0);
					if (!isValid) {
						System.out.println("Incorrect Choice!\n" + "Please choose again");
					}
				} catch (NumberFormatException ex) {
					System.out.println("Incorrect Choice!\n" + "Please choose again");
				}
			}

			System.out.println("Starting game..");
			Thread.sleep(500);
			Assets assets = new Assets();
			GameLoop gameLoop = new GameLoop(assets, difficulty);
			createEnvironmentForKeyListener(gameLoop);
			gameLoop.startGame();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * Creates a JFrame used to capture input for the game -- workaround for Java
	 * Console IO limitations
	 * 
	 * @param keyListener
	 */
	private static void createEnvironmentForKeyListener(KeyListener keyListener) {
		JFrame jFrame = new JFrame();
		jFrame.setVisible(true);
		jFrame.setSize(10, 10);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.addKeyListener(keyListener);
	}

}
