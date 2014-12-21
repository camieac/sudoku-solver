package sudokusolver;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/** 
 * A simple graphical user interface to allow users to insert their own problems graphically, without creating a text file.
 * @author Andrew J. Rigg
 * @since June 2014
 *
 */
public class SudokuGUI extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static int ROWS = 9;
	final static int XROWS = (int) Math.sqrt(ROWS);
	
	JButton [][] singleBox;	
	JPanel sudokuPanel;
	JPanel buttonPanel;
	JButton solve, reset, exit;
	JMenuItem[] choice;
	JPopupMenu menu;
	JButton buttonPressed;
	
	SudokuGUI(){	
		this.setLayout(new BorderLayout());
		menu = new JPopupMenu();
		choice = new JMenuItem[ROWS];
		for (int selections = 0; selections < ROWS; selections ++){
			choice[selections] = new JMenuItem();
			choice[selections].setText("" + (selections+1));	
			choice[selections].addActionListener(this);
			menu.add(choice[selections]);
		}
	
		buttonPressed = new JButton();
		sudokuPanel = new JPanel(new GridLayout(ROWS,ROWS));
		buttonPanel = new JPanel(new GridLayout(1,3));
		singleBox = new JButton[ROWS][ROWS];
		solve = new JButton();
		solve.setText("Solve");
		solve.addActionListener(this);
		reset = new JButton();
		reset.setText("Reset");
		reset.addActionListener(this);
		exit = new JButton();
		exit.setText("Exit");
		exit.addActionListener(this);
		this.setTitle("SudokuSolver");
		this.setVisible(true);
		this.setSize(500, 500);
		for (int i = 0; i < ROWS; i++){
			for (int j = 0; j < ROWS; j++){
				singleBox[i][j] = new JButton();
				singleBox[i][j].setText("");
				singleBox[i][j].addActionListener(this);
				singleBox[i][j].setSize(50, 50);
				singleBox[i][j].add(menu);
				sudokuPanel.add(singleBox[i][j]);
			}
		}
		buttonPanel.add(solve);
		buttonPanel.add(reset);
		buttonPanel.add(exit);
		this.add(sudokuPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < ROWS; i++){
			for (int j = 0; j < ROWS; j++){
				if (e.getSource()==singleBox[i][j]){
					menu.show((Component)e.getSource(), 0, 0);
					buttonPressed = (JButton) e.getSource();
				}
			}
		}	
		if(e.getSource()== exit){
			System.exit(0);
		}
		
		for(int i = 0; i < ROWS;i++){
			if (e.getSource()== choice[i]){
				buttonPressed.setText(choice[i].getText());
			}
		}
		
		if (e.getSource() == reset){
			for (int i = 0; i < ROWS; i++){
				for (int j = 0; j < ROWS; j++){
				singleBox[i][j].setText("");
				}
			}
		}
		if(e.getSource() == solve){
			Controller c = new Controller(ROWS);
			
			
			
			c.loadGUINumbers(extactNumberstoArray());

			c.drawPencilMarks();

			c.solve();
			c.printSudokuTable();
			showResultsinGUI(c.getResults());
			c.printStats();
		}
	}
		
	private void showResultsinGUI(int[][] numbers) {
		for (int i = 0; i < ROWS; i++){
			for (int j = 0; j < ROWS; j++){
				singleBox[i][j].setText("" + numbers[i][j]);
			}
		}
			
		
	}


	private int[][] extactNumberstoArray() {
		int[][] numbers = new int [ROWS][ROWS];
		for (int i = 0; i < ROWS; i++){
			for (int j = 0; j < ROWS; j++){
				if (singleBox[i][j].getText()==""){
					numbers[i][j] = 0;
				}else{
					numbers[i][j] = Integer.parseInt(singleBox[i][j].getText());
				}
				
			}
		}
		
		return numbers;
		
	}


	public static void main (String [] args){
		new SudokuGUI();
	}
	
	
}
