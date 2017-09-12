package helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringHelper 
{
	public static Map<String, Integer> countWords(List<String> words) // numara cuvintele intr-o lista
	{
		Map<String, Integer> map = new HashMap<>();		
		for(String word : words)
		{
			if (map.containsKey(word))
			{
				map.put(word, map.get(word) + 1);
			}
			else
			{
				map.put(word, 1);
			}
		}
		return map;
	}
	public static List<String> splitWords(String s, String splitter) //aducere text la forma lowercase si despartirea cuvintelor dupa splitter
	{
		StringBuilder sb = new StringBuilder(s);
		StringHelper.clean(sb);
		StringHelper.toLower(sb);
		List<String> result = new ArrayList<>();
		int startSubstring = 0;
		int position = sb.indexOf(splitter);
		while(startSubstring <= position)
		{
			if(startSubstring < position)
			{
				result.add(sb.substring(startSubstring, position));
			}
			startSubstring = position + 1;
			position = sb.indexOf(splitter, startSubstring);
		}
		if(startSubstring < sb.length())
		{
			result.add(sb.substring(startSubstring));
		}
		return result;
	}
	public static void clean(StringBuilder sb)//inlocuieste caracterele care nu sunt alfa numerice cu un spatiu
	{
		char[] charArray = new char[] {',', '.', ':', ';', '?', '!', '\'', '~', '!', 
				'@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '+', '=', '\\', '/', '<', '>', '\t', '\r', '\n', '\"',
				'[', ']', '}', '{'};		
		List<Character> list = new ArrayList<Character>();
		for (char c : charArray) {
			  list.add(c);
		}
		for(int i = 0; i < sb.length(); i++)
		{
			if(list.contains(sb.charAt(i)))
			{
				sb.setCharAt(i, ' ');
			}
		}
	}
	public static void toLower(StringBuilder sb) //transforma textul in lowercase
	{
		for(int i = 0; i < sb.length(); i++)
		{
			char ch = sb.charAt(i);
			if(ch >= 'A' && ch <= 'Z')
			{
				ch = (char)(ch + ('a'-'A'));
				sb.setCharAt(i, ch);
			}
		}
	}
	public static String toCanonicForm(String word) //aducere la forma canonica
	{
	    Stemmer s = new Stemmer();
	    char[] letters = word.toCharArray();
	    for (int i = 0; i < letters.length; i++)
	    {
	    	s.add(letters[i]);
	    }
	    s.stem();
		String u;
		u = s.toString();
		return u;
	 }	
}
