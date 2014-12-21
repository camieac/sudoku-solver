package testing;

import java.util.ArrayList;

import sudokusolver.Bucket;
import sudokusolver.Controller;
import sudokusolver.Pair;
import sudokusolver.PairList;
import sudokusolver.SudokuTable;

class Test {
	public Test() {

	}

	public static void main(String[] args) {
		ArrayList<Boolean> overall = new ArrayList<Boolean>();
		Test test = new Test();
		 test.testPairMethod();
		 overall.add(test.testEasyMethod());
		test.testExtremeSudoku();
		test.testCellRule();
		 overall.add(test.testPairList());
overall.add(test.testPossibleValues());
		 overall.add(test.testSudokuTable());
		if (!overall.contains(false)) System.out.println("Overall: Pass");
		else System.out.println("Overall: Fail");
	}

	private boolean testPairMethod() {
		Controller c2 = new Controller(9);
		c2.setFileName("res/sudoku2.txt");
		c2.loadSudoku();

		c2.drawPencilMarks();

		c2.solve();
		c2.printSudokuTable();
		c2.printStats();
		return false;
	}

	private boolean testEasyMethod() {
		Controller c = new Controller(9);
		c.setFileName("res/sudoku1.txt"); //This should be solvable using easy method.
		c.loadSudoku();

		c.drawPencilMarks();

//		c.solve();
		c.singleCandidateAlgorithm();
		c.printSudokuTable();
		areBucketTableEqual(c.getBucketList(), c.getSudokuTable());
		boolean solved = c.isSolved();
		if(solved) System.out.println("Simple Algorithm: Pass");
		else System.out.println("Simple Algorithm: Fail");
		return solved;
	}

	private boolean testExtremeSudoku() {

		Controller c2 = new Controller(9);
		c2.setFileName("res/extremeSudoku.txt");
		c2.loadSudoku();

		c2.drawPencilMarks();

		c2.solve();
		c2.printSudokuTable();
		c2.printStats();
		return false;
	}
	private boolean testCellRule(){
		Controller c2 = new Controller(9);
		c2.setFileName("res/extremeSudoku.txt");
		c2.loadSudoku();

		c2.drawPencilMarks();

		c2.solve();
		c2.singlePositionAlgorithm();
		c2.printSudokuTable();
		c2.printStats();
		return false;
	}
	private boolean testPairList(){
		boolean pass = true;
		PairList pl = new PairList();
		Bucket contents = new Bucket();
		contents.add(3);
		contents.add(7);
		Pair p = new Pair(contents,4,5);
		Pair p2 = new Pair(contents,5,4);
		System.out.println("Adding a valid pair: " + p.toString());
		if(pl.add(p)){
			System.out.println("Pass");
		}
		else{
			System.out.println("Fail");
			pass = false;
		}
		System.out.println("Adding a invalid pair: " + p2.toString());
		if(pl.add(p2)){
			System.out.println("Fail");
			pass = false;
		}
		else{
			System.out.println("Pass");
			
		}
		System.out.println("Adding a invalid pair: " + p.toString());
		if(pl.add(p)){
			System.out.println("Fail");
			pass = false;
		}
		else{
			System.out.println("Pass");
			
		}
		if(pass) System.out.println("Pair List: Pass");
		else System.out.println("Pair List: Fail");
		return pass;
	}
	
	private  boolean areBucketTableEqual(Bucket[][] bucketlist, SudokuTable table){
		boolean same = true;
		int bucketValue = 0;
	out:	for(int i = 0; i < 9;i++){
			for(int j = 0; j < 9;j++){
				try{
				
				bucketValue = bucketlist[i][j].getBucket().get(0);
				}catch(IndexOutOfBoundsException e){
					System.out.println("Empty bucket detected, fail");
					return false;
				}
				int tableValue = table.getEntry(i, j);
				 same =  bucketValue == tableValue || tableValue == 0;
				if(!same){
					System.out.println("Difference detected between " + bucketValue + " and " + tableValue);
					break out;
					
				}
				
			}
		}
		return same;
	}
	
private boolean testPossibleValues(){
	Controller c = new Controller(9);
	int numberOfPossibleValues = c.getWidthAndHeight();
	int sudokuTableWidth = c.getSudokuTable().getWidthAndHeight();
	int bucketArrayWidth = c.getBucketList()[0].length;
	int bucketArrayHeight = c.getBucketList().length;
	boolean result = numberOfPossibleValues == sudokuTableWidth && bucketArrayHeight == bucketArrayWidth && sudokuTableWidth == bucketArrayHeight;
	if (result) System.out.println("Possible values variable test: Pass");
	else System.out.println("Possible values variable test: Fail");
	return result;
}

/**
 * Designed for testing 9x9 sudoku solving.
 * @return
 */
private boolean testSudokuTable(){
	SudokuTable sudoukuTable = new SudokuTable(9);
	boolean validEntryPass;
	boolean invalidEntryFail;
	
	//Test Row
	validEntryPass = sudoukuTable.setEntry(0, 0, 4);
	invalidEntryFail = sudoukuTable.setEntry(0, 5, 4);
	boolean passRow = validEntryPass && !invalidEntryFail;
	if (passRow) System.out.println("SudokuTable row duplicate detection: Pass");
	else System.out.println("SudokuTable row duplicate detection: Fail");
	
	//Test Column
	validEntryPass = sudoukuTable.setEntry(0, 0, 3);
	invalidEntryFail = sudoukuTable.setEntry(7, 0, 3);
	boolean passColumn = validEntryPass && !invalidEntryFail;
	if (passColumn) System.out.println("SudokuTable column duplicate detection: Pass");
	else System.out.println("SudokuTable column duplicate detection: Fail");
	
	//Test Box
	validEntryPass = sudoukuTable.setEntry(0, 0, 2);
	invalidEntryFail = sudoukuTable.setEntry(2, 2, 2);
	boolean passBox = validEntryPass && !invalidEntryFail;
	if (passBox) System.out.println("SudokuTable box duplicate detection: Pass");
	else System.out.println("SudokuTable box duplicate detection: Fail");
	
	boolean pass = passRow && passColumn && passBox;
	return pass;
}
}
