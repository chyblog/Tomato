package com.chyblog.tomato.util;

import android.content.Context;
import android.widget.Toast;

public class TispToastFactory {

	private static Toast toast = null;

	public static Toast getToast(Context context, String hint) {

		if(TispToastFactory.toast == null) {
			toast = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
		} else {
			toast.setText(hint);
		}
		return toast;

	}
}
