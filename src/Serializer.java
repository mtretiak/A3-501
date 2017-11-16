import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import org.jdom2.Document;
import org.jdom2.Element;


/**
 * Created by mtretiak on 2017-11-11.
 */

/**
 * Serializer
 * Purpose: to serialize objects to be put into a document then creates an XML file to be used to be sent to a server
 * side connection
 */
public class Serializer{

//    private final Map<Long, Object> hashMap = new IdentityHashMap<Long, Object>();
//    long nextId = 0;

    public static Document serialize(Object objectTBS){

        //initialize Document with root element (tag name: serialized)
        Element rootElement = new Element("serialized");
        Document document = new Document(rootElement);

        IdentityHashMap hashMap = new IdentityHashMap<>();

        return serializeHelp(hashMap, document, objectTBS);
    }

    /**
     * SerializeHelp
     * This method does all the work.
     * purpose: serializes all the objects that come in. Based on type of object it will filter and do what is needed
     * Parameters: hashMap for the serialized objects, Document for output, and objectTBS is the object to be serialized
     * @param hashMap
     * @param document
     * @param objectTBS
     * @return
     */

    private static Document serializeHelp(IdentityHashMap hashMap, Document document, Object objectTBS) {


        //utlities needed
        ArrayList<Element> elementContents = new ArrayList<>();
        ArrayList<Element> arrayValues = new ArrayList<>();
        ArrayList<Element> arrayReferences = new ArrayList<>();
        Element obj;
        Element value;
        Element reference;
        Element fieldE;

        Class objectClass = objectTBS.getClass();

        try {
            //set id for the object to the size of the hashmap
            String objId = Integer.toString(hashMap.size());
            hashMap.put(objId, objectTBS);

            //create attributes
            obj = new Element("object");
            obj.setAttribute("class", objectClass.getName());
            obj.setAttribute("id", objId);
            document.getRootElement().addContent(obj);


            //checks if array
            if(objectClass.isArray()){

                String arrayLength = String.valueOf(Array.getLength(objectTBS));
                obj.setAttribute("length", arrayLength);

                //gets the type of array for the elements to be recreated
                Class elementType = objectClass.getComponentType();


                for(int i=0; i < Array.getLength(objectTBS); i++){
                    if(elementType.isPrimitive()){
                        value = new Element("value");
                        String elementValue =  String.valueOf(Array.get(objectTBS, i));
                        value.addContent(elementValue);

                        arrayValues.add(value);
                        elementContents = arrayValues;

                    }
                    else {

                        //object id
                        reference = new Element("reference");
                        String arrayObjId = Integer.toString(hashMap.size());
                        reference.addContent(arrayObjId);
                        arrayReferences.add(reference);
                        elementContents = arrayReferences;

                        //if reference we need to serialize all elements
                        if(!hashMap.containsKey(arrayObjId)){
                            Object arrayElementObj = Array.get(objectTBS, i);
                            serializeHelp(hashMap, document,arrayElementObj);
                        }
                    }
                }
                obj.setContent(elementContents);

            }

            //get list of all object fields
            Field objFields[] = objectTBS.getClass().getDeclaredFields();

            //Serializes all fields
            for (Field objectField : objFields){

                //change accessibility to true for all non-public
                if(!Modifier.isPublic(objectField.getModifiers())){
                    objectField.setAccessible(true);
                }


                Object fieldObj = objectField.get(objectTBS);

                fieldE = serializeFields(fieldObj, objectField,  hashMap,document);

                System.out.println(objectField.getName());

                obj.addContent(fieldE);

            }

        }
        catch(Exception e){
            e.printStackTrace();
        }

        //return serialized object as Document
        return document;
    }

    /**
     * serializeFields
     * purpose: serialize each field before sending as it is
     * a special case
     * @param fieldObj
     * @param objectField
     * @param hashMap
     * @param document
     * @return
     */
    public static Element serializeFields( Object fieldObj, Field objectField, IdentityHashMap hashMap, Document document){

        if(!Modifier.isPublic(objectField.getModifiers())){
            objectField.setAccessible(true);
        }

        Element fieldElement;

        if(fieldObj != null){
            fieldElement = new Element("field");

            try{
                String declaringClass =  objectField.getDeclaringClass().getName();
                String name = objectField.getName();
                fieldElement.setAttribute("name", name);
                fieldElement.setAttribute("declaringclass", declaringClass);

                Element value = new Element("value");
                Element reference = new Element("reference");

                Class fieldType = objectField.getType();

                if(!fieldType.isPrimitive()){

                    String fieldObjId = Integer.toString(hashMap.size());
                    reference.addContent(fieldObjId);
                    fieldElement.setContent(reference);

                    //pass back through since it is a reference to another object
                    serializeHelp(hashMap, document, fieldObj);
                }
                else{
                    String fieldValue = fieldObj.toString();
                    value.addContent(fieldValue);
                    fieldElement.setContent(value);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            fieldElement = new Element("null");
        }

        return fieldElement;

    }



}




