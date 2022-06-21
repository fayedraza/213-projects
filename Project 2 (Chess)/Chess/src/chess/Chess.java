package chess;
import java.util.*;

/**
 * @author Fayed Raza, Steven Zyontz 
 * 
 * <p>
 * This is a class where you can play chess
 * 
 * @see  Piece 
 */

public class Chess {

	
	
	public static void main(String[] args) {
		Piece[][] grid = new Piece[8][8];
		grid=populateBoard();
		printBoard(grid);
		
		Scanner command = new Scanner(System.in);
		boolean[][] locationCaptured = new boolean [8][8];
		boolean gameRunning=true;
		int turn=0; //used for determining player, and for en passant
		boolean drawAsked=false;
		boolean promotionAsked = false;
		boolean canDoEnPassant = false;
		boolean canCastle = false;
		boolean kinginCheck = false;
		boolean pieceCaptured = false;
		int firstPosX = 0;
		int firstPosY = 0;
		int secondPosX = 0;
		int secondPosY = 0;
		int lastX = 0;
		int lastY = 0;
		int lastCordinates [][] = null ;
	
	
	Outer:	do {
			
			
			if(turn%2==0)
				System.out.print("White's move: ");
			else System.out.print("Black's move: ");
			String move = command.nextLine().toLowerCase();
			
			if(move.equals("resign")) {
				if(turn%2==0) System.out.println("Black wins");
				else System.out.println("White wins");
				gameRunning=false;
				continue;
			}
				
			if(move.equals("draw")&&drawAsked) {
				gameRunning=false;
				continue;
			}
			
			String[] locs = move.split(" ");
			
			if(locs.length==3) {
				if(locs[2].equals("draw?")) {
					drawAsked=true;
					//drawTurn=turn;
				}
	
				if(locs[2].equals("q") || locs[2].equals("n") || locs[2].equals("b") || locs[2].equals("r")) {
					promotionAsked=true;
				}
			}
			if(locs.length>1) {
				
				if(locs.length==2 && drawAsked) {
					drawAsked=false;
				}
				
			int[][] coords = new int[2][2];
			coords[0]=toCoord(locs[0]);
			coords[1]=toCoord(locs[1]);
						
			
			if(grid[ coords[0][0] ][ coords[0][1] ]==null) {
				System.out.println("Illegal move, try again");
				continue;
			}
			
			if((turn%2==0 && !grid[ coords[0][0] ][ coords[0][1] ].isWhite)	
					||(turn%2==1 && grid[ coords[0][0] ][ coords[0][1] ].isWhite)) {
				System.out.println("Illegal move, try again");
				continue;
			}
			
	
			if( grid[ coords[0][0] ][ coords[0][1] ] instanceof King && Math.abs(coords[0][1] - coords[1][1]) ==2  ) {
			if( !((grid[ coords[0][0] ][ coords[0][1] ] instanceof King) && !canCastle  && !kinginCheck && coords[0][0] ==  coords[1][0] && King.canCastle(grid, coords, locationCaptured))  ) {
				System.out.println("Illegal move, try again");
				continue;
			}else {
				canCastle = true;
			}
			}
			
			if( (canCastle && grid[ coords[0][0] ][ coords[0][1] ] instanceof King && Math.abs(coords[0][1] - coords[1][1]) ==2) || grid[ coords[0][0] ][ coords[0][1] ].moveLegal(grid, coords[1])) {
				
				
			 if((grid[ coords[0][0] ][ coords[0][1] ] instanceof Pawn) &&  !canDoEnPassant && Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[1] - coords[1][1]) != 0 && grid[ coords[1][0] ][ coords[1][1] ] == null  ){
					System.out.println("Illegal move, try again");
					continue;
				}
				
				if(canDoEnPassant) {
					
					if( (grid[ coords[0][0] ][ coords[0][1] ] instanceof Pawn) && lastY == coords[0][0] &&(coords[1][0] == firstPosX &&  coords[1][1] == firstPosY) ||  (coords[1][0] == secondPosX &&  coords[1][1] == secondPosY)) {

						grid[lastY][lastX] = null;
					} else if((grid[ coords[0][0] ][ coords[0][1] ] instanceof Pawn) && Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[1] - coords[1][1]) != 0){
						System.out.println("Illegal move, try again");
						continue;
					}
					
					
					canDoEnPassant = false;
				}
				
				if( (grid[coords[0][0]][coords[0][1]] instanceof Pawn) &&  Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[0] - coords[1][0])== 2 &&  Math.abs( grid[ coords[0][0] ][ coords[0][1] ].location[1] - coords[1][1])== 0) {
					if( grid[ coords[0][0] ][ coords[0][1] ].location[0] - coords[1][0] == 2) {
					firstPosY = grid[coords[0][0]][coords[0][1]].location[1];
				    firstPosX = grid[coords[0][0]][coords[0][1]].location[0]-1 >= 0 ? grid[coords[0][0]][coords[0][1]].location[0]-1 : grid[coords[0][0]][coords[0][1]].location[0];
				    secondPosY = grid[coords[0][0]][coords[0][1]].location[1];
				    secondPosX  = grid[coords[0][0]][coords[0][1]].location[0]-2 >= 0 ?  grid[coords[0][0]][coords[0][1]].location[0]-2 :  grid[coords[0][0]][coords[0][1]].location[0];
					}else {
				    firstPosY = grid[coords[0][0]][coords[0][1]].location[1];
					firstPosX = grid[coords[0][0]][coords[0][1]].location[0]+1 <= 7 ? grid[coords[0][0]][coords[0][1]].location[0]+1 :  grid[coords[0][0]][coords[0][1]].location[0]; ;
					secondPosY = grid[coords[0][0]][coords[0][1]].location[1];
				    secondPosX = grid[coords[0][0]][coords[0][1]].location[0]+2 <= 7 ? grid[coords[0][0]][coords[0][1]].location[0]+2 : grid[coords[0][0]][coords[0][1]].location[0];
					}
				    lastY = coords[1][0];
				    lastX = coords[1][1];
					canDoEnPassant = true;
	
				}
				if(grid[ coords[1][0] ][ coords[1][1] ] != null) {
					 pieceCaptured = true;
				}
				
				if(grid[ coords[1][0] ][ coords[1][1] ] != null ) {
					locationCaptured[ coords[1][0] ][ coords[1][1] ] = true;
				}
				grid[ coords[0][0] ][ coords[0][1] ].location=coords[1];
				grid[ coords[1][0] ][ coords[1][1] ] = grid[ coords[0][0] ][ coords[0][1] ];
				grid[ coords[0][0] ][ coords[0][1] ] = null;
				if(grid[ coords[1][0] ][ coords[1][1] ] instanceof King  || grid[ coords[1][0] ][ coords[1][1] ] instanceof Rook) 
					grid[ coords[1][0] ][ coords[1][1] ].setMoved();
					
				
				turn++;
				
				if( (grid[coords[1][0]][coords[1][1]] instanceof Pawn) && isEightRank(grid[ coords[1][0] ][ coords[1][1] ]) ) {
				
					
					boolean isWhite  =  grid[coords[1][0]][coords[1][1]].isWhite();
					
					if(promotionAsked && locs[2].equals("n")) {
						
						grid[ coords[1][0] ][ coords[1][1] ]
								= new Knight(coords[1], isWhite);
							
					}else if(promotionAsked && locs[2].equals("b")) {
						
						grid[ coords[1][0] ][ coords[1][1] ]
								= new Bishop(coords[1], isWhite);
						
					}else if(promotionAsked && locs[2].equals("r")) {
						
						grid[ coords[1][0] ][ coords[1][1] ]
								= new Rook(coords[1], isWhite);
						
					}else {
						grid[ coords[1][0] ][ coords[1][1] ]
								= new Queen(coords[1], isWhite);
						
					}
					
				}
				
				
			  if(King.isPieceInCheckWithTheirKing(grid, turn-1)) {
				 
				  if(grid[ coords[1][0] ][ coords[1][1] ] instanceof King  || grid[ coords[1][0] ][ coords[1][1] ] instanceof Rook) 
						grid[ coords[1][0] ][ coords[1][1] ].setMoved();
					grid[ coords[1][0] ][ coords[1][1] ].location=coords[0];
					grid[ coords[0][0] ][ coords[0][1] ] = grid[ coords[1][0] ][ coords[1][1] ];
					grid[ coords[1][0] ][ coords[1][1] ] = null;
					
					System.out.println("Illegal move, try again");
					turn--;
					continue;
				}
				
				if(kinginCheck && !King.inCheck(grid, turn-1, lastCordinates) && ( 
						
						pieceCaptured 
						
			            || 
						
			            (grid[ coords[1][0] ][ coords[1][1] ] instanceof King)
			            
			            ||
			             
			            (   grid[ coords[1][0] ][ coords[1][1] ] instanceof Queen || grid[ coords[1][0] ][ coords[1][1] ] instanceof Rook || grid[ coords[1][0] ][ coords[1][1] ] instanceof Bishop) && 
			            		
			            		
			            !isNextToKing(grid,coords[1][0], coords[1][1])
						
						)
						
						) {
					lastCordinates = null;
					kinginCheck = false;
				}else if(kinginCheck) {
					
					grid[ coords[1][0] ][ coords[1][1] ].location=coords[0];
					grid[ coords[0][0] ][ coords[0][1] ] = grid[ coords[1][0] ][ coords[1][1] ];
					grid[ coords[0][0] ][ coords[0][1] ].setMoved();
					grid[ coords[1][0] ][ coords[1][1] ] = null;
					System.out.println("Illegal move, try again");
					turn--;
					continue;
				}
				
			
				if(King.inCheck(grid, turn, coords)) {
					lastCordinates = coords;
					kinginCheck = true;
		
					if(!King.canEscape(grid, turn)) {
						System.out.println("Checkmate");
						if((turn)%2==0) System.out.println("Black wins");
						else System.out.println("White wins");
						break;
					}else {
						System.out.println("Check");
					}
				}
			
				
				if(canCastle) {
				
					if(coords[0][1] - coords[1][1] < 0) {
					if((turn -1) % 2 == 1) {
						grid[ coords[1][0] ][ coords[1][1] - 1]= new Rook(new int[] {coords[1][0],(coords[1][1] - 1)},grid[0][7].isWhite());
						grid[0][7] = null;
					}else {
					grid[ coords[1][0] ][ coords[1][1] - 1]= new Rook(new int[] {coords[1][0],(coords[1][1] - 1)},grid[7][7].isWhite());
					grid[7][7] = null;
					}
				   } else {
					   if((turn -1) % 2 == 1) {
							grid[ coords[1][0] ][ coords[1][1] + 1]= new Rook(new int[] {coords[1][0],(coords[1][1] + 1)},grid[0][0].isWhite());
							grid[0][0] = null;
						}else {
						grid[ coords[1][0] ][ coords[1][1] + 1]= new Rook(new int[] {coords[1][0],(coords[1][1] + 1)},grid[7][0].isWhite());
						grid[7][0] = null;
						}
				   }
					canCastle = false;
					
				}
				printBoard(grid);
			}//end legal move check
			else {
				System.out.println("Illegal move, try again");
			}
			
			}//end not-asking for draw check
			
		}while(gameRunning);
		
		
	}//end main
	
	

	/**
	 * <p>
	 * This method checks to see if piece is at 8 rank 
	 * 
	 * @param piece which has piece is a chess piece 
	 * 
	 * @return if piece is at 8 rank 
	 */

	private static boolean  isEightRank(Piece piece) {
		
        if(piece == null) {
			 return false;
		}
 
     
		return (piece.isWhite() && piece.location[0] == 0) ||  (!piece.isWhite() && piece.location[0] == 7);
		
	}
	
	/**
	 * <p>
	 * This method checks to turn name of piece into coordinates
	 * 
	 * @param string name of a piece
	 * 
	 * @return coordinates of a piece
	 */
	
	private static int[] toCoord(String string) {
		int[] out = new int[2];
		out[0]=8-Integer.parseInt(""+string.charAt(1));
		out[1]=(int)(string.charAt(0))-97;
		return out;
	}
	
	/**
	 * <p>
	 * This method checks to see if piece is next to king
	 * 
	 * @param board a 8 x 8 array of all pieces 
	 * @param posx row numbers of piece
	 * @param posy column numbers of piece
	 * 
	 * @return piece is next to king
	 * 
	 */
	
	private static boolean isNextToKing(Piece[][] board, int posx, int posy) {
		
		if(board[posx][posy].isWhite() ) {
			return (posx - 1>= 0 && board[posx-1][posy] != null && board[posx-1][posy].isWhite() && board[posx-1][posy] instanceof King)
					|| (posx + 1 <= 7 && board[posx+1][posy] != null && board[posx+1][posy].isWhite() && board[posx+1][posy] instanceof King)
					|| (posy + 1 <= 7 &&  board[posx][posy+1] != null && board[posx][posy+1].isWhite() && board[posx][posy+1] instanceof King)
					|| (posy - 1>=0 && board[posx][posy-1] != null && board[posx][posy-1].isWhite() && board[posx][posy-1] instanceof King);
		}else {
			return (posx - 1>= 0 && board[posx-1][posy] != null && !board[posx-1][posy].isWhite() && board[posx-1][posy] instanceof King)
					|| (posx + 1 <= 7 && board[posx+1][posy] != null && !board[posx+1][posy].isWhite() && board[posx+1][posy] instanceof King)
					|| (posy + 1 <= 7 && board[posx][posy+1] != null && !board[posx][posy+1].isWhite() && board[posx][posy+1] instanceof King)
					|| (posy - 1>=0 && board[posx][posy-1] != null && !board[posx][posy-1].isWhite() && board[posx][posy-1] instanceof King);
		}
		
		
		 
		
		
		
	}
	
	/**
	 * <p>
	 * This makes a chess board
	 */
	
	public static  Piece[][] populateBoard() {
		
		Piece[][] board = new Piece[8][8];
		
		board[0][0] = new Rook(new int[] {0,0},false);
		board[0][1] = new Knight(new int[] {0,1},false);
		board[0][2] = new Bishop(new int[] {0,2},false);
		board[0][3] = new Queen(new int[] {0,3},false);
		board[0][4] = new King(new int[] {0,4},false);
		board[0][5] = new Bishop(new int[] {0,5},false);
		board[0][6] = new Knight(new int[] {0,6},false);
		board[0][7] = new Rook(new int[] {0,7},false);
		

		for(int i=0; i<8; i++) {
			board[1][i] = new Pawn(new int[] {1,i},false);
		}
		

		board[7][0] = new Rook(new int[] {7,0},true);
		board[7][1] = new Knight(new int[] {7,1},true);
		board[7][2] = new Bishop(new int[] {7,2},true);
		board[7][3] = new Queen(new int[] {7,3},true);
		board[7][4] = new King(new int[] {7,4},true);
		board[7][5] = new Bishop(new int[] {7,5},true);
		board[7][6] = new Knight(new int[] {7,6},true);
		board[7][7] = new Rook(new int[] {7,7},true);
		

		for(int i=0; i<8; i++) {
			board[6][i] = new Pawn(new int[] {6,i},true);
		}
		
		
		return board;
	}//end populateBoar
	
	
	/**
	 * <p>
	 * This method prints a chess board
	 * 
	 * @param board a 8 x 8 array of all pieces 
	 *
	 * 
	 */
	public static void printBoard(Piece[][] board) {
		System.out.println();
		for(int i=0; i<8; i++) {
			for(int k=0; k<8; k++) {
				if(board[i][k]!=null)
				System.out.print(board[i][k]+" ");
				else {
					if((i+k)%2==0)
						System.out.print("  "+" ");
					else System.out.print("##"+" ");
				}
			}//end cols
			System.out.println(8-i);
		}//end rows
		System.out.println(" a  b  c  d  e  f  g  h \n");
	}
	
	
}
