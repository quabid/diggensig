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
    final static String pathToSaveTheSignatureFile = constants.USRHOME + "sig";
    final static String pathToSaveThePublicKeyFile = constants.USRHOME + "pubKey";
    final static String pathToSaveThePrivateKeyFile = constants.USRHOME + "privKey";
    final static String dataFile = "/home/quabid/bin/tooct";
    final static String signatureAlgorithm = "DSA";
    final static String provider = "SUN";
    final static String dsaAlgorithm = "SHA1withDSA";
    final static int keySize = 1024;

    public static void main(String[] args) {
        /**
         * 
         * @param pathToFileDataToGenerateSignatureFrom
         * @param pathToSaveTheSignatureFile
         * @param pathToSaveThePublicKeyFile
         * @param pathToSaveThePrivateKeyFile
         * @param provider
         * @param signatureAlgorithm
         * @param dsaAlgorithm
         * @param keySize
         */
        Generator generator = new Generator();

        /**
         * @param pathToThePublicKey
         * @param pathToTheSignatureFile
         * @param pathToTheDataFile
         */
        Verifier verifier = new Verifier();

        // generator.generate("/home/sjhadmin/bin/tooct", "sun");
        generator.generate(dataFile, pathToSaveTheSignatureFile, pathToSaveThePublicKeyFile,
                pathToSaveThePrivateKeyFile, provider, signatureAlgorithm, dsaAlgorithm, keySize);

        verifier.verify(pathToSaveThePublicKeyFile, pathToSaveTheSignatureFile, dataFile);

    }

    final static void print(Object obj) {
        printer.println(String.valueOf(obj));
    }
}
