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
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        ComboBoxModelGeneric<E> model = new ComboBoxModelGeneric<>(list, addElementNullDefault, isSynchronized, needDetectConcurrentModifications);
        jComboBox.setModel(model);
        
        ListCellRenderer rendererDefault = jComboBox.getRenderer();
        ComboBoxRendererDefault renderer = new ComboBoxRendererDefault(rendererDefault, textNoSeleccionable);
        jComboBox.setRenderer(renderer);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, isSynchronized, needDetectConcurrentModifications);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean isSynchronized, boolean needDetectConcurrentModifications) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", isSynchronized, needDetectConcurrentModifications);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, boolean addElementNullDefault, String textNoSeleccionable) {
        JComboBoxUtils.setProperties(jComboBox, list, addElementNullDefault, textNoSeleccionable, false, true);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list, String textNoSeleccionable) {
        JComboBoxUtils.setProperties(jComboBox, list, true, textNoSeleccionable, false, true);
    }
    
    public static <E> void setProperties(final JComboBox<E> jComboBox, List<E> list) {
        JComboBoxUtils.setProperties(jComboBox, list, true, "- Seleccione -", false, true);
    }
    
}
