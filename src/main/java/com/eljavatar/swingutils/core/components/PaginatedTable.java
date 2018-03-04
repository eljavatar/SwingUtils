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
package com.eljavatar.swingutils.core.components;

import com.eljavatar.swingutils.core.modelcomponents.PaginationDataProvider;
import com.eljavatar.swingutils.core.modelcomponents.LazyDataProvider;
import com.eljavatar.swingutils.core.modelcomponents.JTableDataProvider;
import com.eljavatar.swingutils.core.componentsutils.JComboBoxUtils;
import com.eljavatar.swingutils.core.componentsutils.SwingComponentsUtils;
import com.eljavatar.swingutils.core.modelcomponents.ObjectFilter;
import com.eljavatar.swingutils.core.modelcomponents.TableModelGeneric;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 * http://www.logicbig.com/tutorials/core-java-tutorial/swing/jtable-pagination/
 * https://www.javaworld.com/article/2077503/learn-java/java-tip-137--manage-distributed-jtables.html
 * 
 * @author ElJavatar
 * @param <T>
 */
public class PaginatedTable<T> extends javax.swing.JPanel {

    /**
     * Configuracion cuando se usa un PaginationDataProvider
     */
    private PaginationDataProvider<T> paginationDataProvider;
    
    /**
     * Configuracion cuando se usa un LazyDataProvider
     */
    private LazyDataProvider<T> lazyDataProvider;
    
    /**
     * Tamaños de páginas que aparecerán en el ComboBox
     */
    private int[] pageSizes;
    
    /**
     * Actual tamaño de página que se muestra
     */
    private int currentPageSize;
    
    /**
     * Página actual
     */
    private int currentPage = 1;
    
    /**
     * Panel donde estarán los componentes de paginación
     */
    private JPanel pageLinkPanel;
    
    /**
     * Modelo de datos del JTable
     */
    private TableModelGeneric<T> tableModelGeneric;
    
    /**
     * Cantidad máxima de botones a mostrar en el paginador
     */
    private static final int MAX_PAGING_COMP_TO_SHOW = 9;
    
    /**
     * Texto de separación
     */
    private static final String ELLIPSES = "...";
    
    /**
     * Indica si el JTable cargará los datos de forma perezosa o no
     */
    private boolean lazy;
    
    /**
     * Indica si se añadirá opción para filtrar en la cabecera del JTable
     */
    private boolean globalFilter;
    
    /**
     * Texto donde se escribirá el texto para realizar un filtro en el JTable
     */
    private JTextField jTFglobalFilter;
    
    /**
     * ComboBox que mostrará las columnas por las que se desea hacer un filtro
     */
    private JComboBox jCBcolumnFilter;
    
    /**
     * Fuente del texto de los elementos del paginador
     */
    private Font fontPages;
    
    /**
     * Mapa donde se agregarán los filtros
     */
    private Map<String, Object> filters;
    
    /**
     * Creates new form PaginatedTable
     */
    public PaginatedTable() {
        initComponents();
    }
    
    public PaginatedTable(JTable dataTable, JTableDataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize, boolean globalFilter, Font fontPages) {
        initComponents();
        initProperties(dataTable, dataProvider, pageSizes, defaultPageSize, globalFilter, fontPages);
    }
    
    private void initProperties(JTable dataTable, JTableDataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize, boolean globalFilter, Font fontPages) {
        if (dataProvider instanceof PaginationDataProvider) {
            this.paginationDataProvider = (PaginationDataProvider) dataProvider;
            this.lazy = false;
        } else if (dataProvider instanceof LazyDataProvider) {
            this.lazyDataProvider = (LazyDataProvider) dataProvider;
            this.lazy = true;
        }
        this.pageSizes = pageSizes;
        this.currentPageSize = defaultPageSize;
        this.dataTable = dataTable;
        this.globalFilter = globalFilter;
        this.fontPages = fontPages;
        this.filters = new HashMap<>();
    }
    
    public void decorateAndSet(JTableDataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize, boolean globalFilter, Font fontPages) {
        initProperties(dataTable, dataProvider, pageSizes, defaultPageSize, globalFilter, fontPages);
        this.removeAll();
        this.init();
        this.revalidate();
        this.repaint();
    }
    
