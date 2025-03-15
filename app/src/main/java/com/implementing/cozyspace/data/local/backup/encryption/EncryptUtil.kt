package com.implementing.cozyspace.data.local.backup.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

const val KEY_ALIAS = "your_key_alias"
private const val IV_LENGTH = 12

// Generates or retrieves a secret key from the Android Keystore
fun getSecretKey(): SecretKey {
    val keyStore = KeyStore.getInstance("AndroidKeyStore")
    keyStore.load(null)

    if (!keyStore.containsAlias(KEY_ALIAS)) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    return keyStore.getKey(KEY_ALIAS, null) as SecretKey
}


// Encrypts the content using AES encryption
fun encryptData(data: String, secretKey: SecretKey): String {
    try {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv // Get the generated IV
        val encryptedData = cipher.doFinal(data.toByteArray())

        // Combine IV and encrypted data for storage
        val combinedData = ByteArray(iv.size + encryptedData.size)
        System.arraycopy(iv, 0, combinedData, 0, iv.size)
        System.arraycopy(encryptedData, 0, combinedData, iv.size, encryptedData.size)

        return Base64.encodeToString(combinedData, Base64.DEFAULT)
    } catch (e: Exception) {
        throw RuntimeException("Error during encryption: ${e.message}", e)
    }
}

// Decrypts the content (when needed)
fun decryptData(encryptedData: String, secretKey: SecretKey): String {
    try {
        val combinedData = Base64.decode(encryptedData, Base64.DEFAULT)
        val iv = combinedData.copyOfRange(0, IV_LENGTH) // Extract IV
        val encryptedBytes = combinedData.copyOfRange(IV_LENGTH, combinedData.size) // Extract encrypted data

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv) // 128-bit authentication tag
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        val decryptedData = cipher.doFinal(encryptedBytes)
        return String(decryptedData)
    } catch (e: Exception) {
        throw RuntimeException("Error during decryption: ${e.message}", e)
    }
}