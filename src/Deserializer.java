import java.io.File;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

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
    public static Object deserialize(Document document){

       Element root = document.getRootElement();
       List objectList = root.getChildren();
       HashMap hashMap = new HashMap();
       Object object = null;

       try{
           createObject(objectList, hashMap);

           setFields(objectList, hashMap);;

           object = hashMap.get("0");
       }catch (Exception e){
           e.printStackTrace();
       }
       return object;

    }


    /**
     * createObject
     * purpose: creates the objects
     * @param objectList
     * @param hashMap
     */
    public static void createObject(List objectList, HashMap hashMap){

        for(int i = 0; i<objectList.size();i++){
            try{
                Element objectE = (Element) objectList.get(i);

                Class objectClass = Class.forName(objectE.getAttributeValue("class"));

                Object objectInstance;
                if(objectClass.isArray()){

                    //get length of array
                    int length = Integer.parseInt(objectE.getAttributeValue("length"));
                    //get type of array
                    Class type = objectClass.getComponentType();

                    //build new array
                    objectInstance = Array.newInstance(type, length);
                }else{

                    //is not array
                    Constructor constructor = objectClass.getConstructor(null);

                    if(!Modifier.isPublic(constructor.getModifiers())){
                        constructor.setAccessible(true);
                    }

                    objectInstance = constructor.newInstance(null);
                }

                String objectId = objectE.getAttributeValue("id");
                hashMap.put(objectId, objectInstance);
             }catch (Exception e){
                e.printStackTrace();

            }

        }

    }

    /**
     * setFields
     * purpose: finds all attributes of the elements and creates fields from them
     * @param objectList
     * @param hashMap
     */
    public static void setFields(List objectList, HashMap hashMap){
        for(int i = 0; i<objectList.size(); i++){
            try{
                Element objectE = (Element) objectList.get(i);
                Object objectInstance = hashMap.get(objectE.getAttributeValue("id"));
                List children = objectE.getChildren();

                Class objectClass = objectInstance.getClass();
                System.out.println(objectClass.getName());

                if(objectClass.isArray()){
                    Class type = objectClass.getComponentType();
                    for(int n = 0; n < children.size(); n++){
                        Element arrayElements = (Element) children.get(n);

                        Object object;

                        String elementType = arrayElements.getName();

                        if(elementType.equals("reference")){
                            object = hashMap.get(arrayElements.getText());
                        }else if(elementType.equals("value")){
                            object = deserializeField(type, arrayElements);
                        }else{
                            object = arrayElements.getText();
                        }
                    }
                }else{
                    //is not array
                    for(int n = 0; n<children.size(); n++) {
                        //for all children

                        Element fieldE = (Element) children.get(n);

                        String name = fieldE.getAttributeValue("name");
                        Class declaringClass = Class.forName(fieldE.getAttributeValue("declaringclass"));
                        Field field = declaringClass.getDeclaredField(name);

                        if (!Modifier.isPublic(field.getModifiers())) {
                            //set so we can access
                            field.setAccessible(true);
                            Field modifiers = Field.class.getDeclaredField("modifiers");
                        }

                        Class type = field.getType();
                        Element fieldContent = (Element) fieldE.getChildren().get(0);
                        Object obj;
                        if (type.equals("reference")) {
                            obj = hashMap.get(fieldContent.getText());
                        } else if (type.equals("value")) {
                            obj = deserializeField(type, fieldContent);
                        } else {
                            obj = fieldContent.getText();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    /**
     * deserializeField
     * purpose: deserializes all field based on type
     * @param type
     * @param value
     * @return
     */
    public static Object deserializeField(Class type, Element value){

        Object valueObject = null;

        if(type.equals(boolean.class)){
            String bool = value.getText();
        }else if (type.equals(byte.class)){
            valueObject = Byte.valueOf(value.getText());
        }else if (type.equals(double.class)){
            valueObject = Double.valueOf(value.getText());
        }else if (type.equals(float.class)){
            valueObject = Float.valueOf(value.getText());
        }else if (type.equals(int.class)){
            valueObject = Integer.valueOf(value.getText());
        }else if (type.equals(long.class)){
            valueObject = Long.valueOf(value.getText());
        }else if (type.equals(short.class)){
            valueObject = Short.valueOf(value.getText());
        }

        return valueObject;
    }
}
