package crf;

import java.util.HashMap;

public class CRFMain {
	public static void main(String[]args){
		Template tmp = new Template("我是好学生","U05:%x[-2,0]/%x[-1,0]",'B',1);
		Template tmp2 = new Template("我是坏学生","U05:%x[-2,0]/%x[-1,0]",'B',1);
		
		HashMap<String,Integer>hashMap = new HashMap<String,Integer>();
		hashMap.put(tmp.getCore(), 0);
		if(hashMap.containsKey(tmp2.getCore()))
		{
			System.out.print(tmp.compareTo(tmp2));
		}
		else{
			
		}
	}
}
