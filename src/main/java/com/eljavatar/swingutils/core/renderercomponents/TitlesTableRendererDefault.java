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
package com.eljavatar.swingutils.core.renderercomponents;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class TitlesTableRendererDefault implements javax.swing.table.TableCellRenderer {

    private final TableCellRenderer rendererDefault;

    public TitlesTableRendererDefault(TableCellRenderer rendererDefault) {
        this.rendererDefault = rendererDefault;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        javax.swing.JComponent component = (javax.swing.JComponent) rendererDefault.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        javax.swing.JLabel label = (javax.swing.JLabel) component;
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        return label;
    }
    
}
