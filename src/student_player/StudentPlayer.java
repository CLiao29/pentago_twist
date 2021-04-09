package student_player;

import java.util.ArrayList;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoBoardState.Piece;
import pentago_twist.PentagoMove;

/** A player file submitted by a student. */
/**
 * 
 * @author Congming Liao 260790998
 *
 */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260790998");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        int player = boardState.getTurnPlayer();
    	Piece myPiece  = player==0?              Piece.WHITE : Piece.BLACK;
    	
    	int depth = 4;
    	int alpha = Integer.MIN_VALUE;
    	int beta = Integer.MAX_VALUE;
    	
    	MyTools.Pair myPair = MyTools.negaMax(boardState,depth,alpha,beta,myPiece);
        assert myPair.getMove()!= null;
        // Return your move to be processed by the server.
        return myPair.getMove();
    }
}