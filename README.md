# Compte rendu — TP3 : programmation réseau avec threads

## Introduction

Ce TP prolonge le TP2 de programmation client/serveur. Dans le TP2, le client se connectait au serveur par une socket pour lui envoyer des demandes de calcul. Le serveur traitait les échanges dans son flot principal : il ne pouvait donc pas s'occuper de plusieurs clients simultanément. Le TP3 reprend ces activités et introduit les threads afin que chaque connexion soit traitée indépendamment.

Le projet contient deux activités : la première échange des données textuelles, la seconde transmet des objets Java sérialisés et partage un compteur de traitements.

## Objectifs

- Accepter plusieurs connexions clientes sans bloquer le serveur sur une seule conversation.
- Associer un thread à chaque client.
- Mettre en œuvre deux formats d'échange : lignes de texte et objets sérialisés.
- Observer le partage d'une ressource entre plusieurs threads avec un compteur synchronisé.

## Activité 1 — Calculatrice avec protocole textuel

### Étapes de réalisation

1. Le client ouvre une socket vers `localhost:1234`.
2. Il lit une opération saisie au clavier et vérifie sa syntaxe avec une expression régulière.
3. Il envoie les deux opérandes et l'opérateur sous forme de trois lignes.
4. Le serveur attend une connexion. À chaque connexion, il crée un objet `ClientProcess` et démarre son thread.
5. Le thread lit les lignes, calcule le résultat, puis l'envoie au client. La conversation continue jusqu'à la déconnexion.

Le motif utilisé par le client accepte deux nombres entiers séparés par l'un des opérateurs `+`, `-`, `*` ou `/` :

```java
static final Pattern SYNTAX = Pattern.compile(
    "^\\s*(\\d+)\\s*([+\\-*/])\\s*(\\d+)\\s*$"
);
```

Le serveur délègue chaque socket à un thread dédié :

```java
Socket socket = serverSocket.accept();
numero++;
new ClientProcess(socket, numero).start();
```

La classe `ClientProcess` étend `Thread`. La méthode `run()` contient le dialogue avec un client :

```java
public class ClientProcess extends Thread {
    @Override
    public void run() {
        // Lire les opérandes et l'opérateur, calculer et répondre
    }
}
```

L'appel à `start()` lance un nouveau thread ; le serveur peut alors revenir à `accept()` pour recevoir une autre connexion. Chaque thread possède sa propre socket et son numéro de client.

## Activité 2 — Expressions sérialisées et compteur partagé

Cette activité conserve le serveur multithread, mais remplace les lignes de texte par des objets Java. Le client construit une `Expression` contenant les deux opérandes et l'opérateur, puis l'envoie avec un `ObjectOutputStream`. La classe implémente `Serializable` pour permettre cet échange :

```java
public class Expression implements Serializable {
    private final double op1;
    private final String operation;
    private final double op2;
}
```

Le serveur reçoit l'objet, vérifie son type, calcule le résultat et renvoie une chaîne de réponse :

```java
Object recu = ois.readObject();
while (recu instanceof Expression) {
    Expression exp = (Expression) recu;
    oos.writeObject(format(exp));
    compteur.incrementer();
    recu = ois.readObject();
}
```

Pour terminer la conversation, le client envoie `null`. Le serveur sort alors de la boucle et ferme les flux et la socket.

### Compteur partagé et synchronisation

Le serveur crée un seul `Compteur` et le transmet à tous les threads clients. Ceux-ci incrémentent donc la même instance. Comme plusieurs threads peuvent modifier le compteur en même temps, l'incrémentation est déclarée `synchronized` :

```java
public synchronized void incrementer() {
    compteur++;
}
```

La synchronisation empêche deux incrémentations concurrentes de perdre une mise à jour. Chaque thread affiche ensuite la valeur courante du compteur.

## Exécution

Pour chaque activité, lancer d'abord la classe `ServerMT`, puis démarrer un ou plusieurs clients correspondants. Le serveur écoute sur le port `1234`. Les classes de l'activité 1 et de l'activité 2 sont dans des packages distincts : ne lancer ensemble que le serveur et le client de la même activité, car ils utilisent le même port.

Dans le client, saisir par exemple `12 + 5`, `8 / 2`, puis `quit` pour terminer. L'activité 1 utilise les flux texte ; l'activité 2 utilise les flux d'objets. Plusieurs clients peuvent se connecter au même serveur en activité 1 comme en activité 2.

## Conclusion

Le passage aux threads permet au serveur de traiter plusieurs clients en parallèle : la boucle principale accepte les connexions tandis que chaque thread gère une conversation. L'activité 2 montre aussi qu'un objet doit être sérialisable pour traverser un flux d'objets et que l'accès concurrent à une donnée partagée nécessite une synchronisation adaptée.

Dans la version actuelle de l'activité 2, la division par zéro n'est pas explicitement vérifiée ; avec des nombres `double`, elle peut produire `Infinity` ou `NaN`. La validation de cette situation peut être ajoutée au calcul pour harmoniser son comportement avec l'activité 1.
