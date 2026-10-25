package Activite1.serverPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT {

    static final int PORT = 1234;

    public static void main(String[] args) {
        int numero = 0;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");

            while (true) {
                Socket socket = serverSocket.accept();
                numero++;
                System.out.println("Client n°" + numero + " connecté, adresse : "
                        + socket.getRemoteSocketAddress());
                new ClientProcess(socket, numero).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

}
