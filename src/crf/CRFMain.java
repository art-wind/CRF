package crf;

import java.util.HashMap;

public class CRFMain {
	public static void main(String[]args){
//		Template tmp = new Template("我是好学生","U06:%x[-1,0]/%x[0,0]",'B',1,'\0');
//		Template tmp2 = new Template("他是你学生","U06:%x[-1,0]/%x[0,0]",'B',1,'\0');
//		Template tmps = new Template("我是好学生","U06:%x[-1,0]/%x[0,0]",'S',1,'\0');
//		Template tmps2 = new Template("他是你学生","U06:%x[-1,0]/%x[0,0]",'S',1,'\0');
		//通过目录读入种种信息
		CRFReader crfReader = new CRFReader("data/train.utf8","data/labels.utf8","data/template.utf8");
		//为每一个label标签创建一个HashMap
		int labelSize = crfReader.getLabelArray().length;
		HashMap<Template,Integer>[]hashMaps =new HashMap[labelSize];
		
		for(int i=0;i<labelSize;i++)
		{
			hashMaps[i]= new HashMap<Template,Integer>();
			init(hashMaps[i],crfReader.getPassage(),crfReader.getLabelArray(i),
					crfReader.getTemplateUArray(),crfReader.getTemplateBArray(),crfReader.getTagArray());
//			for(int j:(hashMaps[i].values()))
//			{
//				System.out.println("Vlaue"+j);
//			}
		}
		char[]predictTag = null;
		predictTag = (predict(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
				crfReader.getTemplateBArray(),crfReader.getTagArray(),hashMaps)).toCharArray();
		int time=4;
		System.out.println(predictTag);
		for(int i=0;i<time;i++)
		{
			modify(hashMaps, crfReader.getTagArray(), predictTag, crfReader.getPassage(), crfReader.getTemplateUArray(), crfReader.getTemplateBArray());
			predictTag = (predict(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
					crfReader.getTemplateBArray(),crfReader.getTagArray(),hashMaps)).toCharArray();
			System.out.println(predictTag);
			System.out.println(compare(predictTag,crfReader.getTagArray()));
			System.out.println("End");
			//System.out.println(predictTag);
			//System.out.println(compare(predictTag,crfReader.getTagArray()));
		}
		
		
		crfReader = new CRFReader("data/test.utf8","data/labels.utf8","data/template.utf8");
		predictTag = (predict(crfReader.getPassage(),crfReader.getLabelArray(),crfReader.getTemplateUArray(),
				crfReader.getTemplateBArray(),crfReader.getTagArray(),hashMaps)).toCharArray();
		System.out.println(predictTag);
		System.out.println(compare(predictTag,crfReader.getTagArray()));
		System.out.println("End");
		//System.out.print(predictTag);

		//modify(hashMaps, crfReader.getTagArray(), predictTag, crfReader.getPassage(), crfReader.getTemplateUArray(), crfReader.getTemplateBArray());
		
//		for(int i=0;i<labelSize;i++)
//		{
//			for(Template t:hashMaps[i].keySet())
//			{
//				if(hashMaps[i].get(t)<-100)
//					//System.out.println("i: "+i+"  "+t.getCore()+" "+t.getRule());
//			}
//		}
//		
		
		//System.out.print(predictTag);
		
		
		
//		HashMap<Template,Integer>hashMap= new HashMap<Template,Integer>();
//		hashMap.put(tmp, 0);
//		//System.out.print(tmp.equals(tmp2));
//		if(hashMap.containsKey(tmp2))
//		{
//			System.out.print("Yes!!!");
//			int value = hashMap.get(tmp2);
//			value++;
//			hashMap.put(tmp2, value);
//		}
//		else{
//			
//		}
//		System.out.print("Value:"+hashMap.get(tmp2));
//		
	}

	private static double compare(char[] predictTag, char[] tagArray) {
		// TODO Auto-generated method stub
		int ret=0;
		for(int m=0;m<predictTag.length;m++)
		{
			if((predictTag[m]-tagArray[m])==0)
			{
				ret++;
			}
		}
		System.out.println("Pre "+ret +"/ "+predictTag.length);
		return (double)((ret*1000)/(predictTag.length*1000));
	}

