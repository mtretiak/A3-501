import java.io.*;
import java.net.Socket;

/**
 * Created by mtretiak on 2017-11-12.
 */
public class Client {
    public static void main(String args[])throws Exception{

        if (args.length != 2){
            System.out.println("Must enter two (2) arguments, <Server IP Address> <Server Forwarding Port>");
            System.exit(1);
        }

        //creates new socket client from command line arguments. Server IP then Port
        Socket client = new Socket(args[0], Integer.parseInt(args[1]));

        DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
        BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String inputString = "";
        boolean stop = false;
        BufferedReader inputClient = new BufferedReader(new InputStreamReader(System.in));


        while(!stop){
            System.out.print("Enter a command to be sent to the server or 'Exit' to quit: ");

            while ((!inputClient.ready() && !inputBuffer.ready())){

            }
            if(inputClient.ready()){
                inputString = inputClient.readLine();
            }
            else if(inputBuffer.ready()){
                inputString = inputBuffer.readLine();
            }

            if(inputString.contains("Send")) {

                outputStream.writeBytes(inputString + '\n');

                File file = new File("inputFile.xml");
                FileOutputStream outputFile = new FileOutputStream(file);
                ObjectOutputStream objOutput = new ObjectOutputStream(outputFile);

                ObjectInputStream test = new ObjectInputStream(client.getInputStream());

                objOutput.writeObject(test.readObject());
                objOutput.close();

                System.out.println("HERE");

                System.out.println("Client Sent File");

                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objIn = new ObjectInputStream(fileIn);
                DummbyObject testObj = (DummbyObject) objIn.readObject();


                inputString = inputBuffer.readLine();

                fileIn.close();

            }
            else{

                if (inputString.equals("Exit") || inputString.equals("exit"))
                    stop = true;

                outputStream.writeBytes(inputString + '\n');

                inputString = inputBuffer.readLine();

                System.out.println("\nServer: " + inputString);
            }


        }

        System.out.println("Goodbye");
        client.close();
    }

}
