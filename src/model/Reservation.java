package model;

import java.sql.Date;

public class Reservation {
    private int ID_Livre;
    private int ID_Membre;
    private Date Date_Reservation;

    public Reservation(int ID_Livre, int ID_Membre, Date Date_Reservation) {
        this.ID_Livre = ID_Livre;
        this.ID_Membre = ID_Membre;
        this.Date_Reservation = Date_Reservation;
    }

    public int getID_Livre() {
        return ID_Livre;
    }

    public int getID_Membre() {
        return ID_Membre;
    }

    public Date getDate_Reservation() {
        return Date_Reservation;
    }
}