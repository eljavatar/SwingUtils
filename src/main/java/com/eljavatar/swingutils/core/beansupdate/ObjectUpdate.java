package com.eljavatar.swingutils.core.beansupdate;

import java.util.List;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class ObjectUpdate {
    
    private TipoUpdateEnum tipoUpdateEnum;
    private String component;
    private List<String> listComponents;

    public ObjectUpdate(TipoUpdateEnum tipoUpdateEnum) {
        this.tipoUpdateEnum = tipoUpdateEnum;
    }

    public ObjectUpdate(TipoUpdateEnum tipoUpdateEnum, String component) {
        this.tipoUpdateEnum = tipoUpdateEnum;
        this.component = component;
    }

    public ObjectUpdate(TipoUpdateEnum tipoUpdateEnum, List<String> listComponents) {
        this.tipoUpdateEnum = tipoUpdateEnum;
        this.listComponents = listComponents;
    }

    public TipoUpdateEnum getTipoUpdateEnum() {
        return tipoUpdateEnum;
    }

    public void setTipoUpdateEnum(TipoUpdateEnum tipoUpdateEnum) {
        this.tipoUpdateEnum = tipoUpdateEnum;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<String> getListComponents() {
        return listComponents;
    }

    public void setListComponents(List<String> listComponents) {
        this.listComponents = listComponents;
    }
    
}
