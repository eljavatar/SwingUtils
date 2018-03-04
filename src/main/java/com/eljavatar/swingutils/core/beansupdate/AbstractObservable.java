/*
 * Copyright 2018 Andres.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eljavatar.swingutils.core.beansupdate;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <C> Controlador desde el cual se notificaran los cambios
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
