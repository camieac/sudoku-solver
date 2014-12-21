package sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.sound.midi.SysexMessage;

/**
 * The main solving class, contains all solving algorithms. Note that 'bucket'
 * refers to an array of integers that represent the possible values of a cell
 * in the sudoku table, these are also known as 'pencilmarks'.
 * 
 * @author Cameron A. Craig
 * @since June 2014
 * 
 */
public class Controller {
	public static void main(String[] args) {
		Controller c = new Controller(9);
		c.setFileName("res/extremeSudoku.txt");
		c.loadSudoku();
		c.printSudokuTable();
		c.drawPencilMarks();

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.singleCandidateAlgorithm();
		c.singlePositionAlgorithm();
		c.hardSolve();
		System.out.println("Number of cells solved: "
				+ c.getSudokuTable().getNumberSolved());

		c.printSudokuTable();
		c.printPencilMarks(c.getBucketList());
	}

	/**
	 * This is a 2D array of {@link Bucket} objects. It represents the pencil
	 * marks for each cell in the sudoku. Each possible number is stored in the
	 * array as an integer.
	 */
	private Bucket[][] pencilMarks;

	/**
	 * The name of the file , including the file path. This is used if importing
	 * a sudoku from a text file. See {@link IO} for file formatting.
	 */
	private String fileName;

	private IO io;
	SudokuTable sudokutable;
	private boolean notSolved = true;
	private int simpleIterations;
	private int pairsFound;
	private int widthAndHeight;

	public int getWidthAndHeight() {
		return widthAndHeight;
	}

	private int[] possibleValues;

	public Controller(int rows) {
		pairsFound = 0;
		simpleIterations = 0;
		widthAndHeight = rows;
		sudokutable = new SudokuTable(widthAndHeight);
		pencilMarks = new Bucket[widthAndHeight][widthAndHeight];
		for (int i = 0; i < widthAndHeight; i++) {
			for (int j = 0; j < widthAndHeight; j++) {
				pencilMarks[i][j] = new Bucket();
			}
		}
		io = new IO(widthAndHeight);
		possibleValues = new int[widthAndHeight];
	}

	/**
	 * @param currentBox
	 * @param currentBucket
	 */
	private void checkBox(int[] currentBox, Bucket currentBucket) {
		for (int i = 0; i < widthAndHeight; i++) {
			currentBucket.remove(currentBox[i]);
		}
	}

	public Bucket[][] getBucketList() {
		return pencilMarks;
	}

	public SudokuTable getSudokuTable() {
		return sudokutable;
	}

	/**
	 * For each bucket in the bucketlist, if the bucket has been reduced to one
	 * possible value (now a definite value), then transfer that value into the
	 * correct position in the results table.
	 */
	private void transferPencilMarks() {
		boolean allSingular = true;
		for (int i = 0; i < widthAndHeight; i++) {
			for (int j = 0; j < widthAndHeight; j++) {
				if (pencilMarks[i][j].isSingular()) {
					// System.out.println("\nBucket at " + i + ", " + j +
					// " is singular, inserting " + bucketlist[i][j] +
					// " into sudoku table");
					sudokutable.setEntry(i, j,
							pencilMarks[i][j].getRemainingValue());
				} else
					allSingular = false;
			}
		}
		notSolved = !allSingular;

	}

	private void checkColumn(int[] currentColumn, Bucket currentBucket) {
		for (int i = 0; i < widthAndHeight; i++) {
			currentBucket.remove(currentColumn[i]);
		}
	}

	/**
	 * Returns true if the two buckets are the same. (If the two buckets are the
	 * same, this is repeating). False otherwise.
	 * 
	 * @param previous
	 *            The previous bucket
	 * @param current
	 *            The current bucket
	 * @return
	 */
	private boolean checkRepeating(Bucket[][] previous, Bucket[][] current) {
		// System.out.println("Checking equality between:\n");
		printPencilMarks(previous);
		// System.out.println("\n and:\n");
		printPencilMarks(current);
		boolean repeat = Arrays.deepEquals(previous, current);
		// System.out.println("Same: " + repeat);
		return repeat;

	}

	/**
	 * Check all values in the row to eliminate any values that the element at
	 * the specified position cannot be.
	 * 
	 * @param positionInRow
	 * @param rowNumber
	 */
	private void checkRow(int[] currentRow, Bucket currentBucket) {
		for (int i = 0; i < widthAndHeight; i++) {
			currentBucket.remove(currentRow[i]);
		}
	}

