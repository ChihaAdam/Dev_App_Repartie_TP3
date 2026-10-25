package Activite2.serverPackage;

import Activite2.ressources.Compteur;
import Activite2.ressources.Expression;

import java.io.*;
import java.net.Socket;


public class ClientProcess extends Thread {

    private Socket socket;
    private Compteur compteur;
    public ClientProcess(Socket socket,Compteur compteur) {
        this.socket = socket;
        this.compteur = compteur;
    }
    private static double calculer (Expression exp) throws NumberFormatException{
        String operation = exp.getOperation();
        double x = exp.getOp1();
        double y = exp.getOp2();
        if (operation.equals("+")) return x+y;
        if (operation.equals("-")) return x-y;
        if (operation.equals("*")) return x*y;
        if (operation.equals("/"))return x/y;
        throw new NumberFormatException("operation invalide");
    }
    private static String format(Expression exp) throws NumberFormatException{
        return exp.getOp1() + exp.getOperation() + exp.getOp2() + " = " + calculer(exp);
    }

    @Override
    public void run(){
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object recu = ois.readObject();
            while (recu instanceof Expression){
                Expression exp = (Expression) recu;
                oos.writeObject(format(exp));
                compteur.incrementer();
                recu = ois.readObject();
            }
            System.out.println("le serveur a traite " + compteur.getCompteur() + "operations");
            oos.close();
            ois.close();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
