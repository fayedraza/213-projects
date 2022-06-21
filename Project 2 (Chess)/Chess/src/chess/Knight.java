

package chess;

import java.util.Arrays;

/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of the knight piece where we need to see if it can legally move to the destination that the user requested while playing chess.
 * 
 * @see  Piece 
 * @see  Chess
 */




public class Knight extends Piece{
	
	/**
	 * <p>
	 * class constructor specifying location of piece and color of it (black or white).
	 */

	public Knight(int[] loc, boolean white) {
		super(loc, white);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * <p>
	 * This method checks to see if the knight piece can reach to destination and returns true/false if it can
	 * 
	 * @param board a 8 x 8 array of all pieces 
	 * @param dest the destination index of where the piece wants to land
	 * 
	 * @return if the piece can land on that destination or not
	 * 
	 */

	public boolean moveLegal(Piece[][] board, int[] dest) {
		
		if(Arrays.equals(dest, location)) return false;
		
		if(dest[0]==location[0]-2 && dest[1]==location[1]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//NNW
		
		if(dest[0]==location[0]-2 && dest[1]==location[1]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//NNE
		
		if(dest[0]==location[0]+2 && dest[1]==location[1]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//SSW
		
		if(dest[0]==location[0]+2 && dest[1]==location[1]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//SSE
		
		if(dest[1]==location[1]-2 && dest[0]==location[0]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//ENE
		
		if(dest[1]==location[1]-2 && dest[0]==location[0]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//ESE
		
		if(dest[1]==location[1]+2 && dest[0]==location[0]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//WNW
		
		if(dest[1]==location[1]+2 && dest[0]==location[0]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite)
				return true;
		}//WSW
			
		
		return false;
	}
	
	/** <p>
	 * This method returns the name of piece in string format
	 * 
	 * @return the name of the piece where wN means white knight and bN means black knight
	 */
	
	public String toString() {
		if(isWhite)
			return "wN";
		else return "bN";
	}
}
