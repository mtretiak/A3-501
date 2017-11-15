import java.io.Serializable;

/**
 * Created by mtretiak on 2017-11-14.
 */
public class RefObj implements Serializable {


    private SimpleObj refObj;

    public RefObj(SimpleObj simpleObj){
        refObj = simpleObj;
    }

    public RefObj(){

    }

}
