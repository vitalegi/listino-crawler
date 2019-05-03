package it.vitalegi.listinocrawler;

import java.util.List;
import java.util.function.Predicate;

public class IsNotEmptyArray implements Predicate<List<String>> {

	public boolean test(List<String> input) {
		if (input == null) {
			return false;
		}
		for (String str : input) {
			if (str != null && str.trim().length() > 0) {
				return true;
			}
		}
		return false;
	}

}
