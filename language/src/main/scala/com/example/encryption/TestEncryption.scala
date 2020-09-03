package com.example.encryption

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Base64

import javax.crypto.{Cipher, SecretKeyFactory}
import javax.crypto.spec.{PBEKeySpec, SecretKeySpec}

object TestEncryption {
  def getRandomNonce(numBytes: Int): Array[Byte] = {
    val nonce = new Array[Byte](numBytes);
    new SecureRandom().nextBytes(nonce);
    nonce
  }

  def getKeyFromPass(password: Array[Char], salt: Array[Byte]) = {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec = new PBEKeySpec(password, salt, 65536, 256);
    val secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    secret;
  }

  def encrypt1(pText: Array[Byte], password: String) = {
    // 16 bytes salt
    val salt = getRandomNonce(16);
    val iv = getRandomNonce(12);
    val aesKeyFromPassword = getKeyFromPass(password.toCharArray(), salt);
    val cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword);
    val cipherText = cipher.doFinal(pText);
    val cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length).put(iv).put(salt).put(cipherText).array();
    Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
  }

  // we need the same password, salt and iv to decrypt it
  def decrypt(cText: String, password: String): String = {
    val decode = Base64.getDecoder().decode(cText.getBytes("UTF-8"));
    val bb = ByteBuffer.wrap(decode);
    val iv = new Array[Byte](12);
    val salt = new Array[Byte](16);
    //it seems iv/salt is in the begining
    bb.get(iv);
    bb.get(salt);
    val cipherText = new Array[Byte](bb.remaining());
    bb.get(cipherText);
    val aesKeyFromPassword = getKeyFromPass(password.toCharArray(), salt);
    val cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword);
    val plainText = cipher.doFinal(cipherText);
    new String(plainText, "utf-8");
  }

  def main(args: Array[String]): Unit = {
    println("---------TEST 1 Correct way-----------")
    val encryptedData = encrypt1("Hello".getBytes(), "password")
    println(encryptedData)
    val decrptedData = decrypt(encryptedData, "password")
    println(decrptedData)
  }
}
