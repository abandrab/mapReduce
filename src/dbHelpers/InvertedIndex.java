package dbHelpers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import db.DbIIEntry;
import models.Doc;

public class InvertedIndex//Helper pentru adaugarea/extragerea datelor din baza de date din colectia invertedindex
{
	private DbIIEntry dbClient;
	
	public InvertedIndex()
	{
		this.dbClient = new DbIIEntry();
	}
	public void add(Map<String, HashSet<Doc>> map)
	{
		try
		{
			dbClient.connect();
			dbClient.addMany(map);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		finally
		{
			dbClient.closeConnection();
		}
	}
	public Map<String, HashSet<Doc>> getAll()
	{
		Map<String, HashSet<Doc>> map = null;
		try
		{
			dbClient.connect();
			map = dbClient.getAll();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		finally
		{
			dbClient.closeConnection();
		}
		return map;
	}
	public List<Doc> getDocs(String word)
	{
		
		List<Doc> docs = null;
		try
		{
			dbClient.connect();
			docs = dbClient.getById(word);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
		finally
		{
			dbClient.closeConnection();
		}
		return docs;
	}
}
