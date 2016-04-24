package uk.co.sleonard.unison.input.tests;

import java.util.List;

import uk.co.sleonard.unison.input.NewsArticle;

import junit.framework.TestCase;

public class NewsArticleTest extends TestCase {
	public void testGetNewsgroupList() {
		NewsArticle test = new NewsArticle();
		test
				.setReferences("<effrl8$hmn$2@emma.aioe.org> <effupe$hr0$1@netlx020.civ.utwente.nl>");
		List<String> msgs = test.getReferencesList();
		assertEquals(2, msgs.size());
	}
}
