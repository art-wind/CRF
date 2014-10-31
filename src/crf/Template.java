package crf;

public class Template implements Comparable {
	private char before_2;
	private char before_1;
	private char beforeTag;
	private char currentTag;
	private char after_1;
	private char after_2;
	private String rule;
	private String core;
	private int type;//State whether it is unigram
	Template(String window,String pattern,char tag,int type)
	{
		before_2 = window.charAt(0);
		before_1 = window.charAt(1);
		currentTag = tag;
		after_1 = window.charAt(3);
		after_2 = window.charAt(4);
		rule = generalize(pattern);
		String[]array = rule.split("/");
		for(String str:array)
		{
			int index = Integer.parseInt(str);
			System.out.println("i"+index);
			switch(index){
			case -2:
					setCore(getCore() + before_2);
				break;
			case -1:
				setCore(getCore() + before_1);
				break;
			case 0:
				setCore(getCore() + currentTag);
				break;
			case 1:
				setCore(getCore() + after_1);
				break;
			case 2:
				setCore(getCore() + after_2);
				break;
				
			}
		}
		if(type==2)
		{
			//rule+="|";
		}
	}
	public String getRule()
	{
		return rule;
	}
	String generalize(String pattern){
		String ret = pattern;
		int begin = ret.indexOf("%x");
		ret = ret.substring(begin+2);
		ret = ret.replace(",0]","");
		ret = ret.replace("[","");
		ret = ret.replace("%x","");

		return ret;
	}
	@Override
	public int compareTo(Object temp) {
		String otherRule = ((Template)temp).getRule();
		
		if(rule.equals(otherRule))
		{
			String[]array = rule.split("/");
			for(String str:array)
			{
				int index = Integer.parseInt(str);
				System.out.println("i"+index);
				switch(index){
				case -2:
					if(before_2 != ((Template)temp).getBefore_2()){
						return 0;
					}
					break;
				case -1:
					if(before_1 != ((Template)temp).getBefore_1()){
						return 0;
					}
					break;
				case 0:
					if(currentTag!= ((Template)temp).getCurrentTag()){
						return 0;
					}
					break;
				case 1:
					if(after_1!= ((Template)temp).getAfter_1()){
						return 0;
					}
					break;
				case 2:
					if(after_2!= ((Template)temp).getAfter_2()){
						return 0;
					}
					break;
					
				}
			}
			return 1;
		}
		else{
			return -1;
		}
		// TODO Auto-generated method stub
		
	}
	public char getBefore_2() {
		return before_2;
	}
	public void setBefore_2(char before_2) {
		this.before_2 = before_2;
	}
	public char getBefore_1() {
		return before_1;
	}
	public void setBefore_1(char before_1) {
		this.before_1 = before_1;
	}
	public char getBeforeTag() {
		return beforeTag;
	}
	public void setBeforeTag(char beforeTag) {
		this.beforeTag = beforeTag;
	}
	public char getCurrentTag() {
		return currentTag;
	}
	public void setCurrentTag(char currentTag) {
		this.currentTag = currentTag;
	}
	public char getAfter_1() {
		return after_1;
	}
	public void setAfter_1(char after_1) {
		this.after_1 = after_1;
	}
	public char getAfter_2() {
		return after_2;
	}
	public void setAfter_2(char after_2) {
		this.after_2 = after_2;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getCore() {
		return core;
	}
	public void setCore(String core) {
		this.core = core;
	}
}
