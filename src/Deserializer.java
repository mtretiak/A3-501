import java.io.File;
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


    public static void deserialize(Document document){

        SAXBuilder saxBuilder = new SAXBuilder();
        File xmlFile = new File("xmlFile.xml");


        Element rootNode = document.getRootElement();
        List list = rootNode.getChildren("Object");


        for(int i = 0; i<list.size(); i++){
            Element node = (Element) list.get(i);

            for(Attribute attribute : node.getAttributes()){
                System.out.println("--------------------------------------------------");
                System.out.println("Object Attribute " + attribute);
            }

            System.out.println("--------------------------------------------------");

            for(Element field : node.getChildren()){
                for(Attribute attribute : field.getAttributes()){
                    System.out.println("Value ----- " + attribute);
                }
                for(Content content : field.getContent()){
                    System.out.println("Value ----- " + content.getValue());
                }
            }

        }


    }
}
