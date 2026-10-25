package Activite1.serverPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Un thread = un client. Contient le dialogue qui était dans main() du Server du TP2-1.
 */
public class ClientProcess extends Thread {

    private Socket socket;
    private int numero;

    public ClientProcess(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);

            System.out.println("Bienvenue, vous êtes le client n°" + numero);
            String operande1 = br.readLine();
            while (operande1 != null && !operande1.equals("quit")) {
                String operateur = br.readLine();
                String operande2 = br.readLine();
                if (operateur == null || operande2 == null) {
                    break;
                }
                System.out.println("[client n°" + numero + "] Opération reçue : "
                        + operande1 + " " + operateur + " " + operande2);
                String reponse;
                try {
                    reponse = calculer(Double.parseDouble(operande1), operateur,
                            Double.parseDouble(operande2));
                } catch (NumberFormatException e) {
                    reponse = "Erreur : opérande invalide";
                }
                pw.println(reponse);
                System.out.println("[client n°" + numero + "] Réponse envoyée : " + reponse);

                operande1 = br.readLine();
            }

            System.out.println("Client n°" + numero + " déconnecté (fin de la communication)");
        } catch (IOException e) {
            System.out.println("Erreur client n°" + numero + " : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
    static String calculer(double a, String operateur, double b) {
        double r;
        switch (operateur) {
            case "+": r = a + b; break;
            case "-": r = a - b; break;
            case "*": r = a * b; break;
            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        return a + " " + operateur + " " + b + " = " + r;
    }
    static String formater(double v) {
        if (v == Math.rint(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}
