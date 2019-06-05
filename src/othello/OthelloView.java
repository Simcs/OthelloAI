package othello;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import static othello.OthelloPane.*;
import static othello.OthelloStone.*;

import javax.swing.*;

public class OthelloView extends JFrame 
		implements ActionListener, PaneOberserver, StatusObserver {
	
	public final int LINE_WIDTH = 50;
	public final int STONE_SIZE = LINE_WIDTH*5/7;
	
	private final int FRAME_LENGTH = LINE_WIDTH*(LINE_NUM+2);
	private final int STATUS_LENGTH = 20;
	
	private OthelloModelInterface othello;
	private OthelloControllerInterface controller;
	
	private JPanel pOthello;
	
	private JMenuBar jmb = new JMenuBar();
	private JMenu menu = new JMenu("메뉴");
	private JMenuItem iEasy = new JMenuItem("초급");
	private JMenuItem iNormal = new JMenuItem("중급");
	private JMenuItem iHard = new JMenuItem("고급");
	private JMenuItem iRestart = new JMenuItem("다시 한번");
	private JMenuItem iChangeStone = new JMenuItem("백으로 하기");
	private JMenuItem iUndo = new JMenuItem("무르기(b)");
	
	private Color background = Color.lightGray;
	private Color black = Color.black;
	private Color white = Color.white;
	
	private Font mFont = new Font("함초롬돋움", Font.PLAIN, 12);
	
	private Rectangle rOthello = new Rectangle(0, 0, FRAME_LENGTH, FRAME_LENGTH);
	private Rectangle rStatus =  new Rectangle(0, FRAME_LENGTH, FRAME_LENGTH, FRAME_LENGTH+STATUS_LENGTH);
	
	private Image img;
	private Graphics gImg;
	
	private MouseHandler mouseHandler = new MouseHandler();
	
	OthelloView(String title, OthelloControllerInterface controller, OthelloModelInterface othello) {
		
		super(title);
		
		this.controller = controller;
		this.othello = othello;
		othello.registerPaneObserver(this);
		othello.registerStatusObserver(this);
		
		menuSetUp();
		
		pOthello = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, this);
			}
		};
		pOthello.setSize(FRAME_LENGTH, FRAME_LENGTH+STATUS_LENGTH);
		add(pOthello);
		int frame_width = pOthello.getWidth();
		int frame_height = pOthello.getHeight();
		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(100, 100);
		setSize(getInsets().left+getInsets().right+frame_width,
				getInsets().top+getInsets().bottom+frame_height+jmb.getHeight());
		
		img = pOthello.createImage(frame_width, frame_height);
		gImg = img.getGraphics();
		updatePane();
		updateStatus("흑 차례");
		pOthello.addMouseListener(mouseHandler);
	}
	
	private void menuSetUp() {
		
		iEasy.addActionListener(this);
		iNormal.addActionListener(this);
		iHard.addActionListener(this);
		iRestart.addActionListener(this);
		
		iChangeStone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				controller.changeStone();
				iChangeStone.setText((othello.isBlackTurn()?"백":"흑")+"으로 하기");
			}
		});
		
		iUndo.setMnemonic('b');
		iUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				controller.undo();
			}
		});
		
		menu.add(iEasy);
		menu.add(iNormal);
		menu.add(iHard);
		menu.add(iRestart);
		menu.addSeparator();
		menu.add(iChangeStone);
		menu.add(iUndo);
		jmb.add(menu);
		setJMenuBar(jmb);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == iEasy) {
			controller.changeAI(Stage.EASY);
		} else if(ae.getSource() == iNormal) {
			controller.changeAI(Stage.NORMAL);
		} else if(ae.getSource() == iHard) {
			controller.changeAI(Stage.HARD);
		} else if(ae.getSource() == iRestart) {
			controller.restart();
		}
	}
	
	class MouseHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent me) {
			if(me.getModifiersEx() != MouseEvent.BUTTON1_DOWN_MASK) return;
			int r = me.getY()/LINE_WIDTH-1;
			int c = me.getX()/LINE_WIDTH-1;
			System.out.println(r+" "+c);
			controller.putStone(r, c);
		}
	}
	
	@Override
	public void updateStatus(String text) {
		drawStatus(text);
	}
	
	@Override
	public void updatePane() {
		drawBoard();
	}
	
	public void disableUndo() {
		iUndo.setEnabled(false);
	}
	
	public void enableUndo() {
		iUndo.setEnabled(true);
	}
	
	public void disableChangeStone() {
		iEasy.setEnabled(false);
		iNormal.setEnabled(false);
		iHard.setEnabled(false);
		iChangeStone.setEnabled(false);
	}
	
	public void enableChangeStone() {
		iEasy.setEnabled(true);
		iNormal.setEnabled(true);
		iHard.setEnabled(true);
		iChangeStone.setEnabled(true);
	}
	
	public void drawStatus(String text) {
		gImg.setColor(background);
		gImg.fillRect(rStatus.x, rStatus.y, 
					rStatus.x+rStatus.width, rStatus.y+rStatus.height);
		gImg.setColor(black);
		gImg.setFont(mFont);
		gImg.drawString(text, LINE_WIDTH, FRAME_LENGTH+STATUS_LENGTH-10);
		
		pOthello.paintImmediately(rStatus);
	}
	
	public void drawBoard() {
		gImg.setColor(background);
		gImg.fillRect(0, 0, getWidth(), getHeight());
		
		gImg.setColor(black);
		for(int i=1; i<=LINE_NUM+1; i++) {
			gImg.drawLine(LINE_WIDTH, LINE_WIDTH*i, FRAME_LENGTH-LINE_WIDTH, LINE_WIDTH*i);
			gImg.drawLine(LINE_WIDTH*i, LINE_WIDTH, LINE_WIDTH*i, FRAME_LENGTH-LINE_WIDTH);
		}
		
		for(int i=0; i<LINE_NUM; i++)
			for(int j=0; j<LINE_NUM; j++)
				drawStone(i, j);
		
		pOthello.paintImmediately(rOthello);
	}
	
	private void drawStone(int r, int c) {
		if(othello.getStone(r, c) == EMPTY) return;
		Point p = new Point(LINE_WIDTH*(c+1)+LINE_WIDTH*1/2-STONE_SIZE/2,
							LINE_WIDTH*(r+1)+LINE_WIDTH*1/2-STONE_SIZE/2);
		
		if(othello.getStone(r, c) == AVAILABLE) {
			gImg.setColor(white);
			gImg.drawOval(p.x, p.y, STONE_SIZE, STONE_SIZE);
		} else {
			gImg.setColor(othello.getStone(r, c) == BLACK ? black : white);
			gImg.fillOval(p.x, p.y, STONE_SIZE, STONE_SIZE);
			gImg.setColor(othello.getStone(r, c) == BLACK ? white : black);
			gImg.drawOval(p.x, p.y, STONE_SIZE, STONE_SIZE);
		}
	}
	
	public void gameOver() {
		pOthello.removeMouseListener(mouseHandler);
	}
}
