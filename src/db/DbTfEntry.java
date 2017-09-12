package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

public class DbTfEntry extends DbClient//Access la baza de date la colectia tf
{
	public DbTfEntry()
	{
		this.setCollection("tf");
	}
	public void addMany(Map<String, HashMap<String, Double>> map)
	{
		List<UpdateOneModel<Document>> documents = new ArrayList<>();
		for (Map.Entry<String, HashMap<String, Double>> pair : map.entrySet()) 
		{
		    Document filterDocument = new Document();
		    filterDocument.append("_id", pair.getKey());

		    Document updateDocument = new Document();
		    List<Document> list = new ArrayList<>();
		    for(Map.Entry<String, Double> p : pair.getValue().entrySet())
		    {
		    	list.add(new Document("word", p.getKey()).append("tf", p.getValue()));
		    }
		    updateDocument.append("$set",  new Document("words", list));

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
	public double getTf(String documentId, String term)
	{
		FindIterable<Document> documents = this.collection.find(new Document("_id", documentId));
		Document document =  documents.first();
		if(document != null)
		{
			List<Document> words = (List<Document>) document.get("words");
			for(Document w : words)
			{
				if(w.getString("word").equals(term))
					return w.getDouble("tf");
			}
		}
		return 0.0;
	}
	public Map<String, Double> getAll(String documentId)
	{
		FindIterable<Document> documents = this.collection.find(new Document("_id", documentId));
		Document document =  documents.first();
		if(document != null)
		{
			Map<String, Double> map = new HashMap<>();
			List<Document> words = (List<Document>) document.get("words");
			for(Document w : words)
			{
				map.put(w.getString("word"), w.getDouble("tf"));
			}
			return map;
		}
		return null;
	}
}
