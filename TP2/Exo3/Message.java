import java.io.Serializable;

public static int CODE_ASK_TASK = 0;
public static int CODE_RECEIVE_TASK = 1;

public class Message implements Serializable {
    private int code;
    private Object object;

    public int getCode(){
        return this.code;
    }

    public Object getObject(){
        return object;
    }
}
