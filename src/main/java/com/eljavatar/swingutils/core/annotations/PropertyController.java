package com.eljavatar.swingutils.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta anotacion es usada cuando sólo se desea acceder a una propiedad del
 * controlador, pero que no está asociada a un objeto o modelo de datos
 * 
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyController {
    
    /**
     * 
     * @return Nombre personalizado del atributo en el controlador. Si se desea conservar
     * el mismo nombre con el que fue declarado el atributo, se deja vacío por defecto
     */
    String name() default "";
    
}
