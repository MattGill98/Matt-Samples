package uk.me.mattgill.samples.client.auth.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeystoreManager {

    private static final Logger LOGGER = Logger.getLogger(KeystoreManager.class.getName());

    private final CertificateFactory cf;

    private final KeyStore store;
    private final File file;
    private final String password;

    /**
     * Creates a new KeystoreManager.
     * 
     * @param file     the keystore file.
     * @param password thepassword to access the file.
     * 
     * @throws NoSuchAlgorithmException if the algorithm used to check the integrity
     *                                  of the keystore cannot be found.
     * @throws CertificateException     if any of the certificates in the keystore
     *                                  could not be loaded.
     * @throws IOException              if a password is required but not given, or
     *                                  if the given password was incorrect. If the
     *                                  error is due to a wrong password, the cause
     *                                  of the IOException should be an
     *                                  UnrecoverableKeyException.
     * @throws KeyStoreException        if a default keystore is unable to be
     *                                  created.
     * @throws IllegalArgumentException if the provided keystore or password were
     *                                  null or incorrect.
     */
    public KeystoreManager(File file, String password)
            throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {

        if (file == null) {
            throw new IllegalArgumentException("The keystore file cannot be null.");
        }
        if (password == null) {
            throw new IllegalArgumentException("The password cannot be null.");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException(String.format("A file wasn't found at %s.", file.getAbsolutePath()));
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException(
                    String.format("The provided file was invalid: %s.", file.getAbsolutePath()));
        }
        this.cf = CertificateFactory.getInstance("X.509");
        this.file = file;
        this.password = password;
        this.store = KeyStore.getInstance(KeyStore.getDefaultType());

        // Load store from file
        FileInputStream in = new FileInputStream(file);
        LOGGER.log(Level.INFO, "Loading keystore from file: {0}.", file.getAbsolutePath());
        store.load(in, password.toCharArray());
        in.close();
        LOGGER.log(Level.INFO, "Keystore loaded successfully.", file.getAbsolutePath());
    }

    /**
     * Adds a certificate to the store.
     * 
     * @throws KeyStoreException    if the keystore has not been initialized
     *                              (loaded).
     * @throws IOException          if there was a problem reading from the file.
     * @throws CertificateException if the certificate could not be read.
     */
    public void add(String certificateName, File file) throws KeyStoreException, CertificateException, IOException {
        try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
            add(certificateName, fis);
        }
    }

    /**
     * Adds a certificate to the store.
     * 
     * @throws KeyStoreException    if the keystore has not been initialized
     *                              (loaded).
     * @throws IOException          if there was a problem reading from the file.
     * @throws CertificateException if the certificate could not be read.
     */
    public void add(String certificateName, InputStream inputStream) throws KeyStoreException, CertificateException, IOException {
        Certificate cert = load(inputStream);
        store.setCertificateEntry(certificateName, cert);
    }

    /**
     * Load a certificate from a given file.
     * 
     * @throws IOException          if there was a problem reading from the file.
     * @throws CertificateException if the certificate could not be read.
     */
    private Certificate load(InputStream inputStream) throws IOException, CertificateException {
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            byte[] bytes = new byte[dis.available()];
            dis.readFully(bytes);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            return cf.generateCertificate(bais);
        }
    }

    /**
     * Writes the keystore back to the given file.
     * 
     * @throws KeyStoreException        if the keystore has not been initialized
     *                                  (loaded).
     * @throws IOException              if there was an I/O problem with data.
     * @throws NoSuchAlgorithmException if the appropriate data integrity algorithm
     *                                  could not be found.
     * @throws CertificateException     if any of the certificates included in the
     *                                  keystore data could not be stored.
     */
    public void save() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        LOGGER.log(Level.INFO, "Writing keystore to file: {0}.", file.getAbsolutePath());
        FileOutputStream out = new FileOutputStream(file);
        store.store(out, password.toCharArray());
        out.flush();
        out.close();
        LOGGER.log(Level.INFO, "Keystore written successfully.", file.getAbsolutePath());
    }

}