	/**
	 * Search through an array of buckets and find any buckets with the same two
	 * possible values, then eliminate any other occurrence of those individual
	 * values in the array.
	 * 
	 * @param bucketArray
	 *            An array of buckets, this could be a sudoku row, column or
	 *            box.
	 */
	private void doublePairsAlgorithm(Bucket[] bucketArray) {
		// System.out.println("Starting findPairs");
		PairList pairs = new PairList();
		for (int i = 0; i < widthAndHeight; i++) {
			Bucket currentBucket = bucketArray[i];
			// System.out.println("Created currentBucket:");
			// System.out.println(currentBucket.toString());
			// System.out.println("this has size" + currentBucket.size());
			if (currentBucket.size() == 2) {
				// System.out.println("found bucket of size 2");
				for (int j = 0; j < widthAndHeight; j++) {
					Bucket testBucket = bucketArray[j];
					// System.out.println("Is " + currentBucket.toString() +
					// " the same as "+ testBucket.toString());
					if (currentBucket.equals(testBucket) && (i != j)) {
						// System.out.println("Pair found: " + bucketArray[i]
						// + " at position " + i + " and " + j);
						Pair pair = new Pair(bucketArray[i], i, j);
						if (pairs.add(pair)) {
							// System.out.println("Pair added");
							pairs.add(pair);
							pairsFound++;
						} else {
							// System.out.println("Duplicate pair not added");

						}

					} else {
						// System.out.println("No");
					}
				}
			}
		}

		/*
		 * Remove any occurrence of any of the pair contents from the rest of
		 * the buckets in the bucket list. The bucket list is
		 */
		// System.out.println("Pair List: " + pairs.toString());
		int bucketNumber = 0;
		for (Bucket b : bucketArray) {// TODO: Can be much more efficient by
										// making the pairs for loop the
										// outermost, but I don't want to fiddle
										// around and break it at the moment.
			if (b.size() > 1) {
				// System.out.println("Bucket size: " + b.size());
				// System.out.println("Looking at bucket: " + b.toString());
				for (Pair p : pairs) {
					// System.out.println("Looking at Pair: " + p.toString());
					for (int i : p.getContents()) {
						// System.out.println("Looking at content of pair: " +
						// i);
						/* Make sure it doesn't remove from it's self */
						if (b.contains(i) && p.getFirstIndex() != bucketNumber
								&& p.getSecondIndex() != bucketNumber) {
							b.markRemoval(i);
							System.out.println("Remove " + i + " from "
									+ b.toString());
						}
					}

				}
				b.removeMarkedValues();

			}
			bucketNumber++;
		}

	}

	public int[][] getResults() {
		return sudokutable.getTable();
	}

	/**
	 * This is a series of algorithms that are only required for more difficult
	 * sudoku puzzles. Firstly it finds pairs(a pair is two buckets containing
	 * two of the same number) it then removes any occurrence of the two numbers
	 * in the pair in the row, column and box buckets.
	 */
	void hardSolve() {
		// System.out.println("Bucket list before.");
		// printBucketList(bucketlist);
		/* Checking columns for pairs */
		for (int w = 0; w < widthAndHeight; w++) {
			// System.out.println("Checking column " +
			// Arrays.toString(bucketlist[w]));
			doublePairsAlgorithm(pencilMarks[w]);
		}

		/* Checking rows for pairs */

		for (int w = 0; w < widthAndHeight; w++) {
			// System.out.println("Checking row " +
			// Arrays.toString(transpose[w]));
			doublePairsAlgorithm(getBucketRow(w));
		}

		/* Checking buckets for pairs */
		for (int k = 0; k < widthAndHeight; k = k
				+ (int) Math.sqrt(widthAndHeight)) {
			for (int l = 0; l < widthAndHeight; l = l
					+ (int) Math.sqrt(widthAndHeight)) {
				// System.out.println("\t"+ k + ", " + l);
				Bucket[] box = getBucketBox(k, l);
				// System.out.println("Checking box " + Arrays.toString(box));
				doublePairsAlgorithm(box);
			}
		}

		// System.out.println("Bucket list after.");
		// printBucketList(bucketlist);

	}

