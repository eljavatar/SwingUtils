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

import com.eljavatar.swingutils.core.annotations.ComboBoxView;
import com.eljavatar.swingutils.core.annotations.DateTextView;
import com.eljavatar.swingutils.core.annotations.NumberTextView;
import com.eljavatar.swingutils.core.annotations.PaginatedTableView;
import com.eljavatar.swingutils.core.annotations.PasswordTextView;
import com.eljavatar.swingutils.core.annotations.TableView;
import com.eljavatar.swingutils.core.annotations.TextView;
import com.eljavatar.swingutils.core.annotations.ToggleButtonView;
import java.lang.reflect.Field;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ComponentAnnotatedViewFactory {
    
    public static ComponentAnnotatedViewStrategy getStrategy(Field fieldView) {
        if (fieldView.isAnnotationPresent(TextView.class)) {
            return new TextViewStrategy();
        } else if (fieldView.isAnnotationPresent(DateTextView.class)) {
            return new DateTextViewStrategy();
        } else if (fieldView.isAnnotationPresent(NumberTextView.class)) {
            return new NumberTextViewStrategy();
        } else if (fieldView.isAnnotationPresent(PasswordTextView.class)) {
            return new PasswordTextViewStrategy();
        } else if (fieldView.isAnnotationPresent(ToggleButtonView.class)) {
            return new ToggleButtonViewStrategy();
        } else if (fieldView.isAnnotationPresent(ComboBoxView.class)) {
            return new ComboBoxViewStrategy();
        } else if (fieldView.isAnnotationPresent(TableView.class)) {
            return new TableViewStrategy();
        } else if (fieldView.isAnnotationPresent(PaginatedTableView.class)) {
            return new PaginatedTableViewStrategy();
        } else {
            return new ComponentViewStrategy();
        }
    }
    
}
