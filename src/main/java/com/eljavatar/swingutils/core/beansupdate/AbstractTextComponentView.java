/*
 * Copyright 2020 Andres.
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

import com.eljavatar.swingutils.core.componentsutils.NotifyUtils;
import com.eljavatar.swingutils.util.DigestEnum;
import com.eljavatar.swingutils.util.DigestUtils;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
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
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class AbstractTextComponentView {
    
    private boolean failedValidation;

    public AbstractTextComponentView() {
        this.failedValidation = false;
    }

    public boolean isFailedValidation() {
        return failedValidation;
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
    protected void updateGenericTextViewData(Class typeComponentView, Object valueComponentView, String nameComponentView, Field fieldModel, Class typeFieldModel, Method methodSetFieldModel, Method methodGetFieldModel, Object objectModelBean, TipoUpdateEnum tipoUpdateEnum, String pattern, Locale locale, boolean required, String requiredMessage, DigestEnum digest) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
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
    
}
