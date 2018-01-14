/*
 * Copyright 2017 Andres.
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
package com.eljavatar.swingutils.core.components;

import com.eljavatar.swingutils.core.componentsutils.JComboBoxUtils;
import com.eljavatar.swingutils.core.componentsutils.SwingComponentsUtils;
import com.eljavatar.swingutils.core.modelcomponents.TableModelGeneric;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

/**
 * https://java-swing-tips.blogspot.com.co/2008/03/jtable-pagination-example-using.html
 * https://www.roseindia.net/tutorial/java/swing/javaPagination.html
 * https://gist.github.com/aterai/7261520
 * 
 * http://www.logicbig.com/tutorials/core-java-tutorial/swing/jtable-pagination/
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <T> Tipo de dato del modelo de datos del JTable
 */
public class LazyJTable<T> extends javax.swing.JPanel {

    private static final String PAGINATOR_FILTER = "paginator";
    private static final String GLOBAL_FILTER = "global";
    private static final int LR_PAGE_SIZE = 5;
    private static final LinkViewRadioButtonUI LINKVIEW_RADIOBUTTON_UI = new LinkViewRadioButtonUI();
    private final Box box = Box.createHorizontalBox();
    private TableRowSorter<TableModelGeneric> sorter;
    private Font fontPages;
    private int sizeData;
    private LazyJTableSettings settings;
    private JTextField jTFglobalFilter;
    private final LinkedList<RowFilter<TableModelGeneric, Integer>> listFilters = new LinkedList<>();
    private final Map<String, RowFilter> mapFilters = new HashMap<>();
    private JComboBox jCBcolumnFilter;
    
    /**
     * Creates new form LazyJTable
     */
    public LazyJTable() {
        initComponents();
        
        settings = new LazyJTableSettings();
        
        this.fontPages = getFont();
    }
    
    public LazyJTable(LazyJTableSettings settings) {
        initComponents();
        this.settings = settings;
        this.fontPages = getFont();
    }
    
