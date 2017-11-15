import java.io.Serializable;

/**
 * Created by mtretiak on 2017-11-14.
 */
public class SimpleObj implements Serializable {


    private int simpleInt;
    private double simpleDouble;
    float simpleFloat;
    byte simpleByte;

    public SimpleObj(){
    //for reconstruction
    }


    public SimpleObj(int pInt, double pDouble, float pFloat, byte pByte){
        simpleDouble = pDouble;
        simpleInt = pInt;
        simpleFloat = pFloat;
        simpleByte = pByte;
    }


}
