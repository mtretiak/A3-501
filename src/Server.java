
import org.jdom2.Document;
import org.jdom2.input.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mtretiak on 2017-11-12.
 */
public class Server {
    public static void main(String args[]){

        try{
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(args[0]));

            if(args.length != 1){
            System.out.println("Please check your Port Forwarding and enter a <Port>.");
            System.exit(1);
        }

            Socket socket = serverSocket.accept();
            System.out.println("Server Connected . . .");

            File file = new File("serverFile.xml");

            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte[] byteArray = new byte[1024*1024];
            int i = 0;
            while((i = inputStream.read(byteArray))>0){
                fileOutputStream.write(byteArray, 0, i);
                break;
            }
            System.out.println("File Received on Server end");


            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = (Document) saxBuilder.build(file);
            Object object = Deserializer.deserialize(document);

            System.out.println("\n-------------Starting Inspection--------------");
            Inspector inspector = new Inspector();
            inspector.inspect(object, false);
            System.out.println("\n-------------Inspection Done--------------");



            socket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
