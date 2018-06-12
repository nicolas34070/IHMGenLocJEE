/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pib.bean;

import ihm.Accueil;
import ihm.clientTCP;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nico
 */
@Named(value = "coordinatesBean")
@SessionScoped
public class coordinatesBean implements Serializable {

    private double latitude;
    private double longitude;
    private clientTCP leClient;
    private boolean etatReceptionMessages;
    
    /**
     * Creates a new instance of coordinatesBean
     */
    public coordinatesBean() {
       setLatitude(43.3554722);
       setLongitude(5.338666666666667);
        
    etatReceptionMessages = false;
        
        try {
            leClient = new clientTCP();
            leClient.demarrageClient();
            leClient.recevoirMessage();
            
        } catch (IOException ex) {
            Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
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
