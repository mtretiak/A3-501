import java.io.Serializable;

/**
 * Created by mtretiak on 2017-11-14.
 */
public class SimpleObj implements Serializable {


    private int simpleInt;
    private double simpleDouble;

    public SimpleObj(){

    }


    public SimpleObj(int pInt, double pDouble){
        simpleDouble = pDouble;
        simpleInt = pInt;
    }


}
