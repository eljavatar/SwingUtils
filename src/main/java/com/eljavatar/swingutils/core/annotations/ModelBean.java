package com.eljavatar.swingutils.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta Anotación es usada para identificar un objeto en el controlador y que
 * sera usado como modelo de datos
 * 
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelBean {

    /**
     * 
     * @return Nombre personalizado del objeto modelo en el controlador. Si se desea conservar
     * el mismo nombre con el que fue declarado el objeto, se deja vacío por defecto
     */
    String name() default "";
    
}
