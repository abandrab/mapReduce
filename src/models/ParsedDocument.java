package models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParsedDocument {
	private File file;
	private String title;
	private String keywords;
	private String description;
	private String robots;
	private List<String> links;
	private String bodyContent;
	
	public ParsedDocument(File file){
		this.setFile(file);
	}
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRobots() {
		return robots;
	}
	public void setRobots(String robots) {
		this.robots = robots;
	}
	public List<String> getLinks() {
		return links;
	}
	public void addLink(String link) {
		if(this.links == null)
		{
			this.links = new  ArrayList<String>();
		}
		this.links.add(link);
	}
	public String getBodyContent() {
		return bodyContent;
	}
	public void setBodyContent(String bodyContent) {
		this.bodyContent = bodyContent;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	public String toString(){
		return "Title: " + this.getTitle() + "\nDescription: " + this.getDescription()+"\nKeywords: "
						+ this.getKeywords() + "\nRobots: " + this.getRobots()
						+ "\nBody: " + this.getBodyContent()+ "\nLinks: "+this.showLinks().toString();	
	}
	
	public String showLinks(){
		StringBuilder sb = new StringBuilder();
		if(this.links !=null){
			for(String link : links){
				sb.append(link+", ");
			}
		}
		return sb.toString();
	}
}
