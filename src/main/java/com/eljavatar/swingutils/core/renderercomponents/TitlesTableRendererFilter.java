/*
 * Copyright 2018 ElJavatar.
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

import com.eljavatar.swingutils.core.componentsutils.SwingComponentsUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 */
public class TitlesTableRendererFilter implements TableCellRenderer {

    private final TableCellRenderer rendererDefault;
    private JTextField jTFfiltro;
    private final Font fontFilter;

    public TitlesTableRendererFilter(TableCellRenderer rendererDefault, Font fontFilter) {
        this.rendererDefault = rendererDefault;
        this.fontFilter = fontFilter;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        javax.swing.JComponent component = (javax.swing.JComponent) rendererDefault.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        javax.swing.JLabel label = (javax.swing.JLabel) component;
        label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(label);
        
        Box boxFiltro = Box.createHorizontalBox();
        boxFiltro.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 2));

        jTFfiltro = new JTextField();
        jTFfiltro.setFont(fontFilter);
        SwingComponentsUtils.setPlaceholder(label.getText().trim() + "...", jTFfiltro);
        jTFfiltro.setEnabled(true);
        jTFfiltro.setFocusable(true);

//        jTFglobalFilter.addKeyListener(new java.awt.event.KeyAdapter() {
//            @Override
//            public void keyTyped(java.awt.event.KeyEvent evt) {
//                jTFfiltroKeyTyped(evt);
//            }
//        });

        boxFiltro.removeAll();
        boxFiltro.add(jTFfiltro);
        panel.add(boxFiltro, BorderLayout.SOUTH);
        
        panel.setEnabled(true);
        panel.setFocusable(true);
        
        
        panel.validate();
        panel.updateUI();
        
        return panel;
    }

}
