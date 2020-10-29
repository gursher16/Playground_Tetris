package com.playground.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import com.playground.tetris.Assets.Tetromino;

/**
 * Class containing core game loop and functionality
 * 
 * @author Gursher
 *
 */
public class GameLoop implements KeyListener {

	Assets assets;
	// the current piece
	Tetromino currentPiece;
	// tracks the game state
	boolean isGameOver;
	// rotation of the current piece
	int currentRotation;
	// X coordinate of current piece
	int currentX;
	// Y coordinate of current piece
	int currentY;
	// base speed of the game - increment to increase difficulty
	float speed;
	// used to control the timing
	float speedCounter;
	// used to control the speed of descent of pieces
	float speedMultiplier;
	// flag to move tetromino downwards
	boolean forcePieceDown;
	// list of horizontal lines formed by adjacent pieces
	List<Integer> horizontalLines;
	// tracks the game score
	int score;

	public GameLoop(Assets assets, int difficulty) {
		isGameOver = false;
		this.assets = assets;
		currentPiece = Tetromino.ONE;
		currentRotation = 0;
		currentY = 0;
		currentX = assets.boardWidth / 2;

		switch (difficulty) {
		case 1:
			speed = 60f;
			break;
		case 2:
			speed = 40f;
			break;
		case 3:
			speed = 20f;
			break;
		}

		speedCounter = 0f;
		speedMultiplier = 1f;
		forcePieceDown = false;
		horizontalLines = new ArrayList<>();
		score = 0;
	}

	/**
	 * Main game loop -- Starts the game
	 * 
	 * @throws InterruptedException
	 */
	public void startGame() throws InterruptedException {

		while (!isGameOver) {

			clearConsole();
			speedCounter += speedMultiplier;
			forcePieceDown = (speedCounter >= speed);

			// place current piece on board
			placePieceOnBoard(currentPiece, currentRotation, currentX, currentY);
			// render board with current piece
			renderBoard();
			// remove any full horizontal lines created by pieces
			removeHorizontalLine();
			// check if piece can move downwards
			if (forcePieceDown) {
				if (doesPieceFit(currentPiece, currentRotation, currentX, currentY + 1)) {
					currentY += 1; // move the piece downward
				} else {
					// Lock current piece in the field
					lockPieceOnBoard(currentPiece, currentRotation, currentX, currentY);
					// detect if any full horizontal line is formed by the pieces
					checkForHorizontalLine(currentX, currentY);
					// create next piece
					createNewPiece();
					// check if new piece can fit at the top of the screen - if not then game over
					isGameOver = !doesPieceFit(currentPiece, currentRotation, currentX, currentY);
				}
				speedCounter = 0;
			}
		}

		System.out.println("\n\n===GAME OVER!!===");
	}

	/**
	 * Creates a new tetromino
	 */
	private void createNewPiece() {
		currentX = assets.boardWidth / 2;
		currentY = 0;
		currentRotation = 0;
		currentPiece = Tetromino.values()[(int) (Math.random() * Tetromino.values().length)];
		speedMultiplier += 0.5;
	}

	// ===================RENDER BOARD=============================//

