package com.gmail.quabidlord;

import java.io.PrintStream;

import com.gmail.quabidlord.pathmanager.MyConstants;
import com.gmail.quabidlord.pathmanager.PathValidator;

/**
 * java -jar digsiggen.jar ~/bin/tohex "$HOME/sig" "$HOME/pub" "$HOME/pri" "SUN"
 * "DSA" "SHA1withDSA" "1024"
 *
 */
public class App {
    final static PrintStream printer = new PrintStream(System.out);
    static String[] providers;
    final static MyConstants constants = new MyConstants();
    final static PathValidator pathValidator = new PathValidator();
    static String pathToSaveTheSignatureFile = constants.USRHOME + "sig";
    static String pathToSaveThePublicKeyFile = constants.USRHOME + "pubKey";
    static String pathToSaveThePrivateKeyFile = constants.USRHOME + "privKey";
    static String dataFile = "/home/quabid/bin/tooct";
    static String signatureAlgorithm = "DSA";
    static String provider = "SUN";
    static String dsaAlgorithm = "SHA1withDSA";
    static int keySize = 1024;

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
     * 
     * @param pathToThePublicKey
     * @param pathToTheSignatureFile
     * @param pathToTheDataFile
     * @param signatureAlgorithm
     * @param provider
     * @param dsaAlgorithm
     */
    final static Verifier verifier = new Verifier();

    public static void main(String[] args) {

        if (args.length != 8) {
            print(String.format(
                    "\n\tThis program is expecting the following arguments:\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s\n\t%s",
                    "dataFile", "pathToSaveTheSignatureFile", "pathToSaveThePublicKeyFile",
                    "pathToSaveThePrivateKeyFile", "provider", "signatureAlgorithm", "dsaAlgorithm", "keySize"));
            usage();
        } else {
            dataFile = args[0];
            pathToSaveTheSignatureFile = args[1];
            pathToSaveThePublicKeyFile = args[2];
            pathToSaveThePrivateKeyFile = args[3];
            provider = args[4];
            signatureAlgorithm = args[5];
            dsaAlgorithm = args[6];
            keySize = Integer.parseInt(args[7]);

            generator.generate(dataFile, pathToSaveTheSignatureFile, pathToSaveThePublicKeyFile,
                    pathToSaveThePrivateKeyFile, provider, signatureAlgorithm, dsaAlgorithm, keySize);

            verifier.verify(pathToSaveThePublicKeyFile, pathToSaveTheSignatureFile, dataFile, signatureAlgorithm,
                    provider, dsaAlgorithm);

            if (!pathValidator.pathExists(args[1]) || !pathValidator.pathExists(args[2])
                    || !pathValidator.pathExists(args[3])) {
                usage();
            }
        }

    }

    final static void print(Object obj) {
        printer.println(String.valueOf(obj));
    }

    final static void usage() {
        print("\n\tExample Usage: java -jar digsiggen.jar ~/bin/tohex $HOME/sig $HOME/pub $HOME/pri SUN DSA SHA1withDSA 1024\n");
    }
}
