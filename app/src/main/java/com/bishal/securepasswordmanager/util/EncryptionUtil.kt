import android.content.Context
import android.os.Build
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Utility class for encrypting and decrypting data using AES encryption with CBC mode and PKCS5 padding.
 */
object EncryptionUtil {

    private const val AES_TRANSFORMATION = "AES/CBC/PKCS5PADDING"
    private const val IV_SIZE_BYTES = 16 // 128 bits

    /**
     * Encrypts the given string value using AES encryption.
     *
     * @param context Application context.
     * @param value String value to encrypt.
     * @return Encrypted string encoded in Base64, or null if encryption fails.
     */
    fun encrypt(context: Context, value: String): String? {
        val secretKey = KeyMaker.getKey(context) ?: return null

        try {
            // Generate a new IV (Initialization Vector) for each encryption operation
            val iv = ByteArray(IV_SIZE_BYTES)
            SecureRandom().nextBytes(iv)

            val ivSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val encrypted = cipher.doFinal(value.toByteArray(StandardCharsets.UTF_8))

            // Concatenate IV and encrypted data
            val combined = iv + encrypted

            // Encode combined data into Base64 string
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                java.util.Base64.getEncoder().encodeToString(combined)
            } else {
                android.util.Base64.encodeToString(combined, android.util.Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Decrypts the given Base64-encoded encrypted string using AES decryption.
     *
     * @param context Application context.
     * @param encrypted Encrypted string encoded in Base64.
     * @return Decrypted string, or null if decryption fails.
     */
    fun decrypt(context: Context, encrypted: String?): String? {
        if (encrypted == null) return null
        val secretKey = KeyMaker.getKey(context) ?: return null

        try {
            // Decode the Base64-encoded encrypted data
            val combined = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                java.util.Base64.getDecoder().decode(encrypted)
            } else {
                android.util.Base64.decode(encrypted, android.util.Base64.DEFAULT)
            }

            // Extract IV and encrypted data from combined data
            val iv = combined.copyOfRange(0, IV_SIZE_BYTES)
            val encryptedData = combined.copyOfRange(IV_SIZE_BYTES, combined.size)

            val ivSpec = IvParameterSpec(iv)
            val cipher = Cipher.getInstance(AES_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            val original = cipher.doFinal(encryptedData)

            // Convert decrypted byte array to string using UTF-8 encoding
            return original.toString(StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
