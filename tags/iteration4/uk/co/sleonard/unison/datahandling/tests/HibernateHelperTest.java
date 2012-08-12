package uk.co.sleonard.unison.datahandling.tests;

import junit.framework.TestCase;
import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

public class HibernateHelperTest extends TestCase {

	private HibernateHelper helper;

	@Override
	protected void setUp() throws Exception {
		helper = new HibernateHelper(null);
	}

	@Override
	protected void tearDown() throws Exception {
	}

	public void testData() throws Exception {
		String query = "SELECT  n.fullname, count(*) as total"
				+ " FROM newsgroup n, newsgroup_topic as nt, message m "
				+ " where nt.topic_id = m.topic_id "
				+ " and n.newsgroup_id = nt.newsgroup_id "
				+ " group by n.fullname " + " order by total desc";

		try {
			helper.getListResults(query, NewsGroup.class);

		} catch (Exception e) {
			fail("ERROR" + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("OK");
	}


}
