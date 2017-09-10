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
    
    String name() default "";
    
}
