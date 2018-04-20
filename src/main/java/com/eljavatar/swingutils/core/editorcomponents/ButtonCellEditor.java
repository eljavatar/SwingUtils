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
package com.eljavatar.swingutils.core.editorcomponents;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author ElJavatar - Andres Mauricio (http://www.eljavatar.com)
 */
public abstract class ButtonCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private final JButton editorButton;
    private JTable jTable;
    private int rowSelected;
    private int colSelected;
    private boolean isPushed;

    public ButtonCellEditor(JButton jButton) {
        this.editorButton = jButton;
//        System.out.println("Entramos al constructor");
        jButton.addActionListener(ButtonCellEditor.this);
        
        //fireEditingStopped();
    }

    private void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

    private void setRowSelected(int rowSelected) {
        this.rowSelected = rowSelected;
    }

    private void setColSelected(int colSelected) {
        this.colSelected = colSelected;
    }
    
    public abstract void action(JTable table, int row, int column);

    @Override
    public final Object getCellEditorValue() {
//        System.out.println("Obtenemos valor");
        if (isPushed)  {
            //action(jTable, rowSelected, colSelected);
        }
        
        isPushed = false;
        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//        System.out.println("Iniciamos editor");
        
        if (isSelected) {
            editorButton.setForeground(table.getSelectionForeground());
            editorButton.setBackground(table.getSelectionBackground());
        } else {
            editorButton.setForeground(table.getForeground());
            editorButton.setBackground(table.getBackground());
        }
        
        setjTable(table);
        setRowSelected(row);
        setColSelected(column);
        
        if (isPushed)  {
            //action(jTable, rowSelected, colSelected);
        } else {
            isPushed = true;
        }
        
        return editorButton;
    }

    @Override
    public boolean stopCellEditing() {
//        System.out.println("\nStop edicion\n");
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
//        System.out.println("\nFired edicion\n");
        super.fireEditingStopped();
        isPushed = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        System.out.println("\nParamos edicion\n");
        super.fireEditingStopped();
        isPushed = false;
        action(jTable, rowSelected, colSelected);
    }

}
