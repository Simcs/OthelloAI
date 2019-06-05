package alphaO;

import java.awt.Point;

import othello.OthelloPane;

public class Alphabeta implements AlphaO {
	
	private int depth;
	
	public Alphabeta(int depth) {
		this.depth = depth;
	}

	@Override
	public Point getMove(OthelloPane othello) {
		OthelloNode origin = new OthelloNode(othello);
		int value = alphaBeta(origin, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, origin.isBlackTurn());
		
		for(OthelloNode node : origin.getChilds())
			if(value == node.getValue())
				return node.getSelect();
		return null;
	}
	
	private int alphaBeta(OthelloNode node, int depth, 
							int alpha, int beta, boolean maximizingPlayer) {
		node.makeChild();
		if(depth == 0 || node.isGameOver())
			return node.state();
		if(maximizingPlayer) {
			for(OthelloNode child : node.getChilds()) {
				alpha = Math.max(alpha, alphaBeta(child, depth-1, alpha, beta, child.isBlackTurn()));
				child.setValue(alpha);
				if(beta <= alpha) // alpha cut-off
					break;
			}
			return alpha;
		} else {
			for(OthelloNode child : node.getChilds()) {
				beta = Math.min(beta, alphaBeta(child, depth-1, alpha, beta, child.isBlackTurn()));
				child.setValue(beta);
				if(beta <= alpha) // beta cut-off
					break;
			}
			return beta;
		}
	}
	
}