	/**
	 * Get an array of the buckets that are contained within a box, e.g a 3x3 square in a 9x9 sudoku.
	 * @param yCoordinate The y coordinate of the top row of the box.
	 * @param xCoordinate The x coordinate of the left column of the box.
	 * @return Array of {@link Bucket}s
	 */
	public Bucket[] getBucketBox(int yCoordinate, int xCoordinate) {
		int boxWidth = (int) Math.sqrt(widthAndHeight);
		int boxX = boxWidth * (xCoordinate / boxWidth);
		int boxY = boxWidth * (yCoordinate / boxWidth);

		Bucket[] box = new Bucket[widthAndHeight];

		int offX = 0;
		int offY = 0;

		for (int i = 0; i < widthAndHeight; i++) {
			box[i] = pencilMarks[boxY + offY][boxX + offX];
			if (i == (boxWidth - 1))
				offY = 1;
			if (i == (2 * boxWidth) - 1)
				offY = 2;
			offX++;
			if (offX > 2)
				offX = 0;
		}
		return box;
	}

	public void loadGUINumbers(int[][] numbers) {
		sudokutable.setTable(numbers);
	}

	public void loadSudoku() {
		sudokutable = io.readSudoku(fileName);
	}

	public void drawPencilMarks() {
		int widthAndHeight = sudokutable.getWidthAndHeight();

		for (int i = 0; i < widthAndHeight; i++) {
			possibleValues[i] = i + 1;
		}
		System.out.println("Possible Values for this suduko: "
				+ Arrays.toString(possibleValues));
		for (int w = 0; w < widthAndHeight; w++) {
			for (int h = 0; h < widthAndHeight; h++) {
				if (sudokutable.getEntry(w, h) != 0) {
					pencilMarks[w][h].add(sudokutable.getEntry(w, h));
				} else {
					pencilMarks[w][h].add(possibleValues);

				}

			}
		}
	}

	private void printPencilMarks(Bucket[][] bucketList) {
		StringBuffer sb = new StringBuffer();
		sb.append("Buckets:\n");
		try {
			for (int i = 0; i < widthAndHeight; i++) {
				for (int j = 0; j < widthAndHeight; j++) {
					sb.append(bucketList[j][i].toString() + "\t");
				}
				sb.append("\n");

			}
			System.out.println(sb.toString());
		} catch (NullPointerException e) {
			System.out.println("Empty bucket");
		}

	}

	public void printStats() {
		System.out.println("Number of simple iterations: " + simpleIterations);
		System.out.println("Number of pairs found using hard method: "
				+ pairsFound);

	}

