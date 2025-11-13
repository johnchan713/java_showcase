package com.example.demo.showcase;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Demonstrates Java Cryptography API
 * Covers hashing, encryption/decryption, digital signatures, key generation, and secure random
 */
public class CryptographyShowcase {

    public static void demonstrate() {
        System.out.println("\n========== CRYPTOGRAPHY SHOWCASE ==========\n");

        hashingDemo();
        symmetricEncryptionDemo();
        asymmetricEncryptionDemo();
        digitalSignatureDemo();
        keyGenerationDemo();
        secureRandomDemo();
        passwordHashingDemo();
        base64Demo();
    }

    // ========== Hashing ==========

    private static void hashingDemo() {
        System.out.println("--- Hashing (Message Digest) ---");
        System.out.println("One-way cryptographic hash functions\n");

        String message = "Hello, World!";
        System.out.println("Original message: " + message);

        try {
            // MD5 (deprecated - shown for completeness)
            System.out.println("\n1. MD5 (✗ Deprecated - weak):");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Hash = md5.digest(message.getBytes(StandardCharsets.UTF_8));
            System.out.println("   Hash: " + bytesToHex(md5Hash));
            System.out.println("   Length: " + md5Hash.length + " bytes (128 bits)");
            System.out.println("   USE: Legacy systems only, NOT secure");

            // SHA-1 (deprecated)
            System.out.println("\n2. SHA-1 (✗ Deprecated - weak):");
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] sha1Hash = sha1.digest(message.getBytes(StandardCharsets.UTF_8));
            System.out.println("   Hash: " + bytesToHex(sha1Hash));
            System.out.println("   Length: " + sha1Hash.length + " bytes (160 bits)");
            System.out.println("   USE: Legacy systems only, NOT secure");

            // SHA-256 (recommended)
            System.out.println("\n3. SHA-256 (✓ Recommended):");
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] sha256Hash = sha256.digest(message.getBytes(StandardCharsets.UTF_8));
            System.out.println("   Hash: " + bytesToHex(sha256Hash));
            System.out.println("   Length: " + sha256Hash.length + " bytes (256 bits)");
            System.out.println("   USE: General purpose hashing, file integrity");

            // SHA-512
            System.out.println("\n4. SHA-512 (✓ More secure):");
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            byte[] sha512Hash = sha512.digest(message.getBytes(StandardCharsets.UTF_8));
            System.out.println("   Hash: " + bytesToHex(sha512Hash));
            System.out.println("   Length: " + sha512Hash.length + " bytes (512 bits)");
            System.out.println("   USE: High security requirements");

            // Verify hash integrity
            System.out.println("\n5. Hash verification:");
            String message2 = "Hello, World!";
            byte[] hash2 = sha256.digest(message2.getBytes(StandardCharsets.UTF_8));
            boolean matches = MessageDigest.isEqual(sha256Hash, hash2);
            System.out.println("   Hashes match: " + matches);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Symmetric Encryption ==========

