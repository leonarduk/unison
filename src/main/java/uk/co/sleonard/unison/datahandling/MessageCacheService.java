package uk.co.sleonard.unison.datahandling;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import uk.co.sleonard.unison.datahandling.DAO.Message;

/**
 * Simple wrapper around Ehcache used for caching {@link Message} entities.
 */
public class MessageCacheService {

    private final CacheManager cacheManager;
    private final Cache<String, Message> messagesCache;

    public MessageCacheService() {
        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        this.messagesCache = this.cacheManager.createCache("messagesCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Message.class,
                        ResourcePoolsBuilder.heap(1000)));
    }

    public Message get(String key) {
        return this.messagesCache.get(key);
    }

    public void put(Message message) {
        if (message != null) {
            this.messagesCache.put(message.getUsenetMessageID(), message);
        }
    }
}

