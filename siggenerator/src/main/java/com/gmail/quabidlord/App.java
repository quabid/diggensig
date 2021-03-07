package com.gmail.quabidlord;

import java.io.PrintStream;

import com.gmail.quabidlord.pathmanager.MyConstants;
import com.gmail.quabidlord.pathmanager.PathValidator;

/**
 * @implNote Generate Signature
 * @implSpec java -jar digsiggen.jar ~/bin/tohex $HOME/sig $HOME/pub $HOME/pri
 *           SUN DSA SHA1withDSA 1024
 * 
 * @implSpec java -jar digsiggen.jar ~/bin/tohex $HOME/sig $HOME/pub $HOME/pri
 *           SUN DSA SHA256withDSA 2048
 *
 * @implNote Verify Signature
 * @implSpec java -jar digsiggen.jar /home/quabid/pub /home/quabid/sig
 *           /home/quabid/bin/tohex DSA SUN SHA1withDSA
 * 
 * @implSpec java -jar digsiggen.jar /home/quabid/pub /home/quabid/sig
 *           /home/quabid/bin/tohex DSA SUN SHA256withDSA
 */
public class App {
    final static PrintStream printer = new PrintStream(System.out);
    static String[] providers;
    final static MyConstants constants = new MyConstants();
    final static PathValidator pathValidator = new PathValidator();
    static String pathToSaveTheSignatureFile = constants.USRHOME + "sig";
    static String pathToSaveThePublicKeyFile = constants.USRHOME + "pubKey";
    static String pathToSaveThePrivateKeyFile = constants.USRHOME + "privKey";
    static String dataFile = constants.USRHOME + "tooct";
    static String signatureAlgorithm = "DSA";
    static String provider = "SUN";
    static String typeOfAlgorithm = "SHA1withDSA";
    static int keySize = 1024;

    /**
     * 
     * @param pathToFileDataToGenerateSignatureFrom
     * @param pathToSaveTheSignatureFile
     * @param pathToSaveThePublicKeyFile
     * @param pathToSaveThePrivateKeyFile
     * @param provider
     * @param signatureAlgorithm
     * @param typeOfAlgorithm
     * @param keySize
     */
    final static Generator generator = new Generator();

    /**
     * 
     * @param pathToThePublicKey
     * @param pathToTheSignatureFile
     * @param pathToTheDataFile
     * @param signatureAlgorithm
     * @param provider
     * @param typeOfAlgorithm
     */
    final static Verifier verifier = new Verifier();

    public static void main(String[] args) {
        if (args.length == 8) {
            dataFile = args[0];
            pathToSaveTheSignatureFile = args[1];
            pathToSaveThePublicKeyFile = args[2];
            pathToSaveThePrivateKeyFile = args[3];
            provider = args[4].toUpperCase();
            signatureAlgorithm = args[5];
            typeOfAlgorithm = args[6];
            keySize = Integer.parseInt(args[7]);

            generator.generate(dataFile, pathToSaveTheSignatureFile, pathToSaveThePublicKeyFile,
                    pathToSaveThePrivateKeyFile, provider, signatureAlgorithm, typeOfAlgorithm, keySize);

            verifier.verify(pathToSaveThePublicKeyFile, pathToSaveTheSignatureFile, dataFile, signatureAlgorithm,
                    provider, typeOfAlgorithm);

        } else if (args.length == 6) {
            pathToSaveThePublicKeyFile = args[0];
            pathToSaveTheSignatureFile = args[1];
            dataFile = args[2];
            signatureAlgorithm = args[3];
            provider = args[4].toUpperCase();
            typeOfAlgorithm = args[5];
            verifier.verify(pathToSaveThePublicKeyFile, pathToSaveTheSignatureFile, dataFile, signatureAlgorithm,
                    provider, typeOfAlgorithm);
        } else {
            usage();
        }
    }

    final static void print(Object obj) {
        printer.println(String.valueOf(obj));
    }

    final static void usage() {
        print(String.format("\n\tGenerate Signature:\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s", "dataFile",
                "pathToSaveTheSignatureFile", "pathToSaveThePublicKeyFile", "pathToSaveThePrivateKeyFile", "provider",
                "signatureAlgorithm", "typeOfAlgorithm", "keySize"));
        print("\n\tExample Usage: java -jar digsiggen.jar ~/bin/tohex $HOME/sig $HOME/pub $HOME/pri SUN DSA SHA1withDSA 1024\n");
        print("\n\tExample Usage: java -jar digsiggen.jar ~/bin/tohex $HOME/sig $HOME/pub $HOME/pri SUN DSA SHA256withDSA 2048\n");

        print(String.format("\n\tVerify Signature:\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s", "pathToSaveThePublicKeyFile",
                "pathToSaveTheSignatureFile", "dataFile", "signatureAlgorithm", "provider", "typeOfAlgorithm"));
        print("\n\tExample Usage: java -jar digsiggen.jar /home/quabid/publicKey /home/quabid/signature /home/quabid/bin/tohex DSA SUN SHA1withDSA\n");
        print("\n\tExample Usage: java -jar digsiggen.jar /home/quabid/publicKey /home/quabid/signature /home/quabid/bin/tohex DSA SUN SHA256withDSA\n");
    }

    final static void status() {
        print("\n\tProgram executed successfully\n");
    }
}
