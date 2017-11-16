import java.lang.reflect.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by mtretiak on 2017-11-15.
 * DUPLICATE FROM ASSIGNMENT 2
 */
public class Inspector {

    private Vector objectsToInspect;

    //Constructor to initialize Vector
    public Inspector(){
        this.objectsToInspect = new Vector();
    }


    /************************
     * Preform introspection on passed object at runtime and display/output
     * Information of object.
     * @param obj to be inspected
     * @param recursive to search all fields
     */

    public void inspect(Object obj, boolean recursive)
    {
        if(obj != null){
            Class ObjClass = obj.getClass();

            System.out.println("inside inspector: " + obj.getClass().getTypeName() + " (recursive = "+recursive+")");

            if(obj.getClass().isArray()){

                for(int i = 0; i < Array.getLength(obj); i++){
                    System.out.println("Element at index " + i + " = ");
                    inspect(Array.get(obj, i), recursive);
                }
            }
            else{
                //inspect the current class
                inspectDeclaringClass(obj);
                inspectInterfaceClass(obj);
                inspectMethods(obj);
                inspectConstructor(obj);
                inspectField(obj, ObjClass,objectsToInspect);
                inspectSuperclass(obj);

                if(recursive)
                    inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
            }
        }
        else{
            System.out.println("NULL");
        }
    }


    /************************
     *Inspect classes fields if recursive is set.
     * @param obj
     * @param ObjClass
     * @param objectsToInspect
     * @param recursive
     */
    private void inspectFieldClasses(Object obj,Class ObjClass,
                                     Vector objectsToInspect,boolean recursive)
    {
        if(objectsToInspect.size() > 0 )
            System.out.println("---- Inspecting Field Classes ----");


        Enumeration e = objectsToInspect.elements();
        while(e.hasMoreElements())
        {
            Field f = (Field) e.nextElement();
            System.out.println("Inspecting Field: " + f.getName() );

            try
            {
                System.out.println("******************");
                inspect( f.get(obj) , recursive);
                System.out.println("******************");
            }
            catch(Exception exp) { exp.printStackTrace(); }
        }
    }


    /************************
     * Inspect declaring class
     * print name of declaring class.
     * @param obj
     */

    protected void inspectDeclaringClass(Object obj){
        System.out.println("\n--------Inspecting Declaring Class--------\n");

        if(!obj.getClass().getName().isEmpty())
        {
            System.out.println("Class Name = " + obj.getClass().getName());
        } else
        {
            System.out.println("Null");
        }
    }

    /************************
     * Inspect superclass
     * print: name, interfaces, constructor, parameters, modifiers, methods, exceptions
     * 			return type
     * @param obj
     */

    protected void inspectSuperclass(Object obj){
        System.out.println("\n--------Inspecting Immediate Superclass--------\n");

        if(!obj.getClass().getSuperclass().getName().isEmpty())
        {
            System.out.println("Immediate Superclass Name = " + obj.getClass().getSuperclass().getName());
        } else
        {
            System.out.println("No Superclass");
        }

        if(obj.getClass().getSuperclass().getInterfaces().length>=1){
            for(Class interFace:obj.getClass().getSuperclass().getInterfaces()){
                System.out.println("Superclass Interface " + " = " + interFace.getName());
            }
        } else {
            System.out.println("No Interfaces");
        }


        //get all constructors
        for(Constructor<?> constructor : obj.getClass().getSuperclass().getConstructors()){
            System.out.println("Superclass Constructor = " + constructor.getName());

            //for all parameters in the constructor
            for(Parameter parameter : constructor.getParameters()){
                System.out.println("Superclass Constructor Parameters = " + parameter.getParameterizedType());
            }
            System.out.println("Superclass Constructor Modifier = " + Modifier.toString(constructor.getModifiers()) + "\n");
        }



        for(Method method:obj.getClass().getSuperclass().getMethods()){
            if(method.getDeclaringClass().equals(obj.getClass().getSuperclass())){
                System.out.println("\nSuperclass Method " + " Name = " + method.getName());

                //loop to handle multiple exceptions and params
                for(Class exceptions : method.getExceptionTypes()){
                    System.out.println("Exceptions Thrown by class = " + exceptions.getName());
                }
                for(Parameter parameter : method.getParameters()){
                    System.out.println("Exceptions Thrown by class = " + parameter.getName());
                }

                System.out.println("Return Type = " + method.getReturnType().getName());

                System.out.println("Modifiers = " + Modifier.toString(method.getModifiers()));

            }
        }
    }


