package crf;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CRFReader {
	//private String charArray;
	private String passage;
	private char[]tagArray;
	private char[]labelArray;
	private String[]templateUArray;
	private String[]templateBArray;
	CRFReader(String charPath,String labelPath,String templatePath){
		String tags="P",chars="NN";
		File set = new File(charPath);
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(set);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(fileInput.hasNext())
		{
			chars+=fileInput.next();
			tags+=fileInput.next();
		}
		//this.charArray = chars.toCharArray();
		this.passage = (chars+"NN");
		System.out.println("P: "+this.passage.length());
		this.tagArray = (tags+'P').toCharArray();
		System.out.println("T: "+this.tagArray.length);
		
		set = new File(labelPath);
		try {
			fileInput = new Scanner(set);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String labels = "";
		while(fileInput.hasNext())
		{
			labels+=fileInput.next();
		}
		this.labelArray = labels.toCharArray();
		
		set = new File(templatePath);
		try {
			fileInput = new Scanner(set);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String templateU = "";
		String templateB = "";
		int flag=0;
		while(fileInput.hasNext())
		{
			String tmp =fileInput.nextLine();
			if(tmp.contains("gram"))
				flag++;
			else
			{
				if(flag==1)
				{
					if(!tmp.isEmpty())
						templateU += generalize(tmp.trim())+"|";
				}
				else{
					templateB += generalize(tmp.trim())+'|';
				}
			}
			
		}
		//System.out.println(templateU);
		
		this.templateUArray = templateU.split("\\|");
		this.templateBArray = templateB.split("\\|");
		for(String template:this.templateUArray)
		{
			System.out.println(template);
		}
		
		/*
		for(String template:this.templateUArray)
		{
			template = generalize(template);
		}
		
		for(String template:this.templateUArray)
		{
			template = generalize(template);
		}*/
	}
	String generalize(String pattern){
		String ret = pattern;
		int begin = ret.indexOf("%x");
		ret = ret.substring(begin+2);
		ret = ret.replace(",0]","");
		ret = ret.replace("[","");
		ret = ret.replace("%x","");
		//System.out.println("Ret:  "+ret);
		return ret;
	}
	
	public char getCharPassage(int index) {
		return passage.charAt(index);
	}
	public String getPassage() {
		return passage;
	}


	public char[] getTagArray() {
		return tagArray;
	}


	public char[] getLabelArray() {
		return labelArray;
	}


	public String[] getTemplateUArray() {
		return templateUArray;
	}


	public String[] getTemplateBArray() {
		return templateBArray;
	}


	public char getTagArray(int index) {
		return tagArray[index];
	}
	public char getLabelArray(int index) {
		return labelArray[index];
	}
	public String getTemplateUArray(int index) {
		return templateUArray[index];
	}
	public String getTemplateBArray(int index) {
		return templateBArray[index];
	}

}
