package controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServeurChat {

    int nbClients;
    private List<Conversation>  clientsConnectes = new ArrayList<>();


    public void run() {
        try {
            ServerSocket ss = new ServerSocket(234);


            while (true){
                Socket s = ss.accept();

                ++nbClients;
                Conversation c = new Conversation(s,nbClients);
                clientsConnectes.add(c);
                c.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void broadCast(String message, int[] numeroClients){
        try {
            for (Conversation c : clientsConnectes) {
                boolean trouve = false;
                for(int i=0;i<numeroClients.length;i++){
                    if(c.numeroClient==numeroClients[i]){
                        trouve=true;
                        break;
                    }
                }
                if(trouve==true) {
                    PrintWriter pw = new PrintWriter(c.socket.getOutputStream(), true);
                    pw.println(message);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    class  Conversation extends Thread{

        protected   Socket socket;
        protected int numeroClient;

        public Conversation(Socket socket, int num){

            super();
            this.socket=socket;
            this.numeroClient=num;
        }

        @Override
        public void run() {

            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);


                OutputStream os = socket.getOutputStream();
                PrintWriter pw =  new PrintWriter(os,true);


                String IP = socket.getRemoteSocketAddress().toString();
                System.out.println("Connexion du client numéro"+numeroClient+"IP="+IP);

                pw.println("Bienvenue , vous étre le client numéro"+numeroClient);
                pw.println("Devinez le nombre secret en 0 et 1000");

                while (true){
                    String req;
                    while ((req=br.readLine())!=null){
                        String[] t= req.split("-");
                        String message = t[0];
                        String[] t2=t[1].split(",");
                        int [] numeroClients = new int [t2.length];

                        for( int i=0 ; i<t2.length;i++){
                            numeroClients[i]= Integer.parseInt(t2[i]);
                        }


                        broadCast(message, numeroClients);




                    }



                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    public static void main(String[] args) {
        new ServeurChat(). start();

    }

    private void start() {
    }


}
