



package com.example.chess;

/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class piece where other pieces (king, queen, pawn, etc.) extend it to get info about color, row, etc.
 *
 */

public class Piece {

	/**
	 * <p>
	 * location specifies location of piece
	 *
	 * isWhite tells the color of the piece
	 *
	 * hasMoved tells if the piece has moved
	 */

	int[] location;
	boolean isWhite;
	boolean hasMoved;

	/**
	 * <p>
	 * class constructor specifying location of piece and color of it (black or white).
	 *
	 */

	public Piece(int[] loc, boolean white) {

		location=loc;
		isWhite=white;
		this.hasMoved = false;
	}


	/**
	 * <p>
	 * This method checks to see if the piece can reach to destination and returns true/false if it can
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param dest the destination index of where the piece wants to land
	 *
	 * @return if the piece can land on that destination or not
	 *
	 */

	public boolean moveLegal(Piece[][] board, int[] dest) {
		return false;
	}

	/** <p>
	 * This method returns the row number of the piece
	 *
	 * @return he row number of the piece
	 */

	public int getRow() {
		//Translation to board: 8-getRow() = 8 -> 1
		return location[0];
	}

	/** <p>
	 * This method returns the row column of the piece
	 *
	 * @return he row column of the piece
	 */

	public int getCol() {
		//Translation to board: (char)(97+getCol()) is 'a' ->'h'
		return location[1];
	}

	/** <p>
	 * This method returns if the piece has moved or not
	 *
	 * @return if the piece has moved or not
	 */

	public boolean hasMoved() {
		return hasMoved;
	}

	/** <p>
	 * This method sets if the piece has moved
	 */

	public void setMoved() {
		this.hasMoved = !this.hasMoved;
	}

	/** <p>
	 * This method returns if the piece's color is white
	 *
	 * @return if the piece's color is white
	 */

	public boolean isWhite() {
		return isWhite;
	}

	/** <p>
	 * This method sets the location
	 *
	 * @param loc a location array where first value is row number and second value is column number
	 */

	public void setLoc(int[] loc) {
		location=loc;
	}

	/** <p>
	 * This method sets the row
	 *
	 * @param row a row number
	 */
	public void setRow(int row) {
		location[0]=row;
	}

	/** <p>
	 * This method sets the col
	 *
	 * @param col a collumn number
	 */

	public void setCol(int col) {
		location[1]=col;
	}

	/** <p>
	 * This method returns the name of piece in string format
	 *
	 * @return spaces which is " " or ## to make a checkered board
	 */
	public String toString() {
		if(isWhite)
			return "  ";
		else return "##";
	}


}
