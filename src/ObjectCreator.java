import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mtretiak on 2017-11-15.
 */
public class ObjectCreator {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){

    }

    /**
     * createSimpleObj
     * this creates a simple object with double, int, float, and byte values
     * @return
     */
    public static SimpleObject createSimpleObj(){

        //prompt for user input
        System.out.println("Enter the parameters for your simple object. One at a time.");

        SimpleObject simpleObject = null;
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
        simpleObject = new SimpleObject(integerParameter,doubleParameter, floatParameter, byteParameter);
        System.out.print("Your simple object has bee created with " + integerParameter + " and " + doubleParameter + " as parameters.");


        return simpleObject;
    }

    /**
     * createRefArray
     * purpose: creates an object array of simple objects.
     * User input is needed to create the simple objects
     * @return
     */
    public static ReferenceArray createRefArray(){
        System.out.println("Enter the length of your array: ");

        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Be sure to enter an integer value please!");
        }

        int length = userInput.nextInt();
        Object[] newArray = new Object[length];

        for(int i = 0; i<length; i++){
            SimpleObject simpleObject = createSimpleObj();
            newArray[i] = simpleObject;
        }

        ReferenceArray referenceArray = new ReferenceArray(newArray);
        System.out.println("reference array has been made.");
        return referenceArray;

    }

    /**
     //     * createSimpleArray
     //     * purpose: create an array. needs user input for length
     //     * @return
     //     */
    public static SimpleArray createSimpleArray() {
        System.out.println("Enter the length of your array: ");

        Scanner userInput = new Scanner(System.in);

        while (!userInput.hasNextInt()) {
            userInput.next();
            System.out.println("Be sure to enter an integer value please!");
        }

        int length = userInput.nextInt();
        int[] newArray = new int[length];

        for(int i = 0; i< newArray.length;i++)
        {
            System.out.println(newArray[i]);
        }

        SimpleArray simpleArray = new SimpleArray(newArray);
        System.out.println("simple array has been made.");
        return simpleArray;
    }


    /**
     //     * createRefObj
     //     * purpose: creates a reference object using a simple object
     //     * User input is needed to create the simple objects
     //     * @return
     //     */
    public static ReferenceObject createRefObj(){
        System.out.println("Let's create a reference object.\n");
        System.out.println("We'll have to create a simple object for reference.");

        //call simple object to be create and get user prompts
        SimpleObject simpleObject = createSimpleObj();
        //create reference object based off simpleObject
        ReferenceObject referenceObject = new ReferenceObject(simpleObject);

        System.out.println("Great we have a reference object created!!!");
        return referenceObject;
    }

    /**
     * CreatecollectionObject
     * creates a collection of simple objects up to 10 based on the users choice.
     * @return
     */
    public static CollectionObject createCollectionObject(){

        Scanner userInput = new Scanner(System.in);

        System.out.println("Let's create a collection of simple objects!");
        System.out.println("\nHow many objects would you like to make? max 10");

        int objectAmount = 0;

        while (!userInput.hasNextInt()){
            userInput.next();
            System.out.println("Please enter a integer between 1-4");
        }

//        while(userInput.nextInt()>4){
//            userInput.next();
//            System.out.println("Number is toooooo high. Try Again: ");
//        }

        objectAmount = userInput.nextInt();

        CollectionObject collectionObj = new CollectionObject();

        ArrayList<SimpleObject> collectionParameter = new ArrayList<>();


        for(int i = 0 ; i<objectAmount; i++){
//            System.out.println("HHEREREREREREER");
            SimpleObject simpleObject = createSimpleObj();
            collectionParameter.add(simpleObject);

        }

        return collectionObj;

    }


}
