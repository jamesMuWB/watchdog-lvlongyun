package per.cloudy.entity;

import java.util.LinkedList;

public class Bucket {
	public static final int MAX_SIZE=100;
	public LinkedList<Integer> list=new LinkedList<Integer>();
	public int max;
	public int index;
	public void add(Integer count){
		if(list.size()==0||max<count){
			max=count;
		}
		if(list.size()>=MAX_SIZE){
			list.remove(0);
		}
		list.add(count);
		checkMax();
	}
	public void checkMax(){
		index++;
		if(index%MAX_SIZE==0){
			int tmpMax=0;
			for(Integer count:list){
				if(count>tmpMax){
					tmpMax=count;
				}
			}
			this.max=tmpMax;
		}
	}
	
}
