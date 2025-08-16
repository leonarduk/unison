/**
 * UNISoNCAnalysis
 *
 * @author ${author}
 * @since 30-May-2016
 */
package uk.co.sleonard.unison;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import uk.co.sleonard.unison.datahandling.DAO.*;
import uk.co.sleonard.unison.datahandling.HibernateHelper;

import java.util.*;
import java.util.Map.Entry;


@Slf4j
public class UNISoNAnalysis {
    private final NewsGroupFilter filter;
    private final Session session;
    private final HibernateHelper helper;

    UNISoNAnalysis(final NewsGroupFilter filter, final Session session,
                   final HibernateHelper helper) {
        this.filter = filter;
        this.session = session;
        this.helper = helper;
    }

    /**
     * Gets the top countries list.
     *
     * @return the top countries list
     */
    public List<ResultRow> getTopCountriesList() {
        List<ResultRow> results = null;
        final HashMap<String, Integer> summaryMap = new HashMap<>();

        for (final ListIterator<Message> iter = this.filter.getMessagesFilter().listIterator(); iter
                .hasNext(); ) {
            final Message nextMessage = iter.next();

            String nextCountry;
            if ((null != nextMessage.getPoster()) && (null != nextMessage.getPoster().getLocation())
                    && (null != nextMessage.getPoster().getLocation().getCountry())) {
                nextCountry = nextMessage.getPoster().getLocation().getCountry();
            } else {
                nextCountry = "UNKNOWN";
            }

            Integer count = summaryMap.get(nextCountry);
            if (null == count) {
                count = Integer.valueOf(0);
            }
            summaryMap.put(nextCountry, Integer.valueOf(count.intValue() + 1));
        }
        results = new Vector<>();
        for (final Entry<String, Integer> entry : summaryMap.entrySet()) {
            results.add(new ResultRow(entry.getKey(), entry.getValue().intValue(), Location.class));
        }
        Collections.sort(results);
        return results;
    }

    /**
     * Gets the top groups list.
     *
     * @return the top groups list
     */
    public List<ResultRow> getTopGroupsList() {
        List<ResultRow> results = null;
        final HashMap<NewsGroup, Integer> summaryMap = new HashMap<>();

        for (final ListIterator<Message> iter = this.filter.getMessagesFilter().listIterator(); iter
                .hasNext(); ) {
            Message next = iter.next();
            Set<NewsGroup> newsgroups = next.getNewsgroups();
            for (final NewsGroup nextGroup : newsgroups) {
                if ((null == this.filter.getSelectedNewsgroups())
                        || (this.filter.getSelectedNewsgroups().size() == 0)
                        || this.filter.getSelectedNewsgroups().contains(nextGroup)) {
                    Integer count = summaryMap.get(nextGroup);
                    if (null == count) {
                        count = Integer.valueOf(0);
                    }
                    summaryMap.put(nextGroup, Integer.valueOf(count.intValue() + 1));

                }
            }
        }
        results = new Vector<>();
        for (final Entry<NewsGroup, Integer> entry : summaryMap.entrySet()) {
            results.add(
                    new ResultRow(entry.getKey(), entry.getValue().intValue(), NewsGroup.class));
        }
        Collections.sort(results);
        return results;
    }

    public Vector<Vector<Object>> getTopGroupsVector() throws HibernateException {

        final String sql = "SELECT count(*) as posts, newsgroup_id FROM newsgroup_message "
                + " group by newsgroup_id " + " order by posts desc";

        final SQLQuery query = this.session.createSQLQuery(sql);

        final List<?> returnVal = query.list();

        final Vector<Vector<Object>> tableData = new Vector<>();
        final Iterator<?> iter = returnVal.iterator();
        while (iter.hasNext()) {
            final Vector<Object> row = new Vector<>();
            final Object[] array = (Object[]) iter.next();
            final int userID = ((Integer) array[1]).intValue();

            final List<NewsGroup> posters = this.helper.runQuery(
                    "from " + NewsGroup.class.getName() + " where id = " + userID, this.session,
                    NewsGroup.class);
            if (posters.size() > 0) {
                final NewsGroup usenetUser = posters.get(0);
                row.add(new GUIItem<>(usenetUser.getFullName(), usenetUser));
                row.add(array[0].toString());
            } else {
                log.warn("Poster " + userID + " not found");
            }
            tableData.add(row);
        }
        return tableData;
    }

    /**
     * Gets the top posters.
     *
     * @return the top posters
     */
    public Vector<ResultRow> getTopPosters() {
        Vector<ResultRow> results = null;
        final HashMap<UsenetUser, Integer> summaryMap = new HashMap<>();

        for (final ListIterator<Message> iter = this.filter.getMessagesFilter().listIterator(); iter
                .hasNext(); ) {
            final Message next = iter.next();

            // Want to check if any of the groups are selected
            boolean keep = true;
            if ((null != this.filter.getSelectedNewsgroups())
                    && (this.filter.getSelectedNewsgroups().size() > 0)) {
                final Set<NewsGroup> newsgroupsCopy = new HashSet<>();
                newsgroupsCopy.addAll(next.getNewsgroups());
                newsgroupsCopy.removeAll(this.filter.getSelectedNewsgroups());
                if (newsgroupsCopy.size() == next.getNewsgroups().size()) {
                    keep = false;
                }
            }

            final UsenetUser poster = next.getPoster();
            if (keep && ((null == this.filter.getSelectedPosters())
                    || (this.filter.getSelectedPosters().size() == 0)
                    || this.filter.getSelectedPosters().contains(poster))) {
                Integer count = summaryMap.get(poster);
                if (null == count) {
                    count = Integer.valueOf(0);
                }
                summaryMap.put(poster, Integer.valueOf(count.intValue() + 1));
            }
        }
        results = new Vector<>();
        for (final Entry<UsenetUser, Integer> entry : summaryMap.entrySet()) {
            results.add(
                    new ResultRow(entry.getKey(), entry.getValue().intValue(), UsenetUser.class));
        }
        Collections.sort(results);
        return results;
    }

}
