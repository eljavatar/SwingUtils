package com.eljavatar.swingutils.core.beansupdate;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <C>
 */
public class AbstractObservable<C extends Observer> extends Observable {
    
    public AbstractObservable(C controller) {	
        super();
        super.addObserver(controller);
    }
    
    public void changeData(Object object) {
        setChanged(); // the two methods of Observable class
        notifyObservers(object);
    }
    
}
