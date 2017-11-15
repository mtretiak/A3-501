
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

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







//        String clientString;
//        ServerSocket serverSocket = new ServerSocket(Integer.valueOf(args[0]));
//        boolean stop = false;
//
//        if(args.length != 1){
//            System.out.println("Please check your Port Forwarding and enter a <Port>.");
//            System.exit(1);
//        }
//
//        Socket connection = serverSocket.accept();
//        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader((connection.getInputStream())));
//        DataOutputStream outputToClient = new DataOutputStream(connection.getOutputStream());
//
//
//        while(!stop){
//
//
//            clientString = inputFromClient.readLine();
//            System.out.println("String Received from Client: " + clientString);
//
//            if(clientString.contains("Exit")){
//                stop = true;
//                System.out.println("Stopped");
//            }
//            if(clientString.contains("Send")){
//                clientString = "";
//                DummbyObject objTest = new DummbyObject();
//
//                Document document = Serializer.serializeObj(objTest);
//                File outputFile = new File("outputFile.xml");
//
//                Deserializer.deserialize(document);
//
//                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//                objectOutputStream.writeObject(objTest);
//                objectOutputStream.close();
//
//                System.out.println("Connection has been made and file has been deserialized and recieved.");
//
//                byte[] byteArray = new byte[(int) outputFile.length()];
//
//                FileInputStream fileIn = null;
//
//                try{
//                    fileIn = new FileInputStream(outputFile);
//                }catch (FileNotFoundException e){
//
//                }
//
//                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileIn);
//
//                try{
//                    bufferedInputStream.read(byteArray, 0, byteArray.length);
//                    outputToClient.write(byteArray, 0, byteArray.length);
//                    outputToClient.flush();
//                }catch (IOException e){
//
//                }
//
//                System.out.println("Server has sent file.");
//
//            }
//
//            clientString = clientString.toUpperCase() + '\n';
//            outputToClient.writeBytes(clientString);
//
//        }
    }
}
