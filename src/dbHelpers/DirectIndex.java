package dbHelpers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import db.DbDIEntry;
import models.Word;

public class DirectIndex//Helper pentru adaugarea/extragerea datelor din baza de date din colectia directindex
{
	private DbDIEntry dbClient;
	
	public DirectIndex()
	{
		this.dbClient = new DbDIEntry();
	}
	
	public void add(Map<String, HashSet<Word>> map)
	{
		try
		{
			dbClient.connect();
			dbClient.addMany(map);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			dbClient.closeConnection();
		}
	}
	
	public List<Word> getWords(String documentId)
	{
		List<Word> words = null;
		try
		{
			dbClient.connect();
			words = dbClient.getById(documentId);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			dbClient.closeConnection();
		}
		return words;
	}
}

