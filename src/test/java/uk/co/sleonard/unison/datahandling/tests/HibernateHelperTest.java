package uk.co.sleonard.unison.datahandling.tests;

import static org.junit.Assert.fail;

import org.hibernate.Session;
import org.junit.Before;

import uk.co.sleonard.unison.datahandling.HibernateHelper;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

public class HibernateHelperTest {

	private HibernateHelper helper;

	@Before
	public void setUp() throws Exception {
		helper = new HibernateHelper(null);
	}

	public void testData() throws Exception {
		Session session = null; //TODO mock this and add expected return
		String query = "SELECT  n.fullname, count(*) as total"
				+ " FROM newsgroup n, newsgroup_topic as nt, message m "
				+ " where nt.topic_id = m.topic_id "
				+ " and n.newsgroup_id = nt.newsgroup_id "
				+ " group by n.fullname " + " order by total desc";

		try {
			helper.getListResults(query, NewsGroup.class, session);

		} catch (Exception e) {
			fail("ERROR" + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("OK");
	}


}