    /************************
     * Inspect class interfaces
     * print: interfaces
     * @param obj
     */
    protected void inspectInterfaceClass (Object obj){
        System.out.println("\n--------Inspecting Class Interfaces--------\n");

        int i = 1;

        if(obj.getClass().getInterfaces().length>=1){
            for(Class interFace:obj.getClass().getInterfaces()){
                System.out.println("Interface " + i + " = " + interFace.getName());
                i++;
            }
        } else {
            System.out.println("No Interfaces");
        }
    }


    /************************
     * Inspect methods
     * Print: name , exceptions, return type, modifiers of method
     * @param obj
     */
    protected void inspectMethods(Object obj){
        int i=1;
        System.out.println("\n--------Inspecting Methods--------\n");
        for(Method method:obj.getClass().getMethods()){
            if(method.getDeclaringClass().equals(obj.getClass())){
                System.out.println("\nMethod " + i + " Name = " + method.getName());

                //loop to handle multiple exceptions and params
                for(Class exceptions : method.getExceptionTypes()){
                    System.out.println("Exceptions Thrown by class = " + exceptions.getName());
                }
                for(Parameter parameter : method.getParameters()){
                    System.out.println("Exceptions Thrown by class = " + parameter.getName());
                }

                System.out.println("Return Type = " + method.getReturnType().getName());

                System.out.println("Modifiers = " + Modifier.toString(method.getModifiers()));

                i++;
            }
        }
    }

    /************************
     * Inspect Constructr
     * print: name, parameters, and modifier of constructor
     * @param obj
     */
    protected void inspectConstructor (Object obj){
        System.out.println("\n--------Inspecting Constructor--------\n");

        //get all constructors
        for(Constructor<?> constructor : obj.getClass().getConstructors()){
            System.out.println("Constructor = " + constructor.getName());

            //for all parameters in the constructor
            for(Parameter parameter : constructor.getParameters()){
                System.out.println("Constructor Parameters = " + parameter.getParameterizedType());
            }
            System.out.println("Constructor Modifier = " + Modifier.toString(constructor.getModifiers()) + "\n");
        }
    }

    /************************
     * Inspect given object fields
     * has print help
     * @param obj
     * @param oClass
     * @param objToInspect
     */
    protected void inspectField(Object obj, Class oClass, Vector objToInspect){

        System.out.println("\n--------Inspecting Fields--------\n");

        if(oClass.getDeclaredFields().length >= 1){
            for(Field field : oClass.getDeclaredFields()){
                field.setAccessible(true);

                try{
                    printFieldInformation(field, obj);
                } catch (Exception e){

                }
            }
            if(oClass.getSuperclass() != null){
                if(oClass.getSuperclass().getDeclaredFields().length >= 1){
                    inspectField(obj,oClass.getSuperclass(), objToInspect);
                }
            }
        }

    }

    /************************
     * Print helper for inspect field
     * @param field
     * @param obj
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void printFieldInformation(Field field, Object obj) throws IllegalArgumentException, IllegalAccessException{
        System.out.println("\nField = " + field.getName() + "\nType = " + field.getType().getName()
                + " = " + field.get(obj).toString() + "\nModifier = " + Modifier.toString(field.getModifiers()) +"\n");

        if(field.getType().isArray()){
            System.out.format("---Array---%n" + "Length = %s%n" + "Component Type: %s%n",
                    Array.getLength(field.get(obj)), field.getType().getComponentType().getName());
            System.out.println("HErEEEEE " + Array.getLength(field.get(obj)));
        }

    }

    public Vector getObjectVector() {
        return objectsToInspect;
    }
}
