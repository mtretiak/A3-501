import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;


/**
 * Created by mtretiak on 2017-11-12.
 */
public class Deserializer {

    /**
     * deserialize
     * purpose: this builds the objects after deserializing the xml document it has been passed.
     * this is done on the server end.
     * @param document
     * @return
     */
    /**
     * Deserialize document and build object from from document contents
     * @param document - Document of serialized object, to now be deserialized
     * @return deserialized object
     */
    public static Object deserialize(Document document){
        //get root element and list of nested object elements
        Element rootElement = document.getRootElement();
        List objList = rootElement.getChildren();
        HashMap objMap =  new HashMap();


        //Object to be instantiated via deserialization
        Object obj = null;

        try{

            //create instances of each object in object list
            createObjectInstances(objList, objMap);

            //set values for object fields using field elements
            setFieldValues(objList, objMap);

            //first object in object HashMap should be main object that was serialized, main one we care about
            obj = objMap.get("0");

        }
        catch(Exception e){
            e.printStackTrace();
        }

        //return deserialized object
        return obj;
    }


    /**
     * Deserialize value from field element content
     * @param fieldType class type of field element
     * @param valueElement element containing field value
     * @return
     */
    private static Object deserializeFieldValue(Class fieldType, Element valueElement){

        Object valueObject = null;

        if(fieldType.equals(int.class))
            valueObject = Integer.valueOf(valueElement.getText());
        else if(fieldType.equals(byte.class))
            valueObject = Byte.valueOf(valueElement.getText());
        else if(fieldType.equals(short.class))
            valueObject = Short.valueOf(valueElement.getText());
        else if(fieldType.equals(long.class))
            valueObject = Long.valueOf(valueElement.getText());
        else if(fieldType.equals(float.class))
            valueObject = Float.valueOf(valueElement.getText());
        else if(fieldType.equals(double.class))
            valueObject = Double.valueOf(valueElement.getText());
        else if(fieldType.equals(boolean.class)){

            String boolString = valueElement.getText();

            if(boolString.equals("true"))
                valueObject = Boolean.TRUE;
            else
                valueObject = Boolean.FALSE;
        }

        return valueObject;
    }

    /**
     * General method for deserializing a content element into an object
     * @param classType - type of content element class
     * @param contentElement - element that is content of another element
     * @param objMap - HashMap of deserialized objects
     * @return
     */
    private static Object deserializeContentElement(Class classType, Element contentElement, HashMap objMap){
        Object contentObject;

        String contentType = contentElement.getName();

        if(contentType.equals("reference")){
            contentObject = objMap.get(contentElement.getText());
        }
        else if(contentType.equals("value"))
            contentObject = deserializeFieldValue(classType, contentElement);
        else
            contentObject = contentElement.getText();


        return contentObject;
    }

    /**
     * Create instances for all object elements from Document
     * @param objList - list of object elements from Document
     * @param objMap - HashMap of deserialized objects
     */
    private static void createObjectInstances(List objList, HashMap objMap){
        for(int i =0; i < objList.size(); i++){
            try{
                Element objElement = (Element) objList.get(i);

                //create uninitialized instance using element attribute
                Class objClass =  Class.forName(objElement.getAttributeValue("class"));
                //System.out.println(objClass.getName());

                //check for class type then create new instance
                Object objInstance;
                if(objClass.isArray()){

                    //get length (via element attributes) and component type of array object instantiation
                    int arrayLength = Integer.parseInt(objElement.getAttributeValue("length"));
                    Class arrayType = objClass.getComponentType();

                    objInstance = Array.newInstance(arrayType, arrayLength);

                }
                else{
                    //non-array object, instantiate with no arg constructor
                    Constructor constructor =  objClass.getConstructor(null);
                    //check constructor modifiers, just in case
                    if(!Modifier.isPublic(constructor.getModifiers())){
                        constructor.setAccessible(true);
                    }

                    objInstance = constructor.newInstance(null);
                }

                //associate the new instance with the object's unique id (element attribute)
                String objId = objElement.getAttributeValue("id");
                objMap.put(objId, objInstance);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * set values for fields using field element attributes from Document
     * @param objList - list of objects from Document
     * @param objMap - HashMap of deserialized objects
     */
    private static void setFieldValues(List objList, HashMap objMap){
        for(int i =0; i < objList.size(); i++){
            try{
                Element objElement = (Element) objList.get(i);

                Object objInstance =  objMap.get(objElement.getAttributeValue("id"));


                //get list of all children of object element (fields if non-array, elements if array)
                List objChildrenList = objElement.getChildren();


                // if array object, set value of each element
                // if non-array object, assign values to all fields/instance variables
                Class objClass = objInstance.getClass();
                System.out.println(objClass.getName());

//                System.out.println("HEREREREREER " + objClass.isArray() + " " + objClass.getName());

                if(objClass.isArray()){


                    //set values for each array element
                    Class arrayType =  objClass.getComponentType();
                    for(int j= 0; j < objChildrenList.size(); j++){
                        Element arrayContentElement = (Element) objChildrenList.get(j);

                        Object arrayContent = deserializeContentElement(arrayType, arrayContentElement, objMap);

                        Array.set(objInstance, j, arrayContent);

                    }
                }
                else{

//                    System.out.println("HEREREREREER " + objClass.isArray() + " " + objClass.getName());
                    //non-array object, assign values to all fields
                    for(int j = 0; j < objChildrenList.size(); j++){
                        Element fieldElement = (Element) objChildrenList.get(j);

                        //get declaring class and field name (via field attributes) and load Field metaobject dynamically
                        Class declaringClass =  Class.forName(fieldElement.getAttributeValue("declaringclass"));
                        String fieldName = fieldElement.getAttributeValue("name");
                        Field field = declaringClass.getDeclaredField(fieldName);

                        if(!Modifier.isPublic(field.getModifiers())){
                            field.setAccessible(true);

                            //in case field also has final modifier, reset modifiers field with bitwise negation
                            Field modifiersField = Field.class.getDeclaredField("modifiers");
                            modifiersField.setAccessible(true);
                            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                        }

                        //check field element content for value/reference and set accordingly
                        Class fieldType = field.getType();
                        Element fieldContentElement = (Element) fieldElement.getChildren().get(0);

                        Object fieldContent = deserializeContentElement(fieldType, fieldContentElement, objMap);

                        field.set(objInstance, fieldContent);
                    }

                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        //end of loop, object list should be deserialized and instantiated
    }
}
