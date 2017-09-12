package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.ParsedDocument;

public class FileHelper 
{
	public static String getFileContent(String path) //returneaza tot continutul unui fisier
	{
		File file = new File(path);
		byte[] data;
		String content;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			content = new String(data, "UTF-8");
			return content;
		}
		catch(IOException ex)
		{
			System.out.println(ex.getMessage());
		}
		return null;
	}
	public static List<String> parseFile(String path) //parsare fisier
	{
		List<String> output = new ArrayList<String>();
		Parser parser = new Parser();
		ParsedDocument pd= parser.parse(new File(path));
		String text = pd.getBodyContent();
		if(text!=null)
		{			
			List<String> words = StringHelper.splitWords(text, " ");
			output = selectWords(words);
		}
		return output;
	}
	public static List<String> selectWords(List<String> words) //selectare cuvinte care nu fac parte din stopwords,
	//aducere la forma canonica (daca nu e exceptie) si returnare lista cuvinte
	{
		List<String> stopWords = StringHelper.splitWords(FileHelper.getFileContent("stopWords.txt"), " ");
		List<String> exceptions = StringHelper.splitWords(FileHelper.getFileContent("exceptions.txt"), " ");
		List<String> output = new ArrayList<String>();
		for(String word : words)
		{
			if(!stopWords.contains(word))
			{
				if(!exceptions.contains(word))
				{
					word = StringHelper.toCanonicForm(word);
				}
				output.add(word);
			}
		}
		return output;
	}
}
