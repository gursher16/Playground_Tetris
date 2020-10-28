package com.playground.tetris;

import java.awt.event.KeyListener;
import java.util.Scanner;

import javax.swing.JFrame;

public class Tetris {

	public static void main(String args[]) {

		System.out.println("Welcome to Tetris!");
		System.out.println("Press Enter to continue..");
		Scanner in = new Scanner(System.in);
		in.nextLine();
		Assets assets = new Assets();
		GameLoop gameLoop = new GameLoop(assets);
		createEnvironmentForKeyListener(gameLoop);

		// assets.displayAllPieces();

		try {
			gameLoop.startGame();
		} catch (InterruptedException e) {
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
