import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread {

    int nbClients;
    private  int  nombreSecret;
    private  boolean fin ;
    private  String gagnant;
    private static ServerMT me;



    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(234);
            nombreSecret=(int)(Math.random() * 1000);

            while (true){
                Socket s = ss.accept();
                ++nbClients;
                new Conversation(s,nbClients).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class  Conversation extends Thread{

        private  Socket socket;
        private  int numeroClient;

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
                        System.out.println(IP+"à envoyer"+req);

                        int  nb = Integer.parseInt(req);
                        if(fin==false){
                            if(nb<nombreSecret){
                                pw.println("votre nombre est plus petit");


                            }
                            else if(nb>nombreSecret){
                                pw.println("votre nombre est plus grand ");


                            }
                            else {
                                synchronized (me){
                                    gagnant = IP;
                                    fin= true;
                                }

                                pw.println("Bravo , vous avez gagné......");
                                System.out.println("***********************");
                                System.out.println("Bravooooooooo mr "+IP);
                                System.out.println("****************");
                            }
                        }
                        else {
                            pw.println(" Le jeu est terminé , le gagnant est:"+gagnant);

                        }

                        String rep= "Taille="+req.length();
                        pw.println(rep);

                    }



                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    public static void main(String[] args) {
        me = new ServerMT();
        me.start();

    }
}
