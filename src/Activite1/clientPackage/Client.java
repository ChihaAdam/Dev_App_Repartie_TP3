package Activite1.clientPackage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    static final Pattern SYNTAX = Pattern.compile("^\\s*(\\d+)\\s*([+\\-*/])\\s*(\\d+)\\s*$");
    public static void main(String[] args) throws IOException, UnknownHostException {
        Socket socket = new Socket("localhost",1234);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os,true);
        InputStream is = socket.getInputStream();
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        Scanner sc = new Scanner(System.in);
        System.out.println("connecté au serveur");

        while (true){
            System.out.println("donner une operation et pour quitter tapper \"quit\"");
            String entered = sc.nextLine().trim();
            System.out.println(entered);
            if (entered.equals("quit")) break;
            Matcher matcher = SYNTAX.matcher(entered);
            if (!matcher.matches()) {
                System.out.println("verifier votre saisie\n");
                continue;
            }
            pw.println(matcher.group(1));
            pw.println(matcher.group(2));
            pw.println(matcher.group(3));
            String resultat = br.readLine();
            System.out.println(resultat+"\n");
        }
        pw.close();
        br.close();
        os.close();
        is.close();
        sc.close();
        socket.close();
    }
}
