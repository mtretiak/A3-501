import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mtretiak on 2017-11-12.
 */
public class Client {
    private static ArrayList<Object> objectArray;

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
        objectArray = new ArrayList<>();


        while(!stop){
//            System.out.print("Enter a command to be sent to the server or 'Exit' to quit: ");


            System.out.println("\n--------------------------------------------");
            System.out.println("Create your own object: \n" +
            "Enter 1 for: Simple Object. Parameters: Int, Double\n" +
            "Enter 2 for: Reference Object. Creates simple object\n" +
            "Enter 3 for: Simple Array Object. \n" +
            "Enter 4 for: Reference Array Object.\n" +
            "Enter 5 for: Collection Object. \n" +
            "Enter 'Quit' to stop progarm.\n" +
            "Enter 'Send' to send over server. \n");
            System.out.println("\n--------------------------------------------");

            System.out.println("What would you like to create today?");





            while ((!inputClient.ready() && !inputBuffer.ready())){

            }
            if(inputClient.ready()){
                inputString = inputClient.readLine();
            }
            else if(inputBuffer.ready()){
                inputString = inputBuffer.readLine();
            }

//            if(inputString.contains("1")){
//                SimpleObj simpleObj = new SimpleObj();
//            }
//            if(inputString.contains("2")){
//                objectArray.add(createRefObj());
//            }



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




    private static SimpleObj createSimpleObj(){
        System.out.println("Enter your parameters for your simple object. One at a time.");

        SimpleObj simpleObj = null;

        try{
            Scanner input = new Scanner(System.in);


            System.out.println("Please enter the Integer parameter: eg. 1,2,89");
            while(!input.hasNextInt()){
                input.next();
                System.out.println("No no no. Simple object needs an Integer value. Try again: ");
            }

            int pInt = input.nextInt();

            System.out.println("PLease enter the Double parameter: eg. 2.0, 1000.1");
            while (!input.hasNextDouble()){
                System.out.println("Come on ... Simple object needs an Double value. Try again: ");
            }

            double pDouble = input.nextDouble();


            //create simple object HEERRRRREEEE
            simpleObj = new SimpleObj(pInt,pDouble);
            System.out.print("Your simple object has bee created with " + pInt + " and " + pDouble + " as parameters.");

        }
        catch (Exception e){
            e.printStackTrace();
        }


        return simpleObj;
    }


    private static RefObj createRefObj(){
        System.out.println("Let's create a reference object.");
        System.out.println("Let's use a simple object for reference.");

        SimpleObj simpleObj = createSimpleObj();
        RefObj refObj = new RefObj(simpleObj);

        System.out.println("Great we have a reference object created!!!");
        return refObj;
    }

}
