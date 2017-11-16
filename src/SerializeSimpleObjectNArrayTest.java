import org.jdom2.output.XMLOutputter;
import java.util.List;
import java.util.Scanner;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.jdom2.*;

/**
 * Created by mtretiak on 2017-11-15.
 */
public class SerializeSimpleObjectNArrayTest {

    //Tests simple object serialization
    @Test
    public void serialSimpleTest(){

        byte b = 1;
        Object obj = new SimpleObject(1, 1.0, 1f, b);
        Document document = Serializer.serialize(obj);
        assertNotNull(document);

        assertNotNull(document.getRootElement());
        Element rootElement = document.getRootElement();
        assertTrue(rootElement.getName().equals("serialized"));
        assertNotNull(rootElement.getChildren());

        List objList = rootElement.getChildren();
        assertNotNull(objList);
        assertNotNull(objList.get(0));

        Element objElement = (Element) objList.get(0);
        assertTrue(objElement.getName().equals("object"));
        assertNotNull(objElement.getAttribute("class"));
        assertNotNull(objElement.getAttribute("id"));
        assertNull(objElement.getAttribute("length"));
        assertEquals("0", objElement.getAttributeValue("id"));
        assertNotNull(objElement.getChildren());


        List objContents = objElement.getChildren();
        for (int i =0; i < objContents.size(); i++){
            Element contentElement = (Element) objContents.get(i);
            assertEquals(null, contentElement.getAttributeValue("declaringclass"));
            assertNotNull(contentElement.getValue());

            if(contentElement.getName().equals("simpleInt"))
                assertEquals("1", contentElement.getValue());
            if(contentElement.getName().equals(("simpleDouble")))
                assertEquals("1.0", contentElement.getValue());
            if(contentElement.getName().equals("simpleFloat"))
                assertEquals("1", contentElement.getValue());
            if(contentElement.getName().equals(("simpleByte")))
                assertEquals("1", contentElement.getValue());
        }
    }




}
