package as.com.au.ptvwear.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

public class AlertUtils {

    public static void showError(Context context, Exception e) {
        String errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "";
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
        e.printStackTrace();
    }

    public static ProgressDialog createLoadingDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        return dialog;
    }


    public static void showErrorDialog(Context context, String message) {
        showErrorDialog(context, message, null);
    }

    public static void showConfirmDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle("Confirmation")
                .setMessage(message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", listener)
                .show();
    }

    public static void showErrorDialog(Context context, String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context).setTitle("Error")
                .setMessage(message)
                .setNegativeButton("OK", listener)
                .show();
    }

    public static void showError(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }

    public static Toast getErrorToast(Context context, String error) {
        return Toast.makeText(context, error, Toast.LENGTH_LONG);
    }

    public static void logError(Exception e) {
        e.printStackTrace();
    }
}