package com.vvdev;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface Task extends Serializable {
    public void execute();

    public int getNumber();

    public boolean isPrimeNumber();

    public static byte[] serializable(Task task) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(task);
            out.flush();
            byte[] taskBytes = bos.toByteArray();
            out.close();
            return taskBytes;
        } catch (IOException e) {
            System.out.print("" + e);
            System.exit(0);
        }
        return null;
    }

    public static Task deserialize(byte[] taskBytes) {
        Task task = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(taskBytes);
            ObjectInput in = new ObjectInputStream(bis);
            task = (Task) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.print("" + e);
            System.exit(0);
        }

        if (task == null) {
            System.out.print("Task is null in deserializeTask");
            System.exit(0);
        }

        return task;
    }
}
