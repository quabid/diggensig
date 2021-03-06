package com.gmail.quabidlord;

import java.io.PrintStream;

import com.gmail.quabidlord.pathmanager.MyConstants;

/**
 * Hello world!
 *
 */
public class App {
    final static PrintStream printer = new PrintStream(System.out);
    static String[] providers;
    final static Generator generator = new Generator();
    final static MyConstants constants = new MyConstants();

    public static void main(String[] args) {
        // Generator generator = new Generator();
        Verifier verifier = new Verifier();

        // generator.generate("/home/sjhadmin/bin/tooct", "sun");
        String signatureFile = constants.USRHOME + "sig";
        String publicKeyFile = constants.USRHOME + "pubKey";
        String dataFile = "/home/sjhadmin/bin/tooct";

        verifier.verify(publicKeyFile, signatureFile, dataFile);

    }

    final static void print(Object obj) {
        printer.println(String.valueOf(obj));
    }
}
