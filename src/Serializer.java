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

    public static Document serializeHelp(IdentityHashMap hashMap, Document document, Object objectTBS) {


        //utlities needed
        ArrayList<Element> elementContents = new ArrayList<>();
        ArrayList<Element> arrayValues = new ArrayList<>();
        ArrayList<Element> arrayReferences = new ArrayList<>();
        Element obj;
        Element value;
        Element reference;
        Element fieldE;

        try {
            //set id for the object to the size of the hashmap
            String objId = Integer.toString(hashMap.size());
            hashMap.put(objId, objectTBS);

            //create attributes
            obj = new Element("object");
            obj.setAttribute("class", objectTBS.getClass().getName());
            obj.setAttribute("id", objId);
            document.getRootElement().addContent(obj);


            //checks if array
            if(objectTBS.getClass().isArray()){

                //gets the type of array for the elements to be recreated
                Class elementType = objectTBS.getClass().getComponentType();

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

                //when field is not null serialize
                if(fieldObj != null){

                    String dClass = objectField.getDeclaringClass().getName();
                    String fName = objectField.getName();
                    fieldE = new Element("field");
                    fieldE.setAttribute("name", fName);
                    fieldE.setAttribute("declaringClass", dClass);

                    Element fieldValue = new Element("value");
                    Element fieldReference = new Element("reference");

                    Class fType = objectField.getType();


                    if (!fType.isPrimitive()) {
                        String fieldObjId = Integer.toString(hashMap.size());

                        fieldReference.addContent(objId);

                        fieldE.setContent(fieldReference);


                        serializeHelp(hashMap, document, fieldObj);
                    } else {
                        String fValue = fieldObj.toString();
                        fieldValue.addContent(fValue);

                        fieldE.setContent(fieldValue);
                    }
                }else{
                    fieldE = new Element("null");

                }
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
}














































