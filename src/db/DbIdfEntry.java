package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

public class DbIdfEntry extends DbClient//Access la baza de date la colectia idf
{
	public DbIdfEntry()
	{
		this.setCollection("idf");
	}
	
	public void addMany(Map<String, Double> map)
	{
		List<UpdateOneModel<Document>> documents = new ArrayList<>();
		for (Map.Entry<String, Double> pair : map.entrySet()) 
		{
		    Document filterDocument = new Document();
		    filterDocument.append("_id", pair.getKey());

		    Document updateDocument = new Document();
		    updateDocument.append("$set",  new Document("idf", pair.getValue()));

		    UpdateOptions updateOptions = new UpdateOptions();
		    updateOptions.upsert(true); 
		    documents.add(
		            new UpdateOneModel<Document>(
		                    filterDocument,
		                    updateDocument,
		                    updateOptions));

		}
		if(!documents.isEmpty())
		{
			this.collection.bulkWrite(documents);
		}	
	}
	public double getIdf(String term)
	{
		FindIterable<Document> docs = this.collection.find(new Document("_id", term));
		Document doc =  docs.first();
		return doc.getDouble("idf");
	}
	public Map<String, Double> getAll()
	{
		Map<String, Double> map = new HashMap<>();
		HashSet<Document> documents = this.collection.find().into(
				new HashSet<Document>());
		for(Document document : documents)
		{
			map.put(document.getString("_id"), document.getDouble("idf"));
		}
		return map;
	}
}
