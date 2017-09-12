package indexing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import helpers.FileHelper;
import helpers.StringHelper;
import models.Word;

public class MapReduceDI
{
	private Map<String, HashSet<Word>> finalBlock;
	public Map<String, HashSet<Word>> map(String documentId, String path) //creare bloc de forma {docId: {word1, count1},.., {wordn, countn}}
	{//cate un thread pentru fiecare document si formeaza cate un bloc
		System.out.println(Thread.currentThread());
		List<String> words = FileHelper.parseFile(path);
		Map<String, Integer> counter = StringHelper.countWords(words);
		Map<String, HashSet<Word>> block = new HashMap<>();
		
		HashSet<Word> wordsSet = new HashSet<>();
		counter.forEach((key, value) -> wordsSet.add(new Word(key, value)));
		block.put(documentId, wordsSet);		
		return block;
	}
	public Object reduce(Map<String, HashSet<Word>> block) //adaugare fiecare bloc obtinut prin map^ la un ConcurrentHashMap
	{//cate un thread pentru fiecare bloc ce trebuie adaugat
		System.out.println(Thread.currentThread());
		if(this.finalBlock == null)
		{
			this.finalBlock = new ConcurrentHashMap<>();
		}
		for(Map.Entry<String, HashSet<Word>> pair : block.entrySet())
		{
			HashSet<Word> wordsSet = null;
			if(this.finalBlock.containsKey(pair.getKey()))
			{
				wordsSet = this.finalBlock.get(pair.getKey());
			}
			else
			{
				wordsSet = new HashSet<>();
			}
			wordsSet.addAll(pair.getValue());
			this.finalBlock.put(pair.getKey(), wordsSet);
		}
		return null;
	}
	
	public boolean mapReduce(Map<String, String> map)
	{
		ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		HashSet<Future<Map<String, HashSet<Word>>>> futures = new HashSet<>();
		for(Map.Entry<String, String> pair : map.entrySet())
		{
			Future<Map<String, HashSet<Word>>> future = es.submit(() -> map(pair.getKey(), pair.getValue()));//pentru fiecare 
			//document se creaza cate un thread care se ocupa de faza de map
			futures.add(future);
		}
		for(Future<Map<String, HashSet<Word>>> future : futures)//fiecare bloc returnat de catre thread-urile de la faza de map
		//este trimis pentru merge printr-un alt thread la faza de reduce
		{
	        try
	        {
	        	Map<String, HashSet<Word>> block = future.get();
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
	 	    	System.out.println("Finished direct index!");
	 	    	return true;
	 	    }
	    }
	}
	public Map<String, HashSet<Word>> getFinalBlock()//preluare rezultat
	{
		return finalBlock;
	}
}

