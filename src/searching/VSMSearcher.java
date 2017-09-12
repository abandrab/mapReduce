package searching;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import helpers.FileHelper;
import helpers.StringHelper;

public class VSMSearcher 
{
	private VSM vsm;
	public VSMSearcher()
	{
		this.vsm = new VSM();
	}
	public Map<String, Double> search(String query)
	{
		List<String> words = StringHelper.splitWords(query, " "); //despartire in cuvinte
		List<String> output = FileHelper.selectWords(words); //eliminare stopwords si aducere la forma canonica
		Map<String, Integer> countedWords = StringHelper.countWords(output); //numarul de aparitii a fiecarui cuvant in query
		
		List<Double> tfIdfQ = vsm.tfIdfQ(countedWords , output.size());// Vectorul tdIdf pt. query
		List<String> finalWords = new ArrayList<>();
		finalWords.addAll(countedWords.keySet());
		Map<String, List<Double>> tfIdfD =  vsm.tfIdfD(finalWords); // Vectorii tfIdf pentru documente
		
		Map<Double, String> cs = new TreeMap<>(Collections.reverseOrder());
		for(Map.Entry<String, List<Double>> pair : tfIdfD.entrySet())//calcul similaritate cosinus pentru fiecare document
		{
			cs.put(vsm.cosineSimilarity(pair.getValue(), tfIdfQ), pair.getKey());
		}
		
		return invert(cs);
	}
	public Map<String, Double> invert(Map<Double, String> map)
	{
		Map<String, Double> invertedMap= new LinkedHashMap<>();
		for(Map.Entry<Double, String> pair : map.entrySet())
		{
			invertedMap.put(pair.getValue(), pair.getKey());
		}
		return invertedMap;
	}
}