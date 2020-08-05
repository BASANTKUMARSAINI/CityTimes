package interfaces;

import java.util.HashMap;
import java.util.List;

public interface UpdateInterface {
    public void update(String place, List<String> value);
    public void update(String place,List<String>value1,List<String>value2);
    public void update(String place,HashMap<String,Boolean>value);
    public void update(String place,boolean value);
    public void update(String place, List<String> value,Double logitude,Double latitude);

}