	public void printSudokuTable() {
		System.out.println(sudokutable.toString());

	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setWidthAndHeight(int widthAndHeight) {
		this.widthAndHeight = widthAndHeight;
	}

	/**
	 * This method is good enough for solving most 'easy' rated sudoku puzzles.
	 * This algorithm simply eliminates possibilities from each square by
	 * looking in it's corresponding row, column and box.
	 */
	public void singleCandidateAlgorithm() {
		boolean notRepeating = true;
		while (notSolved && notRepeating) {
			for (int xCoordinate = 0; xCoordinate < widthAndHeight; xCoordinate++) {
				for (int yCoordinate = 0; yCoordinate < widthAndHeight; yCoordinate++) {
					Bucket currentBucket = pencilMarks[yCoordinate][xCoordinate];
					int currentCellValue = sudokutable.getEntry(yCoordinate,
							xCoordinate);
					int[] currentRow = sudokutable.getRow(yCoordinate);
					int[] currentColumn = sudokutable.getColumn(xCoordinate);
					int[] currentBox = sudokutable.getBox(yCoordinate,
							xCoordinate);
					if (currentCellValue == 0) {
						checkRow(currentRow, currentBucket);
						checkColumn(currentColumn, currentBucket);
						checkBox(currentBox, currentBucket);
						transferPencilMarks();
						// printBuckets(bucketlist);
					}

				}

			}
			simpleIterations++;
			if (simpleIterations >= (widthAndHeight * widthAndHeight))
				notRepeating = false;

		}
		if (!notSolved)
			System.out.println("Solved.");
		if (!notRepeating)
			System.out.println("Repeating bucketlist detected.");
		printPencilMarks(pencilMarks);
	}

	/**
	 * Solve the sudoku. Each cell in the puzzle has a corresponding bucket
	 * array. When a number is discounted from a cell, it is removed from the
	 * bucket array. Eventually only one value will be left in the bucket array
	 * and this will be the number that goes in the cell to solve the suduko.
	 * When there is only one value left in the bucket array, it is inserted
	 * into the sudoku table cell in the corresponding location.
	 * 
	 */
	public void solve() {
		singleCandidateAlgorithm();
		if (notSolved) {
			System.out
					.println("This is a hard sudoku, using the hard algorithm!");
			hardSolve();
			System.out
					.println("Running simple algorithm again to remove any rejected possibilities.");
			singleCandidateAlgorithm();
		}

	}
	//TODO:Fully implement
	/**
	 * Look at the pencil marks for each box in the sudoku, for each box, if
	 * there is only one row or column where a number can be placed, then that
	 * number can be removed from the pencil marks of that row or column, outside
	 * of the box.
	 * 
	 */
	private void candidiateLines() {
		for(int i = 0;i < widthAndHeight; i++){
	//		getBucketBox(w, h)
		}

	}

	/**
	 * This is known as the Cell Rule, or Single Position algorithm. Look at
	 * each row, column and box. Generate an array of all the numbers missing in
	 * the row, column or box. Iterate through each missing number and see if it
	 * can be correctly placed in only one cell. If it can only be correctly
	 * placed in one cell, then that number is inserted into that cell and
	 * removed from the missing numbers array.
	 */
	/**
	 * 
	 */
	public void singlePositionAlgorithm() {
		System.out.println("Using the cell rule...");
		// Iterating from 0 to the width of the sudoku table minus one.
		for (int i = 0; i < widthAndHeight; i++) {
			// Setting up local variables
			Bucket missingNumbersRow = new Bucket(possibleValues);
			Bucket missingNumbersColumn = new Bucket(possibleValues);
			ArrayList<Integer> row = new ArrayList<Integer>();
			ArrayList<Integer> column = new ArrayList<Integer>();

			// Constructing an ArrayList of integers containing the current row.
			for (int b : sudokutable.getRow(i)) {
				row.add(b);
			}
			System.out.println("Checking row: " + row.toString());
			// Constructing an ArrayList of integers containing the current
			// column.
			for (int b : sudokutable.getColumn(i)) {
				column.add(b);
			}
			System.out.println("Checking column: " + column.toString());
			// Iterating through the possible values that could be placed in the
			// current cell.
			for (int j : possibleValues) {
				// System.out.println("Looking for " + j);
				// If the current row already contains number j, remove it from
				// the missing numbers for that row.
				if (row.contains(j)) {
					// System.out.println(j + " found in row " +
					// row.toString());
					missingNumbersRow.remove(j);

				}
				// If the current column already contains number j, remove it
				// from the missing numbers for that column.
				if (column.contains(j)) {
					// System.out.println(j + " found in column "
					// + column.toString());
					missingNumbersColumn.remove(j);

				}

			}

			// We now have a list of missing numbers for a row and a column,
			// TODO: the box has not been implemented yet
			System.out.println(i + ", Missing Numbers Row: "
					+ missingNumbersRow);
			System.out.println(i + ", Missing Numbers Column: "
					+ missingNumbersColumn);
			// The next thing to do is iterate through the missing numbers and
			// see if there is only one valid position for that number, if there
			// is only one valid position, then it is inserted into the sudoku
			// table.
			for (int m : missingNumbersRow) {
				int validEntries = 0;
				int validXPosition = 0;
				int validYPosition = 0;

				for (int p = 0; p < widthAndHeight; p++) {
					if (sudokutable.validEntry(i, p, m)) {
						validEntries++;
						validXPosition = p;
						validYPosition = i;
					}
				}
				System.out.println("Valid entries for " + m + " in row "
						+ validYPosition + " is " + validEntries);
				if (validEntries == 1) {
					// Enter the value at the only valid position.
					System.out.println("Inserting " + m
							+ "into the sudoku table (x:" + validXPosition
							+ ", y:" + validYPosition + ")"
							+ ", only valid location");
					boolean success = sudokutable.setEntry(validYPosition,
							validXPosition, m);
					if (!success)
						System.out.println("MASSIVE ERROR IN ALGORITHM");
				}
			}

		}
	}

	/**
	 * Check whether putting a value in a position within a row, column or box;
	 * is the only valid position for that value.
	 * 
	 * @param array
	 *            The row, column or box.
	 * @param value
	 *            The value being tested
	 * @param position
	 *            The position in the array that the value is being tested in.
	 * @return True if it is the only valid position, false otherwise.
	 */
	private boolean isOnlyValidPosition(int value, int yCoordinate,
			int xCoordinate) {
		int validEntries = 0;

		return (validEntries == 1);
	}

	private Bucket[] getBucketColumn(int i) {
		return pencilMarks[i];
	}

	private Bucket[] getBucketRow(int k) {
		Bucket[][] transpose = new Bucket[widthAndHeight][widthAndHeight];
		for (int i = 0; i < widthAndHeight; i++) {
			for (int j = 0; j < widthAndHeight; j++) {
				transpose[i][j] = pencilMarks[j][i];
			}

		}
		return transpose[k];
	}

	public boolean isSolved() {
		return !notSolved;
	}

}
