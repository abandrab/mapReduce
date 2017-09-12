package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Filters;

import models.Word;

public class DbDIEntry extends DbClient//Access la baza de date la colectia directindex
{
	public DbDIEntry()
	{
		this.setCollection("directindex");
	}
	
	public void add(String id, HashSet<Word> words)
	{
		if(this.exists(new ObjectId(id)))
		{
			return;
		}
		Document document = new Document("_id", id).append("words", words).append("count", words.size());
		this.collection.insertOne(document);
	}
	public void addMany(Map<String, HashSet<Word>> map)
	{
		List<Document> documents = new ArrayList<Document>();
		for (Map.Entry<String, HashSet<Word>> pair : map.entrySet())
		{
			String id = pair.getKey();
			if(this.exists(new ObjectId(id)))
			{
				map.remove(id);
			}
		}
		if(!map.isEmpty())
		{
			for (Map.Entry<String, HashSet<Word>> pair : map.entrySet())
			{
				List<Document> wordsSet = new ArrayList<>();
				pair.getValue().forEach(d -> wordsSet.add(new Document("word", d.getWord()).append("count", d.getCount())));
				documents.add(new Document("_id", new ObjectId(pair.getKey()))
						.append("words", wordsSet));
			}
			
		}
		
		if(!documents.isEmpty())
		{
			this.collection.insertMany(documents);
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<Word> getById(String id)
	{
		BasicDBObject query=new BasicDBObject("_id",new ObjectId(id));
		Document document = (Document) this.collection.find(query).first();
		List<Word> w = new ArrayList<>();
		List<Document> docs= (List<Document>)document.get("words");
		for(Document d : docs)
		{
			w.add(new Word(d.getString("word"), d.getInteger("count")));
		}
		return w;
	}
	@SuppressWarnings("unchecked")
	public Map<String, HashSet<Word>> getAll()
	{
		Map<String, HashSet<Word>> map = new HashMap<String, HashSet<Word>>();
		HashSet<Document> documents = (HashSet<Document>) this.collection.find().into(
				new HashSet<Document>());
		for(Document document : documents)
		{
			map.put(document.getString("_id"), (HashSet<Word>)document.get("words"));
		}
		return map;
	}
	public boolean exists(ObjectId id) 
	{
		long rows = this.collection.count(Filters.and(Filters.eq("_id",id))) ;
		return rows>0;
	}
}
