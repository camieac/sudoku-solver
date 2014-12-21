package sudokusolver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Handles the reading in of sudoku puzzles from text files. The following
 * format is required in order to successfully read in a sudoku:
 * <ul>
 * <li>Each line of the sudoku is on it's own line in the text file.</li>
 * <li>Each number in the row is separated by a comma.</li>
 * <li>Unknown cells are filled in with a 0(zero).</li></ul>
 * Currently each file can only hold one sudoku.
 * 
 * @author Cameron A. Craig
 * @since June 2014
 * 
 */
class IO {
	private int widthAndHeight;

	public IO(int widthAndHeight) {
		this.widthAndHeight = widthAndHeight;
	}

	public static void main(String[] args) {
		IO io = new IO(9);
		SudokuTable st = io.readSudoku("res/sudoku1.txt");
		System.out.println(st.toString());
	}

	public SudokuTable readSudoku(String fileName) {

		// This will reference one line at a time
		String line = null;

		try {

			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String[] sudokuline = new String[widthAndHeight];
			SudokuTable st = new SudokuTable(widthAndHeight);
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				sudokuline = line.split(",");
				st.setRow(i, stringArrayToIntegerArray(sudokuline));
				// System.out.println(line);
				i++;
			}
			bufferedReader.close();
			return st;

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
			return null;
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			return null;
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	private int[] stringArrayToIntegerArray(String[] stringArray) {
		int[] integerArray = new int[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			integerArray[i] = Integer.parseInt(stringArray[i]);
		}
		return integerArray;

	}

}
