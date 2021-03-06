package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer implements Runnable {
    private ServerSocket server;
    private ArrayList<ClientRunner> clients = new ArrayList<ClientRunner>();
    public ChatServer() {
        try {
            server = new ServerSocket(8765);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while(true) {
            Socket clientSocket = null;
            try {
                clientSocket = server.accept();
                System.out.println("New client connected");
                ClientRunner client = new ClientRunner(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void transmit(Message m) {
        for(ClientRunner c : clients) {
            if(c != null) {
                c.transmitMessage(m);
            }
        }
    }

    public static void main(String[] args) {
        Thread t = new Thread(new ChatServer());
        t.start();
        try {
            t.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}