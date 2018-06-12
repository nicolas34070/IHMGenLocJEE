/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pib.bean;

import ihm.clientTCP;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Nico
 */
@Named(value = "buttonView")
@SessionScoped
public class ButtonView implements Serializable {
    
    private boolean etatConnexion = false;
    
    private double latitude;
    private double longitude;
    private clientTCP leClient;
    private boolean etatReceptionMessages;
    
    public ButtonView(){
        setLatitude(43.3554722);
        setLongitude(5.33866667);
    }

    /**
     * Creates a new instance of ButtonView
     * @param actionEvent
     */
    public void buttonAction(ActionEvent actionEvent) {
        if(etatConnexion == true){
            System.out.println("Déconnexion");
            etatConnexion = false;
            try {
                leClient.fermerClient();
            } catch (IOException ex) {
                Logger.getLogger(ButtonView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println("Connexion");
            etatConnexion = true;
            try {
                leClient = new clientTCP();
                leClient.demarrageClient();
                leClient.recevoirMessage();
            } catch (IOException ex) {
                Logger.getLogger(ButtonView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
}
