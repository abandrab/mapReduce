package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

import models.Doc;

public class DbIIEntry extends DbClient//Access la baza de date la colectia invertedindex
{
	public DbIIEntry()
	{
		this.setCollection("invertedindex");
	}
	
	public void add(String id, HashSet<Doc> documents)
	{
		Document document = new Document("_id", id).append("documents", documents);
		this.collection.insertOne(document);
	}
	public void addMany(Map<String, HashSet<Doc>> map)
	{
		List<UpdateOneModel<Document>> documents = new ArrayList<>();
		for (Map.Entry<String, HashSet<Doc>> pair : map.entrySet()) 
		{

			List<Document> docs = new ArrayList<>();
		    Document filterDocument = new Document();
		    filterDocument.append("_id", pair.getKey());

		    Document updateDocument = new Document();
		    Document setDocument = new Document();
		    pair.getValue().forEach(d -> docs.add(new Document("documentId", d.getDocumentId()).append("count", d.getCount())));
		    setDocument.append("documents", docs);
		    updateDocument.append("$set", setDocument);

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
	
	@SuppressWarnings("unchecked")
	public List<Doc> getById(String id)
	{
		BasicDBObject query=new BasicDBObject("_id",id);
		Document document = this.collection.find(query).first();
		if(document != null){
			List<Doc> d = new ArrayList<>();
			List<Document> docs = (List<Document>) document.get("documents");
			for(Document doc : docs)
			{
				d.add(new Doc(doc.getString("documentId"), doc.getInteger("count")));
			}
			return d;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public Map<String, HashSet<Doc>> getAll()
	{
		Map<String, HashSet<Doc>> map = new HashMap<String, HashSet<Doc>>();
		List<Document> documents = this.collection.find().into(
				new ArrayList<Document>());
		for(Document document : documents)
		{
			List<Document> list = (List<Document>)document.get("documents");
			HashSet<Doc> docs = new HashSet<>();
			System.out.println("List " + list);
			list.forEach(doc -> docs.add(new Doc(doc.getString("documentId"), doc.getInteger("count"))));
			map.put(document.getString("_id"), docs);
			
		}
		return map;
	}
}
