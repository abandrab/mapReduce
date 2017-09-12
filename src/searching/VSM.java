package searching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import dbHelpers.Idf;
import dbHelpers.InvertedIndex;
import dbHelpers.Tf;
import models.Doc;
import models.Word;

public class VSM 
{
	private Idf idf;
	private Tf tf;
	private InvertedIndex invertedIndex;
	private Map<String, Double> idfs;
	private Set<String> words;
	
	public VSM()
	{
		this.idf = new Idf();
		this.tf = new Tf();
		this.invertedIndex = new InvertedIndex();
		this.idfs = idf.getAll();
		this.words = idfs.keySet();
	}
	
	public int numOfWords(HashSet<Word> wordSet)
	{
		int sum = 0;
		for(Word w : wordSet)
		{
			sum += w.getCount();
		}
		return sum;
	}
	public void tf(Map<String, HashSet<Word>> di)//calculare tf pentru fiecare cuvant din fiecare document si adaugare in baza de date
	{
		Map<String, HashMap<String, Double>> tfs = new HashMap<>();
		for(Map.Entry<String, HashSet<Word>> pair : di.entrySet())
		{
			HashMap<String, Double> m = new HashMap<>();
			
			for(Word w : pair.getValue())
			{
				double tfrez = w.getCount() / (1.0 * numOfWords(pair.getValue()));
				m.put(w.getWord(), tfrez);
			}
			tfs.put(pair.getKey(), m);
		}
		tf.add(tfs);
	}
	public void idf(Map<String, HashSet<Doc>> map, int totalNumDocs)//calculare idf si adaugare in baza de date
	{
		Map<String, Double> idfMap = new TreeMap<>();
		for(Map.Entry<String, HashSet<Doc>> pair: map.entrySet())
		{
			double numDocs = pair.getValue().size();
			double idf = Math.log(totalNumDocs * (1.0 /(numDocs)));
			idfMap.put(pair.getKey(), idf);
		}
		idf.add(idfMap);
	}
	
	public List<Double> tfIdfQ(Map<String, Integer> map, int numWords) //calculare vector q: {key: tf(key, q) * idf(key)}
	{
		List<Double> tfIdfs = new ArrayList<>();
		for(String word : words)
		{
			double tfVal = 0.0;
			double idfVal = 0.0;
			if(map.containsKey(word))
			{
				tfVal = map.get(word) * (1.0 / numWords);
				idfVal = idfs.get(word);
			}
			tfIdfs.add(tfVal * idfVal);
		}
		return tfIdfs;
	}
	public List<Double> tfIdfDoc(String documentId)//calculare vector tfIdf pentru un singur document d: {key: tf(key, d) * idf(key)}
	{
		List<Double> tfIdfs = new ArrayList<>();	
		for(String word : words)
		{
			double tfVal = tf.getTf(documentId, word);
			double idfVal = idfs.get(word);
			tfIdfs.add(tfVal * idfVal);
		}
		return tfIdfs;
	}
	public Map<String, List<Double>> tfIdfD(List<String> queryWords) //calculare toti vectorii pentru documente
	{
		Map<String, List<Double>> tfIdfD = new HashMap<>();	
		HashSet<String> documentIds = getDocIds(queryWords);
		for(String documentId : documentIds)
		{
			tfIdfD.put(documentId, tfIdfDoc(documentId));
		}
		return tfIdfD;
	}
	
	public HashSet<String> getDocIds(List<String> queryWords) //reuniunea tuturor documentelor care contin cel putin unul 
	//din termenii cautati (din query)
	{
		HashSet<Doc> documents= new HashSet<>();
		for(String term : queryWords)
		{
			List<Doc> docs = this.invertedIndex.getDocs(term);
			if(docs != null) documents.addAll(docs);
		}
		HashSet<String> documentIds = new HashSet<>();
		for(Doc doc : documents)
		{
			documentIds.add(doc.getDocumentId());
		}
		return documentIds;
	}
	public Double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) //calculare similaritate cosinus
	{
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.size(); i++) {
	        dotProduct += vectorA.get(i) * vectorB.get(i);
	        normA += Math.pow(vectorA.get(i), 2);
	        normB += Math.pow(vectorB.get(i), 2);
	    }   
	    if(normA != 0 && normB != 0)
	    {
	    	return (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
	    }
	    return 0.0;
	}
}
