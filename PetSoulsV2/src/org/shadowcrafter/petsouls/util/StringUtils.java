package org.shadowcrafter.petsouls.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static List<String> asList(String... str){
		List<String> output = new ArrayList<>();
		for (String i : str) {
			output.add(i);
		}
		return output;
	}
	
	public static String replaceLast(String input, String regex, String replacement) {
		String str = new StringBuilder(input).reverse().toString();
		str = str.replaceFirst(regex, replacement);
		return new StringBuilder(str).reverse().toString();
	}
	
	public static String firstUpperCase(String input) {
		char[] chars = input.toCharArray();
		
		chars [0] = Character.toUpperCase(chars[0]);
		
		return String.valueOf(chars);
	}
	
}
