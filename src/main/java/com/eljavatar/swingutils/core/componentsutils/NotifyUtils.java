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
package com.eljavatar.swingutils.core.componentsutils;

import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import javax.swing.JFrame;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class NotifyUtils {
    
    private static Notify notifyWarning;
    private static Notify notifyError;
    
    public static void showWarningAutoHide(JFrame jFrame, String title, String message) {
        notifyWarning = Notify.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .darkStyle()
                .hideAfter(10000);
        
        //ApplicationContext.getInstance().getListNotifyWarning().add(notifyWarning);
        notifyWarning.showWarning();
    }
    
    
    public static void showErrorAutoHide(JFrame jFrame, String title, String message) {
        notifyError = Notify.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .darkStyle()
                .hideAfter(10000);
        
        //ApplicationContext.getInstance().getListNotifyError().add(notifyError);
        notifyError.showError();
    }
    
}
