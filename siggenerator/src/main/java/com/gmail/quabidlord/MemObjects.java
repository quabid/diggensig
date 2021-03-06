package com.gmail.quabidlord;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

public final class MemObjects {
    public static KeyPairGenerator keyPairGenerator;
    public static KeyPair keyPair;
    public static PrivateKey privateKey;
    public static PublicKey publicKey;
    public static SecureRandom random;
    public static Signature signature;

    private MemObjects() {
        super();
    }

    public final static void setMembers(KeyPairGenerator kpg, KeyPair kp, PrivateKey pk, PublicKey puk, Signature sig) {
        keyPairGenerator = kpg;
        keyPair = kp;
        privateKey = pk;
        publicKey = puk;
        signature = sig;
    }

    final static MemObjects getInstance() {
        return new MemObjects();
    }
}
