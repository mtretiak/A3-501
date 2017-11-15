import java.io.*;
import java.net.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.*;

/**
 * Created by mtretiak on 2017-11-12.
 */

/**
 * Purpose: to get user input on client side to prepare objects, arrays, etc to be serialized
 * and then sent to a server side!
 */
public class Client {

    private static ArrayList<Object> objectArray;


    /**
     * Main
     * purpose: driver for user client side input and object creation
     * this takes commandline arguments for server connection
     * @param args
     * @throws Exception
     */
    public static void main(String args[])throws Exception{

        objectArray = new ArrayList<>();
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        boolean stop = false;
        String objectToCreate;

        if (args.length != 2){
            System.out.println("Must enter two (2) arguments, <Server IP Address> <Server Forwarding Port>");
            System.exit(1);
        }



        while(!stop){
            System.out.println("\n*-------------------Create your own object:-------------------------*");
            System.out.println("\nWhat kind of object would you like to create?\n" +
            "\nSimple Objects: \n"+
            "Simple Object. Parameters: Int, Double\n" +
            "Simple Array Object.\n" +
            "\nReference Objects: \n" +
            "Reference Object. Creates simple object\n"  +
            "Reference Array Object.\n" +
            "\nCollection: \n"+
            "Collection Object. \n" +
            "\nEnter stop to quit program.\n" +
            "Enter send to serialize and send over server. \n");
            System.out.println("*-----------------------------------------------------------*\n");

            System.out.println("Which object would you like to create? Eg. 'simple object'\n");

            Scanner userInput = new Scanner(System.in);


            while (!userInput.hasNext()){
                userInput.next();
                System.out.println("Incorrect command. Please try again: ");
            }
            //change input to lower case for if cases
            objectToCreate = userInput.next().toLowerCase();

            //filter which object to create
            if(objectToCreate.equals("simple")){
                objectArray.add(createSimpleObj());
            }
            if(objectToCreate.equals("reference")){
                objectArray.add(createRefObj());
            }
            if(objectToCreate.equals("simple_array")){
                objectArray.add(createSimpleArray());
            }
            if(objectToCreate.equals("reference_array")){
                objectArray.add(createRefArray());
            }

            //collection not working
            if(objectToCreate.equals("collection")){
                objectArray.add(createRefObj());
            }
            if(objectToCreate.equals("send")){
                serializeObjects(host, port);
            }
            if(objectToCreate.equals("stop")){
                System.out.println("Goodbye");
                System.exit(1);
            }
        }
    }


    /**
     * createSimpleObj
     * purpose: to create a simple object with. user will enter int, double, float, and byte values
     * parameters: none
     * @return SimpleObj
     */
    private static SimpleObj createSimpleObj(){

        //prompt for user input
        System.out.println("Enter the parameters for your simple object. One at a time.");

        SimpleObj simpleObj = null;
        Scanner input = new Scanner(System.in);

        //prompt for user input of first parameter in our simple object
        System.out.println("Please enter the Integer parameter: eg. 1,2,89");

        //while scanner does not get an Int prompt user again!
        while(!input.hasNextInt()){
            input.next();
            System.out.println("No no no. Simple object needs an Integer value. Try again: ");
        }

        //once we have good input set parameter
        int integerParameter = input.nextInt();

        System.out.println("PLease enter the Double parameter: eg. 2.0, 1000.1");
        while (!input.hasNextDouble()){
            System.out.println("Come on ... Simple object needs a Double value. Try again: eg. 2.0 ");
        }

        double doubleParameter = input.nextDouble();


        System.out.println("PLease enter the Float parameter: eg. 65");
        while (!input.hasNextFloat()){
            System.out.println("Come on ... Simple object needs a float value. Try again:  ");
        }

        float floatParameter = input.nextFloat();

        System.out.println("PLease enter the byte parameter: eg. 2");
        while (!input.hasNextByte()){
            System.out.println("Come on ... Simple object needs a byte value. Try again: eg. 2 ");
        }

        byte byteParameter = input.nextByte();


        //creates simple object with userInput parameters
        simpleObj = new SimpleObj(integerParameter,doubleParameter, floatParameter, byteParameter);
        System.out.print("Your simple object has bee created with " + integerParameter + " and " + doubleParameter + " as parameters.");


        return simpleObj;
    }

    /**
     * createSimpleArray
     * purpose: create an array. needs user input for length
     * @return
     */
    private static SimpleArray createSimpleArray() {
        System.out.println("Enter the length of your array: ");

        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Be sure to enter an integer value please!");
        }

        int length = userInput.nextInt();
        int[] newArray = new int[length];

        SimpleArray simpleArray = new SimpleArray(newArray);
        System.out.println("simple array has been made.");
        return simpleArray;
    }


    /**
     * createRefArray
     * purpose: creates an object array of simple objects.
     * User input is needed to create the simple objects
     * @return
     */
    private static RefArray createRefArray(){
        System.out.println("Enter the length of your array: ");

        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Be sure to enter an integer value please!");
        }

        int length = userInput.nextInt();
        Object[] newArray = new Object[length];

        for(int i = 0; i<length; i++){
            SimpleObj simpleObj = createSimpleObj();
            newArray[i] = simpleObj;
        }

        RefArray refArray = new RefArray(newArray);
        System.out.println("reference array has been made.");
        return refArray;

    }
    /**
     * createRefObj
     * purpose: creates a reference object using a simple object
     * User input is needed to create the simple objects
     * @return
     */
    private static RefObj createRefObj(){
        System.out.println("Let's create a reference object.\n");
        System.out.println("We'll have to create a simple object for reference.");

        //call simple object to be create and get user prompts
        SimpleObj simpleObj = createSimpleObj();
        //create reference object based off simpleObject
        RefObj refObj = new RefObj(simpleObj);

        System.out.println("Great we have a reference object created!!!");
        return refObj;
    }


    /**
     * serializeObjects
     * purpose: Serializes objects, creates xml doc, and sends file to server side.
     * @param host these are given via command line
     * @param port
     */
    private static void serializeObjects(String host, int port){
        //serialize and send created objects list

        for(Object obj : objectArray) {


            System.out.println("Object is being Serialized");
            Document document = Serializer.serialize(obj);

            Element rootElement = document.getRootElement();
            System.out.println(rootElement.getName());

            Element objElement = rootElement.getChild("object");


            File file = new File("clientFile.xml");
            try {
                //found on jdom.org
                XMLOutputter xmlOutputter = new XMLOutputter();
                xmlOutputter.setFormat(Format.getPrettyFormat());
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                xmlOutputter.output(document, bufferedWriter);
                bufferedWriter.close();
                System.out.println("file created");

                //new socket to create file
                Socket socket = new Socket(host, port);

                //code help found on stackOverflow
                OutputStream out = socket.getOutputStream();
                FileInputStream in = new FileInputStream(file);
                //send file as byte array stream
                byte[] fileBytes = new byte[100000];
                int bytesRead;
                while ((bytesRead = in.read(fileBytes)) > 0) {
                    out.write(fileBytes, 0, bytesRead);
                    System.out.println(bytesRead);
                }


                in.close();
                out.close();
                socket.close();

                System.out.println("File has been sent successfully");

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

}
