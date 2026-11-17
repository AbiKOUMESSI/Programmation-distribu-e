import java.io.*;
import java.net.*;

class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Initialisation des flux de communication
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Bienvenue sur le serveur de chat !");

            String message;
            // Boucle : on attend que le client envoie un message
            while ((message = in.readLine()) != null) {
                System.out.println("Message reçu : " + message);
                // On demande au serveur de diffuser ce message aux autres
                ChatServer.broadcast(message, this);
            }
        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void closeConnection() {
        try {
            socket.close();
            ChatServer.removeClient(this);
            System.out.println("Client déconnecté.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}