	private static String predict(String passage, char[] labelArray,String[] uArray,  String[] bArray, 
			char[] tagArray, HashMap<Template, Integer>[] hashMaps) {
		// TODO Auto-generated method stub
		Template temp;
		String retTags="P";
		long s = System.currentTimeMillis();
		//predictTag = new char[tagArray.length];
		for(int i=2;i<passage.length()-2;i++)
		{
			int[]score = new int[4];
			for(int j=0;j<hashMaps.length;j++)
			{
				for(int p=0;p<uArray.length;p++)
				{
					temp = new Template(passage.substring(i-2, i+3), uArray[p], labelArray[j], 1, '\0');
					if(hashMaps[j].containsKey(temp))
					{
						score[j]+=(hashMaps[j].get(temp));
						
					}
					//System.out.println(temp.hashCode());
				}
				for(int p=0;p<bArray.length;p++)
				{
					//Need to implement another free Tag
					temp = new Template(passage.substring(i-2, i+3), bArray[p], labelArray[j], 2, tagArray[i-2]);
					if(hashMaps[j].containsKey(temp))
					{
						score[j]+=(hashMaps[j].get(temp));
					}
				}
			}
			try{
				
			//predictTag[i] = tagArray[max(score)];
			retTags += labelArray[max(score)];
			//System.out.print("Max" + max(score));
			}
			catch(Exception e){
				
			}
			
		}
		long e = System.currentTimeMillis();
		retTags+="P";
		System.out.println("Return Tags: "+retTags.length()+" Tags: "+tagArray.length);
		//System.out.print("Time " +(e-s)/1000);
		return retTags;
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
	private static void init(HashMap<Template, Integer> hashMap,
			String passage, char label, String[] uArray, String[] bArray, char[] tags) {
		Template temp = null;
		for(int i=2;i<passage.length()-2;i++){
			for(int p=0;p<uArray.length;p++)
			{
				temp = new Template(passage.substring(i-2, i+3), uArray[p], label, 1, '\0');
				hashMap.put(temp, 0);
				//System.out.println(temp.hashCode());
			}
			for(int p=0;p<bArray.length;p++)
			{
				//Need to implement another free Tag
				temp = new Template(passage.substring(i-2, i+3), bArray[p], label, 2, tags[i-2]);
				hashMap.put(temp, 0);
			}//System.out.print(hashMap.size());
		}
	}
	private static void modify(HashMap<Template, Integer>[]hashMaps,char[]tags,char[]preTags,String passage,
			String[] uArray, String[] bArray){
		for(int i=1;i<preTags.length;i++)
		{
			char tag = tags[i];
			char preTag = preTags[i];
			if( (tag-preTag)!=0 )
			{
				Template temp=null;
				for(int p=0;p<uArray.length;p++)
				{
					temp = new Template(passage.substring(i-1, i+4), uArray[p], preTag, 1, '\0');
					int index = getHashMapsIndex(preTag);
					if(hashMaps[index].containsKey(temp))
					{
						int value = hashMaps[index].get(temp);
						value--;
						hashMaps[index].put(temp,value);
					}
					else{
						hashMaps[index].put(temp,-1);
					}
					
					temp = new Template(passage.substring(i-1, i+4), uArray[p], tag, 1, '\0');
					index = getHashMapsIndex(tag);
					if(hashMaps[index].containsKey(temp))
					{
						int value = hashMaps[index].get(temp);
						//System.out.println("Value Sub: "+ value);
						value++;
						hashMaps[index].put(temp,value);
					}
					else{
						hashMaps[index].put(temp,1);
					}
					//System.out.println(temp.hashCode());
				}
				
				for(int p=0;p<bArray.length;p++)
				{
					//Need to implement another free Tag
					temp = new Template(passage.substring(i-1, i+4), bArray[p], preTag, 2, tags[i-1]);
					int index = getHashMapsIndex(preTag);
					if(hashMaps[index].containsKey(temp))
					{
						int value = hashMaps[index].get(temp);
						value--;
						hashMaps[index].put(temp,value);
					}
					else{
						hashMaps[index].put(temp,-1);
					}
					
					temp = new Template(passage.substring(i-1, i+4), bArray[p], tag, 2, tags[i-1]);
					index = getHashMapsIndex(tag);
					if(hashMaps[index].containsKey(temp))
					{
						int value = hashMaps[index].get(temp);
						value++;
						hashMaps[index].put(temp,value);
					}
					else{
						hashMaps[index].put(temp,1);
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
