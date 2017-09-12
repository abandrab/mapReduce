package dbHelpers;

import java.io.File;
import java.util.HashSet;
import java.util.Map;

import db.DbMapEntry;

public class Mapper//Helper pentru adaugarea/extragerea datelor din baza de date din colectia mapper
{
	private DbMapEntry dbClient;
	public Mapper()
	{
		this.dbClient = new DbMapEntry();
	}
	public void map(String path, HashSet<String> paths)
	{
		File directory = new File(path);
		File[] fileList = directory.listFiles();
	    for (File file : fileList)
	    {
	        if (file.isFile())
	        {
	        	paths.add(file.getAbsolutePath());
	        } 
	        else if (file.isDirectory())
	        {
	            map(file.getAbsolutePath(), paths);
	        }
	    }
	    this.add(paths);	    
	}
	public void add(HashSet<String> paths)
	{
		try
	    {
	    	this.dbClient.connect();
	    	this.dbClient.addMany(paths);
	    }
	    catch(Exception ex)
	    {
	    	System.out.println(ex.getMessage());
	    }
	    finally
	    {
	    	this.dbClient.closeConnection();
	    }
	}
	public Map<String, String> getMap()
	{
		Map<String, String> map = null;
		try
		{
			this.dbClient.connect();
			map = this.dbClient.getAll();			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			this.dbClient.closeConnection();
		}
		return map;
	}
	public HashSet<String> getIds()
	{
		HashSet<String> ids = null;
		try
		{
			this.dbClient.connect();
			ids = this.dbClient.getIds();			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			this.dbClient.closeConnection();
		}
		return ids;
	}
	public String getById(String id)
	{
		String path = null;
		try
		{
			this.dbClient.connect();
			path = this.dbClient.getById(id);			
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			this.dbClient.closeConnection();
		}
		return path;
	}
}
