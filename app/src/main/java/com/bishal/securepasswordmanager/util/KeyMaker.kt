import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.bishal.securepasswordmanager.constant.EncryptionConstants.AES_KEY_SIZE_BITS
import com.bishal.securepasswordmanager.constant.EncryptionConstants.KEY_ALIAS
import com.bishal.securepasswordmanager.constant.EncryptionConstants.SHARED_PREF_NAME
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Object responsible for managing encryption keys using Android's EncryptedSharedPreferences.
 */
object KeyMaker {

    /**
     * Initializes EncryptedSharedPreferences with a master key for secure storage.
     *
     * @param context Application context.
     */
    fun init(context: Context) {
        try {
            // Create a master key using AES256_GCM key scheme
            val masterKeyAlias = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Initialize EncryptedSharedPreferences with the master key
            EncryptedSharedPreferences.create(
                context,
                SHARED_PREF_NAME,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Generates a new 256-bit AES key and saves it securely in EncryptedSharedPreferences.
     *
     * @param context Application context.
     */
    fun generateAndSaveSecretKey(context: Context) {
        try {
            // Generate a 256-bit AES key
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(AES_KEY_SIZE_BITS)
            val secretKey = keyGenerator.generateKey()

            // Save the generated AES key securely
            saveKey(context, secretKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Saves a given AES secret key securely in EncryptedSharedPreferences.
     *
     * @param context Application context.
     * @param secretKey AES secret key to be saved.
     */
    private fun saveKey(context: Context, secretKey: SecretKey) {
        try {
            // Create a master key using AES256_GCM key scheme
            val masterKeyAlias = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Initialize EncryptedSharedPreferences with the master key
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                SHARED_PREF_NAME,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // Encode the secret key and save it in SharedPreferences
            val editor = sharedPreferences.edit()
            val secretKeyEncoded = secretKey.encoded
            val secretKeyBase64 = Base64.encodeToString(secretKeyEncoded, Base64.DEFAULT)
            editor.putString(KEY_ALIAS, secretKeyBase64)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Retrieves the AES secret key stored securely in EncryptedSharedPreferences.
     *
     * @param context Application context.
     * @return SecretKey object retrieved from SharedPreferences, or null if retrieval fails.
     */
    fun getKey(context: Context): SecretKey? {
        try {
            // Create a master key using AES256_GCM key scheme
            val masterKeyAlias = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Initialize EncryptedSharedPreferences with the master key
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                SHARED_PREF_NAME,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            // Retrieve and decode the AES secret key from SharedPreferences
            val secretKeyBase64 = sharedPreferences.getString(KEY_ALIAS, null) ?: return null
            val secretKeyEncoded = Base64.decode(secretKeyBase64, Base64.DEFAULT)
            return SecretKeySpec(secretKeyEncoded, 0, secretKeyEncoded.size, "AES")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