    public static <T> PaginatedTable<T> decorate(JTable table, JTableDataProvider<T> dataProvider, int[] pageSizes, int defaultPageSize, boolean globalFilter, Font fontPages) {
        PaginatedTable<T> decorator = new PaginatedTable<>(table, dataProvider, pageSizes, defaultPageSize, globalFilter, fontPages);
        decorator.init();
        return decorator;
    }

    private void init() {
        initDataModel();
        initPaginationComponents();
        initListeners();
        paginate();
    }
    
    private void initDataModel() {
        TableModel model = dataTable.getModel();
        if (!(model instanceof TableModelGeneric)) {
            throw new IllegalArgumentException("TableModel must be a subclass of TableModelGeneric");
        }
        tableModelGeneric = (TableModelGeneric) model;
    }
    
    private void initPaginationComponents() {
        this.setLayout(new BorderLayout());
        JPanel paginationPanel = createPaginationPanel();
        addGlobalFilter();
        this.add(paginationPanel, BorderLayout.SOUTH);
        jSPdataTable = new JScrollPane();
        jSPdataTable.setViewportView(dataTable);
        this.add(jSPdataTable);
    }
    
    private void addGlobalFilter() {
        if (globalFilter) {
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
            JComboBoxUtils.setProperties(jCBcolumnFilter, tableModelGeneric.getFiltersProperties(), "- Seleccione una columna -", ObjectFilter.class, "getNameIntoComboBox");
            
            boxFiltro.removeAll();
            boxFiltro.add(jCBcolumnFilter);
            boxFiltro.add(jTFglobalFilter);
            
            this.add(boxFiltro, BorderLayout.NORTH);
        }
    }
    
    public void filtro() {
        int columna = jCBcolumnFilter.getSelectedIndex() - 1;
        if (columna < 0) {
            SwingComponentsUtils.mostrarMensaje(this, "Debe seleccionar una columna para filtrar", "Mensaje de error", SwingComponentsUtils.ERROR);
            return;
        }
        
        ObjectFilter objectFilter = (ObjectFilter) jCBcolumnFilter.getSelectedItem();
        filters.clear();
        
        filters.put(objectFilter.getNameFilter(), jTFglobalFilter.getText().trim());
        if (!jTFglobalFilter.getText().trim().isEmpty()) {
            if (lazy) {
                filters.put(objectFilter.getNameFilter(), jTFglobalFilter.getText().trim());
            } else {
                String filtro = jTFglobalFilter.getText().trim().replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
                String pattern = "(?i)(.*)" + filtro + "(.*)";
                filters.put(objectFilter.getNameFilter(), pattern);
            }
        }
        
        currentPage = 1;
        paginate();
    }
    
    private JPanel createPaginationPanel() {
        JPanel paginationPanel = new JPanel();
        pageLinkPanel = new JPanel(new GridLayout(1, MAX_PAGING_COMP_TO_SHOW, 3, 3));
        paginationPanel.add(pageLinkPanel);

        if (pageSizes != null) {
            JComboBox<Integer> pageComboBox = new JComboBox<>(Arrays.stream(pageSizes).boxed().toArray(Integer[]::new));
            pageComboBox.setFont(fontPages);
            pageComboBox.addActionListener((ActionEvent e) -> {
                //to preserve current rows position
                int currentPageStartRow = ((currentPage - 1) * currentPageSize) + 1;
                currentPageSize = (Integer) pageComboBox.getSelectedItem();
                currentPage = ((currentPageStartRow - 1) / currentPageSize) + 1;
                paginate();
            });
            paginationPanel.add(Box.createHorizontalStrut(15));
            JLabel label = new JLabel("Page Size: ");
            label.setFont(fontPages);
            paginationPanel.add(label);
            paginationPanel.add(pageComboBox);
            pageComboBox.setSelectedItem(currentPageSize);
        }
        return paginationPanel;
    }
    
    private void initListeners() {
        tableModelGeneric.addTableModelListener(this::refreshPageButtonPanel);
    }
    
