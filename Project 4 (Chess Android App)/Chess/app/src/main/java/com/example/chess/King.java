


package com.example.chess;
import java.util.Arrays;

/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class of the King piece where we need to see if it can legally move to the destination that the user requested while playing chess.
 * 
 * @see  Piece
 */

public class King extends Piece{

	/**
	 * <p>
	 * class constructor specifying location of piece and color of it (black or white).
	 *
	 */

	public King(int[] loc, boolean white) {
		super(loc, white);
		// TODO Auto-generated constructor stub
	}

	/**
	 * <p>
	 * This method checks to see if the king piece can reach to destination and returns true/false if it can
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param dest the destination index of where the piece wants to land
	 *
	 * @return if the piece can land on that destination or not
	 *
	 */

	public boolean moveLegal(Piece[][] board, int[] dest) {

		if(Arrays.equals(dest, location)) return false;

		if(dest[0]==location[0]-1 && dest[1]==location[1]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//NW

		if(dest[0]==location[0]-1 && dest[1]==location[1]) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//N

		if(dest[0]==location[0]-1 && dest[1]==location[1]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//NE

		if(dest[0]==location[0] && dest[1]==location[1]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//E

		if(dest[0]==location[0]+1 && dest[1]==location[1]+1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//SE

		if(dest[0]==location[0]+1 && dest[1]==location[1]) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//S

		if(dest[0]==location[0]+1 && dest[1]==location[1]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//SW

		if(dest[0]==location[0] && dest[1]==location[1]-1) {
			if(board[dest[0]][dest[1]]==null) {
				return true;
			}
			else if(board[dest[0]][dest[1]].isWhite!=isWhite) {
				return true;
			}
		}//W

		return false;
	}

	/**
	 * <p>
	 * This method checks to see if the king piece is in check
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param turn telling who is currently playing
	 * @param coords telling the start and end coordinates of that move
	 *
	 * @return if the king is in check
	 *
	 */

	public static boolean inCheck(Piece[][] board, int turn, int coords[][]) {
		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {

				if(board[i][k]!=null &&
						((board[i][k].isWhite() && (turn)%2 ==1 ) || (!board[i][k].isWhite() && (turn)%2 == 0 ))
						&& (board[i][k] instanceof King )) {

					if( board[coords[1][0]][coords[1][1]].moveLegal(board, board[i][k].location) ) {

						return true;

					}


				}
			}
		}

		return false;
	}


	/**
	 * <p>
	 * This method checks to see if moving a piece will cause its king to be checked
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param turn telling who is currently playing
	 *
	 * @return if moving a piece will cause its king to be checked
	 *
	 */


	public static boolean isPieceInCheckWithTheirKing(Piece[][] board, int turn) {


		int x =   0, y = 0;


		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {



				if(board[i][k]!=null &&
						( (board[i][k].isWhite() && turn%2 ==0 ) || (!board[i][k].isWhite() && turn%2 == 1 ) )
						&& (board[i][k] instanceof King )) {

					x = i; y = k;

				}
			}
		}


		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {



				if(board[i][k]!=null &&
						((!board[i][k].isWhite() && turn%2 ==0 ) || (board[i][k].isWhite() && turn%2 == 1 ))) {

					if( board[i][k].moveLegal(board, board[x][y].location) ) {

						return true;

					}


				}
			}
		}

		return false;
	}

	/**
	 * <p>
	 * This method checks to see if moving a king piece can castle
	 *
	 * @param board a 8 x 8 array of all pieces
	 * @param coords a 2d array showing the starting point and ending point
	 * @param locationCaptured telling which locations has the piece been captured before
	 *
	 * @return if moving a king piece can castle
	 *
	 */

	public static boolean canCastle(Piece[][] board, int coords[][], boolean[][] locationCaptured) {

		if(board[coords[0][0]][coords[0][1]].hasMoved()  ) {
			return false;
		}

		if(coords[0][1] - coords[1][1] == -2) {


			for(int i=coords[0][1]+1;  i <= 7; i++) {

				if(board[coords[0][0]][i] == null) {
					continue;
				}

				if(i <= coords[1][1] && locationCaptured[coords[0][0]][i]) {

					return false;
				}
				if(board[coords[0][0]][i] != null && i != 7) {

					return false;
				}

				if(board[coords[0][0]][i] != null && !board[coords[0][0]][i].hasMoved() && i == 7 && board[coords[0][0]][i] instanceof Rook
						&&  ( (board[coords[0][0]][i].isWhite() && board[coords[0][0]][coords[0][1]].isWhite()) || (!board[coords[0][0]][i].isWhite() && !board[coords[0][0]][coords[0][1]].isWhite()) ) ) {

					return true;
				}
			}

		}else if(coords[0][1] - coords[1][1] == 2) {
			for(int i=coords[0][1]-1;  i >= 0; i--) {

				if(board[coords[0][0]][i] == null) {
					continue;
				}

				if(i <= coords[1][1] && locationCaptured[coords[0][0]][i]) {
					return false;
				}

				if(board[coords[0][0]][i] != null && i != 0) {
					return false;
				}

				if(board[coords[0][0]][i] != null && !board[coords[0][0]][i].hasMoved() &&i == 0 && board[coords[0][0]][i] instanceof Rook
						&&  ( (board[coords[0][0]][i].isWhite() && board[coords[0][0]][coords[0][1]].isWhite()) || (!board[coords[0][0]][i].isWhite() && !board[coords[0][0]][coords[0][1]].isWhite()) ) ) {
					return true;
				}
			}

		}

		return false;
	}


	/** <p>
	 * This method returns the name of king piece in string format
	 *
	 * @return the name of the piece where wK means white King and bK means black King
	 */

	public String toString() {
		if(isWhite)
			return "wK";
		else return "bK";
	}

	/**
	 * <p>
	 * This method checks to see if the king piece can escape from being checked or not
	 *
	 * @param turn telling who is currently playing
	 *
	 * @return if the king piece can escape from being checked or not
	 *
	 */

	public static boolean canEscape(Piece[][] grid, int turn) {
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[i].length; j++) {
				if(   (   grid[ i ][j ] != null && (grid[ i ][j ] instanceof Rook || grid[ i ][j ] instanceof King || grid[ i ][j ] instanceof Bishop || grid[ i ][j ] instanceof Queen) && (( turn%2 ==0 && grid[ i ][j ].isWhite()) || ( turn%2 ==1 && !grid[ i ][j].isWhite())) )&&     tryIt(grid,turn,i,j)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * This method is a helper method for tryit method
	 *
	 * @param isWhite telling the collor of the piece
	 * @param x row of the piece
	 * @param y collumn of the piece
	 *
	 * @return if the king piece can be attacked
	 *
	 * @see King#tryIt(Piece[][] grid, int turn, int k, int l)
	 *
	 */

	private static boolean canAttackKing(Piece[][] grid, boolean isWhite, int x, int y) {

		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[i].length; j++) {

				if(grid[i][j] != null && (grid[i][j].isWhite() == isWhite) && grid[i][j].moveLegal(grid, new int[] {x,y})) {
					return true;
				}

			}
		}
		return false;

	}

	/**
	 * <p>
	 * This method is a helper method for canEscape method
	 *
	 * @param turn telling who is currently playing
	 * @param k telling row of king who is cheked
	 * @param l telling collumn of king who is cheked
	 *
	 * @return if there is a piece where the king can escape from being checked
	 *
	 * @see King#canEscape(Piece[][] grid, int turn)
	 */

	private static boolean tryIt(Piece[][] grid, int turn, int k, int l) {
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[i].length; j++) {
				if( (  ( grid[ i ][ j ] == null )  || (  (turn%2 ==0 && !grid[ i ][j ].isWhite()) || (turn%2 == 1 && grid[ i ][j ].isWhite()) ) ) && grid[ k ][l ].moveLegal(grid, new int[] {i,j}) ) {


					Piece temp  = grid[ i ][ j ];
					grid[ k ][ l ].location= new int[] {i,j};
					grid[ i ][ j ] = grid[ k ][ l ];
					grid[ k ][ l ] = null;


					for(int ptr=0; ptr < grid.length; ptr++) {
						for(int m=0; m < grid[ptr].length; m++) {

							if(grid[ptr][m] !=  null && grid[ptr][m] instanceof King  &&  ( ( turn % 2 == 1 && !grid[ptr][m].isWhite())
									|| ( turn % 2 == 0 && grid[ptr][m].isWhite() ) ) )

								if(!King.canAttackKing(grid, !grid[ptr][m].isWhite(), ptr, m)) {
									grid[ i ][ j ].location= new int[] {k,l};
									grid[ k ][ l ] = grid[ i ][ j ];
									grid[ i ][ j ] = temp;

									return true;
								}


						}
					}



					grid[ i ][ j ].location= new int[] {k,l};
					grid[ k ][ l ] = grid[ i ][ j ];
					grid[ i ][ j ] = temp;
				}
			}
		}
		return false;

	}
}


