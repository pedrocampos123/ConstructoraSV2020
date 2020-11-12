
package com.utilidades;

/**
 * Nombre de la clase: ComboItem
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class ComboItem {
    private int value;
    private String label;

    public ComboItem() {
    }

    public ComboItem(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
