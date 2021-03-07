package com.gmail.quabidlord.core.abstracts;

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

import com.gmail.quabidlord.pathmanager.PathValidator;

public abstract class GeneratorParent {
    private final ArrayList<String> providers = new ArrayList<String>();
    private boolean success = false;

    private final PrintStream printer = new PrintStream(System.out);
    private final PathValidator pathValidator = new PathValidator();

    public GeneratorParent() {
        if (Security.getProviders().length > 0) {
            for (Provider provider : Security.getProviders()) {
                String[] splitProvider = String.valueOf(provider).split("\t| ", 0);
                providers.add(splitProvider[0]);
            }
        }
    }

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
    public final void generate(String pathToFileDataToGenerateSignatureFrom, String pathToSaveTheSignatureFile,
            String pathToSaveThePublicKeyFile, String pathToSaveThePrivateKeyFile, String provider,
            String signatureAlgorithm, String typeOfAlgorithm, int keySize) {
        KeyPairGenerator keyGenerator = null;
        SecureRandom random = null;
        Signature signature = null;
        KeyPair keyPair = null;
        PrivateKey privateKey = null;
        PublicKey publicKey = null;

        if (null != pathToFileDataToGenerateSignatureFrom && !pathToFileDataToGenerateSignatureFrom.isBlank()
                && !pathToFileDataToGenerateSignatureFrom.isEmpty()) {
            if (null != provider && !provider.isEmpty() && !provider.isBlank()) {
                if (!pathValidator.pathExists(pathToFileDataToGenerateSignatureFrom)) {
                    print("\n\t\tPath " + pathToFileDataToGenerateSignatureFrom + " does not exist!\n");
                    return;
                } else {
                    try {
                        // Create a Key Pair Generator
                        // The first step is to get a key-pair generator object for generating keys for
                        // the signature algorithm e.g. DSA, RSA, DES
                        keyGenerator = KeyPairGenerator.getInstance(signatureAlgorithm, provider.toUpperCase());

                        // The source of randomness must be an instance of the SecureRandom class that
                        // provides a cryptographically strong random number generator (RNG).
                        random = SecureRandom.getInstance("SHA1PRNG", provider.toUpperCase());

                        // Initialize the Key Pair Generator
                        // The next step is to initialize the key pair generator.
                        // The keysize for a DSA key generator is the key length (in bits), which you
                        // will set to 1024.
                        keyGenerator.initialize(keySize, random);

                        // Generate the Pair of Keys
                        // The final step is to generate the key pair and to store the keys in
                        // PrivateKey and PublicKey objects.
                        keyPair = keyGenerator.genKeyPair();

                        privateKey = keyPair.getPrivate();
                        publicKey = keyPair.getPublic();

                        // Sign the Data
                        // Now that you have created a public key and a private key, you are ready to
                        // sign the data.

                        // Get a Signature Object: The following gets a Signature object for generating
                        // or verifying signatures using a valid algorithm e.g. SHA256withDSA, SHA1withDSA
                        signature = Signature.getInstance(typeOfAlgorithm, provider.toUpperCase());

                        // Initialize the Signature Object
                        // Before a Signature object can be used for signing or verifying, it must be
                        // initialized. The initialization method for signing requires a private key.
                        signature.initSign(privateKey);

                        // Supply the Signature Object the Data to Be Signed
                        FileInputStream fis = new FileInputStream(pathToFileDataToGenerateSignatureFrom);
                        BufferedInputStream bufin = new BufferedInputStream(fis);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bufin.read(buffer)) >= 0) {
                            signature.update(buffer, 0, len);
                        }
                        ;
                        bufin.close();

                        // Generate the Signature
                        // Once all of the data has been supplied to the Signature object, you can
                        // generate the digital signature of that data.
                        byte[] realSig = signature.sign();

                        /* Save the signature in a file */
                        FileOutputStream sigfos = new FileOutputStream(pathToSaveTheSignatureFile);
                        sigfos.write(realSig);
                        sigfos.close();

                        /* Save the public key in a file */
                        byte[] pubKey = publicKey.getEncoded();
                        FileOutputStream fosPubKey = new FileOutputStream(pathToSaveThePublicKeyFile);
                        fosPubKey.write(pubKey);
                        fosPubKey.close();

                        /* Save the private key in a file */
                        byte[] priKey = privateKey.getEncoded();
                        FileOutputStream fosPriKey = new FileOutputStream(pathToSaveThePrivateKeyFile);
                        fosPriKey.write(priKey);
                        fosPriKey.close();
                        success = true;
                    } catch (NoSuchProviderException nspe) {
                        success = false;
                        nspe.printStackTrace();
                        listProviders();
                        return;
                    } catch (NoSuchAlgorithmException nsae) {
                        success = false;
                        nsae.printStackTrace();
                        return;
                    } catch (InvalidKeyException ike) {
                        success = false;
                        ike.printStackTrace();
                        return;
                    } catch (FileNotFoundException fnfe) {
                        success = false;
                        fnfe.printStackTrace();
                        return;
                    } catch (IOException ioe) {
                        success = false;
                        ioe.printStackTrace();
                        return;
                    } catch (SignatureException se) {
                        success = false;
                        se.printStackTrace();
                        return;
                    } finally {
                        status();
                    }
                }
            } else {
                print("\n\t\tMust enter a valid provider name\n");
                return;
            }
        } else {
            print("\n\t\tMust enter a valid file path\n");
            return;
        }

    }

    public final void listProviders() {
        if (providers.size() > 0) {
            print("\n\tList of Security Providers on the system\n");
            Iterator<String> itr = providers.iterator();
            while (itr.hasNext()) {
                print(itr.next());
            }
        }
    }

    private final void print(Object obj) {
        printer.println(String.valueOf(obj));
    }

    private final void status() {
        String msg = "\n\tSignature, Private and Public Keys successfully written to files!!\n";

        String status = success == true ? msg : "Program failed, Big Time!!";
        print(status);
        return;
    }

    public String toString() {
        return "Key Pair Generator";
    }
}
