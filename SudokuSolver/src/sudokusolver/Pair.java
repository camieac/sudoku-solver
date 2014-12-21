package sudokusolver;

public class Pair {
	private Bucket contents;
	private int firstIndex;
	private int secondIndex;
	
	public Pair(){
		contents = new Bucket();
		firstIndex = 0;
		secondIndex = 0;
	}
	public Pair(Bucket contents,int firstIndex, int secondIndex){
		this.contents = contents;
		this.firstIndex = firstIndex;
		this.secondIndex = secondIndex;
	}
	public Bucket getContents(){
		return contents;
	}
	public int getFirstIndex(){
		return firstIndex;
	}
	public int getSecondIndex(){
		return secondIndex;
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		sb.append(contents.toString());
		sb.append(", " + firstIndex + ", " + secondIndex + "]");
		return sb.toString();
	}
}
