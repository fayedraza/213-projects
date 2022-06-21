
package com.example.chess;

import java.util.Arrays;

/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of the bishop piece where we need to see if it can legally move to the destination that the user requested while playing chess.
 * 
 * @see  Piece
 */

public class Bishop extends Piece{

	/**
	 * <p>
	 * class constructor specifying location of piece and color of it (black or white).
	 */

	public Bishop(int[] loc, boolean white) {
		super(loc, white);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * This method checks to see if the bishop piece can reach to destination and returns true/false if it can
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param dest the destination index of where the piece wants to land
	 *
	 * @return if the piece can land on that destination or not
	 *
	 */
	public boolean moveLegal(Piece[][] board, int[] dest) {

		if(Arrays.equals(dest, location)) return false;

		if(Math.abs(dest[0]-location[0])!=Math.abs(dest[1]-location[1])) //check if its in a diagonal
			return false;


		if(dest[0]<location[0] && dest[1]<location[1]) {

			int displace=1;
			while(location[0]-displace!=dest[0] && location[1]-displace!=dest[1]) {
				if(board[location[0]-displace][location[1]-displace]!=null)
					return false;
				else displace++;
			}
			if(board[dest[0]][dest[1]]==null)
				return true;
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//NW movement

		if(dest[0]<location[0] && dest[1]>location[1]) {

			int displace=1;
			while(location[0]-displace!=dest[0] && location[1]+displace!=dest[1]) {
				if(board[location[0]-displace][location[1]+displace]!=null)
					return false;
				else displace++;
			}
			if(board[dest[0]][dest[1]]==null)
				return true;
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//NE movement

		if(dest[0]>location[0] && dest[1]>location[1]) {

			int displace=1;
			while(location[0]+displace!=dest[0] && location[1]+displace!=dest[1]) {
				if(board[location[0]+displace][location[1]+displace]!=null)
					return false;
				else displace++;
			}
			if(board[dest[0]][dest[1]]==null)
				return true;
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//SE movement

		if(dest[0]>location[0] && dest[1]<location[1]) {

			int displace=1;
			while(location[0]+displace!=dest[0] && location[1]-displace!=dest[1]) {
				if(board[location[0]+displace][location[1]-displace]!=null)
					return false;
				else displace++;
			}
			if(board[dest[0]][dest[1]]==null)
				return true;
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//SW movement

		return false;
	}

	/** <p>
	 * This method returns the name of piece in string format
	 *
	 * @return the name of the piece where wB means white bishop and bB means black bishop
	 */
	public String toString() {
		if(isWhite)
			return "wB";
		else return "bB";
	}
}
