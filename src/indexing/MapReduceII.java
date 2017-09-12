package indexing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.Doc;
import models.Word;

public class MapReduceII 
{
	private Map<String, HashSet<Doc>> finalBlock;
	public Map<String, HashSet<Doc>> map(String documentId, HashSet<Word> wordsSet)
	//creare bloc de forma {word1 : {docId, count1}, word2: {docId, count2},..., wordn: {docId, countn}}
	//cate un thread pentru fiecare document si formeaza cate un bloc
	{
		System.out.println(Thread.currentThread());
		Map<String, HashSet<Doc>> block = new HashMap<>();
		for(Word word : wordsSet)
		{
			String w = word.getWord();
			int count = word.getCount();
			HashSet<Doc> documents = new HashSet<Doc>();
			documents.add(new Doc(documentId, count));
			block.put(w, documents);
		}
		return block;
	}
	public void reduce(Map<String, HashSet<Doc>> block)//adaugare fiecare bloc obtinut prin map^ la un ConcurrentHashMap
	//cate un thread pentru fiecare bloc ce trebuie adaugat
	{
		System.out.println(Thread.currentThread());
		if(this.finalBlock == null)
		{
			this.finalBlock = new ConcurrentHashMap<>();
		}
		for(Map.Entry<String, HashSet<Doc>> pair : block.entrySet())
		{
			HashSet<Doc> documents = null;
			if(this.finalBlock.containsKey(pair.getKey()))
			{
				documents = this.finalBlock.get(pair.getKey());
			}
			else
			{
				documents = new HashSet<>();
			}
			documents.addAll(pair.getValue());	
			this.finalBlock.put(pair.getKey(), documents);
		}
	}
	
	public boolean mapReduce(Map<String, HashSet<Word>> directIndex)
	{
		ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		HashSet<Future<Map<String, HashSet<Doc>>>> futures = new HashSet<>();
		for(Map.Entry<String, HashSet<Word>> pair : directIndex.entrySet())
		{
			Future<Map<String, HashSet<Doc>>> future = es.submit(() -> map(pair.getKey(), pair.getValue()));//pentru fiecare 
			//document se creaza cate un thread care se ocupa de faza de map
			futures.add(future);
		}
		for(Future<Map<String, HashSet<Doc>>> future : futures)//fiecare bloc returnat de catre thread-urile de la faza de map
		//este trimis pentru merge printr-un alt thread la faza de reduce
		{
	        try 
	        {
	        	Map<String, HashSet<Doc>> block = future.get();
				es.submit(() -> reduce(block)); 	
	        } 
	        catch (ExecutionException e) 
	        {
				e.printStackTrace();
			} 
	        catch (InterruptedException e) 
	        {
				e.printStackTrace();
			}
	    }
		es.shutdown();
	    while(true)
	    {
	    	if(es.isTerminated())
	 	    {
	 	    	System.out.println("Finished inverted index!");
	 	    	return true;
	 	    }
	    }
	}
	public void sort()
	{
		Map<String, HashSet<Doc>> map = new TreeMap<>();
		for(Map.Entry<String, HashSet<Doc>> pair : finalBlock.entrySet())
		{
			map.put(pair.getKey(), pair.getValue());
		}
		finalBlock = map;
	}
	public Map<String, HashSet<Doc>> getFinalBlock()
	{
		return finalBlock;
	}
}

