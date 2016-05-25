package baajna.scroll.owner.mobioapp.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Jewel
 */
public class Decryption {
    // password for encryption and decryption
    byte[] iv = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    Cipher cipher;
    IvParameterSpec ivS;

    public Decryption(){
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ivS=new IvParameterSpec(iv);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Decryption.class.getName()).log(Level.SEVERE, null, ex);
        }
    }





    public  byte[] encrypt( byte[] data) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(iv, "AES");

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivS);

        byte[] encrypted = cipher.doFinal(data);


        return encrypted;

    }
    public byte[] decrypt(byte[] encrypted)  {

        SecretKeySpec skeySpec = new SecretKeySpec(iv, "AES");

        byte[] decrypted=null;



        try {

            cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivS);

            decrypted = cipher.doFinal(encrypted);


        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException ex) {

        }

        return decrypted;

    }

    public  byte[] getAudioFileFromSdCard(String fileName)

    {


        byte[] inarry = null;


        try {

            String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dir+Globals.DOWNLOAD_FOLDER+fileName); //Creating file object
            FileInputStream fileInputStream = null;

            byte[] bFile = new byte[(int) file.length()];

            fileInputStream = new FileInputStream(file);

            fileInputStream.read(bFile);

            fileInputStream.close();

            inarry = bFile;


        } catch (IOException e) {

            // TODO Auto-generated catch block

            Log.d("Jewel",": : "+e.toString());

        }


        return inarry;

    }

    public void saveSong(byte[]data,String fileLocation){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(fileLocation));
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {

        }



    }
}
