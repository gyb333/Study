package Study.SpringCloud.GYB.result;

import java.io.Serializable;

public interface CodeMessage extends Serializable {

    int getCode();

    String getMessage();



}
