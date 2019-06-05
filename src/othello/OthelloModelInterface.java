package othello;

import java.awt.Point;

public interface OthelloModelInterface {
	
	boolean isBlackTurn();
	
	void turnBlackTurn();
	
	void setPrevious(OthelloPane otheello);
	
	void backToPrevious();
	
	boolean hasPrevious();
	
	OthelloPane getOthelloPane();
	
	OthelloStone getStone(int r, int c) throws IllegalArgumentException;
	
	void putStone(Point p) throws IllegalArgumentException;
	
	void putStone(int r, int c) throws IllegalArgumentException;
	
	boolean checkCanContinue();
	
	void reset();
	
	Result result();
	
	void registerPaneObserver(PaneOberserver o);
	
	void removePaneObserver(PaneOberserver o);
	
	void registerStatusObserver(StatusObserver o);
	
	void removeStatusObserver(StatusObserver o);
}
