package uk.co.sleonard.unison.gui;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;

import uk.co.sleonard.unison.datahandling.HibernateHelper;

public class GUIItem<T> {
    private String name;
    
    private T object;
    
    public GUIItem(String name, T data) {
        this.name = name;
        this.object = data;
    }
    
    public T getItem() {
        return object;
    }
    
    @SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("GUIItem");
    
    @Override
    public String toString() {
          return name;
    }
    
    public static Vector<GUIItem<?>> getGUIList(Object[] array) {
        return getGUIList(Arrays.asList(array));
    }
    
    public static Vector<GUIItem<?>> getGUIList(List<?> list) {
        Vector<GUIItem<?>> returnList = new Vector<GUIItem<?>>();
        
        for (ListIterator<?> iter = list.listIterator(); iter.hasNext();) {
            Object next = iter.next();
            String text = null;
            if ( next instanceof NewsGroup) {
                text = ((NewsGroup) next).getFullName();
            } else {
                text = HibernateHelper.getText(next);
            }
            returnList.add(new GUIItem<Object>(text, next));
        }
        return returnList;
    }
}