import java.io.Serializable;

/**
 * Created by mtretiak on 2017-11-14.
 */
public class SimpleObject implements Serializable {


    private int simpleInt;
    private double simpleDouble;
    float simpleFloat;
    byte simpleByte;

    public SimpleObject(){
    //for reconstruction
    }


    public SimpleObject(int pInt, double pDouble, float pFloat, byte pByte){
        simpleDouble = pDouble;
        simpleInt = pInt;
        simpleFloat = pFloat;
        simpleByte = pByte;
    }


}
