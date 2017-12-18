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
import com.eljavatar.swingutils.core.annotations.PropertyController;
import com.eljavatar.swingutils.core.annotations.TableView;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;
import com.eljavatar.swingutils.core.annotations.ToggleButtonView;
import com.eljavatar.swingutils.core.modelcomponents.TableModelGeneric;
import javax.swing.JTable;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 * @param <C> Controlador a sincronizar
 * @param <V> Vista a sincronizar
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
    
    /**
     * Método con el que se asigna el Controlador y la Vista para que escuchen 
     * los respectivos cambios
     * @param controller Controlador a sincronizar
     * @param view Vista a sincronizar
     */
    public final void setListeners(C controller, V view) {
        this.controller = controller;
        this.view = view;
        this.observable = new AbstractObservable<>(controller);
    }
    
    /**
     * Método con el que se indica que hay cambios en la Vista o el Modelo que deben ser actualizados
     * @param object Indica el tipo de Actualizavion (Vista o Modelo) y/o los objetos a actualizar.
     *               <code>TipoUpdateEnum</code> lo usamos cuando sólo deseamos que se actualice toda la vista o todo el modelo.
     *               <code>ObjectUpdate</code> lo usamos cuando deseamos que solo se actualicen ciertos objetos en especifico
     */
    protected void changeData(Object object) {
        this.observable.changeData(object);
    }
    
    /**
     * Método que obtiene el nombre del componente SwingUtils asignado en el Controlador
     * @param fieldController
     * @return
     * @throws IllegalArgumentException 
     */
    private String getNameAnnotationComponentController(Field fieldController) throws IllegalArgumentException {
        ModelBean annotationModelBean = fieldController.getAnnotation(ModelBean.class);
        PropertyController annotationPropertyController = fieldController.getAnnotation(PropertyController.class);
        
        String nameComponentController = null;
        int count = 0;
        
        if (annotationModelBean != null && annotationModelBean instanceof ModelBean) {
            nameComponentController = !annotationModelBean.name().trim().isEmpty() ? annotationModelBean.name().trim() : fieldController.getName();
            count++;
        }
        if (annotationPropertyController != null && annotationPropertyController instanceof PropertyController) {
            nameComponentController = !annotationPropertyController.name().trim().isEmpty() ? annotationPropertyController.name().trim() : fieldController.getName();
            count++;
        }
        
        if (count > 1) {
            throw new IllegalArgumentException("El atributo " + fieldController.getName() + " en el controlador, tiene mas de una anotacion de SwingUtils");
        }
        
        return nameComponentController;
    }
    
    /**
     * Método que valida si un componente en la Vista contiene alguna de las anotaciones de SwingUtils
     * @param fieldView
     * @return 
     */
    private boolean presentAnnotationView(Field fieldView) {
        return (fieldView.isAnnotationPresent(TextView.class)
                || fieldView.isAnnotationPresent(DateTextView.class)
                || fieldView.isAnnotationPresent(NumberTextView.class)
                || fieldView.isAnnotationPresent(ToggleButtonView.class)
                || fieldView.isAnnotationPresent(ComboBoxView.class)
                || fieldView.isAnnotationPresent(TableView.class)
                || fieldView.isAnnotationPresent(ComponentView.class));
    }
    
    /**
     * Metodo que obtiene el nombre del componente SwingUtils asignado en la Vista
     * @param fieldView
     * @return
     * @throws IllegalArgumentException 
     */
    private String getNameAnnotationComponentView(Field fieldView) throws IllegalArgumentException {
        TextView annotationTextView = fieldView.getAnnotation(TextView.class);
        DateTextView annotationDateTextView = fieldView.getAnnotation(DateTextView.class);
        NumberTextView annotationNumberTextView = fieldView.getAnnotation(NumberTextView.class);
        ToggleButtonView annotationCheckBoxView = fieldView.getAnnotation(ToggleButtonView.class);
        ComboBoxView annotationComboBoxView = fieldView.getAnnotation(ComboBoxView.class);
        TableView annotationTableView = fieldView.getAnnotation(TableView.class);
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
        if (annotationTableView != null && annotationTableView instanceof TableView) {
            nameComponentView = annotationTableView.name();
            count++;
        }
        if (annotationComponentView != null && annotationComponentView instanceof ComponentView) {
            nameComponentView = annotationComponentView.name();
            count++;
        }
        
        if (count > 1) {
            throw new IllegalArgumentException("El atributo " + fieldView.getName() + " en la vista, tiene mas de una anotacion de SwingUtils");
        }
        
        return nameComponentView;
    }
    
    
    /**
     * Método que valida si el nombre de un componente en vista o controlador es válido
     * @param listComponentsToUpdate
     * @param nameComponentToUpdate
     * @param valuesComponentToUpdate
     * @param componentViewNameModel
     * @param componentViewNameField
     * @param nameComponentView
     * @param isModelBean
     * @return 
     */
    private boolean isValidNamesComponentView(List<String> listComponentsToUpdate, String nameComponentToUpdate, String[] valuesComponentToUpdate, String componentViewNameModel, String componentViewNameField, String nameComponentView, boolean isModelBean) {
        // Si tenemos un componente a actualizar, y es solo el nombre del modelo, lo comparamos con el nombre del componente en la Anotacion
        if (isModelBean && valuesComponentToUpdate != null && valuesComponentToUpdate.length == 1 && !Objects.equals(componentViewNameModel, valuesComponentToUpdate[0])) {
            return false;
        }
        // Si tenemos un componente a actualizar, y es modelo.atributo, lo comparamos con el nombre del atributo en la Anotacion
        // Solo comparamos que el nombre del atributo corresponda con el de la anotacion
        if (isModelBean && valuesComponentToUpdate != null && valuesComponentToUpdate.length == 2 && !Objects.equals(componentViewNameField, valuesComponentToUpdate[1])) {
            return false;
        }
        // Si tenemos un componente a actualizar y no es de tipo modelo en el controlador, 
        // lo comparamos con el nombre del componente en la vista
        if (!isModelBean && nameComponentToUpdate != null && !Objects.equals(nameComponentView, nameComponentToUpdate)) {
            return false;
        }

        // Si tenemos una lista de componentes a actualizar hacemos las mismas validaciones
        if (listComponentsToUpdate != null) {
            Iterator<String> itr = listComponentsToUpdate.iterator();
            boolean coincide = false;
            while (itr.hasNext()) {
                String componentToUpdateIntoList = itr.next();

                String[] valuesComponentToUpdateIntoList = componentToUpdateIntoList != null ? componentToUpdateIntoList.split("\\.") : null;

                if (isModelBean && valuesComponentToUpdateIntoList != null && valuesComponentToUpdateIntoList.length > 2) {
                    throw new IllegalArgumentException("El nombre de los Componentes a actualizar debe ser: model.atributo ó model");
                }

                if (isModelBean && valuesComponentToUpdateIntoList != null && valuesComponentToUpdateIntoList.length == 1 && Objects.equals(componentViewNameModel, valuesComponentToUpdateIntoList[0])) {
                    coincide = true;
                    break;
                }
                if (isModelBean && valuesComponentToUpdateIntoList != null && valuesComponentToUpdateIntoList.length == 2 && Objects.equals(componentViewNameField, valuesComponentToUpdateIntoList[1])) {
                    coincide = true;
                    break;
                }
                if (!isModelBean && componentToUpdateIntoList != null && Objects.equals(nameComponentView, componentToUpdateIntoList)) {
                    coincide = true;
                    break;
                }
            }
            if (!coincide) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Método que realiza el proceso de actualizacion en vista o controlador
     * @param controller
     * @param view
     * @param tipoUpdateEnum
     * @param componentToUpdate
     * @param listComponentsToUpdate 
     */
    private void actualizar(C controller, V view, TipoUpdateEnum tipoUpdateEnum, String componentToUpdate, List<String> listComponentsToUpdate) {
        Class classController = controller.getClass();
        Class classView = view.getClass();
        
        // Obtenemos los Field del Controlador
        Field[] fieldsController = classController.getDeclaredFields();
        // Obtenemos los Field de la vista
        Field[] fieldsView = classView.getDeclaredFields();
        
        // Recorremos cada Field de la vista
        for (Field fieldView : fieldsView) {
            fieldView.setAccessible(true);
            Class typeFieldView = fieldView.getType();
            
            if (presentAnnotationView(fieldView)) {

                String nameComponentView = null;
                try {
                    nameComponentView = getNameAnnotationComponentView(fieldView);
                } catch (IllegalArgumentException e) {
                    throw e;
                }
                
                // Recorremos cada Field del controlador por cada Field de la vista
                for (Field fieldController : fieldsController) {
                    fieldController.setAccessible(true);
                    //String nameFieldController = fieldController.getName();
                    Class typeFieldController = fieldController.getType();

                    String nameComponentController = null;
                    try {
                        nameComponentController = getNameAnnotationComponentController(fieldController);
                    } catch (IllegalArgumentException e) {
                        throw e;
                    }
                    
                    try {
                        if (fieldController.isAnnotationPresent(PropertyController.class)) {
                            // Validamos  en caso que haya una lista de componentes a actualizar
                            // Si se especifican los componentes a actualizar y uno de ellos coincide
                            // con el actual, continuamos el proceso, de lo contrario saltamos a la
                            // siguiente iteración de la vista rompiendo el ciclo (Al siguiente Field del View)
                            if (!isValidNamesComponentView(listComponentsToUpdate, componentToUpdate, null, null, null, nameComponentView, false)) {
                                break;
                            }

                            if (Objects.equals(nameComponentView, nameComponentController)) {
                                asignValue(fieldView, nameComponentView, typeFieldView, fieldController, typeFieldController, controller, tipoUpdateEnum, view);

                                break;
                            }

                        } else if (fieldController.isAnnotationPresent(ModelBean.class)) {
                            String[] valuesNameComponentView = nameComponentView.split("\\.");
                            String[] valuesComponentToUpdate = componentToUpdate != null ? componentToUpdate.split("\\.") : null;

                            // Si el nombre del componente en la vista, no empieza con el nombre del componente
                            // En el controlador, pasamos a la siguiente iteracion (Al siguiente Field del controlador)
                            if (!nameComponentView.startsWith(nameComponentController + ".")) {
                                continue;
                            }

                            if (valuesNameComponentView.length != 2) {
                                throw new IllegalArgumentException("El nombre del Componente " + nameComponentView + " debe ser: model.atributo");
                            }

                            if (valuesComponentToUpdate != null && valuesComponentToUpdate.length > 2) {
                                throw new IllegalArgumentException("El nombre del Componente a actualizar debe ser: model.atributo ó model");
                            }

                            String componentViewNameModel = valuesNameComponentView[0];
                            String componentViewNameField = valuesNameComponentView[1];

                            // Validamos  en caso que haya una lista de componentes a actualizar
                            // Si se especifican los componentes a actualizar y uno de ellos coincide
                            // con el actual, continuamos el proceso, de lo contrario saltamos a la
                            // siguiente iteración (Al siguiente Field)
                            if (!isValidNamesComponentView(listComponentsToUpdate, componentToUpdate, valuesComponentToUpdate, componentViewNameModel, componentViewNameField, null, true)) {
                                break;
                            }

                            if (Objects.equals(componentViewNameModel, nameComponentController)) {
                                Object objectModelBean = fieldController.get(controller);
                                if (objectModelBean == null) {
                                    throw new NullPointerException("ModelBean " + nameComponentController + " no se ha inicializado");
                                }

                                Class classModel = objectModelBean.getClass();
                                //Field[] fieldsModel = classModel.getDeclaredFields();
                                // Obtenemos el Field del Modelo para hacer la asignación de valores
                                Field fieldModel = classModel.getDeclaredField(componentViewNameField);
                                fieldModel.setAccessible(true);
                                Class typeFieldModel = fieldModel.getType();
                                
                                asignValue(fieldView, nameComponentView, typeFieldView, fieldModel, typeFieldModel, objectModelBean, tipoUpdateEnum, view);

                                break;
                            }
                        }
                        
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "IllegalArgumentException", ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "IllegalAccessException", ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "InvocationTargetException", ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "SecurityException", ex);
                    } catch (NullPointerException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "NullPointerException", ex);
                    } catch (NoSuchMethodException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "NoSuchMethodException", ex);
                    } catch (NoSuchFieldException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "NoSuchFieldException", ex);
                    }
                }
            }
        }
    }
    
    /**
     * Método que asigna un valor a un componente en la vista o en el controlador
     * @param fieldView
     * @param nameComponentView
     * @param typeComponentView
     * @param fieldModel
     * @param nameFieldModel
     * @param typeFieldModel
     * @param objectModelBean
     * @param tipoUpdateEnum
     * @param view
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    private void asignValue(Field fieldView, String nameComponentView, Class typeComponentView, Field fieldModel, Class typeFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, V view) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Object objectComponentView = fieldView.get(view);
        if (objectComponentView == null) {
            throw new NullPointerException("Component in View " + nameComponentView + " is null");
        }
        
        // Obtenemos el metodo get y set del atributo del modelo o propiedad en el controlador
        Method methodSetFieldModel = null;
        Method methodGetFieldModel = null;
        try {
            methodSetFieldModel = objectModelBean.getClass().getMethod("set" + StringUtils.capitalize(fieldModel.getName()) , typeFieldModel);
        } catch (NoSuchMethodException ex) {
            throw new NoSuchMethodException("El atributo " + fieldModel.getName() + " no tiene metodo público set");
        }
        try {
            methodGetFieldModel = objectModelBean.getClass().getMethod("get" + StringUtils.capitalize(fieldModel.getName()) , (Class<?>[]) null);
        } catch (NoSuchMethodException ex) {
            throw new NoSuchMethodException("El atributo " + fieldModel.getName() + " no tiene metodo público get");
        }

        if (fieldView.isAnnotationPresent(TextView.class)) {
            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, null);

        } else if (fieldView.isAnnotationPresent(DateTextView.class)) {
            DateTextView annotationDateTextView = fieldView.getAnnotation(DateTextView.class);
            String pattern = annotationDateTextView.pattern();

            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, pattern);

        } else if (fieldView.isAnnotationPresent(NumberTextView.class)) {
            NumberTextView annotationNumberTextView = fieldView.getAnnotation(NumberTextView.class);
            String pattern = annotationNumberTextView.pattern();

            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, pattern);

        } else if (fieldView.isAnnotationPresent(ToggleButtonView.class)) {
            if (JToggleButton.class.isAssignableFrom(typeComponentView)) {
                JToggleButton jToggleButton = (JToggleButton) objectComponentView;
                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                    //fieldModel.set(objectModelBean, jToggleButton.isSelected());
                    methodSetFieldModel.invoke(objectModelBean, jToggleButton.isSelected());
                }
                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                    if (typeFieldModel == Boolean.class || typeFieldModel == boolean.class) {
                        //Boolean bool = (Boolean) fieldModel.get(objectModelBean);
                        Boolean bool = (Boolean) methodGetFieldModel.invoke(objectModelBean);
                        jToggleButton.setSelected(bool != null ? bool : Boolean.FALSE);
                    } else {
                        throw new IllegalArgumentException("Atributo in Model " + fieldModel.getName() + " no es de tipo boolean");
                    }
                }

            } else {
                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo JToggleButton en java swing");
            }
        } else if (fieldView.isAnnotationPresent(ComboBoxView.class)) {
            if (typeComponentView == JComboBox.class) {
                JComboBox jCBcombo = (JComboBox) objectComponentView;
                ComboBoxModel model = jCBcombo.getModel();
                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                    //fieldModel.set(objectModelBean, model.getSelectedItem());
                    methodSetFieldModel.invoke(objectModelBean, model.getSelectedItem());
                }
                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                    //model.setSelectedItem(fieldModel.get(objectModelBean));
                    model.setSelectedItem(methodGetFieldModel.invoke(objectModelBean));
                }
            } else {
                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo ComboBox en java swing");
            }
        } else if (fieldView.isAnnotationPresent(TableView.class)) {
            if (typeComponentView == JTable.class) {
                JTable jTable = (JTable) objectComponentView;
                TableModelGeneric model = (TableModelGeneric) jTable.getModel();
                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                    //fieldModel.set(objectModelBean, model.getListElements());
                    methodSetFieldModel.invoke(objectModelBean, model.getListElements());
                }
                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                    //model.setListElements((List) fieldModel.get(objectModelBean));
                    model.setListElements((List) methodGetFieldModel.invoke(objectModelBean));
                    model.fireTableDataChanged();
                }
            } else {
                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo Table en java swing");
            }
        } else {
            // ComponentView
            ComponentView annotationComponentView = fieldView.getAnnotation(ComponentView.class);
            String nameProperty = annotationComponentView.nameProperty().trim();

            for (Method m : typeComponentView.getMethods()) {

                if (m.getName().equals("get" + StringUtils.capitalize(nameProperty))) {
                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
                        //fieldModel.set(objectModelBean, m.invoke(objectComponentView));
                        methodSetFieldModel.invoke(objectModelBean, m.invoke(objectComponentView));
                    }
                }

                if (m.getName().equals("set" + StringUtils.capitalize(nameProperty))) {
                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
                        //m.invoke(objectComponentView, fieldModel.get(objectModelBean));
                        m.invoke(objectComponentView, methodGetFieldModel.invoke(objectModelBean));
                    }
                }
            }
        }
    }
    
    /**
     * Método que realiza la actualización de valores cuando el componente java swing es de tipo JTextComponent
     * @param typeComponentView
     * @param valueComponentView
     * @param nameComponentView
     * @param fieldModel
     * @param typeFieldModel
     * @param objectModelBean
     * @param tipoUpdateEnum
     * @param pattern
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private void updateGenericTextViewData(Class typeComponentView, Object valueComponentView, String nameComponentView, Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Method methodGetFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, String pattern) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (JTextComponent.class.isAssignableFrom(typeComponentView)) {
            JTextComponent jTFfield = (JTextComponent) valueComponentView;
            setTextData(fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, jTFfield, pattern);
        } else {
            throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo texto (JTextComponent) en java swing");
        }
    }
    
    /**
     * Método que asigna texto cuando el componente java swing es de tipo JTextComponent
     * @param fieldModel
     * @param typeFieldModel
     * @param objectModelBean
     * @param tipoUpdateEnum
     * @param jTextComponent
     * @param pattern
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private void setTextData(Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Method methodGetFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, JTextComponent jTextComponent, String pattern) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
            if (getTextFromComponent(jTextComponent).isEmpty()) {
                methodSetFieldModel.invoke(objectModelBean, (Object) null);
                //fieldModel.set(objectModelBean, null);
            } else {
                insertIntoFieldModelFromTextComponent(fieldModel, typeFieldModel, methodSetFieldModel, objectModelBean, jTextComponent, pattern);
            }
        }

        if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
            insertIntoTextComponentFromFieldModel(fieldModel, typeFieldModel, methodGetFieldModel, objectModelBean, jTextComponent, pattern);
        }
    }
    
    /**
     * Método que inserta texto en un atributo del modelo según su tipo de dato y cuando el componente
     * java swing en la vista es de tipo JTextComponent
     * @param fieldModel
     * @param typeFieldModel
     * @param objectModelBean
     * @param jTextComponent
     * @param pattern
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private void insertIntoFieldModelFromTextComponent(Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Object objectModelBean, JTextComponent jTextComponent, String pattern) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        try  {
            if (typeFieldModel == String.class) {
                //fieldModel.set(objectModelBean, getTextFromComponent(jTextComponent));
                methodSetFieldModel.invoke(objectModelBean, getTextFromComponent(jTextComponent));
            } else if (typeFieldModel == Character.class || typeFieldModel == char.class) {
                //fieldModel.set(objectModelBean, getTextFromComponent(jTextComponent).charAt(0));
                methodSetFieldModel.invoke(objectModelBean, getTextFromComponent(jTextComponent).charAt(0));
            } else if (Number.class.isAssignableFrom(typeFieldModel)) {
                // https://www.ibm.com/developerworks/library/j-numberformat/index.html
                DecimalFormat decimalFormat = null;
                if (pattern != null && !pattern.isEmpty()) {
                    decimalFormat = new DecimalFormat(pattern);
                }
                if (typeFieldModel == Byte.class || typeFieldModel == byte.class) {
                    //fieldModel.set(objectModelBean, decimalFormat != null ? Byte.parseByte(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Byte.parseByte(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Byte.parseByte(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Byte.parseByte(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Short.class || typeFieldModel == short.class) {
                    //fieldModel.set(objectModelBean, decimalFormat != null ? Short.parseShort(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Short.parseShort(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Short.parseShort(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Short.parseShort(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Integer.class || typeFieldModel == int.class) {
                    //fieldModel.set(objectModelBean, decimalFormat != null ? Integer.parseInt(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Integer.parseInt(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Integer.parseInt(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Integer.parseInt(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Long.class || typeFieldModel == long.class) {
                    //fieldModel.set(objectModelBean, decimalFormat != null ? Long.parseLong(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Long.parseLong(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Long.parseLong(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Long.parseLong(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Float.class || typeFieldModel == float.class) {
                    //fieldModel.set(objectModelBean, decimalFormat != null ? Float.parseFloat(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Float.parseFloat(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Float.parseFloat(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Float.parseFloat(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Double.class || typeFieldModel == double.class) {
                    //fieldModel.set(objectModelBean, decimalFormat != null ? Double.parseDouble(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Double.parseDouble(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Double.parseDouble(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Double.parseDouble(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == BigInteger.class) {
                    if (decimalFormat != null) {
                        decimalFormat.setParseBigDecimal(true);
                        BigDecimal valueParsed = (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent));
                        //fieldModel.set(objectModelBean, valueParsed.toBigIntegerExact());
                        methodSetFieldModel.invoke(objectModelBean,  valueParsed.toBigIntegerExact());
                    } else {
                        //fieldModel.set(objectModelBean, new BigInteger(getTextFromComponent(jTextComponent)));
                        methodSetFieldModel.invoke(objectModelBean, new BigInteger(getTextFromComponent(jTextComponent)));
                    }
                } else if (typeFieldModel == BigDecimal.class) {
                    if (decimalFormat != null) {
                        decimalFormat.setParseBigDecimal(true);
                        //fieldModel.set(objectModelBean, (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent)));
                        methodSetFieldModel.invoke(objectModelBean,  (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent)));
                    } else {
                        //fieldModel.set(objectModelBean, new BigDecimal(getTextFromComponent(jTextComponent)));
                        methodSetFieldModel.invoke(objectModelBean,  new BigDecimal(getTextFromComponent(jTextComponent)));
                    }
                }
            } else if (typeFieldModel == Boolean.class || typeFieldModel == boolean.class) {
                //fieldModel.set(objectModelBean, Boolean.parseBoolean(getTextFromComponent(jTextComponent)));
                methodSetFieldModel.invoke(objectModelBean, Boolean.parseBoolean(getTextFromComponent(jTextComponent)));
            } else if (java.util.Date.class.isAssignableFrom(typeFieldModel)) {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setLenient(false);
                if (pattern != null && !pattern.isEmpty()) {
                    sdf = new SimpleDateFormat(pattern);
                }
                if (typeFieldModel == java.sql.Time.class) {
                    //fieldModel.set(objectModelBean, new java.sql.Time(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                    methodSetFieldModel.invoke(objectModelBean,  new java.sql.Time(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else if (typeFieldModel == java.sql.Date.class) {
                    //fieldModel.set(objectModelBean, new java.sql.Date(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                    methodSetFieldModel.invoke(objectModelBean,  new java.sql.Date(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else if (typeFieldModel == java.sql.Timestamp.class) {
                    //fieldModel.set(objectModelBean, new java.sql.Timestamp(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                    methodSetFieldModel.invoke(objectModelBean,  new java.sql.Timestamp(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else {
                    //fieldModel.set(objectModelBean, sdf.parse(getTextFromComponent(jTextComponent)));
                    methodSetFieldModel.invoke(objectModelBean,  sdf.parse(getTextFromComponent(jTextComponent)));
                }
            } else if (java.time.temporal.Temporal.class.isAssignableFrom(typeFieldModel)) {
                DateTimeFormatter formatter = getDateTimeFormatter(typeFieldModel, pattern);
                if (typeFieldModel == LocalDate.class) {
                    //fieldModel.set(objectModelBean, LocalDate.parse(getTextFromComponent(jTextComponent), formatter));
                    methodSetFieldModel.invoke(objectModelBean,  LocalDate.parse(getTextFromComponent(jTextComponent), formatter));
                } else if (typeFieldModel == LocalTime.class) {
                    //fieldModel.set(objectModelBean, LocalTime.parse(getTextFromComponent(jTextComponent), formatter));
                    methodSetFieldModel.invoke(objectModelBean,  LocalTime.parse(getTextFromComponent(jTextComponent), formatter));
                } else if (typeFieldModel == LocalDateTime.class) {
                    //fieldModel.set(objectModelBean, LocalDateTime.parse(getTextFromComponent(jTextComponent), formatter));
                    methodSetFieldModel.invoke(objectModelBean,  LocalDateTime.parse(getTextFromComponent(jTextComponent), formatter));
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "NumberFormatException", ex);
        } catch (ParseException ex) {
            Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "ParseException", ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "IllegalArgumentException", ex);
        }
    }
    
    /**
     * Método que obtiene el formato de fecha según el pattern indicado por el usuario
     * @param typeFieldModel
     * @param pattern
     * @return 
     */
    private DateTimeFormatter getDateTimeFormatter(Class typeFieldModel, String pattern) throws IllegalArgumentException {
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
            throw new IllegalArgumentException("No se reconoce el tipo de dato de fecha en java.time.*");
        }
    }
    
    /**
     * Método que obtiene el texto de un componente java swing de tipo JTextComponent
     * @param jTextComponent
     * @return 
     */
    private String getTextFromComponent(JTextComponent jTextComponent) throws IllegalArgumentException {
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
            throw new IllegalArgumentException("El componente " + jTextComponent.getClass().getName() + " no es de tipo texto (JTextComponent) en la vista");
        }
    }
    
    /**
     * Método que inserta texto en un componente java swing de tipo JTextComponent
     * desde su correspondiente atributo en el modelo
     * @param fieldModel
     * @param typeFieldModel
     * @param objectModelBean
     * @param jTextComponent
     * @param pattern
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private void insertIntoTextComponentFromFieldModel(Field fieldModel, Class typeFieldModel, Method methodGetFieldModel, Object objectModelBean, JTextComponent jTextComponent, String pattern) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String text = getTextFormattedForInsertIntoComponent(fieldModel, typeFieldModel, methodGetFieldModel, objectModelBean, pattern);
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
            throw new IllegalArgumentException("El componente correspondiente al atributo " + fieldModel.getName() + " no es de tipo texto (JTextComponent) en la vista");
        }
    }
    
    /**
     * Método que obtiene el texto formateado de un atributo en el modelo para insertarlo en
     * su correspondiente componente java swing cuando este es de tipo JTextComponent
     * @param fieldModel
     * @param typeFieldModel
     * @param objectModelBean
     * @param pattern
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException 
     */
    private String getTextFormattedForInsertIntoComponent(Field fieldModel, Class typeFieldModel, Method methodGetFieldModel, Object objectModelBean, String pattern) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object object = methodGetFieldModel.invoke(objectModelBean);
        //Object object = fieldModel.get(objectModelBean);
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

