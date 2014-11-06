package crf;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class CRFMain {
	public static void main(String[]args){
		//通过目录读入种种信息
		CRFReader crfReader = new CRFReader("data/train.utf8","data/labels.utf8","data/template.utf8");
		//为每一个label标签创建一个HashMap
		int labelSize = crfReader.getLabelArray().length;
		HashMap<String,Integer>[]hashMaps =new HashMap[labelSize];

		HashMap<String,int[]>hashMap =new HashMap<String,int[]>();
		
		init(hashMap,crfReader.getPassage(),crfReader.getTemplateUArray(),crfReader.getTemplateBArray(),crfReader.getLabelArray());
//		for(int i=0;i<labelSize;i++)
//		{
//			hashMaps[i]= new HashMap<String,Integer>();
//			//init(hashMaps[i],crfReader.getPassage(),crfReader.getTemplateUArray(),crfReader.getTemplateBArray());
//			//			for(int j:(hashMaps[i].values()))
//			//			{
//			//				System.out.println("Vlaue"+j);
//			//			}
//			//System.out.println("i: "+i+" ");
//		}
		char[]predictTag = null;
		predictTag = ( predict(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
			crfReader.getTemplateBArray(),hashMap) ).toCharArray();
		
//		predictTag[3]='B';
//		predictTag[predictTag.length-2]='E';
//		for(int i=4;i<predictTag.length-1;i++){
//			if(i%2==0)
//			{
//				predictTag[i]='I';
//			}
//			else{
//				//predictTag[i]='B';
//			}
//		}
		int i=0;
		//for(int j=0;j<6;j++){
		while(compare(predictTag,crfReader.getTagArray())<0.997){
			System.out.print("j: "+i+" : ");
			i++;
			System.out.println(compare(predictTag,crfReader.getTagArray()));

//			predictTag =  update(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
//					crfReader.getTemplateBArray(),hashMaps,crfReader.getTagArray() ).toCharArray();
			predictTag =  update(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
					crfReader.getTemplateBArray(),hashMap,crfReader.getTagArray() ).toCharArray();
		//	System.out.println(predictTag);
			//System.out.println(crfReader.getTagArray());
//			modify(hashMaps, crfReader.getTagArray(), predictTag, crfReader.getPassage(), 
//					crfReader.getTemplateUArray(), crfReader.getTemplateBArray());
//			//System.out.println("Modify Done!");
			//			
			//			System.out.println("");
//			predictTag = (predict(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
//					crfReader.getTemplateBArray(),hashMaps)).toCharArray();
			
		}

		//		
		crfReader = new CRFReader("data/test.utf8","data/labels.utf8","data/template.utf8");
		predictTag = (predict(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
				crfReader.getTemplateBArray(),hashMap)).toCharArray();
		System.out.println(predictTag);
		System.out.println(crfReader.getTagArray());
		System.out.println(compare(predictTag,crfReader.getTagArray()) );
		System.out.println("End");
	}

	private static double compare(char[] predictTag, char[] tagArray) {
		// TODO Auto-generated method stub
		int ret=0;
		int length =predictTag.length;
		for(int m=0;m<length;m++)
		{
			if((predictTag[m]-tagArray[m])==0)
			{
				ret++;
			}
		}
		return (double)(ret/10)/(double)( length/10);
	}

	private static String predict(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			HashMap<String, int[]>hashMap) {
		// TODO Auto-generated method stub

//		String retTags="PP";
//		long s = System.currentTimeMillis();
//		//predictTag = new char[tagArray.length];
//		char previousTag='P';
//		for(int i=2;i<passage.length()-2;i++)
//		{
//			Template temp;
//			int size = labelArray.length;
//			int[]score = new int[size];
//			//			if(i%1000==0)
//			//				System.out.println("Length:: "+i);
//
//			String slice = passage.substring(i-2, i+3);
//			for(int j=0;j<size;j++)
//			{
//				HashMap<String,Integer>hashMap = hashMaps[j];
//				for(int p=0;p<uArray.length;p++)
//				{
//					//System.out.println("Predict U_P) "+p);
//					temp = new Template(slice, uArray[p], 1, '\0');
//					try{
//						score[j]+=(hashMap.get(temp.getContent()));
//					}
//					catch(Exception e){
//
//					}
//				}
//				for(int p=0;p<bArray.length;p++)
//				{
//					//Need to implement another free Tag
//					//System.out.println("Predict B_P) "+p);
//					temp = new Template(slice, bArray[p], 2, previousTag);
//
//					try{
//						//						score[j]+=(hashMap.get( temp.getContent() ));
//						score[j]+=(hashMap.get(temp.getContent()));
//					}
//					catch(Exception e){
//
//					}
//				}
//
//			}
//			previousTag = labelArray[max(score)];
//			retTags += previousTag;
//		}
//		long e = System.currentTimeMillis();
//		retTags+="PP";
//		return retTags;
		String retTags="PP";
		long s = System.currentTimeMillis();
		//predictTag = new char[tagArray.length];
		char previousTag='P';
		for(int i=2;i<passage.length()-2;i++)
		{
			Template temp;
			int size = labelArray.length;
			int[]score = {0,0,0,0};
			//fillZero(score);
			
			String slice = passage.substring(i-2, i+3);
			//int[]rs=null;
			for(int p=0;p<uArray.length;p++)
			{
				//System.out.println("Predict U_P) "+p);
				temp = new Template(slice, uArray[p], 1, '\0');
				try{
					int[]rs=(hashMap.get(temp.getContent()));
//					int cc=0;
//					for(int q:hashMap.get(temp.getContent()))
//					{
//						rs[cc]=q;
//						
//						cc++;
//					}
					for(int c=0;c<rs.length;c++){
						//System.out.println(rs[i]);
						score[c]+=rs[c];
						//System.out.print(score[c]);
						
						//System.out.println((hashMap.get(temp.getContent()))[i]);
					}
					
				}
				catch(Exception e){
					int[]zeros={0,0,0,0};
					hashMap.put(temp.getContent(),zeros);
					
					//Pattern not found
				}
			}
			for(int p=0;p<bArray.length;p++)
			{
				//Need to implement another free Tag
				//System.out.println("Predict B_P) "+p);
				temp = new Template(slice, bArray[p], 2, previousTag);

				try{
					int[]rs=(hashMap.get(temp.getContent()));
					for(int c=0;c<rs.length;c++){
						score[i]+=rs[i];
					}
				}
				catch(Exception e){
					int[]zeros={0,0,0,0};
					//hashMap.put(temp.getContent(),zeros);
					
					//Pattern not found
				}
			}
//			previousTag = labelArray[max(score)];
//			char preTag = previousTag;
			char preTag = labelArray[max(score)];
			retTags += preTag;
			
		}

		retTags+="PP";
		return retTags;
	}

	private static String update(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			HashMap<String, Integer>[] hashMaps,char[]tags) {
		// TODO Auto-generated method stub

		String retTags="PP";
		long s = System.currentTimeMillis();
		//predictTag = new char[tagArray.length];
		char previousTag='P';
		for(int i=2;i<passage.length()-2;i++)
		{
			Template temp;
			int size = labelArray.length;
			int[]score = new int[size];
			
			String slice = passage.substring(i-2, i+3);
			for(int j=0;j<size;j++)
			{
				HashMap<String,Integer>hashMap = hashMaps[j];
				for(int p=0;p<uArray.length;p++)
				{
					//System.out.println("Predict U_P) "+p);
					temp = new Template(slice, uArray[p], 1, '\0');
					try{
						score[j]+=(hashMap.get(temp.getContent()));
					}
					catch(Exception e){

					}
				}
				for(int p=0;p<bArray.length;p++)
				{
					//Need to implement another free Tag
					//System.out.println("Predict B_P) "+p);
					temp = new Template(slice, bArray[p], 2, previousTag);

					try{
						//						score[j]+=(hashMap.get( temp.getContent() ));
						score[j]+=(hashMap.get(temp.getContent()));
					}
					catch(Exception e){

					}
				}

			}
//			previousTag = labelArray[max(score)];
//			char preTag = previousTag;
			char preTag = labelArray[max(score)];
			retTags += preTag;
			if(preTag-tags[i]!=0){
				Template temp1=null;
				for(int p=0;p<uArray.length;p++)
				{
					temp1 = new Template(slice, uArray[p], 1, '\0');
					String pattern = temp1.getContent();

					int index = getHashMapsIndex(preTag);
					if(hashMaps[index].containsKey(pattern))
					{
						int value = hashMaps[index].get(pattern);
						value--;
						hashMaps[index].put(pattern,value);
					}
					else{
						hashMaps[index].put(pattern,-1);
					}

					//temp = new Template(passage.substring(i-2, i+3), uArray[p], 1, '\0');
					index = getHashMapsIndex(tags[i]);
					if(hashMaps[index].containsKey(pattern))
					{
						int value = hashMaps[index].get(pattern);
						value++;
						hashMaps[index].put(pattern,value);
					}
					else{
						hashMaps[index].put(pattern,1);
					}
				}

				for(int p=0;p<bArray.length;p++)
				{
					//Need to implement another free Tag
					temp1 = new Template(slice, bArray[p],2,previousTag);
					String pattern = temp1.getContent();
					int index = getHashMapsIndex(preTag);
					if(hashMaps[index].containsKey(pattern))
					{
						int value = hashMaps[index].get(pattern);
						value--;
						hashMaps[index].put(pattern,value);
					}
					else{
						hashMaps[index].put(pattern,-1);
					}

					//temp = new Template(passage.substring(i-2, i+3), bArray[p],2,preTags[i-1]);
					index = getHashMapsIndex(tags[i]);
					if(hashMaps[index].containsKey(pattern))
					{
						int value = hashMaps[index].get(pattern);
						value++;
						hashMaps[index].put(pattern,value);
					}
					else{
						hashMaps[index].put(pattern,1);
					}
				}
				previousTag = preTag;
			}
			
		}
	retTags+="PP";
	//System.out.println("Return Tags: "+retTags.length()+" Tags: "+tagArray.length);
	//System.out.print("Time " +(e-s)/1000);
	return retTags;
}



	
	
	private static String update(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			HashMap<String, int[]>hashMap,char[]tags) {
		// TODO Auto-generated method stub

		String retTags="PP";
		long s = System.currentTimeMillis();
		//predictTag = new char[tagArray.length];
		char previousTag='P';
		for(int i=2;i<passage.length()-2;i++)
		{
			Template temp;
			int size = labelArray.length;
			int[]score = {0,0,0,0};
			//fillZero(score);
			
			String slice = passage.substring(i-2, i+3);
			//int[]rs=null;
			for(int p=0;p<uArray.length;p++)
			{
				//System.out.println("Predict U_P) "+p);
				temp = new Template(slice, uArray[p], 1, '\0');
				try{
					int[]rs=(hashMap.get(temp.getContent()));
//					int cc=0;
//					for(int q:hashMap.get(temp.getContent()))
//					{
//						rs[cc]=q;
//						
//						cc++;
//					}
					for(int c=0;c<rs.length;c++){
						//System.out.println(rs[i]);
						score[c]+=rs[c];
						//System.out.print(score[c]);
						
						//System.out.println((hashMap.get(temp.getContent()))[i]);
					}
					
				}
				catch(Exception e){
					int[]zeros={0,0,0,0};
					hashMap.put(temp.getContent(),zeros);
					
					//Pattern not found
				}
			}
			for(int p=0;p<bArray.length;p++)
			{
				//Need to implement another free Tag
				//System.out.println("Predict B_P) "+p);
				temp = new Template(slice, bArray[p], 2, previousTag);

				try{
					int[]rs=(hashMap.get(temp.getContent()));
					for(int c=0;c<rs.length;c++){
						score[i]+=rs[i];
					}
				}
				catch(Exception e){
					int[]zeros={0,0,0,0};
					hashMap.put(temp.getContent(),zeros);
					
					//Pattern not found
				}
			}
//			previousTag = labelArray[max(score)];
//			char preTag = previousTag;
			char preTag = labelArray[max(score)];
			retTags += preTag;
			if(preTag-tags[i]!=0){
				Template temp1=null;
				
				for(int p=0;p<uArray.length;p++)
				{
					int[]tmpRS=null;
					temp1 = new Template(slice, uArray[p], 1, '\0');
					String pattern = temp1.getContent();
//					try{
//						tmpRS = hashMap.get(pattern);
//					}
//					catch(Exception e){
//						
//					}
					tmpRS = hashMap.get(pattern);
					int preIndex= getHashMapsIndex(preTag);
					tmpRS[preIndex]--;
					int index = getHashMapsIndex(tags[i]);
					tmpRS[index]++;
//					for(int q:tmpRS){
//						System.out.print(q);
//					}
//					hashMap.remove(pattern);
//					hashMap.put(pattern, tmpRS);
					//fillZero(tmpRS);
				}

				for(int p=0;p<bArray.length;p++)
				{
					//Need to implement another free Tag
					int[]tmpRS=null;
					temp1 = new Template(slice, bArray[p],2,previousTag);
					String pattern = temp1.getContent();
					int preIndex= getHashMapsIndex(preTag);
					
					int index = getHashMapsIndex(tags[i]);
					
					tmpRS = hashMap.get(pattern);
					tmpRS[preIndex]--;
					tmpRS[index]++;
					hashMap.put(pattern, tmpRS);
					//fillZero(tmpRS);
				}
				previousTag = preTag;
			}
			
		}
	retTags+="PP";
	return retTags;
}



private static void fillZero(int[] tmpRS) {
		for(int i=0;i<tmpRS.length;i++){
			tmpRS[i]=0;
		}
	}

private static int max(int[]array)
{
	int max = array[0];
	int index=0;
	for(int i=1;i<array.length;i++)
	{
		if(array[i]>max)
		{
			max = array[i];
			index = i;
		}
	}
	//System.out.println();
	return index;
}
private static void init(HashMap<String, int[]> hashMap,
		String passage, String[] uArray, String[] bArray,char[]labelArray) {
	Template temp = null;
	
	
	for(int i=2;i<passage.length()-2;i++){
		
		for(int p=0;p<uArray.length;p++)
		{
			int initArray[] = new int [labelArray.length];
			for(int j=0;j<labelArray.length;j++)
			{
				initArray[j]=0;
			}
			temp = new Template(passage.substring(i-2, i+3), uArray[p], 1, '\0');
			hashMap.put(temp.getContent(), initArray);
		}
		for(int p=0;p<bArray.length;p++)
		{
			//Need to implement another free Tag
			int initArray[] = new int [labelArray.length];
			for(int j=0;j<labelArray.length;j++)
			{
				initArray[j]=0;
			}
			
			for(int j=0;j<labelArray.length;j++)
			{
				temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, labelArray[j]);
				//bPatterns.add(temp.getContent());
				hashMap.put(temp.getContent(), initArray);
			}
//			
//			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'B');
//			//bPatterns.add(temp.getContent());
//			hashMap.put(temp.getContent(), initArray);
//			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'I');
//			//bPatterns.add(temp.getContent());
//			hashMap.put(temp.getContent(), initArray);
//			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'E');
//			//bPatterns.add(temp.getContent());
//			hashMap.put(temp.getContent(), initArray);
		}
	}
}



