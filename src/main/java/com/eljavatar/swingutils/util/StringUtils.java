package com.eljavatar.swingutils.util;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class StringUtils {
    
    /**
     * Función para convertir en mayúsculas la primera letra de cada palabra en una línea de texto
     *
     * @param str Recibe el texto a convertir
     * @return el texto convertido
     */
    public static String capitalizeEachWord(String str) {
        char[] caracteres = str.toCharArray();
        caracteres[0] = Character.toUpperCase(caracteres[0]);
        for (int i = 0; i < str.length() - 2; i++) {
            // Es 'palabra'
            if (caracteres[i] == ' ' || caracteres[i] == '.' || caracteres[i] == ',') {
                // Reemplazamos
                caracteres[i + 1] = Character.toUpperCase(caracteres[i + 1]);
            }
        }
        return new String(caracteres);
    }
    
    /**
     * Función para convertir en mayúscula la primera letra de un String
     *
     * @param str Recibe el a convertir
     * @return el texto convertido
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String resultado;
        resultado = str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
        return resultado;
    }
    
    /**
     * 
     * @param str Recibe la cadena a validar
     * @return <code>true</code> si es <code>null</code> o es una cadena vacía
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
}
