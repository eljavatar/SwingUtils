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
package com.eljavatar.swingutils.core.beansupdate;

import com.eljavatar.swingutils.core.annotations.ComboBoxView;
import com.eljavatar.swingutils.core.annotations.ComponentView;
import com.eljavatar.swingutils.core.annotations.ModelBean;
import com.eljavatar.swingutils.core.annotations.TextView;
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
import java.util.Iterator;
import java.util.List;
import com.eljavatar.swingutils.core.annotations.ToggleButtonView;
import com.ibm.icu.text.DecimalFormat;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import com.eljavatar.swingutils.core.annotations.PaginatedTableView;
import com.eljavatar.swingutils.core.annotations.PasswordTextView;
import com.eljavatar.swingutils.core.componentsutils.NotifyUtils;
import com.eljavatar.swingutils.util.DigestEnum;
import com.eljavatar.swingutils.util.DigestUtils;
import com.ibm.icu.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

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
    private boolean failedValidation;
    
    public AbstractObserverController() {
        //ApplicationContext.getInstance(null);
    }
    
    public AbstractObserverController(C controller, V view) {
        setListeners(controller, view);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        this.failedValidation = false;
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
    
    public boolean hasFailedValidation() {
        boolean hasFailed = failedValidation;
        this.failedValidation = false;
        return hasFailed;
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
        //ApplicationContext.getInstance().clearListNotify();
        
        this.observable.changeData(object);


//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (Notify notify : ApplicationContext.getInstance().getListNotifyError()) {
//                    notify.showError();
//                }
//                for (Notify notify : ApplicationContext.getInstance().getListNotifyWarning()) {
//                    notify.showWarning();
//                }
//            }
//        });
//        t.run();


        //ApplicationContext.getInstance().clearListNotify();
    }
    
    
    private static ClassLoader getThreadContextLoader() throws IllegalAccessException, InvocationTargetException {
        // Si ejecutamos sobre JDK 1.2 o superior
        Method method;
        try {
            method = Thread.class.getMethod("getContextClassLoader");
        } catch (NoSuchMethodException e) {
            // En caso de que ejecutemos sobre JDK 1.1 o inferior
            return null;
        }
        return (ClassLoader) method.invoke(Thread.currentThread());
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
                || fieldView.isAnnotationPresent(PasswordTextView.class)
                || fieldView.isAnnotationPresent(ToggleButtonView.class)
                || fieldView.isAnnotationPresent(ComboBoxView.class)
                || fieldView.isAnnotationPresent(TableView.class)
                || fieldView.isAnnotationPresent(PaginatedTableView.class)
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
        PasswordTextView annotationPasswordTextView = fieldView.getAnnotation(PasswordTextView.class);
        ToggleButtonView annotationCheckBoxView = fieldView.getAnnotation(ToggleButtonView.class);
        ComboBoxView annotationComboBoxView = fieldView.getAnnotation(ComboBoxView.class);
        TableView annotationTableView = fieldView.getAnnotation(TableView.class);
        ComponentView annotationComponentView = fieldView.getAnnotation(ComponentView.class);
        PaginatedTableView annotationPaginationTableView = fieldView.getAnnotation(PaginatedTableView.class);

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
        if (annotationPasswordTextView != null && annotationPasswordTextView instanceof PasswordTextView) {
            nameComponentView = annotationPasswordTextView.name();
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
        if (annotationPaginationTableView != null && annotationPaginationTableView instanceof PaginatedTableView) {
            nameComponentView = annotationPaginationTableView.name();
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
                                continue;
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
                                continue;
                            }

                            if (Objects.equals(componentViewNameModel, nameComponentController)) {
                                Object objectModelBean = fieldController.get(controller);
                                if (objectModelBean == null) {
                                    //Class clazzObjectModelBean = fieldController.getType();
                                    //objectModelBean = clazzObjectModelBean.getConstructor().newInstance();
                                    throw new NullPointerException("ModelBean " + nameComponentController + " no se ha inicializado");
                                }

                                Class classModel = objectModelBean.getClass();
                                //Field[] fieldsModel = getFieldsFromSuperClases(classModel, classModel.getDeclaredFields());
                                //Field fieldModel = getFieldByNameFromArrayFields(fieldsModel, componentViewNameField);
                                
                                // Obtenemos el Field del Modelo para hacer la asignación de valores
                                // Field fieldModel = classModel.getDeclaredField(componentViewNameField);
                                Field fieldModel = getFieldByNameFromSuperClasses(classModel, componentViewNameField);
                                
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
                    } catch (IntrospectionException ex) {
                        Logger.getLogger(AbstractObserverController.class.getName()).log(Level.SEVERE, "IntrospectionException", ex);
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
    private void asignValue(Field fieldView, String nameComponentView, Class typeComponentView, Field fieldModel, Class typeFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, V view) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IntrospectionException {
        Object objectComponentView = fieldView.get(view);
        if (objectComponentView == null) {
            throw new NullPointerException("Component in View " + nameComponentView + " is null");
        }
        
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldModel.getName(), fieldModel.getDeclaringClass());
        // Obtenemos el metodo get y set del atributo del modelo o propiedad en el controlador
        Method methodSetFieldModel = propertyDescriptor.getWriteMethod();
        Method methodGetFieldModel = propertyDescriptor.getReadMethod();
        if (methodSetFieldModel == null) {
            throw new NoSuchMethodException("El atributo " + fieldModel.getName() + " no tiene metodo público set");
        }
        if (methodGetFieldModel == null) {
            throw new NoSuchMethodException("El atributo " + fieldModel.getName() + " no tiene metodo público set");
        }
        
        ParametersToAsignValue params = new ParametersToAsignValue(fieldView, nameComponentView, typeComponentView, tipoUpdateEnum, fieldModel, typeFieldModel, objectModelBean, objectComponentView, methodSetFieldModel, methodGetFieldModel, failedValidation);
        ComponentAnnotatedViewContext context = new ComponentAnnotatedViewContext(ComponentAnnotatedViewFactory.getStrategy(fieldView));
        context.asignValue(params);
        
        this.failedValidation = failedValidation ? failedValidation : params.isFailedValidation();
        

//        if (fieldView.isAnnotationPresent(TextView.class)) {
//            TextView annotationTextView = fieldView.getAnnotation(TextView.class);
//            boolean required = annotationTextView.required();
//            String requiredMessage = (annotationTextView.requiredMessage() != null && !annotationTextView.requiredMessage().isEmpty()) ? annotationTextView.requiredMessage() : "El campo '" + nameComponentView + "' debe contener un valor";
//            
//            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, null, null, required, requiredMessage, null);
//
//        } else if (fieldView.isAnnotationPresent(DateTextView.class)) {
//            DateTextView annotationDateTextView = fieldView.getAnnotation(DateTextView.class);
//            String pattern = annotationDateTextView.pattern();
//            Locale locale = annotationDateTextView.locale() != null ? annotationDateTextView.locale().getLocale() : null;
//            boolean required = annotationDateTextView.required();
//            String requiredMessage = (annotationDateTextView.requiredMessage() != null && !annotationDateTextView.requiredMessage().isEmpty()) ? annotationDateTextView.requiredMessage() : "El campo '" + nameComponentView + "' debe contener un valor";
//
//            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, pattern, locale, required, requiredMessage, null);
//
//        } else if (fieldView.isAnnotationPresent(NumberTextView.class)) {
//            NumberTextView annotationNumberTextView = fieldView.getAnnotation(NumberTextView.class);
//            String pattern = annotationNumberTextView.pattern();
//            Locale locale = annotationNumberTextView.locale() != null ? annotationNumberTextView.locale().getLocale() : null;
//            boolean required = annotationNumberTextView.required();
//            String requiredMessage = (annotationNumberTextView.requiredMessage() != null && !annotationNumberTextView.requiredMessage().isEmpty()) ? annotationNumberTextView.requiredMessage() : "El campo '" + nameComponentView + "' debe contener un valor";
//
//            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, pattern, locale, required, requiredMessage, null);
//
//        } else if (fieldView.isAnnotationPresent(PasswordTextView.class)) {
//            PasswordTextView annotationPasswordTextView = fieldView.getAnnotation(PasswordTextView.class);
//            boolean required = annotationPasswordTextView.required();
//            String requiredMessage = (annotationPasswordTextView.requiredMessage() != null && !annotationPasswordTextView.requiredMessage().isEmpty()) ? annotationPasswordTextView.requiredMessage() : "El campo '" + nameComponentView + "' debe contener un valor";
//            DigestEnum digest = annotationPasswordTextView.digest() != null ? annotationPasswordTextView.digest() : DigestEnum.EMPTY;
//            
//            updateGenericTextViewData(typeComponentView, objectComponentView, nameComponentView, fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, null, null, required, requiredMessage, digest);
//            
//        } else if (fieldView.isAnnotationPresent(ToggleButtonView.class)) {
//            if (JToggleButton.class.isAssignableFrom(typeComponentView)) {
//                JToggleButton jToggleButton = (JToggleButton) objectComponentView;
//                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
//                    //fieldModel.set(objectModelBean, jToggleButton.isSelected());
//                    methodSetFieldModel.invoke(objectModelBean, jToggleButton.isSelected());
//                }
//                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
//                    if (typeFieldModel == Boolean.class || typeFieldModel == boolean.class) {
//                        //Boolean bool = (Boolean) fieldModel.get(objectModelBean);
//                        Boolean bool = (Boolean) methodGetFieldModel.invoke(objectModelBean);
//                        jToggleButton.setSelected(bool != null ? bool : Boolean.FALSE);
//                        for (ActionListener actionListener : jToggleButton.getActionListeners()) {
//                            actionListener.actionPerformed(null);
//                        }
//                    } else {
//                        throw new IllegalArgumentException("Atributo in Model " + fieldModel.getName() + " no es de tipo boolean");
//                    }
//                }
//
//            } else {
//                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo JToggleButton de java swing");
//            }
//        } else if (fieldView.isAnnotationPresent(ComboBoxView.class)) {
//            if (typeComponentView == JComboBox.class) {
//                ComboBoxView annotationComboBoxView = fieldView.getAnnotation(ComboBoxView.class);
//                JComboBox jCBcombo = (JComboBox) objectComponentView;
//                boolean required = annotationComboBoxView.required();
//                String requiredMessage = (annotationComboBoxView.requiredMessage() != null && !annotationComboBoxView.requiredMessage().isEmpty()) ? annotationComboBoxView.requiredMessage() : "El campo '" + nameComponentView + "' debe ser obligatorio";
//                
//                ComboBoxModel model = jCBcombo.getModel();
//                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
//                    //fieldModel.set(objectModelBean, model.getSelectedItem());
//                    Object value = model.getSelectedItem();
//                    methodSetFieldModel.invoke(objectModelBean, value);
//                    if (value == null && required) {
//                        NotifyUtils.showErrorAutoHide(null, "Valor Requerido", requiredMessage);
//                        this.failedValidation = true;
//                    }
//                }
//                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
//                    //model.setSelectedItem(fieldModel.get(objectModelBean));
//                    model.setSelectedItem(methodGetFieldModel.invoke(objectModelBean));
//                }
//            } else {
//                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo JComboBox de java swing");
//            }
//        } else if (fieldView.isAnnotationPresent(TableView.class)) {
//            if (typeComponentView == JTable.class) {
//                JTable jTable = (JTable) objectComponentView;
//                TableModelGeneric model = (TableModelGeneric) jTable.getModel();
//                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
//                    //fieldModel.set(objectModelBean, model.getListElements());
//                    methodSetFieldModel.invoke(objectModelBean, model.getListElements());
//                }
//                if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
//                    //model.setListElements((List) fieldModel.get(objectModelBean));
//                    model.setListElements((List) methodGetFieldModel.invoke(objectModelBean));
//                    model.fireTableDataChanged();
//                }
//            } else {
//                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo Table de java swing");
//            }
//        } else if (fieldView.isAnnotationPresent(PaginatedTableView.class)) {
//            if (typeComponentView == PaginatedTable.class) {
//                PaginatedTable paginatedTable = (PaginatedTable) objectComponentView;
//                if (!paginatedTable.isLazy()) {
//                    PaginationDataProvider dataProvider = paginatedTable.getPaginationDataProvider();
//                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
//                        methodSetFieldModel.invoke(objectModelBean, dataProvider.getListData());
//                    }
//                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
//                        List listData = (List) methodGetFieldModel.invoke(objectModelBean);
//                        dataProvider.setListData(listData);
//                        dataProvider.setRowCount(listData.size());
//                        paginatedTable.resetPaginatedTable();
//                    }
//                }
//            } else {
//                throw new IllegalArgumentException("Component in View " + nameComponentView + " no es de tipo PaginatedTable de SwingUtils");
//            }
//        } else {
//            // ComponentView
//            ComponentView annotationComponentView = fieldView.getAnnotation(ComponentView.class);
//            String nameProperty = annotationComponentView.nameProperty().trim();
//
//            for (Method m : typeComponentView.getMethods()) {
//
//                if (m.getName().equals("get" + StringUtils.capitalize(nameProperty))) {
//                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
//                        //fieldModel.set(objectModelBean, m.invoke(objectComponentView));
//                        methodSetFieldModel.invoke(objectModelBean, m.invoke(objectComponentView));
//                    }
//                }
//
//                if (m.getName().equals("set" + StringUtils.capitalize(nameProperty))) {
//                    if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
//                        //m.invoke(objectComponentView, fieldModel.get(objectModelBean));
//                        m.invoke(objectComponentView, methodGetFieldModel.invoke(objectModelBean));
//                    }
//                }
//            }
//        }
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
    private void updateGenericTextViewData(Class typeComponentView, Object valueComponentView, String nameComponentView, Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Method methodGetFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, String pattern, Locale locale, boolean required, String requiredMessage, DigestEnum digest) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (JTextComponent.class.isAssignableFrom(typeComponentView)) {
            JTextComponent jTFfield = (JTextComponent) valueComponentView;
            setTextData(fieldModel, typeFieldModel, methodSetFieldModel, methodGetFieldModel, objectModelBean, tipoUpdateEnum, jTFfield, pattern, locale, required, requiredMessage, digest);
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
    private void setTextData(Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Method methodGetFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, JTextComponent jTextComponent, String pattern, Locale locale, boolean required, String requiredMessage, DigestEnum digest) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.MODEL)) {
            if (getTextFromComponent(jTextComponent).isEmpty()) {
                methodSetFieldModel.invoke(objectModelBean, (Object) null);
                if (required) {
                    NotifyUtils.showErrorAutoHide(null, "Valor Requerido", requiredMessage);
                    this.failedValidation = true;
                }
                //fieldModel.set(objectModelBean, null);
            } else {
                insertIntoFieldModelFromTextComponent(fieldModel, typeFieldModel, methodSetFieldModel, objectModelBean, jTextComponent, pattern, locale, digest);
            }
        }

        if (Objects.equals(tipoUpdateEnum, TipoUpdateEnum.VIEW)) {
            insertIntoTextComponentFromFieldModel(fieldModel, typeFieldModel, methodGetFieldModel, objectModelBean, jTextComponent, pattern, locale);
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
    private void insertIntoFieldModelFromTextComponent(Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Object objectModelBean, JTextComponent jTextComponent, String pattern, Locale locale, DigestEnum digest) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        try  {
            if (digest != null && !Objects.equals(digest, DigestEnum.EMPTY)) {
                methodSetFieldModel.invoke(objectModelBean, DigestUtils.getStringMessageDigest(getTextFromComponent(jTextComponent), digest.getNombre()));
            } else if (typeFieldModel == String.class) {
                methodSetFieldModel.invoke(objectModelBean, getTextFromComponent(jTextComponent));
            } else if (typeFieldModel == Character.class || typeFieldModel == char.class) {
                methodSetFieldModel.invoke(objectModelBean, getTextFromComponent(jTextComponent).charAt(0));
            } else if (Number.class.isAssignableFrom(typeFieldModel)) {
                // https://www.ibm.com/developerworks/library/j-numberformat/index.html
                DecimalFormat decimalFormat = null;
                if (pattern != null && !pattern.isEmpty()) {
                    DecimalFormatSymbols symbols = locale != null ? new DecimalFormatSymbols(locale) : new DecimalFormatSymbols();
                    decimalFormat = new DecimalFormat(pattern, symbols);
                    decimalFormat.setParseStrict(true);
                }
                if (typeFieldModel == Byte.class || typeFieldModel == byte.class) {
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Byte.parseByte(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Byte.parseByte(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Short.class || typeFieldModel == short.class) {
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Short.parseShort(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Short.parseShort(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Integer.class || typeFieldModel == int.class) {
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Integer.parseInt(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Integer.parseInt(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Long.class || typeFieldModel == long.class) {
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Long.parseLong(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Long.parseLong(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Float.class || typeFieldModel == float.class) {
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Float.parseFloat(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Float.parseFloat(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == Double.class || typeFieldModel == double.class) {
                    methodSetFieldModel.invoke(objectModelBean, decimalFormat != null ? Double.parseDouble(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()) : Double.parseDouble(getTextFromComponent(jTextComponent)));
                } else if (typeFieldModel == BigInteger.class) {
                    if (decimalFormat != null) {
                        decimalFormat.setParseBigDecimal(true);
                        //BigDecimal valueParsed = (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent));
                        BigDecimal valueParsed = new BigDecimal(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString());
                        methodSetFieldModel.invoke(objectModelBean, valueParsed.toBigIntegerExact());
                    } else {
                        methodSetFieldModel.invoke(objectModelBean, new BigInteger(getTextFromComponent(jTextComponent)));
                    }
                } else if (typeFieldModel == BigDecimal.class) {
                    if (decimalFormat != null) {
                        decimalFormat.setParseBigDecimal(true);
                        //methodSetFieldModel.invoke(objectModelBean, (BigDecimal) decimalFormat.parse(getTextFromComponent(jTextComponent)));
                        methodSetFieldModel.invoke(objectModelBean, new BigDecimal(decimalFormat.parse(getTextFromComponent(jTextComponent)).toString()));
                    } else {
                        methodSetFieldModel.invoke(objectModelBean, new BigDecimal(getTextFromComponent(jTextComponent)));
                    }
                }
            } else if (typeFieldModel == Boolean.class || typeFieldModel == boolean.class) {
                methodSetFieldModel.invoke(objectModelBean, Boolean.parseBoolean(getTextFromComponent(jTextComponent)));
            } else if (java.util.Date.class.isAssignableFrom(typeFieldModel)) {
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.setLenient(false);
                if (pattern != null && !pattern.isEmpty()) {
                    sdf = locale != null ? new SimpleDateFormat(pattern, locale) : new SimpleDateFormat(pattern);
                }
                if (typeFieldModel == java.sql.Time.class) {
                    methodSetFieldModel.invoke(objectModelBean,  new java.sql.Time(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else if (typeFieldModel == java.sql.Date.class) {
                    methodSetFieldModel.invoke(objectModelBean,  new java.sql.Date(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else if (typeFieldModel == java.sql.Timestamp.class) {
                    methodSetFieldModel.invoke(objectModelBean,  new java.sql.Timestamp(sdf.parse(getTextFromComponent(jTextComponent)).getTime()));
                } else {
                    methodSetFieldModel.invoke(objectModelBean,  sdf.parse(getTextFromComponent(jTextComponent)));
                }
            } else if (java.time.temporal.Temporal.class.isAssignableFrom(typeFieldModel)) {
                DateTimeFormatter formatter = getDateTimeFormatter(typeFieldModel, pattern, locale);
                if (typeFieldModel == LocalDate.class) {
                    methodSetFieldModel.invoke(objectModelBean,  LocalDate.parse(getTextFromComponent(jTextComponent), formatter));
                } else if (typeFieldModel == LocalTime.class) {
                    methodSetFieldModel.invoke(objectModelBean,  LocalTime.parse(getTextFromComponent(jTextComponent), formatter));
                } else if (typeFieldModel == LocalDateTime.class) {
                    methodSetFieldModel.invoke(objectModelBean,  LocalDateTime.parse(getTextFromComponent(jTextComponent), formatter));
                }
            }
        } catch (NumberFormatException ex) {
            NotifyUtils.showErrorAutoHide(null, "Formato Incorrecto", "El valor '" + jTextComponent.getText() + "' no tiene un formato numérico correcto");
            this.failedValidation = true;
        } catch (ParseException ex) {
            NotifyUtils.showErrorAutoHide(null, "Formato Incorrecto", "El valor '" + jTextComponent.getText() + "' no tiene un formato correcto");
            this.failedValidation = true;
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
    private DateTimeFormatter getDateTimeFormatter(Class typeFieldModel, String pattern, Locale locale) throws IllegalArgumentException {
        if (pattern != null && !pattern.isEmpty()) {
            return locale != null ? DateTimeFormatter.ofPattern(pattern, locale) : DateTimeFormatter.ofPattern(pattern);
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
    private void insertIntoTextComponentFromFieldModel(Field fieldModel, Class typeFieldModel, Method methodGetFieldModel, Object objectModelBean, JTextComponent jTextComponent, String pattern, Locale locale) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String text = getTextFormattedForInsertIntoComponent(typeFieldModel, methodGetFieldModel, objectModelBean, pattern, locale);
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
    private String getTextFormattedForInsertIntoComponent(Class typeFieldModel, Method methodGetFieldModel, Object objectModelBean, String pattern, Locale locale) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object object = methodGetFieldModel.invoke(objectModelBean);
        //Object object = fieldModel.get(objectModelBean);
        if (object == null) {
            return null;
        }
        if (pattern == null || pattern.isEmpty()) {
            return object.toString();
        }
        if (Number.class.isAssignableFrom(typeFieldModel)) {
            DecimalFormatSymbols symbols = locale != null ? new DecimalFormatSymbols(locale) : new DecimalFormatSymbols();
            DecimalFormat df = new DecimalFormat(pattern, symbols);
            return df.format(object);
        } else if (java.util.Date.class.isAssignableFrom(typeFieldModel)) {
            SimpleDateFormat sdf = locale != null ? new SimpleDateFormat(pattern, locale) : new SimpleDateFormat(pattern);
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
            DateTimeFormatter formatter = locale != null ? DateTimeFormatter.ofPattern(pattern, locale) : DateTimeFormatter.ofPattern(pattern);
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
    
    
    private Field[] getFieldsFromSuperClases(Class<? extends Object> clazz, Field[] fields) {
        if (clazz.getSuperclass() != null) {
            Class<?> superClass = clazz.getSuperclass();
            Field[] fieldsSuperClass = superClass.getDeclaredFields();
            for (Field f : fieldsSuperClass) {
                fields = Arrays.copyOf(fields, fields.length + 1);
                fields[fields.length - 1] = f;
            }
            fields = getFieldsFromSuperClases(superClass, fields);
        }
        return fields;
    }
    
    private Field getFieldByNameFromArrayFields(Field[] fields, String nameField) {
        for (Field field : fields) {
            if (Objects.equals(field.getName(), nameField)) {
                return field;
            }
        }
        return null;
    }
    
    private Field getFieldByNameFromSuperClasses(Class<? extends Object> clazz, String nameField) throws NoSuchFieldException, SecurityException {
        try {
            return clazz.getDeclaredField(nameField);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() == null) {
                throw ex;
            } else {
                Class<?> superClass = clazz.getSuperclass();
                return getFieldByNameFromSuperClasses(superClass, nameField);
            }
        } catch (SecurityException ex) {
            throw ex;
        }
    }
    
}

