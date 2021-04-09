package student_player;

import java.util.ArrayList;
import java.util.Optional;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoBoardState.Piece;
import pentago_twist.PentagoMove;


/**
 * 
 * @author Congming Liao 260790998
 *
 */
public class MyTools {
    
	/**
	 * 
	 * The package private class Pair, which is utilized when returning stuffs in the negaMax algorithm.
	 *
	 */
	static class Pair{
		private final int aValue;
		private final Optional<PentagoMove> aMove;
		
		public Pair(int pValue, PentagoMove pMove)
		{
			this.aValue = pValue;
			this.aMove = Optional.ofNullable(pMove);
		}
		
		public int getValue() {
			return this.aValue;
		}
		
		public PentagoMove getMove() {
			
			if(aMove.isPresent()){
			return this.aMove.get();
			}
			else {
				return null;
			}
		}
		
	}
	
	/**
	 * 
	 * @param pbs: the Board that we are currently dealing with.
	 * @param depth 
	 * @param alpha 
	 * @param beta 
	 * @param p : The piece, representing the player (White: first player, Black: next Player)
	 * @return a pair containing both the value(for recursion) and the Move that it corresponds to.
	 */
	public static Pair negaMax(PentagoBoardState pbs,int depth, int alpha, int beta, Piece p )
	{	
		assert depth>=0 && p !=Piece.EMPTY;
		
		Piece advP = p==Piece.WHITE? Piece.BLACK:Piece.WHITE;
		
		if( pbs.gameOver() || depth ==0) {
			return new Pair(eval(pbs,p),null);
		}
		
		int best = Integer.MIN_VALUE;
		PentagoMove bestMove = null;
		ArrayList<PentagoMove> allMoves = pbs.getAllLegalMoves();
		
		for(PentagoMove move : allMoves)
		{
			PentagoBoardState child = (PentagoBoardState) pbs.clone();
    		child.processMove(move);
    		int value = - negaMax(child,depth-1,-beta,-alpha,advP).getValue();
    		best = Math.max(best, value);
    		if(best >= beta)
    		{
    			return new Pair(beta,move);
    		}
    		
    		if (value > alpha)
    		{
    			alpha = best;
    			bestMove = move;
    		}
		}
		
		return new Pair(best,bestMove);
	}
	
    /**
     * evaluation function of a PentagoBoardState
     * @param pbs
     * @param p
     * @return 
     */
    public static int eval(PentagoBoardState pbs, Piece p) {
    	assert p != Piece.EMPTY;
       Piece advP = p ==Piece.WHITE? Piece.BLACK:Piece.WHITE;
 	   int[][] myBitBoard  = generateBitBoard(pbs,p);
 	   int[][] advBitBoard = generateBitBoard(pbs,advP);
 	   int result = 10000*num5InARow(myBitBoard) + 1000*num4InARow(myBitBoard) + 100*num3InARow(myBitBoard) + 5*numCenter(myBitBoard);
 	   int result2 = 10000*num5InARow(advBitBoard) + 1000*num4InARow(advBitBoard) + 100*num3InARow(advBitBoard) + 5*numCenter(advBitBoard);
 	   return result-result2;
    }
    
    //helper method for evaluation functions, all set to private
    /**
     * 
     * @param pbs
     * @param p
     * @return a 2d array of bitBoard
     */
    private static int[][] generateBitBoard(PentagoBoardState pbs, Piece p){
	   Piece[][] b = pbs.getBoard();
	   int[][] bitBoard = new int[b.length][b[0].length];
	   
	   for(int i =0; i< b.length; i++)
	   {
		   for(int j = 0; j< b[0].length; j++)
		   {
			   if(b[i][j] == p)
			   {
				   bitBoard[i][j] = 1;
			   }
		   }
	   }
	   
	   return bitBoard;
   }
    
