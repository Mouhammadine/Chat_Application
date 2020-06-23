package view;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        try {
            Socket s = new Socket("localhost", 1234);
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Donner un nombre");
            int nb = scanner.nextInt();

            os.write(nb);
            int rep= is.read();
            System.out.println("Resultat="+rep);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
