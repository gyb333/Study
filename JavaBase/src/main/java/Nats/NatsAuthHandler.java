package Nats;

import io.nats.client.AuthHandler;
import io.nats.client.NKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class NatsAuthHandler  implements AuthHandler {
    private NKey nkey;

    public NatsAuthHandler(String path) throws Exception {
        BufferedReader in = null;

        try {
            in = new BufferedReader((new FileReader(new File(path))));

            char[] buffer = new char[2048];
            int len = in.read(buffer);
            char[] seed = new char[len];

            System.arraycopy(buffer, 0, seed, 0, len);

            this.nkey = NKey.fromSeed(seed);

            for (int i=0;i<buffer.length;i++) {
                buffer[i] = '\0';
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public NKey getNKey() {
        return this.nkey;
    }

    public char[] getID() {
        try {
            return this.nkey.getPublicKey();
        } catch (GeneralSecurityException |IOException|NullPointerException ex) {
            return null;
        }
    }

    public byte[] sign(byte[] nonce) {
        try {
            return this.nkey.sign(nonce);
        } catch (GeneralSecurityException| IOException |NullPointerException ex) {
            return null;
        }
    }

    public char[] getJWT() {
        return null;
    }
}