   /**
    * 
    * @param a bitBoard
    * @return
    */
    private static int num5InARow(int[][] bb) {
	   int result = 0;
	   //check 5-in-a-row vertically
	   for(int i =0; i< bb.length -4; i++)
	   {
		   for(int j =0; j< bb[0].length; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j])&&(bb[i][j] == bb[i+2][j])&&(bb[i][j] == bb[i+3][j])&&(bb[i][j] == bb[i+4][j]))
			   {
				   result++;
			   }
		   }
	   }
	   //check 4-in-a-row horizontally
	   for(int i=0; i < bb.length; i++)
	   {
		   for(int j=0; j < bb[0].length-4; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i][j+1]) && (bb[i][j]==bb[i][j+2])&&(bb[i][j]==bb[i][j+3])&&(bb[i][j]==bb[i][j+4]))
			   {
				   result++;
			   }
		   }
	   }
	   //check 4-in-a-row diagonally
	   
	   //from left to right direction
	   for(int i=0; i<bb.length-4; i++)
	   {
		   for(int j=0; j<bb[0].length -4; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j+1]) && (bb[i][j]==bb[i+2][j+2]) && (bb[i][j]==bb[i+3][j+3])&& (bb[i][j]==bb[i+4][j+4]))
			   {
				   result++;
			   }
		   }
	   }
	   
	   //from right to left direction
	   for(int i=0; i<bb.length-4; i++)
	   {
		   for(int j=4; j<bb[0].length; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j-1]) && (bb[i][j]==bb[i+2][j-2]) && (bb[i][j] == bb[i+3][j-3])&& (bb[i][j] == bb[i+4][j-4]))
			   {
				   result++;
			   }
		   }
	   }
	   return result;
   }
	   
   /**
    * 
    * @param a bitBoard
    * @return
    */
    private static int num4InARow(int[][] bb) {
	   int result = 0;
	   //check 4-in-a-row vertically
	   for(int i =0; i< bb.length-3; i++)
	   {
		   for(int j =0; j< bb[0].length; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j])&&(bb[i][j] == bb[i+2][j])&&(bb[i][j] == bb[i+3][j]))
			   {
				   result++;
			   }
		   }
	   }
	   //check 4-in-a-row horizontally
	   for(int i=0; i < bb.length; i++)
	   {
		   for(int j=0; j < bb[0].length-3; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i][j+1]) && (bb[i][j]==bb[i][j+2])&&(bb[i][j]==bb[i][j+3]))
			   {
				   result++;
			   }
		   }
	   }
	   //check 4-in-a-row diagonally
	   
	   //from left to right direction
	   for(int i=0; i<bb.length-3; i++)
	   {
		   for(int j=0; j<bb[0].length -3; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j+1]) && (bb[i][j]==bb[i+2][j+2]) && (bb[i][j]==bb[i+3][j+3]))
			   {
				   result++;
			   }
		   }
	   }
	   
	   //from right to left direction
	   for(int i=0; i<bb.length-3; i++)
	   {
		   for(int j=3; j<bb[0].length; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j-1]) && (bb[i][j]==bb[i+2][j-2]) && (bb[i][j] == bb[i+3][j-3]))
			   {
				   result++;
			   }
		   }
	   }
	   return result;
   }
   
   /**
    * 
    * @param a bitBoard
    * @return number of 3-in-a-row of the bitBoard
    */
    private static int num3InARow(int[][] bb) {
	   int result = 0;
	   
	   //check 3-in-a-row vertically
	   for(int i =0; i< bb.length-2; i++)
	   {
		   for(int j =0; j< bb[0].length; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j])&&(bb[i][j] == bb[i+2][j]))
			   {
				   result++;
			   }
		   }
	   }
	   
	   //check 3-in-a-row horizontally
	   for(int i=0; i < bb.length; i++)
	   {
		   for(int j=0; j < bb[0].length-2; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i][j+1]) && (bb[i][j]==bb[i][j+2]))
			   {
				   result++;
			   }
		   }
	   }
	   //check 3-in-a-row diagonally
	   
	   //from left to right direction
	   for(int i=0; i<bb.length-2; i++)
	   {
		   for(int j=0; j<bb[0].length -2; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j+1]) && (bb[i][j]==bb[i+2][j+2]))
			   {
				   result++;
			   }
		   }
	   }
	   
	   //from right to left direction
	   for(int i=0; i<bb.length-2; i++)
	   {
		   for(int j=2; j<bb[0].length; j++)
		   {
			   if((bb[i][j]==1)&&(bb[i][j] == bb[i+1][j-1]) && (bb[i][j]==bb[i+2][j-2]))
			   {
				   result++;
			   }
		   }
	   }
	   
	   return result;
   }
   
   /**
    * 
    * @param a bitBoard
    * @return number of center piece in the bitBoard
    * In this one I make the algorithm simple, since I only have to consider the pentago board. 
    */
    private static int numCenter(int[][] bb) {
	   int result = 0;
	   result += bb[1][1]==1? 1:0;
	   result += bb[1][4]==1? 1:0;
	   result += bb[4][1]==1? 1:0;
	   result += bb[4][4]==1? 1:0;
	   return result;
   }
   

   
}