package uk.co.sleonard.unison.datahandling;

import org.hibernate.Session;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.datahandling.DAO.*;
import uk.co.sleonard.unison.input.LocationFinder;
import uk.co.sleonard.unison.input.LocationFinderImpl;
import uk.co.sleonard.unison.input.NewsArticle;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory responsible for creating {@link UsenetUser} entities and related
 * objects.
 */
public class UserFactory {

    private final LocationFinder locationFinder;

    public UserFactory() {
        this.locationFinder = new LocationFinderImpl();
    }

    public UsenetUser createUsenetUser(final NewsArticle article, final Session session,
                                      final Location locationInput, final String gender,
                                      final HibernateHelper helper) throws UNISoNException {
        Location location = locationInput;
        if (article.isFullHeader()) {
            location = this.createLocation(article, session, helper);
        }
        final UsenetUser poster = this.findOrCreateUsenetUser(article, session, gender, helper);
        if (location != null && (poster.getLocation() == null || !poster.getLocation().equals(location))) {
            poster.setLocation(location);
            session.saveOrUpdate(poster);
        }
        return poster;
    }

    private synchronized Location createLocation(final NewsArticle article, final Session session,
                                                 final HibernateHelper helper) {
        Location location;
        final IpAddress ip = this.findOrCreateIpAddress(article, session, helper);
        location = ip.getLocation();
        if (null == location) {
            location = this.findOrCreateLocation(session, ip, helper);
        }
        return location;
    }

    private synchronized IpAddress findOrCreateIpAddress(final NewsArticle article,
                                                         final Session session, final HibernateHelper helper) {
        IpAddress ip = (IpAddress) helper.findByKey(article.getPostingHost(), session,
                IpAddress.class);
        if (null == ip) {
            ip = new IpAddress(article.getPostingHost(), null);
            session.saveOrUpdate(ip);
        }
        return ip;
    }

    synchronized Location findOrCreateLocation(final Session session, final IpAddress ipAddress,
                                               final HibernateHelper helper) {
        Location location = this.locationFinder.createLocation(ipAddress.getIpAddress());
        final Location dbLocation = (Location) helper.findByKey(location.getCity(), session,
                Location.class);
        if (null != dbLocation) {
            location = dbLocation;
            List<IpAddress> ipAddresses = location.getIpAddresses();
            if (null == ipAddresses) {
                ipAddresses = new ArrayList<>();
            }
            ipAddresses.add(ipAddress);
        }
        session.saveOrUpdate(location);
        return location;
    }

    private synchronized UsenetUser findOrCreateUsenetUser(final NewsArticle article,
                                                           final Session session, final String gender,
                                                           final HibernateHelper helper) throws UNISoNException {
        EmailAddress emailAddress;
        try {
            emailAddress = UsenetUserHelper.parseFromField(article);
        } catch (final IllegalArgumentException e) {
            throw new UNISoNException("Failed to parse From field: " + article.getFrom(), e);
        }
        if ((emailAddress == null) || (emailAddress.email() == null)) {
            throw new UNISoNException("Missing email address in From field: " + article.getFrom());
        }
        UsenetUser poster = this.findUsenetUser(emailAddress, session, helper);
        if (null == poster) {
            poster = new UsenetUser(emailAddress.name(), emailAddress.email(),
                    emailAddress.ipAddress(), gender, null);
            session.saveOrUpdate(poster);
        }
        return poster;
    }

    synchronized UsenetUser findUsenetUser(final EmailAddress emailAddress, final Session session,
                                           final HibernateHelper helper) {
        if ((emailAddress == null) || (emailAddress.email() == null)) {
            return null;
        }
        return (UsenetUser) helper.findByKey(emailAddress.email(), session, UsenetUser.class);
    }
}