    private static void symmetricEncryptionDemo() {
        System.out.println("--- Symmetric Encryption (AES) ---");
        System.out.println("Same key for encryption and decryption\n");

        String plaintext = "Secret Message";
        System.out.println("Original: " + plaintext);

        try {
            // Generate AES key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // 256-bit key
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("\n1. AES Key generated: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));

            // AES/GCM encryption (recommended)
            System.out.println("\n2. AES/GCM/NoPadding (✓ Recommended - authenticated encryption):");
            Cipher gcmCipher = Cipher.getInstance("AES/GCM/NoPadding");

            // Generate random IV
            byte[] iv = new byte[12]; // GCM standard IV size
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); // 128-bit auth tag

            // Encrypt
            gcmCipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            byte[] ciphertext = gcmCipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            System.out.println("   Encrypted: " + Base64.getEncoder().encodeToString(ciphertext));
            System.out.println("   IV: " + Base64.getEncoder().encodeToString(iv));

            // Decrypt
            gcmCipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
            byte[] decrypted = gcmCipher.doFinal(ciphertext);
            System.out.println("   Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));

            // AES/CBC (older, needs HMAC for authentication)
            System.out.println("\n3. AES/CBC/PKCS5Padding (older mode):");
            System.out.println("   ✗ Needs separate HMAC for authentication");
            System.out.println("   ✓ Widely supported");
            System.out.println("   USE: Legacy systems, prefer GCM for new code");

            // Key sizes
            System.out.println("\n4. AES key sizes:");
            System.out.println("   128-bit: Fast, good security");
            System.out.println("   192-bit: More secure");
            System.out.println("   256-bit: ✓ Highest security (recommended)");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException |
                 IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Asymmetric Encryption ==========

    private static void asymmetricEncryptionDemo() {
        System.out.println("--- Asymmetric Encryption (RSA) ---");
        System.out.println("Public key encrypts, private key decrypts\n");

        String message = "Confidential Data";
        System.out.println("Original: " + message);

        try {
            // Generate RSA key pair
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048); // 2048-bit keys (minimum recommended)
            KeyPair keyPair = keyPairGen.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            System.out.println("\n1. RSA Key Pair generated:");
            System.out.println("   Public key: " + publicKey.getAlgorithm() + " (" +
                             publicKey.getEncoded().length + " bytes)");
            System.out.println("   Private key: " + privateKey.getAlgorithm() + " (" +
                             privateKey.getEncoded().length + " bytes)");

            // Encrypt with public key
            System.out.println("\n2. Encrypt with public key:");
            Cipher encryptCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256AndMGF1Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = encryptCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            System.out.println("   Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

            // Decrypt with private key
            System.out.println("\n3. Decrypt with private key:");
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256AndMGF1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = decryptCipher.doFinal(encrypted);
            System.out.println("   Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));

            // Export keys (for storage/transmission)
            System.out.println("\n4. Export/Import keys:");
            byte[] publicKeyBytes = publicKey.getEncoded();
            byte[] privateKeyBytes = privateKey.getEncoded();
            System.out.println("   Public key (Base64): " +
                             Base64.getEncoder().encodeToString(publicKeyBytes).substring(0, 50) + "...");

            // Import public key
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey importedPublicKey = keyFactory.generatePublic(publicKeySpec);
            System.out.println("   Public key imported successfully");

            // Import private key
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey importedPrivateKey = keyFactory.generatePrivate(privateKeySpec);
            System.out.println("   Private key imported successfully");

            System.out.println("\n5. RSA characteristics:");
            System.out.println("   ✓ Public key can be shared freely");
            System.out.println("   ✓ No shared secret needed");
            System.out.println("   ✗ Much slower than symmetric encryption");
            System.out.println("   ✗ Limited message size (< key size)");
            System.out.println("   USE: Key exchange, digital signatures, small data");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Digital Signatures ==========

    private static void digitalSignatureDemo() {
        System.out.println("--- Digital Signatures ---");
        System.out.println("Verify authenticity and integrity of data\n");

        String message = "Important Document";
        System.out.println("Original message: " + message);

        try {
            // Generate key pair
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // Sign with private key
            System.out.println("\n1. Sign with private key (SHA256withRSA):");
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(keyPair.getPrivate());
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] digitalSignature = signature.sign();
            System.out.println("   Signature: " + Base64.getEncoder().encodeToString(digitalSignature).substring(0, 50) + "...");
            System.out.println("   Signature length: " + digitalSignature.length + " bytes");

            // Verify with public key
            System.out.println("\n2. Verify with public key:");
            Signature verifySignature = Signature.getInstance("SHA256withRSA");
            verifySignature.initVerify(keyPair.getPublic());
            verifySignature.update(message.getBytes(StandardCharsets.UTF_8));
            boolean isValid = verifySignature.verify(digitalSignature);
            System.out.println("   Signature valid: " + isValid);

            // Tampered message
            System.out.println("\n3. Verify tampered message:");
            String tamperedMessage = "Important Document!!!";
            verifySignature.initVerify(keyPair.getPublic());
            verifySignature.update(tamperedMessage.getBytes(StandardCharsets.UTF_8));
            boolean isTamperedValid = verifySignature.verify(digitalSignature);
            System.out.println("   Tampered message valid: " + isTamperedValid);

            System.out.println("\n4. Digital signature use cases:");
            System.out.println("   ✓ Verify sender identity (authentication)");
            System.out.println("   ✓ Verify message integrity (not modified)");
            System.out.println("   ✓ Non-repudiation (sender cannot deny)");
            System.out.println("   USE: Code signing, SSL/TLS, document signing");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Key Generation ==========

    private static void keyGenerationDemo() {
        System.out.println("--- Key Generation ---");
        System.out.println("Generate cryptographic keys\n");

        try {
            // Symmetric keys
            System.out.println("1. Symmetric key generation:");

            KeyGenerator aesGen = KeyGenerator.getInstance("AES");
            aesGen.init(256);
            SecretKey aesKey = aesGen.generateKey();
            System.out.println("   AES-256: " + Base64.getEncoder().encodeToString(aesKey.getEncoded()));

            KeyGenerator desGen = KeyGenerator.getInstance("DES");
            SecretKey desKey = desGen.generateKey();
            System.out.println("   DES (✗ Deprecated): " + Base64.getEncoder().encodeToString(desKey.getEncoded()));

            // Asymmetric keys
            System.out.println("\n2. Asymmetric key generation:");

            KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
            rsaGen.initialize(2048);
            KeyPair rsaPair = rsaGen.generateKeyPair();
            System.out.println("   RSA-2048 generated");

            KeyPairGenerator dsaGen = KeyPairGenerator.getInstance("DSA");
            dsaGen.initialize(2048);
            KeyPair dsaPair = dsaGen.generateKeyPair();
            System.out.println("   DSA-2048 generated");

            // Elliptic Curve
            KeyPairGenerator ecGen = KeyPairGenerator.getInstance("EC");
            ecGen.initialize(256);
            KeyPair ecPair = ecGen.generateKeyPair();
            System.out.println("   EC-256 generated");

            System.out.println("\n3. Key size recommendations:");
            System.out.println("   Symmetric:");
            System.out.println("     AES-128: Good security, fast");
            System.out.println("     AES-256: ✓ Highest security (recommended)");
            System.out.println("   Asymmetric:");
            System.out.println("     RSA-2048: Minimum for modern use");
            System.out.println("     RSA-4096: ✓ Higher security, slower");
            System.out.println("     EC-256: ✓ Equivalent to RSA-3072, faster");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Secure Random ==========

    private static void secureRandomDemo() {
        System.out.println("--- Secure Random Number Generation ---");
        System.out.println("Cryptographically strong random numbers\n");

        // SecureRandom vs Random
        System.out.println("1. SecureRandom vs Random:");
        System.out.println("   java.util.Random:");
        System.out.println("     ✗ Predictable (pseudo-random)");
        System.out.println("     ✗ NOT suitable for security");
        System.out.println("   java.security.SecureRandom:");
        System.out.println("     ✓ Cryptographically strong");
        System.out.println("     ✓ Unpredictable");
        System.out.println("     ✓ ✓ Use for all security purposes");

        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();

            // Random bytes
            System.out.println("\n2. Generate random bytes:");
            byte[] randomBytes = new byte[16];
            secureRandom.nextBytes(randomBytes);
            System.out.println("   Random bytes: " + bytesToHex(randomBytes));

            // Random integers
            System.out.println("\n3. Generate random integers:");
            System.out.println("   Random int: " + secureRandom.nextInt());
            System.out.println("   Random int (0-99): " + secureRandom.nextInt(100));
            System.out.println("   Random long: " + secureRandom.nextLong());

            // Random boolean and double
            System.out.println("\n4. Other random types:");
            System.out.println("   Random boolean: " + secureRandom.nextBoolean());
            System.out.println("   Random double: " + secureRandom.nextDouble());
            System.out.println("   Random float: " + secureRandom.nextFloat());

            // Seed (optional - automatically seeded)
            System.out.println("\n5. Seeding:");
            byte[] seed = SecureRandom.getSeed(20);
            System.out.println("   Generated seed: " + bytesToHex(seed));
            System.out.println("   Note: SecureRandom auto-seeds, manual seeding rarely needed");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Password Hashing ==========

    private static void passwordHashingDemo() {
        System.out.println("--- Password Hashing (PBKDF2) ---");
        System.out.println("Secure password storage with salt and iterations\n");

        String password = "MySecurePassword123!";
        System.out.println("Password: " + password);

        try {
            // Generate salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            System.out.println("\n1. Salt: " + Base64.getEncoder().encodeToString(salt));
            System.out.println("   ✓ Unique per user");
            System.out.println("   ✓ Prevents rainbow table attacks");

            // PBKDF2 parameters
            int iterations = 65536; // OWASP recommendation
            int keyLength = 256; // bits

            System.out.println("\n2. PBKDF2 parameters:");
            System.out.println("   Iterations: " + iterations);
            System.out.println("   Key length: " + keyLength + " bits");
            System.out.println("   ✓ High iterations slow down brute force");

            // Hash password
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            System.out.println("\n3. Password hash: " + Base64.getEncoder().encodeToString(hash));

            // Verify password
            System.out.println("\n4. Password verification:");
            String inputPassword = "MySecurePassword123!";
            KeySpec verifySpec = new PBEKeySpec(inputPassword.toCharArray(), salt, iterations, keyLength);
            byte[] verifyHash = factory.generateSecret(verifySpec).getEncoded();
            boolean passwordMatch = MessageDigest.isEqual(hash, verifyHash);
            System.out.println("   Password matches: " + passwordMatch);

            // Wrong password
            String wrongPassword = "WrongPassword";
            KeySpec wrongSpec = new PBEKeySpec(wrongPassword.toCharArray(), salt, iterations, keyLength);
            byte[] wrongHash = factory.generateSecret(wrongSpec).getEncoded();
            boolean wrongMatch = MessageDigest.isEqual(hash, wrongHash);
            System.out.println("   Wrong password matches: " + wrongMatch);

            System.out.println("\n5. Best practices:");
            System.out.println("   ✓ Use PBKDF2, bcrypt, or Argon2");
            System.out.println("   ✓ Use unique salt per password");
            System.out.println("   ✓ Use high iteration count (65536+)");
            System.out.println("   ✓ Store salt and hash (not password)");
            System.out.println("   ✗ NEVER use plain MD5/SHA for passwords");

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Base64 Encoding ==========

    private static void base64Demo() {
        System.out.println("--- Base64 Encoding/Decoding ---");
        System.out.println("Encode binary data as text\n");

        String text = "Hello, World!";
        byte[] data = text.getBytes(StandardCharsets.UTF_8);

        // Basic encoding
        System.out.println("1. Basic Base64:");
        String encoded = Base64.getEncoder().encodeToString(data);
        System.out.println("   Original: " + text);
        System.out.println("   Encoded: " + encoded);
        byte[] decoded = Base64.getDecoder().decode(encoded);
        System.out.println("   Decoded: " + new String(decoded, StandardCharsets.UTF_8));

        // URL-safe encoding
        System.out.println("\n2. URL-safe Base64:");
        String urlEncoded = Base64.getUrlEncoder().encodeToString(data);
        System.out.println("   Encoded: " + urlEncoded);
        System.out.println("   ✓ Uses - and _ instead of + and /");
        System.out.println("   USE: URLs, filenames");

        // MIME encoding
        System.out.println("\n3. MIME Base64:");
        String longText = "A".repeat(100);
        String mimeEncoded = Base64.getMimeEncoder().encodeToString(longText.getBytes());
        System.out.println("   ✓ Line breaks every 76 characters");
        System.out.println("   USE: Email, MIME content");

        // Without padding
        System.out.println("\n4. Without padding:");
        String noPadding = Base64.getEncoder().withoutPadding().encodeToString(data);
        System.out.println("   With padding: " + encoded);
        System.out.println("   Without padding: " + noPadding);
        System.out.println("   (No trailing '=' characters)");

        System.out.println();
    }

    // ========== Helper Methods ==========

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
