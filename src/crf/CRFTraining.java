package crf;

import java.util.HashMap;

public class CRFTraining {
	int labelSize=0;
	CRFTraining(int ls){
		this.labelSize = ls;
	}
	//Interface for outer links
	//Specify for the CRFReader and the hashMap
	public void init(CRFReader crf,HashMap<String, int[]>hashMap){
		String passage = crf.getPassage();
		char[] labelArray = crf.getLabelArray();
		String[] uArray = crf.getTemplateUArray();
		String[] bArray = crf.getTemplateBArray();
		init(hashMap, passage, uArray, bArray, labelArray);
	}
	public void learn(CRFReader crf,HashMap<String, int[]>hashMap,double rate){
		//Initialize for the CRFReader 
		String passage = crf.getPassage();
		char[] labelArray = crf.getLabelArray();
		String[] uArray = crf.getTemplateUArray();
		String[] bArray = crf.getTemplateBArray();
		char[]tagArray = crf.getTagArray();
		char[]predictTag = null;
		predictTag = ( predict(passage,labelArray,uArray,
				bArray,hashMap)) .toCharArray();
		int i=0;
		while(compare(predictTag,tagArray)<rate){
			System.out.print("j: "+i+" : ");
			i++;
			System.out.println(compare(predictTag,tagArray));
			predictTag =update(passage,labelArray,uArray,
					bArray,hashMap,tagArray).toCharArray();
		}
	}
	public void test(CRFReader crf,HashMap<String, int[]>hashMap){
		String passage = crf.getPassage();
		char[] labelArray = crf.getLabelArray();
		String[] uArray = crf.getTemplateUArray();
		String[] bArray = crf.getTemplateBArray();
		char[]tagArray = crf.getTagArray();
		char[]predictTag = null;
		predictTag =  predict(passage,labelArray,uArray,bArray,hashMap).toCharArray();
		 System.out.println(predictTag);
		System.out.println(tagArray);
		System.out.println(compare(predictTag,tagArray) );
		System.out.println("End");
		//return predict(passage,labelArray,uArray,bArray,hashMap);
		
	}
	
	public void learn(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			HashMap<String, int[]>hashMap,char[]tagArray){
		char[]predictTag = null;
		predictTag = ( predict(passage,labelArray,uArray,
				bArray,hashMap)) .toCharArray();
		System.out.println(predictTag);
		int i=0;
		while(compare(predictTag,tagArray)<0.999){
			System.out.print("j: "+i+" : ");
			i++;
			System.out.println(compare(predictTag,tagArray));
			predictTag =update(passage,labelArray,uArray,
					bArray,hashMap,tagArray).toCharArray();
		}
	}
	

	//Uses for inner links
	//Private modifiers
	
	private void init(HashMap<String, int[]> hashMap,
			String passage, String[] uArray, String[] bArray,char[]labelArray) {
		Template temp = null;
		for(int i=2;i<passage.length()-2;i++){
			String slice = passage.substring(i-2, i+3);
			
			//Implement for uniGram
			for(int p=0;p<uArray.length;p++)
			{
				int initArray[] = new int [labelArray.length];
				for(int j=0;j<labelArray.length;j++)
				{
					initArray[j]=0;
				}
				temp = new Template(slice, uArray[p], 1, '\0');
				hashMap.put(temp.getContent(),initArray);
			}
			//Implement for biGram
			for(int p=0;p<bArray.length;p++)
			{
				//Need to implement another free Tag
				int initArray[] = new int [labelSize];
				for(int j=0;j<labelSize;j++)
				{
					initArray[j]=0;
				}
				//Implement 'SBIE' mapping
				for(int j=0;j<labelSize;j++)
				{
					temp = new Template(slice, bArray[p], 2, labelArray[j]);
					hashMap.put(temp.getContent(), initArray);
				}
			}
		}
	}
	private  String predict(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			HashMap<String, int[]>hashMap) {
		String retTags="PP";
		char previousTag='P';
		for(int i=2;i<passage.length()-2;i++)
		{
			String slice = passage.substring(i-2, i+3);
			char preTag = getTag(hashMap,labelArray,slice,uArray,bArray,previousTag);
			retTags += preTag;
			previousTag = preTag;
		}
		retTags+="PP";
		return retTags;
	}

	private String update(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			HashMap<String, int[]>hashMap,char[]tags) {
		// TODO Auto-generated method stub

		String retTags="PP";
		//predictTag = new char[tagArray.length];
		char previousTag='P';
		for(int i=2;i<passage.length()-2;i++)
		{

			String slice = passage.substring(i-2, i+3);
			char preTag = getTag(hashMap,labelArray,slice,uArray,bArray,previousTag);
			retTags += preTag;
			if(preTag-tags[i]!=0){
				Template temp=null;

				for(int p=0;p<uArray.length;p++)
				{
					int[]tmpRS=null;
					temp = new Template(slice, uArray[p], 1, '\0');
					String pattern = temp.getContent();
					tmpRS = hashMap.get(pattern);
					int preIndex= getHashMapsIndex(preTag);
					tmpRS[preIndex]--;
					int index = getHashMapsIndex(tags[i]);
					tmpRS[index]++;
				}

				for(int p=0;p<bArray.length;p++)
				{
					//Need to implement another free Tag
					int[]tmpRS=null;
					temp = new Template(slice, bArray[p],2,previousTag);
					String pattern = temp.getContent();
					int preIndex= getHashMapsIndex(preTag);
					int index = getHashMapsIndex(tags[i]);

					tmpRS = hashMap.get(pattern);
					tmpRS[preIndex]--;
					tmpRS[index]++;
				}
				previousTag = preTag;
			}

		}
		retTags+="PP";
		return retTags;
	}
	private  char getTag(HashMap<String,int[]>hashMap,char[]labelArray,
			String slice,String[] uArray,String[] bArray,char previousTag){

		Template temp=null;
		//Score aggregation
		int[]score=new int[labelSize];
		for(int i=0;i<labelSize;i++)
		{
			score[i]=0;
		}
		for(int p=0;p<uArray.length;p++)
		{
			temp = new Template(slice, uArray[p], 1, '\0');
			try{
				
				int[]rs=(hashMap.get(temp.getContent()));
				for(int c=0;c<rs.length;c++){
					score[c]+=rs[c];
				}

			}
			catch(Exception e){
				int[]zeros={0,0,0,0};
				hashMap.put(temp.getContent(),zeros);
			}
		}
		for(int p=0;p<bArray.length;p++)
		{
			//Need to implement another free Tag
			temp = new Template(slice, bArray[p], 2, previousTag);

			try{
				int[]rs=(hashMap.get(temp.getContent()));
				for(int c=0;c<rs.length;c++){
					score[c]+=rs[c];
				}
			}
			catch(Exception e){
				int[]zeros={0,0,0,0};
				hashMap.put(temp.getContent(),zeros);

				//Pattern not found
			}
		}
		return labelArray[max(score)];

	}

	//Find the index of maximum element in an array
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
		return index;
	}
	
	private int getHashMapsIndex(char c){
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
	//Return the percentage of rightness
	private double compare(char[] predictTag, char[] tagArray) {
		int ret=0;
		int length =predictTag.length;
		for(int m=0;m<length;m++)
		{
			if((predictTag[m]-tagArray[m])==0)
			{
				ret++;
			}
		}
		//Need to subtract the given HEADER&TAILER "PP"
		return (double)(ret-4)/(double)(length-4);
	}
}