private static void init(HashMap<String, Integer> hashMap,
		String passage, String[] uArray, String[] bArray) {
	Template temp = null;
	for(int i=2;i<passage.length()-2;i++){
		for(int p=0;p<uArray.length;p++)
		{
			temp = new Template(passage.substring(i-2, i+3), uArray[p], 1, '\0');
			hashMap.put(temp.getContent(), 0);
		}
		for(int p=0;p<bArray.length;p++)
		{
			//Need to implement another free Tag
			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'S');
			//bPatterns.add(temp.getContent());
			hashMap.put(temp.getContent(), 0);
			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'B');
			//bPatterns.add(temp.getContent());
			hashMap.put(temp.getContent(), 0);
			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'I');
			//bPatterns.add(temp.getContent());
			hashMap.put(temp.getContent(), 0);
			temp = new Template(passage.substring(i-2, i+3), bArray[p], 2, 'E');
			//bPatterns.add(temp.getContent());
			hashMap.put(temp.getContent(), 0);
		}
	}
}

private static void modify(HashMap<String, Integer>[]hashMaps,char[]tags,char[]preTags,String passage,
		String[] uArray, String[] bArray){
	for(int i=2;i<preTags.length-2;i++)
	{
		//			if(i%1000==0)
		//				System.out.println("Length:: "+i);
		char tag = tags[i];
		char preTag = preTags[i];
		if( (tag-preTag)!=0 )
		{
			Template temp=null;
			for(int p=0;p<uArray.length;p++)
			{
				temp = new Template(passage.substring(i-2, i+3), uArray[p], 1, '\0');
				String pattern = temp.getContent();

				int index = getHashMapsIndex(preTag);
				if(hashMaps[index].containsKey(pattern))
				{
					int value = hashMaps[index].get(pattern);
					value--;
					hashMaps[index].put(pattern,value);
				}
				else{
					hashMaps[index].put(pattern,-1);
				}

				//temp = new Template(passage.substring(i-2, i+3), uArray[p], 1, '\0');
				index = getHashMapsIndex(tag);
				if(hashMaps[index].containsKey(pattern))
				{
					int value = hashMaps[index].get(pattern);

					value++;
					hashMaps[index].put(pattern,value);
				}
				else{
					hashMaps[index].put(pattern,1);
				}
			}

			for(int p=0;p<bArray.length;p++)
			{
				//Need to implement another free Tag
				temp = new Template(passage.substring(i-2, i+3), bArray[p],2,preTags[i-1]);
				String pattern = temp.getContent();
				int index = getHashMapsIndex(preTag);
				if(hashMaps[index].containsKey(pattern))
				{
					int value = hashMaps[index].get(pattern);
					value--;
					hashMaps[index].put(pattern,value);
				}
				else{
					hashMaps[index].put(pattern,-1);
				}

				//temp = new Template(passage.substring(i-2, i+3), bArray[p],2,preTags[i-1]);
				index = getHashMapsIndex(tag);
				if(hashMaps[index].containsKey(pattern))
				{
					int value = hashMaps[index].get(pattern);
					value++;
					hashMaps[index].put(pattern,value);
				}
				else{
					hashMaps[index].put(pattern,1);
				}
			}

		}
	}
}
private static int getHashMapsIndex(char c){
	switch(c){
	case 'S':
		return 0;
	case 'B':
		return 1;
	case 'I':
		return 2;
	case 'E':
		return 3;
	}
	return 0;
}
}
