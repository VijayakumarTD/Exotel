package atu.testng.reports.utils;

import org.davidmoten.text.utils.WordWrap;

import atu.testng.reports.logging.LogAs;

public class Steps {
	private String description;
	private String inputValue;
	private String expectedValue;
	private String actualValue;
	private String time;
	private String lineNum;
	private String screenShot;
	private LogAs logAs;

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String paramString) {
		String wrapped = 
				  WordWrap.from(paramString)
				    .maxWidth(35)
				    .insertHyphens(true) // true is the default
				    .wrap();
		this.description = wrapped;
	}

	public String getInputValue() {
		return this.inputValue;
	}

	public void setInputValue(String paramString) {
		
		String wrapped = 
				  WordWrap.from(paramString)
				    .maxWidth(25)
				    .insertHyphens(true) // true is the default
				    .wrap();
		this.inputValue = wrapped;
	}

	public String getExpectedValue() {
		return this.expectedValue;
	}

	public void setExpectedValue(String paramString) {
		this.expectedValue = paramString;
	}

	public String getActualValue() {
		return this.actualValue;
	}

	public void setActualValue(String paramString) {
		
		String wrapped = 
				  WordWrap.from(paramString)
				    .maxWidth(30)
				    .insertHyphens(true) // true is the default
				    .wrap();
		this.actualValue = wrapped;
	}

	public String getTime() {
		return this.time;
	}

	public void setTime(String paramString) {
		this.time = paramString;
	}

	public String getLineNum() {
		return this.lineNum;
	}

	public void setLineNum(String paramString) {
		this.lineNum = paramString;
	}

	public String getScreenShot() {
		return this.screenShot;
	}

	public void setScreenShot(String paramString) {
		this.screenShot = paramString;
	}

	public LogAs getLogAs() {
		return this.logAs;
	}

	public void setLogAs(LogAs paramLogAs) {
		this.logAs = paramLogAs;
	}
}