    /**
     * 
     * @param itemsPerPage Cantidad de elementos por página
     * @param currentPageIndex Página actual en la que se está ubicado
     */
    public void initLazy(int itemsPerPage, int currentPageIndex) {
        Objects.requireNonNull(settings, "Se requiere de una configuracion");
        
        this.sorter = new TableRowSorter<>(getTableModel());
        
        this.jTlistData.setRowSorter(sorter);
        
        this.settings.setCurrentPage(currentPageIndex);
        this.settings.setPageSize(itemsPerPage);
        initLinkBox(itemsPerPage, currentPageIndex);
        
        this.setLayout(new BorderLayout());
        
        this.box.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(box, BorderLayout.SOUTH);
        
        if (settings.isAddGlobalFilter()) {
            Box boxFiltro = Box.createHorizontalBox();
            boxFiltro.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
            
            jTFglobalFilter = new JTextField();
            jTFglobalFilter.setFont(fontPages);
            SwingComponentsUtils.setPlaceholder("Ingrese un criterio de filtro...", jTFglobalFilter);
            
            jTFglobalFilter.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    filtro();
                }
            });
            
            jCBcolumnFilter = new JComboBox<>();
            jCBcolumnFilter.setFont(fontPages);
            JComboBoxUtils.setProperties(jCBcolumnFilter, Arrays.asList(getTableModel().getTitleColumns()), "- Seleccione una columna -");
            
            boxFiltro.removeAll();
            boxFiltro.add(jCBcolumnFilter);
            boxFiltro.add(jTFglobalFilter);
            
            add(boxFiltro, BorderLayout.NORTH);
        }
        add(jSPlistData);
        
        validate();
        updateUI();
    }
    
    public void filtro() {
        String filtro = "(?i)" + jTFglobalFilter.getText();
        int columna = jCBcolumnFilter.getSelectedIndex() - 1;
        
        if (columna < 0) {
            SwingComponentsUtils.mostrarMensaje(this, "Debe seleccionar una columna para filtrar", "Mensaje de error", SwingComponentsUtils.ERROR);
            return;
        }
        
        mapFilters.put(GLOBAL_FILTER, RowFilter.regexFilter(filtro, columna));
        
        updateLazy();
    }
    
    /**
     * 
     */
    public void updateLazy() {
        //initLinkBox(this.settings.getPageSize(), this.settings.getCurrentPage());
        initLinkBox(this.settings.getPageSize(), 1);
        
        validate();
        updateUI();
    }
    
    /**
     * 
     * @param itemsPerPage Cantidad de filas a mostrar por pagina
     * @param target Página a la que se quiere navegar
     * @return 
     */
    private RowFilter<TableModelGeneric, Integer> makeRowFilter(final int itemsPerPage, final int target) {
        return new RowFilter<TableModelGeneric, Integer>() {
            @Override
            public boolean include(Entry<? extends TableModelGeneric, ? extends Integer> entry) {
                int ei = entry.getIdentifier();
                return (target * itemsPerPage <= ei && ei < target * itemsPerPage + itemsPerPage);
            }
        };
    }
    
    /**
     * 
     * @param itemsPerPage Cantidad de filas a mostrar por pagina
     * @param target Página a la que se quiere navegar
     * @param title
     * @param flag
     * @return 
     */
    private JRadioButton makePrevNextRadioButton(final int itemsPerPage, final int target, String title, boolean flag) {
        JRadioButton radio = new JRadioButton(title);
        radio.setForeground(Color.BLUE);
        radio.setUI(LINKVIEW_RADIOBUTTON_UI);
        radio.setFont(fontPages);
        radio.setEnabled(flag);
        radio.addActionListener(e -> {
            this.settings.setCurrentPage(target);
            initLinkBox(itemsPerPage, target);
        });
        return radio;
    }
    
    /**
     * 
     * @param itemsPerPage
     * @param current
     * @param target
     * @return 
     */
    private JRadioButton makeRadioButton(final int itemsPerPage, int current, final int target) {
        JRadioButton radio = new JRadioButton(String.valueOf(target)) {
            @Override
            protected void fireStateChanged() {
                ButtonModel buttonModel = getModel();
                if (buttonModel.isEnabled()) {
                    if (buttonModel.isPressed() && buttonModel.isArmed()) {
                        setForeground(Color.GREEN);
                    } else if (buttonModel.isSelected()) {
                        setForeground(Color.RED);
                    //} else if (isRolloverEnabled() && model.isRollover()) {
                    //    setForeground(Color.BLUE);
                    }
                } else {
                    setForeground(Color.GRAY);
                }
                super.fireStateChanged();
            }
        };
        radio.setFont(fontPages);
        radio.setForeground(Color.BLUE);
        radio.setUI(LINKVIEW_RADIOBUTTON_UI);
        if (target == current) {
            radio.setSelected(true);
        }
        radio.addActionListener(e -> {
            this.settings.setCurrentPage(target);
            initLinkBox(itemsPerPage, target);
        });
        return radio;
    }
    
    /**
     * 
     * @param itemsPerPage Cantidad de filas a mostrar por pagina
     * @param currentPageIndex 
     */
    private void initLinkBox(final int itemsPerPage, final int currentPageIndex) {
        //assert currentPageIndex > 0;
        //sorter.setRowFilter(makeRowFilter(itemsPerPage, currentPageIndex - 1));
        
        int rowCount;
        if (this.settings.isLazy()) {
            int first = (itemsPerPage * (currentPageIndex - 1)) + 1;
            
            load(first, itemsPerPage);
            
            rowCount = sizeData;
        } else {
            rowCount = getTableModel().getRowCount();
        }
        
        mapFilters.put(PAGINATOR_FILTER, makeRowFilter(itemsPerPage, currentPageIndex - 1));
        listFilters.clear();
        mapFilters.entrySet().forEach((entry) -> {
            listFilters.add(entry.getValue());
        });
        
        RowFilter filterAnd = RowFilter.andFilter(listFilters);
        sorter.setRowFilter(filterAnd);

        int startPageIndex = currentPageIndex - LR_PAGE_SIZE;
        if (startPageIndex <= 0) {
            startPageIndex = 1;
        }

//#if 0 //BUG
        //int maxPageIndex = (model.getRowCount() / itemsPerPage) + 1;
//#else
        /* "maxPageIndex" gives one blank page if the module of the division is not zero.
         *   pointed out by erServi
         * e.g. rowCount=100, maxPageIndex=100
         */
        
        
        
        int v = rowCount % itemsPerPage == 0 ? 0 : 1;
        int maxPageIndex = rowCount / itemsPerPage + v;
//#endif
        int endPageIndex = currentPageIndex + LR_PAGE_SIZE - 1;
        if (endPageIndex > maxPageIndex) {
            endPageIndex = maxPageIndex;
        }

        box.removeAll();
        if (startPageIndex >= endPageIndex) {
            //if I only have one page, Y don't want to see pagination buttons
            //suggested by erServi
            return;
        }

        ButtonGroup bg = new ButtonGroup();
        JRadioButton first = makePrevNextRadioButton(itemsPerPage, 1, "|<", currentPageIndex > 1);
        box.add(first);
        bg.add(first);

        JRadioButton prev = makePrevNextRadioButton(itemsPerPage, currentPageIndex - 1, "<", currentPageIndex > 1);
        box.add(prev);
        bg.add(prev);

        box.add(Box.createHorizontalGlue());
        for (int i = startPageIndex; i <= endPageIndex; i++) {
            JRadioButton c = makeRadioButton(itemsPerPage, currentPageIndex, i);
            box.add(c);
            bg.add(c);
        }
        box.add(Box.createHorizontalGlue());

        JRadioButton next = makePrevNextRadioButton(itemsPerPage, currentPageIndex + 1, ">", currentPageIndex < maxPageIndex);
        box.add(next);
        bg.add(next);

        JRadioButton last = makePrevNextRadioButton(itemsPerPage, maxPageIndex, ">|", currentPageIndex < maxPageIndex);
        box.add(last);
        bg.add(last);

        box.revalidate();
        box.repaint();
        
        if (this.settings.isLazy()) {
            validate();
            updateUI();
        }
    }
    
    public List<T> load(int first, int pageSize) {
        throw new UnsupportedOperationException("Método no implementado");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSPlistData = new javax.swing.JScrollPane();
        jTlistData = new javax.swing.JTable();

        jTlistData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jSPlistData.setViewportView(jTlistData);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSPlistData)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSPlistData, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jSPlistData;
    private javax.swing.JTable jTlistData;
    // End of variables declaration//GEN-END:variables

    public JTable getjTlistData() {
        return jTlistData;
    }

    public void setjTlistData(JTable jTlistData) {
        this.jTlistData = jTlistData;
    }

    public TableModelGeneric getTableModel() {
        return (TableModelGeneric) jTlistData.getModel();
    }
    
    public void setTableModel(TableModelGeneric tableModelGeneric) {
        this.jTlistData.setModel(tableModelGeneric);
    }
    
    public List<T> getListData() {
        return getTableModel().getListElements();
    }
    
    public void setListData(List<T> listData) {
        getTableModel().setListElements(listData);
        getTableModel().fireTableDataChanged();
    }

    public Font getFontPages() {
        return fontPages;
    }

    public void setFontPages(Font fontPages) {
        this.fontPages = fontPages;
    }

    public int getSizeData() {
        return sizeData;
    }

    public void setSizeData(int sizeData) {
        this.sizeData = sizeData;
    }

    public LazyJTableSettings getSettings() {
        return settings;
    }

    public void setSettings(LazyJTableSettings settings) {
        this.settings = settings;
    }
    
}
