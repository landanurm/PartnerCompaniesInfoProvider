package com.landanurm.partner_companies_info_provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Leonid on 09.12.13.
 */
public class SerializableConvertor {

    public static byte[] serializableToBytes(Serializable serializable) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(serializable);
            bytes = bos.toByteArray();
        } catch (Exception e) { }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) { }
            try {
                bos.close();
            } catch (IOException ex) { }
        }
        return bytes;
    }

    public static Object serializableFromBytes(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object obj = null;
        try {
            in = new ObjectInputStream(bis);
            obj = in.readObject();
        } catch (Exception e) {}
        finally {
            try {
                bis.close();
            } catch (IOException ex) { }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) { }
        }
        return obj;
    }
}
