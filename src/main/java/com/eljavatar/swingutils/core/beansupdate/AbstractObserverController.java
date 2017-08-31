package com.eljavatar.swingutils.core.beansupdate;

import com.eljavatar.swingutils.core.annotations.ComboBoxView;
import com.eljavatar.swingutils.core.annotations.ComponentView;
import com.eljavatar.swingutils.core.annotations.ModelBean;
import com.eljavatar.swingutils.core.annotations.TextView;
import com.eljavatar.swingutils.util.StringUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;
import com.eljavatar.swingutils.core.annotations.DateTextView;
import com.eljavatar.swingutils.core.annotations.NumberTextView;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import com.eljavatar.swingutils.core.annotations.ToggleButtonView;
import java.text.ParsePosition;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <C> Controlador que debe extender de la clase Observer
 * @param <V> Vista
 */
public class AbstractObserverController<C extends Observer, V> implements Observer {

    private C controller;
    private V view;
    private AbstractObservable<C> observable;
    
    public AbstractObserverController() {
        // Constructor vacio por defecto
    }
    
    public AbstractObserverController(C controller, V view) {
        setListeners(controller, view);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ObjectUpdate) {
            ObjectUpdate objectUpdate = (ObjectUpdate) arg;
            actualizar(controller, view, objectUpdate.getTipoUpdateEnum(), objectUpdate.getComponent(), objectUpdate.getListComponents());
        } else if (arg instanceof TipoUpdateEnum) {
            TipoUpdateEnum tipoUpdateEnum = (TipoUpdateEnum) arg;
            actualizar(controller, view, tipoUpdateEnum, null, null);
        } else {
            throw new IllegalArgumentException("No es un tipo de objeto permitido");
        }
    }
    
    public final void setListeners(C controller, V view) {
        this.controller = controller;
        this.view = view;
        this.observable = new AbstractObservable<>(controller);
    }
    
    /**
     * 
     * @param object Indica el tipo de Actualizavion (Vista o Modelo) y/o los objetos a actualizar.
     *               <code>TipoUpdateEnum</code> lo usamos cuando sólo deseamos que se actualice toda la vista o todo el modelo.
     *               <code>ObjectUpdate</code> lo usamos cuando deseamos que solo se actualicen ciertos objetos en especifico
     */
    protected void changeData(Object object) {
        this.observable.changeData(object);
    }
    
    
    
    private boolean presentAnnotation(Field fieldView) {
        return (fieldView.isAnnotationPresent(TextView.class)
                || fieldView.isAnnotationPresent(DateTextView.class)
                || fieldView.isAnnotationPresent(NumberTextView.class)
                || fieldView.isAnnotationPresent(ToggleButtonView.class)
                || fieldView.isAnnotationPresent(ComboBoxView.class)
                || fieldView.isAnnotationPresent(ComponentView.class));
    }
    
    private String getNameAnnotationComponentView(Field fieldView) throws IllegalArgumentException {
        TextView annotationTextView = fieldView.getAnnotation(TextView.class);
        DateTextView annotationDateTextView = fieldView.getAnnotation(DateTextView.class);
        NumberTextView annotationNumberTextView = fieldView.getAnnotation(NumberTextView.class);
        ToggleButtonView annotationCheckBoxView = fieldView.getAnnotation(ToggleButtonView.class);
        ComboBoxView annotationComboBoxView = fieldView.getAnnotation(ComboBoxView.class);
        ComponentView annotationComponentView = fieldView.getAnnotation(ComponentView.class);

        String nameComponentView = null;
        int count = 0;
        
        if (annotationTextView != null && annotationTextView instanceof TextView) {
            nameComponentView = annotationTextView.name();
            count++;
        }
        if (annotationDateTextView != null && annotationDateTextView instanceof DateTextView) {
            nameComponentView = annotationDateTextView.name();
            count++;
        }
        if (annotationNumberTextView != null && annotationNumberTextView instanceof NumberTextView) {
            nameComponentView = annotationNumberTextView.name();
            count++;
        }
        if (annotationCheckBoxView != null && annotationCheckBoxView instanceof ToggleButtonView) {
            nameComponentView = annotationCheckBoxView.name();
            count++;
        }
        if (annotationComboBoxView != null && annotationComboBoxView instanceof ComboBoxView) {
            nameComponentView = annotationComboBoxView.name();
            count++;
        }
        if (annotationComponentView != null && annotationComponentView instanceof ComponentView) {
            nameComponentView = annotationComponentView.name();
            count++;
        }
        
        if (count > 1) {
            throw new IllegalArgumentException("El atributo " + fieldView.getName() + " tiene mas de una anotacion");
        }
        
        return nameComponentView;
    }
    
    public void actualizar(C controller, V view, TipoUpdateEnum tipoUpdateEnum, String component, List<String> listComponents) {
        
        Class classController = controller.getClass();
        Class classView = view.getClass();
        
        Field[] fieldsController = classController.getDeclaredFields();
        Field[] fieldsView = classView.getDeclaredFields();
        
        for (Field fieldView : fieldsView) {
            fieldView.setAccessible(true);
            Class typeComponentView = fieldView.getType();
            
            
            if (presentAnnotation(fieldView)) {

                String nameComponentView = null;
                try {
                    nameComponentView = getNameAnnotationComponentView(fieldView);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
                
                String[] values = nameComponentView.split("\\.");
                String[] valuesComponent = component != null ? component.split("\\.") : null;
                
                if (values.length != 2) {
                    throw new IllegalArgumentException("El nombre del Componente debe ser: model.atributo");
                }
                if (valuesComponent != null && valuesComponent.length > 2) {
                    throw new IllegalArgumentException("El nombre del Componente a actualizar debe ser: model.atributo ó model");
                }
                
                String textViewNameModel = values[0];
                String textViewNameField = values[1];
                
                if (valuesComponent != null && valuesComponent.length == 1 && !Objects.equals(textViewNameModel, valuesComponent[0])) {
                    continue;
                }
                if (valuesComponent != null && valuesComponent.length == 2 && !Objects.equals(textViewNameField, valuesComponent[1])) {
                    continue;
                }
                
                if (listComponents != null) {
                    Iterator<String> itr = listComponents.iterator();
                    boolean coincide = false;
                    while (itr.hasNext()) {
                        String comp = itr.next();
                        
                        String[] valuesComp = comp != null ? comp.split("\\.") : null;
                        
                        if (valuesComp != null && valuesComp.length > 2) {
                            throw new IllegalArgumentException("El nombre de los Componentes a actualizar debe ser: model.atributo ó model");
                        }
                        
                        if (valuesComp != null && valuesComp.length == 1 && Objects.equals(textViewNameModel, valuesComp[0])) {
                            coincide = true;
                            break;
                        }
                        if (valuesComp != null && valuesComp.length == 2 && Objects.equals(textViewNameField, valuesComp[1])) {
                            coincide = true;
                            break;
                        }
                    }
                    if (!coincide) {
                        continue;
                    }
                }
                
                
                for (Field fieldController : fieldsController) {
                    fieldController.setAccessible(true);
                    String nameController = fieldController.getName();
                    //Class typeController = fieldController.getType();
                    
                    ModelBean annotationModelBean = fieldController.getAnnotation(ModelBean.class);
                    
                    
                    if (annotationModelBean != null && annotationModelBean instanceof ModelBean) {
                        String nameModelBean = !annotationModelBean.name().trim().isEmpty() ? annotationModelBean.name().trim() : nameController;
                        
                        
                        if (Objects.equals(textViewNameModel, nameModelBean)) {
                            try {
                                Object objectModelBean = fieldController.get(controller);
                                if (objectModelBean == null) {
                                    throw new NullPointerException("ModelBean " + nameModelBean + " no se ha inicializado");
                                }

                                Class classModel = objectModelBean.getClass();
                                Field[] fieldsModel = classModel.getDeclaredFields();
                                
                                for (Field fieldModel : fieldsModel) {
                                    fieldModel.setAccessible(true);
                                    Class typeFieldModel = fieldModel.getType();
                                    String nameFieldModel = fieldModel.getName();
                                    
                                    
                                    if (Objects.equals(textViewNameField, nameFieldModel)) {
                                        
                                        Object valueComponentView = fieldView.get(view);
                                        if (valueComponentView == null) {
                                            throw new NullPointerException("Component in View " + nameComponentView + " no se ha inicializado");
                                        }
                                        
                                        
                                        if (fieldView.isAnnotationPresent(TextView.class)) {
                                            updateGenericTextViewData(typeComponentView, valueComponentView, nameComponentView, fieldModel, typeFieldModel, objectModelBean, tipoUpdateEnum, null);
                                            
                                        } else if (fieldView.isAnnotationPresent(DateTextView.class)) {
                                            DateTextView annotationDateTextView = fieldView.getAnnotation(DateTextView.class);
                                            String pattern = annotationDateTextView.pattern();
                                            
                                            updateGenericTextViewData(typeComponentView, valueComponentView, nameComponentView, fieldModel, typeFieldModel, objectModelBean, tipoUpdateEnum, pattern);
                                            
                                        } else if (fieldView.isAnnotationPresent(NumberTextView.class)) {
                                            NumberTextView annotationNumberTextView = fieldView.getAnnotation(NumberTextView.class);
                                            String pattern = annotationNumberTextView.pattern();
                                            
                                            updateGenericTextViewData(typeComponentView, valueComponentView, nameComponentView, fieldModel, typeFieldModel, objectModelBean, tipoUpdateEnum, pattern);
                                            
                                        } else if (fieldView.isAnnotationPresent(ToggleButtonView.class)) {
                                            if (JToggleButton.class.isAssignableFrom(typeComponentView)) {
                                                JToggleButton jToggleButton = (JToggleButton) valueComponentView;
                                                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                                                    fieldModel.set(objectModelBean, jToggleButton.isSelected());
                                                }
                                                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                                                    if (typeFieldModel == Boolean.class || typeFieldModel == boolean.class) {
                                                        Boolean bool = (Boolean) fieldModel.get(objectModelBean);
                                                        jToggleButton.setSelected(bool != null ? bool : Boolean.FALSE);
                                                    } else {
                                                        throw new IllegalArgumentException("Atributo in Model " + nameFieldModel + " no es de tipo boolean");
                                                    }
                                                }
                                                
                                            } else {
                                                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo de JToggleButton in java swing");
                                            }
                                        }  else if (fieldView.isAnnotationPresent(ComboBoxView.class)) {
                                            if (typeComponentView == JComboBox.class) {
                                                JComboBox jCBcombo = (JComboBox) valueComponentView;
                                                ComboBoxModel model = jCBcombo.getModel();
                                                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                                                    fieldModel.set(objectModelBean, model.getSelectedItem());
                                                }
                                                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                                                    model.setSelectedItem(fieldModel.get(objectModelBean));
                                                }
                                            } else {
                                                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo de ComboBox in java swing");
                                            }
                                        } else {
                                            // ComponentView
                                            ComponentView annotationComponentView = fieldView.getAnnotation(ComponentView.class);
                                            String nameProperty = annotationComponentView.nameProperty().trim();
                                            
                                            for (Method m : typeComponentView.getMethods()) {
                                                
                                                if (m.getName().equals("get" + StringUtils.capitalize(nameProperty))) {
                                                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                                                        fieldModel.set(objectModelBean, m.invoke(valueComponentView));
                                                    }
                                                }
                                                
                                                if (m.getName().equals("set" + StringUtils.capitalize(nameProperty))) {
                                                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                                                        m.invoke(valueComponentView, fieldModel.get(objectModelBean));
                                                    }
                                                }
                                            }
                                        }
                                        
                                    }
                                    
                                }
                                
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "IllegalArgumentException", ex);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "IllegalAccessException", ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "InvocationTargetException", ex);
                            }
                            
                            break;
                        }
                        
                    }
                }
            }
            
        }
        
    }
    
    
    private void updateGenericTextViewData(Class typeComponentView, Object valueComponentView, String nameComponentView, Field fieldModel, Class typeFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, String pattern) throws IllegalArgumentException, IllegalAccessException {
        if (JTextComponent.class.isAssignableFrom(typeComponentView)) {
            JTextComponent jTFfield = (JTextComponent) valueComponentView;
            setTextData(fieldModel, typeFieldModel, objectModelBean, tipoUpdateEnum, jTFfield, pattern);
        } else {
            throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo de texto in java swing");
        }
    }
    
    
    private void setTextData(Field fieldModel, Class typeFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, JTextComponent jTextComponent, String pattern) throws IllegalArgumentException, IllegalAccessException {
        if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
            if (getTextFromComponent(jTextComponent).isEmpty()) {
                fieldModel.set(objectModelBean, null);
            } else {
                insertIntoFieldModelFromTextComponent(fieldModel, typeFieldModel, objectModelBean, jTextComponent, pattern);
            }
        }

        if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
            insertIntoTextComponentFromFieldModel(fieldModel, typeFieldModel, objectModelBean, jTextComponent, pattern);
        }
    }
    
    
    
    private void insertIntoFieldModelFromTextComponent(Field fieldModel, Class typeFieldModel, Object objectModelBean, JTextComponent jTextComponent, String pattern) throws IllegalArgumentException, IllegalAccessException {
        try  {
            if (typeFieldModel == String.class) {
                fieldModel.set(objectModelBean, getTextFromComponent(jTextComponent));
            } else if (typeFieldModel == Character.class || typeFieldModel == char.class) {
                fieldModel.set(objectModelBean, getTextFromComponent(jTextComponent).charAt(0));
            } else if (Number.class.isAssignableFrom(typeFieldModel)) {
                // https://www.ibm.com/developerworks/library/j-numberformat/index.html
                DecimalFormat decimalFormat = null;
                if (pattern != null && !pattern.isEmpty()) {
                    decimalFormat = new DecimalFormat(pattern);
                }
                if (typeFieldModel == Byte.class || typeFieldModel == byte.class) {
                    fieldModel.set(objectModelBean, decimalFormat != null ? Byte.parseByte(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Byte.parseByte(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Short.class || typeFieldModel == short.class) {
                    fieldModel.set(objectModelBean, decimalFormat != null ? Short.parseShort(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Short.parseShort(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Integer.class || typeFieldModel == int.class) {
                    fieldModel.set(objectModelBean, decimalFormat != null ? Integer.parseInt(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Integer.parseInt(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Long.class || typeFieldModel == long.class) {
                    fieldModel.set(objectModelBean, decimalFormat != null ? Long.parseLong(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Long.parseLong(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Float.class || typeFieldModel == float.class) {
                    fieldModel.set(objectModelBean, decimalFormat != null ? Float.parseFloat(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Float.parseFloat(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Double.class || typeFieldModel == double.class) {
                    fieldModel.set(objectModelBean, decimalFormat != null ? Double.parseDouble(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Double.parseDouble(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == BigInteger.class) {
                    if (decimalFormat != null) {
                        decimalFormat.setParseBigDecimal(true);
                        BigDecimal valueParsed = (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent));
                        fieldModel.set(objectModelBean, valueParsed.toBigIntegerExact());
                    } else {
                        fieldModel.set(objectModelBean, new BigInteger(getTextFromComponent(jTextComponent)));
                    }
                } else if (typeFieldModel == BigDecimal.class) {
                    if (decimalFormat != null) {
                        decimalFormat.setParseBigDecimal(true);
                        fieldModel.set(objectModelBean, (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent)));
                    } else {
                        fieldModel.set(objectModelBean, new BigDecimal(getTextFromComponent(jTextComponent)));
                    }
                }
            } else if (typeFieldModel == Boolean.class || typeFieldModel == boolean.class) {
                fieldModel.set(objectModelBean, Boolean.parseBoolean(getTextFromComponent(jTextComponent)));
            } else if (java.util.Date.class.isAssignableFrom(typeFieldModel)) {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setLenient(false);
                if (pattern != null && !pattern.isEmpty()) {
                    sdf = new SimpleDateFormat(pattern);
                }
                if (typeFieldModel == java.sql.Time.class) {
                    fieldModel.set(objectModelBean, new java.sql.Time(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else if (typeFieldModel == java.sql.Date.class) {
                    fieldModel.set(objectModelBean, new java.sql.Date(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else if (typeFieldModel == java.sql.Timestamp.class) {
                    fieldModel.set(objectModelBean, new java.sql.Timestamp(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else {
                    fieldModel.set(objectModelBean, sdf.parse(getTextFromComponent(jTextComponent)));
                }
            } else if (java.time.temporal.Temporal.class.isAssignableFrom(typeFieldModel)) {
                DateTimeFormatter formatter = getDateTimeFormatter(typeFieldModel, pattern);
                if (typeFieldModel == LocalDate.class) {
                    fieldModel.set(objectModelBean, LocalDate.parse(getTextFromComponent(jTextComponent), formatter));
                } else if (typeFieldModel == LocalTime.class) {
                    fieldModel.set(objectModelBean, LocalTime.parse(getTextFromComponent(jTextComponent), formatter));
                } else if (typeFieldModel == LocalDateTime.class) {
                    fieldModel.set(objectModelBean, LocalDateTime.parse(getTextFromComponent(jTextComponent), formatter));
                }
            }
        } catch (NumberFormatException ex) {
            //System.out.println("No es un número");
            Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "NumberFormatException", ex);
        } catch (ParseException ex) {
            //System.out.println("No es una fecha o un numero");
            Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "ParseException", ex);
        }
    }
    
    private DateTimeFormatter getDateTimeFormatter(Class typeFieldModel, String pattern) {
        if (pattern != null && !pattern.isEmpty()) {
            return DateTimeFormatter.ofPattern(pattern);
        }
        if (typeFieldModel == LocalDate.class) {
            return DateTimeFormatter.ISO_LOCAL_DATE;
        } else if (typeFieldModel == LocalTime.class) {
            return DateTimeFormatter.ISO_LOCAL_TIME;
        } else if (typeFieldModel == LocalDateTime.class) {
            return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        } else {
            throw new IllegalArgumentException("No se reconoce el tipo de dato de fecha de java.time.*");
        }
    }
    
    private String getTextFromComponent(JTextComponent jTextComponent) {
        if (jTextComponent instanceof JTextField) {
            JTextField jTextField = (JTextField) jTextComponent;
            return jTextField.getText();
        } else if (jTextComponent instanceof JFormattedTextField) {
            JFormattedTextField jFormattedTextField = (JFormattedTextField) jTextComponent;
            return jFormattedTextField.getText();
        } else if (jTextComponent instanceof JPasswordField) {
            JPasswordField jPasswordField = (JPasswordField) jTextComponent;
            return jPasswordField.getText();
        } else if (jTextComponent instanceof JTextArea) {
            JTextArea jTextArea = (JTextArea) jTextComponent;
            return jTextArea.getText();
        } else if (jTextComponent instanceof JEditorPane) {
            JEditorPane jEditorPane = (JEditorPane) jTextComponent;
            return jEditorPane.getText();
        } else if (jTextComponent instanceof JTextPane) {
            JTextPane jTextPane = (JTextPane) jTextComponent;
            return jTextPane.getText();
        } else {
            throw new IllegalArgumentException("No se reconoce el tipo de componente como de tipo dee texto");
        }
    }
    
    private void insertIntoTextComponentFromFieldModel(Field fieldModel, Class typeFieldModel, Object objectModelBean, JTextComponent jTextComponent, String pattern) throws IllegalArgumentException, IllegalAccessException {
        String text = getTextFormattedForInsertIntoComponent(fieldModel, typeFieldModel, objectModelBean, pattern);
        if (jTextComponent instanceof JTextField) {
            JTextField jTextField = (JTextField) jTextComponent;
            jTextField.setText(text);
        } else if (jTextComponent instanceof JFormattedTextField) {
            JFormattedTextField jFormattedTextField = (JFormattedTextField) jTextComponent;
            jFormattedTextField.setText(text);
        } else if (jTextComponent instanceof JPasswordField) {
            JPasswordField jPasswordField = (JPasswordField) jTextComponent;
            jPasswordField.setText(text);
        } else if (jTextComponent instanceof JTextArea) {
            JTextArea jTextArea = (JTextArea) jTextComponent;
            jTextArea.setText(text);
        } else if (jTextComponent instanceof JEditorPane) {
            JEditorPane jEditorPane = (JEditorPane) jTextComponent;
            jEditorPane.setText(text);
        } else if (jTextComponent instanceof JTextPane) {
            JTextPane jTextPane = (JTextPane) jTextComponent;
            jTextPane.setText(text);
        } else {
            throw new IllegalArgumentException("No se reconoce el tipo de componente como de tipo de texto");
        }
    }
    
    private String getTextFormattedForInsertIntoComponent(Field fieldModel, Class typeFieldModel, Object objectModelBean, String pattern) throws IllegalArgumentException, IllegalAccessException {
        Object object = fieldModel.get(objectModelBean);
        if (object == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            return object.toString();
        }
        if (Number.class.isAssignableFrom(typeFieldModel)) {
            DecimalFormat df = new DecimalFormat(pattern);
            return df.format(object);
        } else if (java.util.Date.class.isAssignableFrom(typeFieldModel)) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            if (typeFieldModel == java.sql.Time.class) {
                return sdf.format((java.sql.Time) object);
            } else if (typeFieldModel == java.sql.Date.class) {
                return sdf.format((java.sql.Date) object);
            } else if (typeFieldModel == java.sql.Timestamp.class) {
                return sdf.format((java.sql.Timestamp) object);
            } else {
                return sdf.format((java.util.Date) object);
            }
        } else if (java.time.temporal.Temporal.class.isAssignableFrom(typeFieldModel)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            if (typeFieldModel == LocalDate.class) {
                return ((LocalDate) object).format(formatter);
            } else if (typeFieldModel == LocalTime.class) {
                return ((LocalTime) object).format(formatter);
            } else if (typeFieldModel == LocalDateTime.class) {
                return ((LocalDateTime) object).format(formatter);
            }
        }
        return null;
    }
    
}

