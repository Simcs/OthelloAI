package alphaO;

import java.awt.Point;
import java.util.*;

import othello.*;
import static othello.OthelloModel.*;
import static othello.OthelloStone.*;

public class OthelloNode {
	
	private static int[][] SCORE_TABLE = new int[][] {
		{ 4000, -900, 1000, 1000, 1000, 1000, -900, 4000 },
		{ -900, -900,    0,    0,    0,    0, -900, -900 },
		{ 1000,    0,  200,  300,  300,  200,    0, 1000 },
		{ 1000,    0,  300, 1000, 1000,  300,    0, 1000 },
		{ 1000,    0,  300, 1000, 1000,  300,    0, 1000 },
		{ -900, -900,    0,    0,    0,    0, -900, -900 },
		{ 4000, -900, 1000, 1000, 1000, 1000, -900, 4000 }
	};
	
	private OthelloPane othello;
	private List<OthelloNode> childs = new ArrayList<>(20);
	private Point select;
	private int value;
	
	OthelloNode(OthelloPane othello) {
		this(othello, null);
	}
	
	OthelloNode(OthelloPane othello, Point select) {
		this.othello = new OthelloPane(othello);
		this.select = select;
		this.value = 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<OthelloNode> getChilds() {
		return childs;
	}

	public Point getSelect() {
		return select;
	}
	
	public boolean isBlackTurn() {
		return othello.isBlackTurn();
	}
	
	public void makeChild() {
		System.out.println(LINE_NUM);
		for(int i = 0; i < LINE_NUM; i++) {
			for(int j = 0; j < LINE_NUM; j++) {
				System.out.println("i : " + i + " j " + j);
				if(othello.getStone(i, j) == AVAILABLE) {
					OthelloNode child = new OthelloNode(othello, new Point(i, j));
					child.othello.putStone(child.select);
					this.childs.add(child);
				}
			}
		}
	}
	
	public boolean isGameOver() {
		if(childs.size() == 0) {
			othello.turnBlackTurn();
			othello.findAvailablePoint();
			if(!othello.canContinue())
				return true;
		}
		return false;
	}
	
	public int state() {
		int score = 0;
		for(int i = 0; i < LINE_NUM; i++)
			for(int j = 0; j < LINE_NUM; j++)
				if(othello.getStone(i, j) == BLACK)
					score += SCORE_TABLE[i][j];
		return score;
	}
}
