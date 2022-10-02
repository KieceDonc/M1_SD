import java.io.Serializable;

public interface Task extends Serializable {
    public void execute();

    public int getNumber();

    public boolean isPrimeNumber();
}
