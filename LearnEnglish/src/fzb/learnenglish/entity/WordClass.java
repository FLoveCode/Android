package fzb.learnenglish.entity;
/*
 * µ•¥  µÃÂ
 */
public class WordClass {

	private String word;
	private String explain;
	private String change;
	private String pharse;
	private String sentence;
	
	public WordClass() {
	}
	
	public WordClass(String word){
		this.word=word;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getPharse() {
		return pharse;
	}

	public void setPharse(String pharse) {
		this.pharse = pharse;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	
	
	
}
