import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.IdentityHashMap;
import java.util.Vector;

/**
 * Created by mtretiak on 2017-11-11.
 */
public class Serializer {


    public static Document serializeObj(Object obj){
        Vector objToInspect = new Vector<Object>();
        IdentityHashMap hashMap = new IdentityHashMap();

        String hashSize = Integer.toString(hashMap.size());
        hashMap.put(obj, hashSize);

        Element root = new Element("Serialized");
        Document document = new Document(root);

        try{
            document = serializeHelp(document, obj, objToInspect, hashMap);
            inspectFieldClasses(document, obj, objToInspect, hashMap);
        }catch (IllegalArgumentException | IllegalAccessException e){
            e.printStackTrace();
        }
        return document;
    }


    public static Document serializeHelp(Document document, Object obj, Vector objToInspect, IdentityHashMap hashMap) throws IllegalArgumentException, IllegalAccessException {
            try {

                String id = String.valueOf(hashMap.get(obj));

                Element object = new Element("Object ");
                object.setAttribute(new Attribute("ID", id));
                object.setAttribute(new Attribute("Class", obj.getClass().getName()));


                Element value, field, reference = null;

                ArrayList<Element> fieldArrayELement = new ArrayList<>();
                ArrayList<Element> objArrayElement = new ArrayList<>();
                ArrayList<Element> valueArrayElement = new ArrayList<>();
                ArrayList<Element> referenceArrayElement = new ArrayList<>();
                ArrayList<Element> tempArray = new ArrayList<>();

                objArrayElement.add(object);

                if (obj.getClass().isArray()) {
                    String fieldValue;

                    object = new Element("Object");
                    object.setAttribute(new Attribute("ID", id));
                    object.setAttribute(new Attribute("Class", obj.getClass().getName()));
                    object.setAttribute(new Attribute("Length", String.valueOf(Array.getLength(obj))));

                    for (int i = 0; i < Array.getLength(obj); i++) {
                        if (!Array.get(obj, i).getClass().getName().contains("java.lang")) {
                            System.out.println("Found");

                            id = Integer.toString(hashMap.size());
                            field = new Element("Field");
                            hashMap.put(Array.get(obj, i), id);
                            objToInspect.addElement(Array.get(obj, i));

                            reference = new Element("Reference");
                            reference.addContent(id);

                            referenceArrayElement.add(reference);
                            tempArray = referenceArrayElement;
                        } else {
                            fieldValue = String.valueOf(Array.get(obj, i));
                            value = new Element("Value");
                            value.addContent(fieldValue);
                            valueArrayElement.add(value);
                            tempArray = valueArrayElement;
                        }

                    }

                    object.setContent(tempArray);
                }
                for (Field field1 : obj.getClass().getDeclaredFields()) {
                    field1.setAccessible(true);
                    String fieldName = field1.getName();
                    String fieldValue = field1.get(obj).toString();
                    String fieldDeclairedClass = field1.getDeclaringClass().getName();


                    if (!(field1.getType().isPrimitive() || field1.get(obj).getClass().getName().contains("java.lane"))) {
                        field = new Element("Field");
                        id = Integer.toString(hashMap.size());
                        hashMap.put(field1.get(obj), id);
                        objToInspect.addElement(field1);

                        reference = new Element("Reference");
                        reference.addContent(reference);

                        field.setContent(reference);
                        fieldArrayELement.add(field);

                    } else {
                        field = new Element("Field");
                        field.setAttribute(new Attribute("Name", fieldName));

                        if (obj.getClass().isArray()) {
                            field.setAttribute((new Attribute("Length", String.valueOf(Array.getLength(field1.get(obj))))));
                            for (int i = 0; i < Array.getLength(field1.get(obj); i++){
                                fieldValue = String.valueOf(Array.get(field1.get(obj), i));
                                value = new Element("Value");
                                value.addContent(fieldValue);
                                valueArrayElement.add(value);
                            }

                            field.setContent(valueArrayElement);
                        } else {
                            value = new Element("Value");
                            value.addContent(fieldValue);

                            field.setContent(value);
                            fieldArrayELement.add(field);
                        }
                    }
                    object.setContent(fieldArrayELement);
                }

                document.getRootElement().addContent(fieldArrayELement);


                XMLOutputter xmlOutputter = new XMLOutputter();

                xmlOutputter.setFormat(Format.getPrettyFormat());
                xmlOutputter.output(document, new FileWriter("xmlOutputFile.xml"));

                System.out.println("File has been saved.");

            }catch (IOException io){
                System.out.println(io.getMessage());
            }

        return document;
    }


    public static void inspectFieldClasses(Document document, Object obj, Vector objToInspect, IdentityHashMap hashMap){
        if(objToInspect.size()>0){
            Enumeration e = objToInspect.elements();
            while(e.hasMoreElements()){
                Object testE = e.nextElement();
                System.out.println("TEST" + testE.getClass().getSimpleName());
                Field field = (Field) testE;


                try{
                    serializeHelp(document, field.get(obj), objToInspect, hashMap);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }
}














































