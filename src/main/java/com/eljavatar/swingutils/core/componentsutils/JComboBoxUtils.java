package com.eljavatar.swingutils.core.componentsutils;

import com.eljavatar.swingutils.core.renderercomponents.ComboBoxRendererDefault;
import com.eljavatar.swingutils.core.modelcomponents.ComboBoxModelGeneric;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class JComboBoxUtils {
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class clazz, String nameMethod) {
        ComboBoxModelGeneric<E> model = new ComboBoxModelGeneric<>(list, addElementNullDefault, isSynchronized, needDetectConcurrentModifications);
        jComboBox.setModel(model);
        
        ListCellRenderer rendererDefault = jComboBox.getRenderer();
        ComboBoxRendererDefault renderer = new ComboBoxRendererDefault(jComboBox, rendererDefault, textNoSeleccionable, clazz, nameMethod);
        jComboBox.setRenderer(renderer);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications, Class clazz, String nameMethod) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, clazz, nameMethod);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications, Class clazz, String nameMethod) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications, clazz, nameMethod);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, Class clazz, String nameMethod) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true, clazz, nameMethod);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, Class clazz, String nameMethod) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true, clazz, nameMethod);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true, null, null);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, Class clazz, String nameMethod) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true, clazz, nameMethod);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true,null, null);
    }
    
}
