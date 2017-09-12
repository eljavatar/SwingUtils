package com.eljavatar.swingutils.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta Anotacion puede ser usada en cualquier tipo de componente.
 * Lo único que requiere esta anotación, es el nombre de la propiedad con la cual
 * se accede al valor que se desea obtener o modificar
 * 
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentView {
    
    String name();
    
    /**
     * Nombre de la propiedad para acceder al valor requerido mediante su metodo 'get' o 'set'
     * @return 
     */
    String nameProperty();
    
}
