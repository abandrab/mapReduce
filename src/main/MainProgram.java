package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import dbHelpers.Mapper;
import indexing.Indexer;
import searching.VSMSearcher;

public class MainProgram 
{
	public static void main(String[] args) 
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Introduceti path-ul directorului cu fisiere:");
		String path = null;
		try 
		{
			path = br.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if(path != null && !path.isEmpty())
		{
			Indexer indexer =  new Indexer();
			indexer.index(path);//indexare
		}
		System.out.println("Cautati:");
		String query = null;
		try 
		{
			query = br.readLine();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if(query != null && !query.isEmpty())
		{
			VSMSearcher vsms= new VSMSearcher();
			Map<String, Double> cs = vsms.search(query);//cautare
			Mapper m = new Mapper();
			for(Map.Entry<String, Double> pair : cs.entrySet())
			{
				String p = m.getById(pair.getKey());
				System.out.println("Document id: " + pair.getKey() + " Cosine Similarity: " + pair.getValue() + " Path: " + p);
			}
			
		}
		
	}
}


