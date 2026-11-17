import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connecté au serveur ! Tapez vos messages :");

            // THREAD DE RÉCEPTION : Écoute le serveur en arrière-plan
            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("\n[Ami] : " + serverMessage);
                        System.out.print("Moi : "); // Remettre le prompt
                    }
                } catch (IOException e) {
                    System.out.println("Déconnexion du serveur.");
                }
            });
            receiveThread.start();

            // BOUCLE D'ENVOI : Envoie ce quier
            String message;
            while (true) {
                System.out.print("Moi : ");
                message = userInput.readLine();
                if (message == null || message.equalsIgnoreCase("/quit")) break;
                out.println(message);
            }
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}