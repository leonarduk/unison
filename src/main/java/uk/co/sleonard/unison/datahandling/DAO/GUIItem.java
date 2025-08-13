/**
 * GUIItem
 *
 * @author ${author}
 * @since 20-Jun-2016
 */
package uk.co.sleonard.unison.datahandling.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The Class GUIItem.
 *
 * @param <T>
 *            the generic type
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
@Data
@AllArgsConstructor
public class GUIItem<T> {
	private final String name;
	private final T object;
	@Override
	public String toString() {
		return this.name;
	}

}
