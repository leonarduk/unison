package uk.co.sleonard.unison.datahandling;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Additional tests for {@link HibernateHelper} focusing on database access
 * routines.
 */
class HibernateHelperFindByKeyTest {

    private final HibernateHelper helper = new HibernateHelper(null);

    @Nested
    class FindByKey {

        @ParameterizedTest
        @MethodSource("uniqueResults")
        void returnsUniqueResult(Object expected) {
            Session session = mock(Session.class);
            Query<Object> query = mock(Query.class);
            when(session.getNamedQuery(anyString())).thenReturn(query);
            when(query.uniqueResult()).thenReturn(expected);

            Object result = helper.findByKey("key", session, Object.class);

            assertSame(expected, result);
            verify(query).setParameter("key", "key");
        }

        static Stream<Object> uniqueResults() {
            return Stream.of("value", 123);
        }

        @Test
        void wrapsNonUniqueResultException() {
            Session session = mock(Session.class);
            Query<Object> query = mock(Query.class);
            when(session.getNamedQuery(anyString())).thenReturn(query);
            when(query.uniqueResult()).thenThrow(new NonUniqueResultException(2));

            assertThrows(RuntimeException.class,
                    () -> helper.findByKey("key", session, Object.class));
        }
    }
}

