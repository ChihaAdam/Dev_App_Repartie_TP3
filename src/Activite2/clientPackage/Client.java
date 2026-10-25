package Activite2.clientPackage;
import Activite2.ressources.Expression;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Client {
    static final Pattern SYNTAX = Pattern.compile("^\\s*(\\d+)\\s*([+\\-*/])\\s*(\\d+)\\s*$");
    public static void main(String[] args) throws IOException{
        Socket socket = new Socket("localhost",1234);
        ObjectOutputStream  oos = new ObjectOutputStream(socket.getOutputStream());
        oos.flush();
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Scanner sc = new Scanner(System.in);
        System.out.println("connecté au serveur");
        while (true){
            System.out.println("donner une operation et pour quitter tapper \"quit\"");
            String entered = sc.nextLine().trim();
            System.out.println(entered);
            if (entered.equals("quit")) {
                oos.writeObject(null);
                break;
            }
            Matcher matcher = SYNTAX.matcher(entered);
            if (!matcher.matches()) {
                System.out.println("verifier votre saisie\n");
                continue;
            }
            double op1 = Double.parseDouble(matcher.group(1));
            double op2 = Double.parseDouble(matcher.group(3));
            String operation = matcher.group(2);
            Expression exp = new Expression(op1,op2,operation);
            oos.writeObject(exp);
            try{
                String reponse = (String) ois.readObject();
                System.out.println(reponse);
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        oos.close();
        ois.close();
        sc.close();
        socket.close();
    }
}