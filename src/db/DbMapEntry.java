package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;

public class DbMapEntry extends DbClient//Access la baza de date la colectia mapper
{
	public DbMapEntry()
	{
		this.setCollection("mapper");
	}
	
	public void add(String path)
	{
		if(!existsPath(path))
		{
			Document document = new Document("path", path);
			this.collection.insertOne(document);
		}
	}
	public void addMany(HashSet<String> paths)
	{
		List<Document> documents = new ArrayList<Document>();
		for(String path : paths)
		{
			if(existsPath(path))
			{
				continue;
			}
			documents.add(new Document("path", path));
		}
		this.collection.insertMany(documents);	
	}	
	public String getById(String id)
	{
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(id));
		Document document = this.collection.find(query).first();
		return document.getString("path");
	}
	public Map<String, String> getAll()
	{
		Map<String, String> map = new HashMap<String, String>();
		HashSet<Document> documents = this.collection.find().into(
				new HashSet<Document>());
		for(Document document : documents)
		{
			map.put(document.getObjectId("_id").toString(), document.getString("path"));
		}
		return map;
	}
	public boolean existsPath(String path) 
	{
	    FindIterable<Document> iterable = this.collection.find(new Document("path", path));
	    return iterable.first() != null;
	}
	public HashSet<String> getIds()
	{
		HashSet<String> ids = new HashSet<String>();
		HashSet<Document> documents = this.collection.find().into(
				new HashSet<Document>());
		for(Document document : documents)
		{
			ids.add(document.getObjectId("_id").toString());
		}
		return ids;
	}
}


