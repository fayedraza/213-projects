
package chess;

import java.util.Arrays;


/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of the queen piece where we need to see if it can legally move to the destination that the user requested while playing chess.
 * 
 * @see  Piece 
 * @see  Chess
 */


public class Queen extends Piece{

	/**
	 * <p>
	 * class constructor specifying location of piece and color of it (black or white).
	 */
	
	public Queen(int[] loc, boolean white) {
		super(loc, white);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * <p>
	 * This method checks to see if the queen piece can reach to destination and returns true/false if it can
	 * 
	 * @param board a 8 x 8 array of all pieces 
	 * @param dest the destination index of where the piece wants to land
	 * 
	 * @return if the piece can land on that destination or not
	 * 
	 */


	public boolean moveLegal(Piece[][] board, int[] dest) {
		
		if(Arrays.equals(dest, location)) return false;
		
		if(Math.abs(dest[0]-location[0])!=Math.abs(dest[1]-location[1])) { //check if its not in a diagonal
			if((dest[0]==location[0]&&dest[1]!=location[1])||(dest[0]!=location[0]&&dest[1]==location[1])) { //change in only one axis
				
				if(dest[0]!=location[0]) {
					
					if(dest[0]<location[0]) {
						for(int i=location[0]-1; i>dest[0]; i--) {
							if(board[i][dest[1]]!=null)
								return false;  //check if any piece is in the way of the path
						}
						if(board[dest[0]][dest[1]]==null) //check if spot is open
							return true;
						else if(board[dest[0]][dest[1]].isWhite==isWhite) //check if piece belongs to same player
							return false;
						else return true;
					}//end move left check
					
					
					else if(dest[0]>location[0]) {
							for(int i=location[0]+1; i<dest[0]; i++) {
								if(board[i][dest[1]]!=null)
									return false;  //check if any piece is in the way of the path
							}
							if(board[dest[0]][dest[1]]==null) //check if spot is open
								return true;
							else if(board[dest[0]][dest[1]].isWhite==isWhite) //check if piece belongs to same player
								return false;
							else return true;
						}//end move right check
					
					
				}//end row check
				
				if(dest[1]!=location[1]) {
					
					if(dest[1]<location[1]) {
						for(int i=location[1]-1; i>dest[1]; i--) {
							if(board[dest[0]][i]!=null)
								return false;  //check if any piece is in the way of the path
						}
						if(board[dest[0]][dest[1]]==null) //check if spot is open
							return true;
						else if(board[dest[0]][dest[1]].isWhite==isWhite) //check if piece belongs to same player
							return false;
						else return true;
					}//end move up check
					
					
					else if(dest[1]>location[1]) {
							for(int i=location[1]+1; i<dest[1]; i++) {
								if(board[dest[0]][i]!=null)
									return false;  //check if any piece is in the way of the path
							}
							if(board[dest[0]][dest[1]]==null) //check if spot is open
								return true;
							else if(board[dest[0]][dest[1]].isWhite==isWhite) //check if piece belongs to same player
								return false;
							else return true;
						}//end move down check
					
					
				}//end col check
				
			}//end single-axis change check
		}//end Rook movement
		else {
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
		
		}//end Bishop movement
		
		return false;
	}
	
	/** <p>
	 * This method returns the name of piece in string format
	 * 
	 * @return the name of the piece where wQ means white queen and bQ means black queen
	 */
	
	public String toString() {
		if(isWhite)
			return "wQ";
		else return "bQ";
	}
}
