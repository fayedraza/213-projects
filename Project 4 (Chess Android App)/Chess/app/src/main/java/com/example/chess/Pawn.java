
package com.example.chess;

import java.util.Arrays;

/**
 * @author Fayed Raza, Steven Zyontz
 *
 * <p>
 * This is a class of the pawn piece where we need to see if it can legally move to the destination that the user requested while playing chess.
 *
 * @see  Piece
 */

public class Pawn extends Piece{

	/**
	 * <p>
	 * hasMoved tells if the piece has moved
	 */
	boolean hasMoved;

	/**
	 * <p>
	 * class constructor specifying location of piece and color of it (black or white).
	 */
	public Pawn(int[] loc, boolean white) {
		super(loc, white);
		hasMoved=false;
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * This method checks to see if the pawn piece can reach to destination and returns true/false if it can
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param dest the destination index of where the piece wants to land
	 *
	 * @return if the piece can land on that destination or not
	 *
	 */

	public boolean moveLegal(Piece[][] board, int[] dest) {

		if(Arrays.equals(dest, location)) return false;

		int colorDiff=1; //move up if white
		if(!isWhite) colorDiff=-1; //move down if black




		if(dest[0]==location[0]-colorDiff) {
			if(board[dest[0]][dest[1]]==null) {
				hasMoved=true;
				return true;
			}
		}//end 1 move forward

		else if(dest[0]==location[0]-colorDiff*2 && !super.hasMoved && board[location[0]-colorDiff][location[1]]==null) {
			if(board[dest[0]][dest[1]]==null) {
				hasMoved=true;
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				hasMoved=true;
				return true;
			}
		}//end 2 move forward

		if(dest[0]==location[0]-colorDiff && Math.abs(dest[1]-location[1])==1) {
			//System.out.println("HERE");
			if(board[dest[0]][dest[1]]!=null) {
				//System.out.println("HERE2");
				if(board[dest[0]][dest[1]].isWhite!=isWhite) {
					//System.out.println("HERE3");
					hasMoved=true;
					return true;
				}
			}
		}

		return false;
	}

	/** <p>
	 * This method returns the name of piece in string format
	 *
	 * @return the name of the piece where wp means white pawn and bp means black pawn
	 */

	public String toString() {
		if(isWhite)
			return "wp";
		else return "bp";
	}

}
