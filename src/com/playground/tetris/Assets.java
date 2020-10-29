package com.playground.tetris;

import java.util.Random;

/**
 * Class containing all assets required for rendering the game on the console
 * 
 * @author Gursher
 *
 */
public class Assets {

	public int boardWidth = 16;
	public int boardHeight = 18;
	// internal reprentation of the playing board
	public int[] board;

	public Assets() {
		board = new int[boardWidth * boardHeight];
		for (int y = 0; y < boardHeight; y++) {
			for (int x = 0; x < boardWidth; x++) {
				if ((x == 0 || x == boardWidth - 1) || (y == boardHeight - 1)) {
					// represents the boundary of the board
					board[y * boardWidth + x] = 1;
				} else {
					// represents the playing space of the board
					board[y * boardWidth + x] = 0;
				}
			}
		}
	}

	// Enum representing all different configurations of a tertomino (tetris piece)
	public enum Tetromino {
		ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

		public String piece;

		// Enum constructor for creating tetromino pieces
		Tetromino(int x) {
			switch (x) {
			case 1:
				piece = "..X." + "..X." + "..X." + "..X.";
				break;
			case 2:
				piece = "..X." + ".XX." + ".X.." + "....";
				break;
			case 3:
				piece = "..X." + ".XX." + "..X." + "....";

				break;
			case 4:
				piece = ".X.." + ".XX." + ".X.." + "....";

				break;
			case 5:
				piece = "...." + ".X.." + ".X.." + ".XX.";

				break;
			case 6:
				piece = "...." + "..X." + "..X." + ".XX.";

				break;
			case 7:
				piece = "...." + ".XX." + ".XX." + "....";

				break;
			default:
				System.err.println("INCORRECT VALUE..");
			}

		}

		// Enum method for rotating tetris pieces
		public int rotate(int pX, int pY, int r) {
			switch (r % 4) {
			case 0:
				return ((pY * 4) + pX);
			case 1:
				return (12 + pY - (pX * 4));
			case 2:
				return (15 - (pY * 4) - pX);
			case 3:
				return (3 - pY + (pX * 4));
			default:
				return -1;
			}
		}

	}

	/**
	 * Method that displays a tetris piece -- use for debugging purposes
	 * 
	 * @param tPiece
	 */
	public void displayPiece(Tetromino tPiece) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				System.out.print(tPiece.piece.charAt(tPiece.rotate(x, y, 0)));
			}
			System.out.println();
		}
	}

	// Method that displays a tetris piece -- use for debugging purposes
	public void displayPiece(Tetromino tPiece, int currentRotation) {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				System.out.print(tPiece.piece.charAt(tPiece.rotate(x, y, currentRotation)));
			}
			System.out.println();
		}
	}

	/**
	 * Method that displays all tetris pieces -- use for debugging purposes
	 */
	public void displayAllPieces() {
		Tetromino piece = Tetromino.ONE;
		displayPiece(piece);
		System.out.println();

		displayPiece(piece, 1);
		System.out.println();
		displayPiece(piece, 2);
		System.out.println();
		displayPiece(piece, 3);
		System.out.println();
		displayPiece(piece, 4);
		System.out.println();
		displayPiece(piece, 5);
		System.out.println();
		displayPiece(piece, 6);
		System.out.println();

		piece = Tetromino.TWO;
		displayPiece(piece);
		System.out.println();

		piece = Tetromino.THREE;
		displayPiece(piece);
		System.out.println();

		piece = Tetromino.FOUR;
		displayPiece(piece);
		System.out.println();

		piece = Tetromino.FIVE;
		displayPiece(piece);
		System.out.println();

		piece = Tetromino.SIX;
		displayPiece(piece);
		System.out.println();

		piece = Tetromino.SEVEN;
		displayPiece(piece);
		System.out.println();
	}

}