	/**
	 * Clears the console
	 */
	private void clearConsole() {
		final String os = System.getProperty("os.name");

		try { // Clear console on windows
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				// Clear console on linux
				Runtime.getRuntime().exec("clear");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that renders the playing board
	 * 
	 * @throws InterruptedException
	 */
	public void renderBoard() throws InterruptedException {
		for (int y = 0; y < assets.boardHeight; y++) {
			for (int x = 0; x < assets.boardWidth; x++) {
				if (assets.board[y * assets.boardWidth + x] == 1) {
					System.out.print("#");
				} else if (assets.board[y * assets.boardWidth + x] == 2) {
					System.out.print("@");
					// set pos back to zero
					assets.board[y * assets.boardWidth + x] = 0;
				} else if (assets.board[y * assets.boardWidth + x] == 3) {
					System.out.print("@");
				} else if (assets.board[y * assets.boardWidth + x] == 4) {
					System.out.print("=");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("===SCORE: " + score + "===");
	}

	/**
	 * Method that renders the playing board with field values -- used for debugging
	 * purposes
	 */
	public void renderBoardDebug() {
		System.out.println("==============DEBUG=======");
		for (int y = 0; y < assets.boardHeight; y++) {
			for (int x = 0; x < assets.boardWidth; x++) {
				if (assets.board[y * assets.boardWidth + x] == 1) {
					System.out.print("1");
				} else if (assets.board[y * assets.boardWidth + x] == 2) {
					System.out.print("2");
					// set pos back to zero
					assets.board[y * assets.boardWidth + x] = 0;
				} else if (assets.board[y * assets.boardWidth + x] == 3) {
					System.out.print("3");
				} else if (assets.board[y * assets.boardWidth + x] == 4) {
					System.out.print("4");
				} else {
					System.out.print("0");
				}
			}
			System.out.println();
		}
	}

	private void renderDebugInfo(int currentX, int currentY) {
		System.out.println();
		System.out.println();
		System.out.println("CurrentX: " + currentX);
		System.out.println("CurrentY: " + currentY);
		if (!horizontalLines.isEmpty()) {
			System.out.println("Lines value: " + horizontalLines.get(0));
		}
	}

	// ===================GAME LOGIC===============================//

	/**
	 * Places a tetromino on the on the board
	 * 
	 * @param tPiece
	 * @param currentRotation
	 * @param currentX
	 * @param currentY
	 */
	public void placePieceOnBoard(Tetromino tPiece, int currentRotation, int currentX, int currentY) {
		for (int pY = 0; pY < 4; pY++) {
			for (int pX = 0; pX < 4; pX++) {
				if (tPiece.piece.charAt(tPiece.rotate(pX, pY, currentRotation)) == 'X') {
					assets.board[((currentY + pY) * assets.boardWidth) + currentX + pX] = 2;
				}
			}
		}
	}

	/**
	 * Locks a tetromino on the board permanently
	 * 
	 * @param tPiece
	 * @param currentRotation
	 * @param currentX
	 * @param currentY
	 */
	public void lockPieceOnBoard(Tetromino tPiece, int currentRotation, int currentX, int currentY) {
		for (int pY = 0; pY < 4; pY++) {
			for (int pX = 0; pX < 4; pX++) {
				if (tPiece.piece.charAt(tPiece.rotate(pX, pY, currentRotation)) == 'X') {
					assets.board[((currentY + pY) * assets.boardWidth) + currentX + pX] = 3;
				}
			}
		}
		// increment score by 25 per piece locked in
		score += 25;
	}

	/**
	 * Method to perform collision detection. Checks if a tetromino can fit in a
	 * given position on the board.
	 * 
	 * @param tPiece
	 * @param rotation
	 * @param currentX
	 * @param currentY
	 * @return
	 */
	private boolean doesPieceFit(Tetromino tPiece, int rotation, int currentX, int currentY) {
		for (int pY = 0; pY < 4; pY++) {
			for (int pX = 0; pX < 4; pX++) {

				// get index of piece factoring in the rotation
				int pI = tPiece.rotate(pX, pY, rotation);

				// get board index of piece
				int bI = ((currentY + pY) * assets.boardWidth) + (currentX + pX);

				// collision detection
				if ((currentX + pX >= 0) && (currentX + pX < assets.boardWidth)) {
					if ((currentY + pY >= 0) && (currentY + pY < assets.boardHeight)) {
						if ((tPiece.piece.charAt(pI) == 'X') && (assets.board[bI] != 0)) {
							// piece cannot fit in board position
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the latest tertomino that is locked on the board forms a full
	 * horizontal line with neighbouring pieces
	 * 
	 * @param currentX
	 * @param currentY
	 */
	private void checkForHorizontalLine(int currentX, int currentY) {

		for (int pY = 0; pY < 4; pY++) {
			if (currentY + pY < assets.boardHeight - 1) {
				// assume there exists a horizontal line
				boolean isLine = true;
				for (int pX = 1; pX < assets.boardWidth - 1; pX++) {
					// if any blank spaces are present - then isLine = false
					if (assets.board[((pY + currentY) * assets.boardWidth) + pX] == 0) {
						isLine = false;
					}
				}
				if (isLine) {
					for (int pX = 1; pX < assets.boardWidth - 1; pX++) {
						assets.board[((currentY + pY) * assets.boardWidth) + pX] = 4;
					}
					// add row to list
					horizontalLines.add(currentY + pY);
				}
			}
		}
	}

	/**
	 * Removes a horizontal line formed by tetrominoes and shifts the remaining
	 * pieces downwards
	 */
	private void removeHorizontalLine() {
		for (int i : horizontalLines) {
			for (int pX = 1; pX < assets.boardWidth - 1; pX++) {
				for (int pY = i; pY > 0; pY--) {
					assets.board[(pY * assets.boardWidth) + pX] = assets.board[((pY - 1) * assets.boardWidth) + pX];
					// assets.board[pX] = 0;
				}
			}
		}
		// add 100 per line to score -- for 4 consecutive lines, add 1000
		if (!horizontalLines.isEmpty()) {
			if (horizontalLines.size() % 4 == 0) {
				score += (horizontalLines.size() / 4) * 1000;
			} else {
				score += horizontalLines.size() * 100;
			}
		}
		horizontalLines.clear();
	}

	// ===================USER INPUT==============================//
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (doesPieceFit(currentPiece, currentRotation + 1, currentX, currentY)) {
				currentRotation += 1;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (doesPieceFit(currentPiece, currentRotation, currentX, currentY + 1)) {
				currentY += 1;
			}
			break;
		case KeyEvent.VK_LEFT:
			if (doesPieceFit(currentPiece, currentRotation, currentX - 1, currentY)) {
				currentX -= 1;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (doesPieceFit(currentPiece, currentRotation, currentX + 1, currentY)) {
				currentX += 1;
			}
			break;
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
