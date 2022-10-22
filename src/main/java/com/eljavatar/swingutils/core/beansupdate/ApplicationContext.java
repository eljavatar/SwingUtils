/*
 * Copyright 2020 Andres.
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

import dorkbox.notify.Notify;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ApplicationContext {
    
    private final JFrame main;
    private static ApplicationContext applicationContext;
    private final List<Notify> listNotifyError;
    private final List<Notify> listNotifyWarning;
    
    
    private ApplicationContext(JFrame main) {
        this.main = main;
        this.listNotifyError = new ArrayList<>();
        this.listNotifyWarning = new ArrayList<>();
    }
    
    public static ApplicationContext getInstance() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext(null);
        }
        return applicationContext;
    }
    
    public static ApplicationContext getInstance(JFrame main) {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext(main);
        }
        return applicationContext;
    }
    
    public static ApplicationContext getContext() {
        return applicationContext;
    }
    
    public void clearListNotify() {
        listNotifyError.clear();
        listNotifyWarning.clear();
    }
    
    public JFrame getMain() {
        return main;
    }

    public List<Notify> getListNotifyError() {
        return listNotifyError;
    }

    public List<Notify> getListNotifyWarning() {
        return listNotifyWarning;
    }
    
}
