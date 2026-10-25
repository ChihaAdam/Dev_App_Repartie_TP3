package Activite2.serverPackage;

import Activite2.ressources.Compteur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT {

    static final int PORT = 1234;

    public static void main(String[] args) {
        Compteur compteur = new Compteur();
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientProcess(socket, compteur).start();
            }

        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

}
