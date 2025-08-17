package uk.co.sleonard.unison.datahandling.DAO;

/**
 * The Class GUIItem.
 *
 * @param <T> the generic type
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public record GUIItem<T>(String name, T object) {
    @Override
    public String toString() {
        return this.name;
    }
}
