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
        ObjectCreator objCreator = new ObjectCreator();

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
                objectArray.add(objCreator.createSimpleObj());
            }
            else if(objectToCreate.equals("reference")){
                objectArray.add(objCreator.createRefObj());
            }
            else if(objectToCreate.equals("simple_array")){
                objectArray.add(objCreator.createSimpleArray());
            }
            else if(objectToCreate.equals("reference_array")){
                objectArray.add(objCreator.createRefArray());
            }
            //collection not working
            else if(objectToCreate.equals("collection")){
                objectArray.add(objCreator.createRefObj());
            }
            else if(objectToCreate.equals("send")){
                serializeObjects(host, port);
            }
            else if(objectToCreate.equals("stop")){
                System.out.println("Goodbye");
                System.exit(1);
            }else{
                System.out.println("Input does not look correct, try again.");
            }
        }
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
