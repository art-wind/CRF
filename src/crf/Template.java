package crf;
//implements Comparable 
public class Template {
//	private char before_2;
//	private char before_1;
//	private char beforeTag;
//	private char currentTag;
//	private char after_1;
//	private char after_2;
	private String rule;
	private String core="";
	private String content="";
	private int type;//State whether it is unigram
	Template(String window,String pattern,int type,char preTag)
	{
//		before_2 = window.charAt(0);
//		before_1 = window.charAt(1);
//		currentTag =window.charAt(2);;
//		after_1 = window.charAt(3);
//		after_2 = window.charAt(4);
		rule= pattern;
		String[]array = rule.split("/");
		for(String str:array)
		{
			int index = Integer.parseInt(str);
			switch(index){
			case -2:
				this.core += window.charAt(0);
				break;
			case -1:
				this.core += window.charAt(1);
				break;
			case 0:
				this.core += window.charAt(2);;
				break;
			case 1:
				this.core += window.charAt(3);
				break;
			case 2:
				this.core += window.charAt(4);
				break;

			}
		}
		if(type==2)
		{
			rule+='C';
			core+="-"+preTag;
			//beforeTag = preTag;
		}
		this.content = core+"|"+rule;
	}
	public String getRule()
	{
		return rule;
	}
//	String generalize(String pattern){
//		String ret = pattern;
//		int begin = ret.indexOf("%x");
//		ret = ret.substring(begin+2);
//		ret = ret.replace(",0]","");
//		ret = ret.replace("[","");
//		ret = ret.replace("%x","");
//		//System.out.println("Ret:  "+ret);
//		return ret;
//	}

	public boolean equals(Object o)
	{
		Template template = (Template)o;
		return (type==template.getType()) && core.equals(template.getCore()) && rule.equals(template.getRule());
	}
	public int hashCode()
	{
		return (rule+core).hashCode();
	}
//	public char getBefore_2() {
//		return before_2;
//	}
//	public void setBefore_2(char before_2) {
//		this.before_2 = before_2;
//	}
//	public char getBefore_1() {
//		return before_1;
//	}
//	public void setBefore_1(char before_1) {
//		this.before_1 = before_1;
//	}
//	public char getBeforeTag() {
//		return beforeTag;
//	}
//	public void setBeforeTag(char beforeTag) {
//		this.beforeTag = beforeTag;
//	}
//	public char getCurrentTag() {
//		return currentTag;
//	}
//	public void setCurrentTag(char currentTag) {
//		this.currentTag = currentTag;
//	}
//	public char getAfter_1() {
//		return after_1;
//	}
//	public void setAfter_1(char after_1) {
//		this.after_1 = after_1;
//	}
//	public char getAfter_2() {
//		return after_2;
//	}
//	public void setAfter_2(char after_2) {
//		this.after_2 = after_2;
////	}
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
	public String getContent()
	{
		return this.content;
	}
}
