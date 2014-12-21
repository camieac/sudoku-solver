package sudokusolver;

import java.util.ArrayList;
import java.util.Iterator;

public class PairList implements Iterable<Pair>{
	private ArrayList<Pair> pairList;

	public PairList() {
		pairList = new ArrayList<Pair>();
	}

	public boolean add(Pair newPair) {
		boolean unique = true;
		for (Pair pair : pairList) {
			if (!checkUniquePair(pair, newPair)) {
				unique = false;
				break;
			}
		}
		if (unique) {
			pairList.add(newPair);
			return true;
		} else
			return false;

	}

	private boolean checkUniquePair(Pair one, Pair two) {
		if (one.getFirstIndex() == two.getSecondIndex()
				&& two.getFirstIndex() == one.getSecondIndex()) {
			// If the indices are reversed, the pairs are still the same. No
			// need to check the contents of the pair.
			return false;
		} else if (one.getFirstIndex() == two.getFirstIndex()
				&& one.getSecondIndex() == two.getSecondIndex()) {
			// If the indices are the same.
			return false;
		} else {
			return true;
		}
	}
//	public static void main(String[] args){
//		
//	}

	@Override
	public Iterator<Pair> iterator() {
		return pairList.iterator();
	}
	public String toString(){
		return pairList.toString();
	}
}
