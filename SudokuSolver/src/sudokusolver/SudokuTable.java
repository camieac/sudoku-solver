package sudokusolver;

import java.util.Arrays;

public class SudokuTable {

	private int[][] table;
	private int widthAndHeight;

	public SudokuTable(int widthAndHeight) {
		this.widthAndHeight = widthAndHeight;
		table = new int[widthAndHeight][widthAndHeight];

	}

	public boolean setEntry(int yCoordinate, int xCoordinate, int value) {
		boolean valid = validEntry(yCoordinate, xCoordinate, value);
		if (valid) table[yCoordinate][xCoordinate] = value;
		return valid;
		
	}
	void setTable(int[][] table){
		this.table = table;
	}

	public int getEntry(int yCoordinate, int xCoordinate) {
		return table[yCoordinate][xCoordinate];
	}

	public void setRow(int rowNumber, int[] row) {
		for (int i = 0; i < widthAndHeight; i++) {
			table[rowNumber][i] = row[i];
		}
	}

	public int[] getRow(int rowNumber) {
		int[] row = new int[widthAndHeight];
		for (int i = 0; i < widthAndHeight; i++) {
			row[i] = table[rowNumber][i];
		}

		return row;
	}

	public int getWidthAndHeight() {
		return widthAndHeight;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("Suduko: (0 is blank)");

		for (int j = 0; j < widthAndHeight; j++) {
			sb.append("\n" + Arrays.toString(getRow(j)));
		}

		return sb.toString();

	}

	public int[][] getTable() {
		return table;

	}

	public int[] getColumn(int columnNumber) {
		int[] column = new int[widthAndHeight];
		for (int i = 0; i < widthAndHeight; i++) {
			column[i] = table[i][columnNumber];
		}

		return column;
	}

	/** For any given position within the sudoku table, return an array of the entries that are within the box of the given entry.
	 * 
	 * @param yCoordinate
	 * @param xCoordinate
	 * @return
	 */
	public int[] getBox(int yCoordinate, int xCoordinate) {
		int boxWidth = (int) Math.sqrt(widthAndHeight);
		int boxX = boxWidth * (xCoordinate / boxWidth);
		int boxY = boxWidth * (yCoordinate / boxWidth);

		int[] box = new int[widthAndHeight];

		int offX = 0;
		int offY = 0;

		for (int i = 0; i < widthAndHeight; i++) {
			box[i] = table[boxY + offY][boxX + offX];
			if (i > (boxWidth - 1))
				offY = 1;
			if (i > (2*boxWidth)-1)
				offY = 2;
			offX++;
			if (offX > 2)
				offX = 0;
		}
		return box;
	}
	
	public boolean validEntry(int yCoordinate, int xCoordinate,int entry){
		int[] box = getBox(yCoordinate,xCoordinate);
		int[] row = getRow(yCoordinate);
		int[] column = getColumn(xCoordinate);
		for(int i : box){
			if( i == entry) return false;
		}
		for(int i : column){
			if (i == entry) return false;
		}
		for(int i : row){
			if (i == entry) return false;
		}
		return true;
		
		
	}
	
	public int getNumberSolved(){
		int numberSolved = 0;
		for(int xCoordinate = 0; xCoordinate < widthAndHeight; xCoordinate++){
			for(int yCoordinate = 0; yCoordinate < widthAndHeight; yCoordinate++){
				if(getEntry(yCoordinate, xCoordinate) != 0) numberSolved ++; 
			}
		}
		return numberSolved;
	}

	
}
