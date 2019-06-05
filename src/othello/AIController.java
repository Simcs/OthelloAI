package othello;

import java.awt.Point;

import javax.management.RuntimeErrorException;

import alphaO.*;

public class AIController implements OthelloControllerInterface {
	
	private OthelloModelInterface othelloModel;
	private OthelloView view;
	
	private AlphaO alphaO;
	
	private boolean isPlayerBlack;
	
	public AIController(OthelloModelInterface othelloModel) {
		this.othelloModel = othelloModel;
		view = new OthelloView("Othello", this, othelloModel);
		view.disableUndo();
		alphaO = new RandomGenerator();
		isPlayerBlack = true;
	}
	
	@Override
	public void changeStone() {
		isPlayerBlack = !isPlayerBlack;
		restart();
	}

	@Override
	public void changeAI(Stage stage) {
		switch(stage) {
		
		case EASY : alphaO = new RandomGenerator(); break;
		case NORMAL : alphaO = new Alphabeta(3); break;
		case HARD : alphaO = new Alphabeta(5); break;
		}
		restart();
	}

	@Override
	public void restart() {
		othelloModel.reset();
		if(!isPlayerBlack)
			othelloModel.putStone(alphaO.getMove(othelloModel.getOthelloPane()));
	}

	@Override
	public void undo() {
		if(othelloModel.hasPrevious()) {
			othelloModel.backToPrevious();
			if(!othelloModel.hasPrevious())
				view.disableUndo();
		}
	}

	@Override
	public void putStone(int r, int c) {
		try {
			OthelloPane tmp = othelloModel.getOthelloPane();
			othelloModel.putStone(r, c);
			if(!othelloModel.hasPrevious())
				view.disableUndo();
			othelloModel.setPrevious(tmp);
			if(!othelloModel.checkCanContinue()) {
				view.gameOver();
				return;
			}
			
			while(!isPlayerTurn()) {
				System.out.println(othelloModel.getOthelloPane());
				Point p = alphaO.getMove(othelloModel.getOthelloPane());
				System.out.println(p);
				othelloModel.putStone(p);
				
				if(!othelloModel.checkCanContinue()) {
					view.gameOver();
					return;
				}
			}
		} catch(IllegalArgumentException ie) {
			view.updateStatus(ie.getMessage());
			throw new RuntimeException(ie);
		}
	}
	
	private boolean isPlayerTurn() {
		return isPlayerBlack == othelloModel.isBlackTurn();
	}

}
