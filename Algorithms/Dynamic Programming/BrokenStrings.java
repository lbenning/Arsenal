import java.util.*;

/**
* Dynamic program for separating a string into words from a predefined
* set.
*/ 
public class BrokenStrings {

	private BrokenStrings() {}

	public static void main(String[] args) {
		HashSet<String> s = new HashSet<String>();
		s.add("frodo");
		s.add("baggins");
		s.add("of");
		s.add("the");
		s.add("shire");
		String test = "shiretheofbagginsfrodo";
		System.out.println(separate(test,s));
	}


	public static List<String> separate(String i, HashSet<String> dict) {
		if (i == null || i.length() == 0 || dict == null) {
			return Collections.emptyList();
		}

		int[] words = new int[i.length()];
		boolean[] memo = new boolean[i.length()+1];
		memo[0] = true;

		for (int x = 1; x <= i.length(); x++) {
			boolean achieved = false;
			for (int y = x; y >= 1; y--) {
				if (dict.contains(i.substring(y-1,x)) && memo[y-1]) {
					achieved = true;
					words[x-1] = y-1;
					break;
				}
			}
			memo[x] = achieved;
		}

		if (memo[memo.length-1] == false) {
			return Collections.emptyList();
		}

		List<String> l = new ArrayList<String>();
		int x = words.length-1;
		
		while (x > 0) {
			l.add(i.substring(words[x],x+1));
			x = words[x]-1;
		}
		return l;

	}

}
