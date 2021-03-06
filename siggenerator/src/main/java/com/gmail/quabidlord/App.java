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
    final static MyConstants constants = new MyConstants();
    final static String pathToSaveTheSignatureFile = constants.USRHOME + "sig";
    final static String pathToSaveThePublicKeyFile = constants.USRHOME + "pubKey";
    final static String pathToSaveThePrivateKeyFile = constants.USRHOME + "privKey";
    final static String dataFile = "/home/quabid/bin/tooct";
    final static String signatureAlgorithm = "DSA";
    final static String provider = "SUN";
    final static String dsaAlgorithm = "SHA1withDSA";
    final static int keySize = 1024;

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
    final static Generator generator = new Generator();

    /**
     * @param pathToThePublicKey
     * @param pathToTheSignatureFile
     * @param pathToTheDataFile
     */
    final static Verifier verifier = new Verifier();

    public static void main(String[] args) {

        if (args.length != 8) {
            print(String.format(
                    "\n\tThis program is expecting the following arguments:\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s",
                    "dataFile", "pathToSaveTheSignatureFile", "pathToSaveThePublicKeyFile",
                    "pathToSaveThePrivateKeyFile", "provider", "signatureAlgorithm", "dsaAlgorithm", "keySize"));
        } else {
            generator.generate(dataFile, pathToSaveTheSignatureFile, pathToSaveThePublicKeyFile,
                    pathToSaveThePrivateKeyFile, provider, signatureAlgorithm, dsaAlgorithm, keySize);

            verifier.verify(pathToSaveThePublicKeyFile, pathToSaveTheSignatureFile, dataFile);
        }

    }

    final static void print(Object obj) {
        printer.println(String.valueOf(obj));
    }
}
