package db;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class DbClient
{
	private MongoClient mongoClient;
	protected MongoDatabase database;
	protected MongoCollection<Document> collection;
	public DbClient()
	{
		this.connect();
		this.database = mongoClient.getDatabase("riw");//baza de date
	}
	
	public void connect()
	{
		this.mongoClient = new MongoClient("localhost", 27017);//conectare la baza de date
	}
	public void closeConnection()
	{
		this.mongoClient.close();//inchidere conexiune
	}
	
	protected void setCollection(String collection)//setare colectie cu care se lucreaza
	{
		this.collection = this.database.getCollection(collection);
	}
}
