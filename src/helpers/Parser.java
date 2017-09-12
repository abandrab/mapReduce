package helpers;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import models.ParsedDocument;

public class Parser 
{	
	public ParsedDocument parse(File file) //parsare fisier
	{
		ParsedDocument parsedDocument  =  new ParsedDocument(file);
		if(parsedDocument.getFile()!=null)
		{
			try
			{
					Document document =  Jsoup.parse(file, "UTF-8");
					if(document.title() != null)
					{
						parsedDocument.setTitle(document.title());
					}
					Elements metaTags = document.getElementsByTag("meta");
					for (Element metaTag : metaTags)
					{
						String content = metaTag.attr("content");
						String name = metaTag.attr("name");
						if("keywords".equals(name))
						{
							parsedDocument.setKeywords(content);
						}
						if("description".equals(name))
						{
							parsedDocument.setDescription(content);
						}
						if("robots".equals(name))
						{
							parsedDocument.setRobots(content);
						}
					}
					Elements links = document.select("a");
					
					for (Element link : links)
					{
						if (link.attr("href").contains("http") || link.attr("href").contains("https"))
		            	{
		            		parsedDocument.addLink(link.attr("abs:href"));
		            	}
					}
					parsedDocument.setBodyContent(document.body().text());	
			}
			catch(IOException ex)
			{
				System.out.println("Parser exception: " + ex.getMessage());
			}
		}
		return parsedDocument;
	}
}