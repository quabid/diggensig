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

import com.gmail.quabidlord.pathmanager.MyConstants;
import com.gmail.quabidlord.pathmanager.PathValidator;

public class Generator {
    private final ArrayList<String> providers = new ArrayList<String>();
    private boolean success = false;

    private final PrintStream printer = new PrintStream(System.out);
    private final PathValidator pathValidator = new PathValidator();
    private final MyConstants constants = new MyConstants();

    public Generator() {
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
     * @param keyGeneratingAlgorithm
     * @param provider
     * @param destinationPathForSignature
     * @param destinationPathForPublicKey
     * @param destinationPathForPrivateKey
     * @throws FileNotFoundException
     * @throws IOException
     */
    public final void generate(String pathToFileDataToGenerateSignatureFrom, String provider) {
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
                        keyGenerator = KeyPairGenerator.getInstance("DSA", provider.toUpperCase());
                        random = SecureRandom.getInstance("SHA1PRNG", provider.toUpperCase());
                        keyGenerator.initialize(1024, random);
                        keyPair = keyGenerator.genKeyPair();
                        privateKey = keyPair.getPrivate();
                        publicKey = keyPair.getPublic();

                        signature = Signature.getInstance("SHA1withDSA", provider.toUpperCase());
                        signature.initSign(privateKey);

                        FileInputStream fis = new FileInputStream(pathToFileDataToGenerateSignatureFrom);
                        BufferedInputStream bufin = new BufferedInputStream(fis);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bufin.read(buffer)) >= 0) {
                            signature.update(buffer, 0, len);
                        }
                        ;
                        bufin.close();

                        byte[] realSig = signature.sign();

                        /* save the signature in a file */
                        FileOutputStream sigfos = new FileOutputStream(constants.USRHOME + "sig");
                        sigfos.write(realSig);
                        sigfos.close();

                        /* save the public key in a file */
                        byte[] key = publicKey.getEncoded();
                        FileOutputStream keyfos = new FileOutputStream(constants.USRHOME + "pubKey");
                        keyfos.write(key);
                        keyfos.close();

                        print("\n\tSignature and Public Key successful!!\n");
                    } catch (NoSuchProviderException nspe) {
                        nspe.printStackTrace();
                        return;
                    } catch (NoSuchAlgorithmException nsae) {
                        nsae.printStackTrace();
                        return;
                    } catch (InvalidKeyException ike) {
                        ike.printStackTrace();
                        return;
                    } catch (FileNotFoundException fnfe) {
                        fnfe.printStackTrace();
                        return;
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        return;
                    } catch (SignatureException se) {
                        se.printStackTrace();
                        return;
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
        String status = success ? "\n\tPrivate, Public and Signature files successful!" : "Program failed, Big Time!!";
        print(status);
        return;
    }

    public String toString() {
        return "Key Pair Generator";
    }
}
