package dbHelpers;

import java.util.HashMap;
import java.util.Map;

import db.DbTfEntry;

public class Tf//Helper pentru adaugarea/extragerea datelor din baza de date din colectia tf
{
	private DbTfEntry dbClient;
	
	public Tf()
	{
		this.dbClient = new DbTfEntry();
	}
	
	public void add(Map<String, HashMap<String, Double>> map)
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
	
	public Double getTf(String documentId, String word)
	{
		Double tf = null;
		try
		{
			dbClient.connect();
			tf = dbClient.getTf(documentId, word);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			dbClient.closeConnection();
		}
		if(tf == null) tf = 0.0;
		return tf;
	}
	public Map<String, Double> getAllTfs(String documentId)
	{
		Map<String, Double> tfs = new HashMap<>();
		try
		{
			dbClient.connect();
			tfs = dbClient.getAll(documentId);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			dbClient.closeConnection();
		}
		return tfs;
	}
}