    private void refreshPageButtonPanel(TableModelEvent tme) {
        pageLinkPanel.removeAll();
        int totalRows = lazy ? lazyDataProvider.getRowCount() : paginationDataProvider.getRowCount();
        int pages = (int) Math.ceil((double) totalRows / currentPageSize);
        ButtonGroup buttonGroup = new ButtonGroup();
        if (pages > MAX_PAGING_COMP_TO_SHOW) {
            addPageButton(pageLinkPanel, buttonGroup, 1);
            if (currentPage > (pages - ((MAX_PAGING_COMP_TO_SHOW + 1) / 2))) {
                //case: 1 ... n->lastPage
                pageLinkPanel.add(createEllipsesComponent());
                addPageButtonRange(pageLinkPanel, buttonGroup, pages - MAX_PAGING_COMP_TO_SHOW + 3, pages);
            } else if (currentPage <= (MAX_PAGING_COMP_TO_SHOW + 1) / 2) {
                //case: 1->n ...lastPage
                addPageButtonRange(pageLinkPanel, buttonGroup, 2, MAX_PAGING_COMP_TO_SHOW - 2);
                pageLinkPanel.add(createEllipsesComponent());
                addPageButton(pageLinkPanel, buttonGroup, pages);
            } else {//case: 1 .. x->n .. lastPage
                pageLinkPanel.add(createEllipsesComponent());//first ellipses
                //currentPage is approx mid point among total max-4 center links
                int start = currentPage - (MAX_PAGING_COMP_TO_SHOW - 4) / 2;
                int end = start + MAX_PAGING_COMP_TO_SHOW - 5;
                addPageButtonRange(pageLinkPanel, buttonGroup, start, end);
                pageLinkPanel.add(createEllipsesComponent());//last ellipsis
                addPageButton(pageLinkPanel, buttonGroup, pages);//last page link
            }
        } else {
            addPageButtonRange(pageLinkPanel, buttonGroup, 1, pages);
        }
        pageLinkPanel.getParent().validate();
        pageLinkPanel.getParent().repaint();
    }
    
    private void addPageButton(JPanel parentPanel, ButtonGroup buttonGroup, int pageNumber) {
        JToggleButton toggleButton = new JToggleButton(Integer.toString(pageNumber));
        toggleButton.setFont(fontPages);
        toggleButton.setMargin(new Insets(1, 3, 1, 3));
        buttonGroup.add(toggleButton);
        parentPanel.add(toggleButton);
        if (pageNumber == currentPage) {
            toggleButton.setSelected(true);
        }
        toggleButton.addActionListener(ae -> {
            currentPage = Integer.parseInt(ae.getActionCommand());
            paginate();
        });
    }

    private void addPageButtonRange(JPanel parentPanel, ButtonGroup buttonGroup, int start, int end) {
        for (; start <= end; start++) {
            addPageButton(parentPanel, buttonGroup, start);
        }
    }
    
    private Component createEllipsesComponent() {
        return new JLabel(ELLIPSES, SwingConstants.CENTER);
    }
    
    private void paginate() {
        int startIndex = (currentPage - 1) * currentPageSize;
        int endIndex = startIndex + currentPageSize;
        
        if (lazy) {
            lazyDataProvider.getRows(startIndex, endIndex, filters);
        } else {
            paginationDataProvider.getRows(startIndex, endIndex, filters);
        }
        
        List<T> rows = lazy ? lazyDataProvider.getListData() : paginationDataProvider.getListDataFiltered();
        tableModelGeneric.setListElements(rows);
        tableModelGeneric.fireTableDataChanged();
    }
    
    public void clearAndBackToPageOne() {
        if (!lazy) {
            paginationDataProvider.resetListData();
        }
        updatePaginatedTable();
    }
    
    public void updatePaginatedTable() {
        filters.clear();
        jCBcolumnFilter.setSelectedItem(null);
        jTFglobalFilter.setText(null);
        currentPage = 1;
        paginate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSPdataTable = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jSPdataTable.setViewportView(dataTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSPdataTable, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSPdataTable, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable dataTable;
    private javax.swing.JScrollPane jSPdataTable;
    // End of variables declaration//GEN-END:variables

    public JTable getDataTable() {
        return dataTable;
    }

    public TableModelGeneric<T> getTableModelGeneric() {
        return tableModelGeneric;
    }

    public PaginationDataProvider<T> getPaginationDataProvider() {
        return paginationDataProvider;
    }

    public LazyDataProvider<T> getLazyDataProvider() {
        return lazyDataProvider;
    }

    public boolean isLazy() {
        return lazy;
    }

}
