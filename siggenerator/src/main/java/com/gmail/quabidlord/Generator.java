package com.gmail.quabidlord;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Iterator;

import com.gmail.quabidlord.core.abstracts.GeneratorParent;
import com.gmail.quabidlord.pathmanager.MyConstants;
import com.gmail.quabidlord.pathmanager.PathValidator;

public class Generator extends GeneratorParent {

    public Generator() {
       super();
    }

  
    public String toString() {
        return "Key Pair Generator";
    }
}
