package com.bc_manga2.obj;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
public class ToastUnity {

	public static void ShowTost(Context context,String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
   
}
