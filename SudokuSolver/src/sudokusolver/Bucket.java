package sudokusolver;

import java.util.ArrayList;
import java.util.Iterator;

public class Bucket implements Iterable<Integer> {
	private ArrayList<Integer> bucket;
	private ArrayList<Integer> removalList;
	private int size;
	public Bucket() {
		bucket = new ArrayList<Integer>();
		removalList = new ArrayList<Integer>();
		size = 0;
	}
	public Bucket(ArrayList<Integer> bucket) {
		this.bucket = bucket;
		removalList = new ArrayList<Integer>();
		size = bucket.size();
	}
	public Bucket(int[] bucket){
		this.bucket = new ArrayList<Integer>();
		removalList = new ArrayList<Integer>();
		size = bucket.length;
		this.add(bucket);
	}

	public boolean add(int value) {
		if (bucket.contains(value) == true)
			return false;
		else {
			bucket.add(value);
			size++;
			return true;
		}
	}

	boolean add(int[] values) {
		boolean flag = true;
		for (int i = 0; i < values.length; i++) {
			if (bucket.contains(values[i]))
				flag = false;
			else{
				bucket.add(values[i]);
				size++;
			}
			
		}
		return flag;

	}

	boolean remove(int value) {
		if (bucket.contains(value) == false)
			return false;
		else {
			int index = bucket.indexOf(value);
			bucket.remove(index);
			size--;
			return true;
		}

	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer("{");
		for(int i = 0; i < bucket.size();i++){
			sb.append(bucket.get(i));
			if(i < bucket.size() -1) sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}
	public boolean contains(int value){
		return bucket.contains(value);
	}
	public boolean isSingular(){
		return (bucket.size() == 1);
	}
	public int getRemainingValue(){
		return bucket.get(0);
	}
	public ArrayList<Integer> getBucket(){
		return bucket;
	}

	public int size() {
		return size;

	}

	public Iterator<Integer> iterator() {
		return bucket.iterator();
	}
	public void markRemoval(int value){
		removalList.add(value);
	}
	public void removeMarkedValues(){
		bucket.removeAll(removalList);
	}
	public boolean equals(Bucket bucketToCompare){
		if(this.bucket.equals(bucketToCompare.getBucket())){
			return true;
		}
		return false;
	}
	

}
