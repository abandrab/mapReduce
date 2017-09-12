package dbHelpers;

import java.util.Map;

import db.DbIdfEntry;

public class Idf//Helper pentru adaugarea/extragerea datelor din baza de date din colectia idf
{
	private DbIdfEntry dbClient;
	
	public Idf()
	{
		this.dbClient = new DbIdfEntry();
	}
	
	public void add(Map<String, Double> map)
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
	
	public Double getIdf(String word)
	{
		Double idf = null;
		try
		{
			dbClient.connect();
			idf = dbClient.getIdf(word);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			dbClient.closeConnection();
		}
		if(idf == null) idf = 0.0;
		return idf;
	}
	public Map<String, Double> getAll()
	{
		Map<String, Double> idfs = null;
		try
		{
			dbClient.connect();
			idfs = dbClient.getAll();
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			dbClient.closeConnection();
		}
		return idfs;
	}
}
