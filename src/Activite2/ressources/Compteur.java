package Activite2.ressources;

public class Compteur {
    private int compteur = 0;
    public synchronized void incrementer(){
        compteur++;
    }
    public int getCompteur(){
        return compteur;
    }
}
