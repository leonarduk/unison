package uk.co.sleonard.unison;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/** Listener interface used throughout the application to listen for changes in data. */
@FunctionalInterface
public interface DataChangeListener extends PropertyChangeListener {
  /**
   * Called when the underlying data has changed.
   *
   * @param evt description of the change
   */
  void dataChanged(PropertyChangeEvent evt);

  @Override
  default void propertyChange(PropertyChangeEvent evt) {
    dataChanged(evt);
  }
}
