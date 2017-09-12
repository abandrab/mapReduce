package indexing;

import java.util.HashSet;
import java.util.Map;

import dbHelpers.DirectIndex;
import dbHelpers.InvertedIndex;
import dbHelpers.Mapper;
import models.Doc;
import models.Word;
import searching.VSM;

public class Indexer 
{	
	private InvertedIndex invertedIndex;
	private DirectIndex directIndex;
	private VSM vsm;
	
	public Indexer()
	{
		this.invertedIndex =  new InvertedIndex();
		this.directIndex = new DirectIndex();
		this.vsm = new VSM();
	}
	public void index(String path)
	{
		Mapper mapper  = new Mapper();
		mapper.map(path, new HashSet<String>());//creare document de forma id - path pentru fiecre fisier 
		//din folderul introdus de catre utilizator
		Map<String, String> map = mapper.getMap();
		int totalNumDocs = map.size();
		
		MapReduceDI mrDirectIndex= new MapReduceDI();
		if(mrDirectIndex.mapReduce(map))//creare index direct
		{
			Map<String, HashSet<Word>> di = mrDirectIndex.getFinalBlock();
			MapReduceII mrInvertedIndex = new MapReduceII();
			if(mrInvertedIndex.mapReduce(di))//creare index indirect pe baza indexului direct
			{
				Map<String, HashSet<Doc>> ii = mrInvertedIndex.getFinalBlock();
				if(ii != null)
				{
					invertedIndex.add(ii);//adaugare index indirect in baza de date
					vsm.idf(ii, totalNumDocs);//calculare idf si adaugare in baza de date
				}
			}
			if(di != null)
			{
				vsm.tf(di);//calculare tf si adaugare in baza de date
				directIndex.add(di);//adaugare index direct in baza de date
			}
		}	
	}
}
