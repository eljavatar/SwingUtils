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

import com.eljavatar.swingutils.core.renderercomponents.ComboBoxRendererDefault;
import com.eljavatar.swingutils.core.modelcomponents.ComboBoxModelGeneric;
import java.util.List;
import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class JComboBoxUtils {
    
    public synchronized static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, Function<E, String> functionLabel, Function<E, String> functionTooltip) {
        ComboBoxModelGeneric<E> model = new ComboBoxModelGeneric<>(list, addElementNullDefault, isSynchronized, needDetectConcurrentModifications);
        jComboBox.setModel(model);
        
        ListCellRenderer rendererDefault = jComboBox.getRenderer();
        ComboBoxRendererDefault renderer = new ComboBoxRendererDefault(jComboBox, rendererDefault, textNoSeleccionable, functionLabel, functionTooltip);
        jComboBox.setRenderer(renderer);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto y
     *      no seleccionable al principio de la lista
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     * @param nameMethodTooltip Nombre del método que retornará el texto que se mostrará  en el
     *      tooltip de cada ítem del JComboBox. Este método debe estar declarado en la clase del
     *      tipo de objeto que usará el JComboBox
     */
    public synchronized static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, String nameMethodLabel, String nameMethodTooltip) {
        ComboBoxModelGeneric<E> model = new ComboBoxModelGeneric<>(list, addElementNullDefault, isSynchronized, needDetectConcurrentModifications);
        jComboBox.setModel(model);
        
        ListCellRenderer rendererDefault = jComboBox.getRenderer();
        ComboBoxRendererDefault renderer = new ComboBoxRendererDefault(jComboBox, rendererDefault, textNoSeleccionable, clazz, nameMethodLabel, nameMethodTooltip);
        jComboBox.setRenderer(renderer);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto y
     *      no seleccionable al principio de la lista
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, String nameMethodLabel) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, clazz, nameMethodLabel, null);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto y
     *      no seleccionable al principio de la lista
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, Function<E, String> functionLabel, Function<E, String> functionTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, clazz, functionLabel, functionTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     * @param nameMethodTooltip Nombre del método que retornará el texto que se mostrará  en el
     *      tooltip de cada ítem del JComboBox. Este método debe estar declarado en la clase del
     *      tipo de objeto que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, String nameMethodLabel, String nameMethodTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, clazz, nameMethodLabel, nameMethodTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, String nameMethodLabel) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, clazz, nameMethodLabel);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, Function<E, String> functionLabel, Function<E, String> functionTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications, clazz, functionLabel, functionTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     * @param nameMethodTooltip Nombre del método que retornará el texto que se mostrará  en el
     *      tooltip de cada ítem del JComboBox. Este método debe estar declarado en la clase del
     *      tipo de objeto que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, String nameMethodLabel, String nameMethodTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications, clazz, nameMethodLabel, nameMethodTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications, Class<E> clazz, String nameMethodLabel) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications, clazz, nameMethodLabel);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param isSynchronized Define si la lista de elementos será una colección sincronizada
     * @param needDetectConcurrentModifications En caso de que sea una colección sincronizada,
     *      define si necesita detectar modificaciones concurrentes, ya que de acuerdo a esta
     *      opción, se decide cuál será la implementacion óptima para crear la lista de datos
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, Class<E> clazz, Function<E, String> functionLabel, Function<E, String> functionTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true, clazz, functionLabel, functionTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará cada ítem
     *      del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     * @param nameMethodTooltip Nombre del método que retornará el texto que se mostrará  en el
     *      tooltip de cada ítem del JComboBox. Este método debe estar declarado en la clase del
     *      tipo de objeto que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, Class<E> clazz, String nameMethodLabel, String nameMethodTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true, clazz, nameMethodLabel, nameMethodTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, Class<E> clazz, String nameMethodLabel) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true, clazz, nameMethodLabel);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param addElementNullDefault Indica si se desea agregar un elemento nulo por defecto
     *      y no seleccionable al principio de la lista
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, Class<E> clazz, Function<E, String> functionLabel, Function<E, String> functionTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true, clazz, functionLabel, functionTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     * @param nameMethodTooltip Nombre del método que retornará el texto que se mostrará  en el
     *      tooltip de cada ítem del JComboBox. Este método debe estar declarado en la clase del
     *      tipo de objeto que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, Class<E> clazz, String nameMethodLabel, String nameMethodTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true, clazz, nameMethodLabel, nameMethodTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará cada ítem
     *      del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, Class<E> clazz, String nameMethodLabel) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true, clazz, nameMethodLabel);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param textNoSeleccionable Texto que se mostrará en el ítem no seleccionable
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, Class<E> clazz, Function<E, String> functionLabel, Function<E, String> functionTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true, clazz, functionLabel, functionTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     * @param nameMethodTooltip Nombre del método que retornará el texto que se mostrará  en el
     *      tooltip de cada ítem del JComboBox. Este método debe estar declarado en la clase del
     *      tipo de objeto que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, Class<E> clazz, String nameMethodLabel, String nameMethodTooltip) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true, clazz, nameMethodLabel, nameMethodTooltip);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     * @param clazz Class del tipo de objeto que usará el JComboBox
     * @param nameMethodLabel Nombre del método que retornará el texto que se mostrará en cada
     *      ítem del JComboBox. Este método debe estar declarado en la clase del tipo de objeto
     *      que usará el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, Class<E> clazz, String nameMethodLabel) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true, clazz, nameMethodLabel);
    }
    
    /**
     * 
     * @param <E> Tipo de objeto que usará el JComboBox
     * @param jComboBox JComboBox al cual queremos aplicar las propiedades
     * @param list Lista de objetos que se mostrarán en el JComboBox
     */
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true, null, null);
    }
    
}
