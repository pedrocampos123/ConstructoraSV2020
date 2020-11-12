
package com.utilidades;

import java.awt.image.ImageObserver;
import javax.swing.JOptionPane;

/**
 * Nombre de la clase: Mensajeria
 * Fecha: 04/11/2020 
 * CopyRigth: Pedro Campos
 * Modificacion: 04/11/2020
 * Version: 1.0
 * @author pedro
 */
public class Mensajeria {
    public void printMessageAlerts(String textMessage, String title, int iconAlert){
        JOptionPane.showMessageDialog(null, textMessage, title, iconAlert);
    }
    
    public int printMessageConfirm(String textMessage, String title, int iconAlert){
        return JOptionPane.showConfirmDialog(null, textMessage, title, iconAlert);
    }
    
    public String printImputDialog(String texto){
        return JOptionPane.showInputDialog(texto);
    }
}
