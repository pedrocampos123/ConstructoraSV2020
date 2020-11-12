
package com.utilidades;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTextField;

/**
 * Nombre de la clase: ValidarCampos
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 06/11/2020
 * Version: 1.1
 * @author pedro
 */
public class ValidarCampos {
       
    private String nivelAcceso;

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
    
    
    public ValidarCampos() {
    }
            
    /**
     * Validar solo numeros
     */
    public void onlyNumbres(KeyEvent evt){
        if(!Character.isDigit(evt.getKeyChar()))
            evt.consume();
    }
    
    /**
     * Validar solo palabras
     */    
    public void onlyWords(KeyEvent evt){
        if(!Character.isLetter(evt.getKeyChar()))
            evt.consume();
    }
    
    /**
     * Validar solo palabras y espacios
     */    
    public void spaceAndWords(KeyEvent evt, JTextField textField){
        if(!Character.isLetter(evt.getKeyChar()) && evt.getKeyChar() != ' '){
            evt.consume();
        }
    }
    
    /**
     * Validar solo numeros y un punto
     */
    public void numbersAndPoint(KeyEvent evt, JTextField textField){
        if(!Character.isDigit(evt.getKeyChar()) && evt.getKeyChar() != '.'){
            evt.consume();
        }
        if(evt.getKeyChar() == '.' && textField.getText().contains(".")){
            evt.consume();
        }
    }
    
    /**
     * convertir a decimales
     * @param valorEntrada
     * @return 
     */
    public String numberFormat(String valorEntrada){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        
        String valor = "";
        valor = nf.format(Double.parseDouble(valorEntrada));
        
        return valor;
    }
}
