import java.io.Serializable;

/**
 * Created by mtretiak on 2017-11-14.
 */
public class ReferenceObject implements Serializable {


    private SimpleObject refObj;

    public ReferenceObject(SimpleObject simpleObject){
        refObj = simpleObject;
    }

    public ReferenceObject(){

    }

}
