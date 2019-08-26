package uk.org.landeg.kalah.game.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Linetest {
	@Test
	public void lineTest() {
		Pattern pattern = Pattern.compile("games\\/\\d+\\/pits\\/(\\d+)");
		
		String line = "http://localhost:8080/games/1/pits/13";
		final Matcher matcher = pattern.matcher(line);
		System.out.println(matcher.find());
		System.out.println(matcher.group(1));
//		System.out.println(line.substring(line.lastIndexOf("/") + 1));
	}
}
