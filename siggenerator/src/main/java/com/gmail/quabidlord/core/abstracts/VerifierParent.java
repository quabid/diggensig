package com.gmail.quabidlord.core.abstracts;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import com.gmail.quabidlord.pathmanager.core.PathValidator;
import com.gmail.quabidlord.pathmanager.core.constants.MyConstants;

public abstract class VerifierParent {
    private final PrintStream printer = new PrintStream(System.out);
    private final PathValidator pathValidator = new PathValidator();
    private final MyConstants constants = new MyConstants();

    public VerifierParent() {
        super();
    }

    /**
     * @param pathToThePublicKey
     * @param pathToTheSignatureFile
     * @param pathToTheDataFile
     */
    public final void verify(String pathToThePublicKey, String pathToTheSignatureFile, String pathToTheDataFile) {
        if (!pathValidator.pathExists(pathToThePublicKey)) {
            print("\n\t\tPublic Key " + pathToThePublicKey + " does not exist!\n");
            return;
        } else if (!pathValidator.pathExists(pathToTheSignatureFile)) {
            print("\n\t\tSignature File " + pathToTheSignatureFile + " does not exist!\n");
            return;
        } else if (!pathValidator.pathExists(pathToTheDataFile)) {
            print("\n\t\tFile path" + pathToTheDataFile + " does not exist!\n");
            return;
        }

        try {
            // First, read in the encoded public key bytes.
            FileInputStream keyfis = new FileInputStream(pathToThePublicKey);
            byte[] encKey = new byte[keyfis.available()];
            keyfis.read(encKey);

            keyfis.close();

            // So, first you need a key specification.
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);

            // Now you need a KeyFactory object to do the conversion.
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");

            // Finally, you can use the KeyFactory object to generate a PublicKey from the
            // key specification.
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

            // Next, input the signature bytes from the file specified as the second command
            // line argument.

            FileInputStream sigfis = new FileInputStream(pathToTheSignatureFile);
            byte[] sigToVerify = new byte[sigfis.available()];
            sigfis.read(sigToVerify);
            sigfis.close();

            // You can now proceed to do the verification.
            // Initialize the Signature Object for Verification
            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");

            // Next, you need to initialize the Signature object. The initialization method
            // for verification requires the public key.
            signature.initVerify(pubKey);

            // Supply the Signature Object With the Data to be Verified You now need to
            // supply the Signature object with the data for which a signature was
            // generated.
            FileInputStream datafis = new FileInputStream(pathToTheDataFile);
            BufferedInputStream bufin = new BufferedInputStream(datafis);

            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                signature.update(buffer, 0, len);
            }
            ;

            bufin.close();

            // Verify the Signature
            boolean verifies = signature.verify(sigToVerify);
            print("\n\tsignature verifies: " + verifies + "!\n");
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return;
        } catch (NoSuchProviderException nspe) {
            nspe.printStackTrace();
            return;
        } catch (InvalidKeySpecException ikse) {
            ikse.printStackTrace();
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
            return;
        } catch (SignatureException se) {
            se.printStackTrace();
            return;
        }
    }

    private final void print(Object obj) {
        printer.println(String.valueOf(obj));
    }
}
