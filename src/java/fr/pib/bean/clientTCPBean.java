/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.pib.bean;

import ihm.Accueil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Nico
 */
//@Named(value = "clientTCPBean")
@ManagedBean
@ViewScoped
public class clientTCPBean implements Serializable {

    private boolean etatClient;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String messageRecu;
    private static double latitude;
    private static double longitude;
    private String idBuggy;
    private MapModel simpleModel;
    private LatLng coord1;
    private Marker marker1;

    public clientTCPBean() throws IOException {
        etatClient = false;
        simpleModel = new DefaultMapModel();

        setLatitude(43.355308);
        setLongitude(5.338436);

        coord1 = new LatLng(getLatitude(), getLongitude());
        marker1 = new Marker(coord1, "GenLoc");
        simpleModel.addOverlay(marker1);

    }

    public void refresh() {
        marker1.setVisible(false);
        setLatitude(getLatitude());
        setLongitude(getLongitude());
        coord1 = new LatLng(getLatitude(), getLongitude());
        marker1 = new Marker(coord1, "GenLoc");
        simpleModel = new DefaultMapModel();
        //simpleModel.addOverlay(new Marker(coord1, "GenLoc"));
        simpleModel.addOverlay(marker1);
        marker1.setVisible(true);
        System.out.println("mise a jour");
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }

    public void demarrageClient() throws IOException {

        setSocket(new Socket("192.168.1.12", 1234));
        System.out.println("Connexion effectuée");

        in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
        out = new PrintWriter(getSocket().getOutputStream());
        etatClient = true;
    }

    public void fermerClient() throws IOException {

        try {
            in.close();
            out.close();
            getSocket().close();
            System.out.println("The server is shut down!");
            etatClient = false;
        } catch (IOException e) {
            /* failed */ }
    }

    public void recevoirMessage() {

        Thread thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("reception des messages...");

                while (true) {
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
                    } catch (IOException ex) {
                        Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        setMessageRecu(in.readLine());    //Pour lire il faut envoyer à la fin du message un \n !!!!
                        traitementMessage();
                    } catch (IOException ex) {
                        Logger.getLogger(Accueil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Received from  server: " + getMessageRecu());

                }
            }
        });
        thread.start();
    }

    public void traitementMessage() {
        //4321.3177,N,00520.27288,E,Buggy1
        double tamponDegresLatitude;
        double tamponMinutesLatitude;
        double tamponSecondesLatitude;
        double tamponDegresLongitude;
        double tamponMinutesLongitude;
        double tamponSecondesLongitude;

        double latitudeDecimale;
        double longitudeDecimale;
        
        tamponDegresLatitude = Integer.parseInt(getMessageRecu().substring(0, 2));
        tamponMinutesLatitude = Integer.parseInt(getMessageRecu().substring(2, 4));
        tamponSecondesLatitude = Integer.parseInt(getMessageRecu().substring(5, 9));

        tamponDegresLongitude = Integer.parseInt(getMessageRecu().substring(14, 15));
        tamponMinutesLongitude = Integer.parseInt(getMessageRecu().substring(15, 17));
        tamponSecondesLongitude = Integer.parseInt(getMessageRecu().substring(18, 22));

        latitudeDecimale = tamponDegresLatitude + (tamponMinutesLatitude / 60.0) + ((tamponSecondesLatitude * 0.006) / 3600.0);
        longitudeDecimale = tamponDegresLongitude + (tamponMinutesLongitude / 60.0) + ((tamponSecondesLongitude * 0.006) / 3600.0);
        
        idBuggy = getMessageRecu().substring(26, 32);

        setLatitude(latitudeDecimale);
        setLongitude(longitudeDecimale);
        setIdBuggy(idBuggy);

        System.out.println("Latitude : " + latitudeDecimale);
        System.out.println("Longitude : " + longitudeDecimale);
        System.out.println("ID Buggy : " + idBuggy);
    }
    
    public void allumerLED(ActionEvent actionEvent){
        String message_Allumer_LED_3 = "AT+GPIOEXTSET=57,1\n";
        
        out.println(message_Allumer_LED_3);
        out.flush();
    }
    
    public void eteindreLED(ActionEvent actionEvent){
        String message_Eteindre_LED_3 = "AT+GPIOEXTSET=57,0\n";
        
        out.println(message_Eteindre_LED_3);
        out.flush();
    }
    

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return the messageRecu
     */
    public String getMessageRecu() {
        return messageRecu;
    }

    /**
     * @param messageRecu the messageRecu to set
     */
    public void setMessageRecu(String messageRecu) {
        this.messageRecu = messageRecu;
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

    /**
     * @return the idBuggy
     */
    public String getIdBuggy() {
        return idBuggy;
    }

    /**
     * @param idBuggy the idBuggy to set
     */
    public void setIdBuggy(String idBuggy) {
        this.idBuggy = idBuggy;
    }

}
