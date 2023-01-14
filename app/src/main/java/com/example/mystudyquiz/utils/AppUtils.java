package com.example.mystudyquiz.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ArrayUtils;
import com.blankj.utilcode.util.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import dev.sasikanth.colorsheet.ColorSheet;

public class AppUtils {


    public interface AlertDialogCallback{
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    public interface ColorPickerCallback{
        void onColorPicked(int color);
    }

    public static void showAlertDialog(Context context,String message){
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show();
    }

    public static void showAlertDialog(Context context,String message,String title, AlertDialogCallback callback){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if(callback != null)
                        callback.onPositiveButtonClicked();
                })
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                    if(callback != null)
                        callback.onNegativeButtonClicked();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    public static void showAlertDialog(Context context,String message, AlertDialogCallback callback){
        new AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if(callback != null)
                        callback.onPositiveButtonClicked();
                })
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> {
                    if(callback != null)
                        callback.onNegativeButtonClicked();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    public static void openHtmlTextDialog(Activity activity, String fileNameInAssets) {
        String str = "";
        InputStream is = null;

        try {
            is = activity.getAssets().open(fileNameInAssets);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(activity);
        materialAlertDialogBuilder.setPositiveButton("Close", null);

        materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));

        androidx.appcompat.app.AlertDialog al = materialAlertDialogBuilder.show();
        TextView alertTextView = al.findViewById(android.R.id.message);
        alertTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }



    public static void colorPickerBottomSheet(ColorPickerCallback colorPickerCallback, FragmentManager fragmentManager){
        new ColorSheet()
                .colorPicker(new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,Color.BLACK,Color.GRAY,Color.DKGRAY,Color.LTGRAY,Color.YELLOW}, null, true, integer -> {
                    if (integer == ColorSheet.NO_COLOR)
                        integer = Color.WHITE;
                    colorPickerCallback.onColorPicked(integer);
                    return null;
                })
                .show(fragmentManager);
    }

    /**
     * Hides the soft keyboard.
     *
     * @param context The context.
     * @param view    The view that the keyboard should be hidden for.
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * Shows a short toast message.
     *
     * @param context The context.
     * @param message The message to show.
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a long toast message.
     *
     * @param context The context.
     * @param message The message to show.
     */
    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Launches the email app with a new email pre-populated with the specified subject and recipient.
     *
     * @param context  The context.
     * @param recipient The email address of the recipient.
     * @param subject   The subject of the email.
     */
    public static void sendEmail(Context context, String recipient, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * Launches the phone app with the specified phone number.
     *
     * @param context The context.
     * @param phoneNumber The phone number to call.
     */
    public static void makePhoneCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * Returns the screen width in pixels.
     *
     * @param activity The activity.
     * @return The screen width in pixels.
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * Returns the screen height in pixels.
     *
     * @param activity The activity.
     * @return The screen height in pixels.
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    /**
     * Converts a Drawable to a Bitmap.
     *
     * @param drawable The Drawable to convert.
     * @return The Bitmap.
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Changes the color of a Drawable.
     *
     * @param drawable The Drawable to change the color of.
     * @param color    The new color.
     * @return The Drawable with the new color.
     */
    public static Drawable changeDrawableColor(Drawable drawable, int color) {
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    /**
     * Saves a Bitmap to a file.
     *
     * @param context The context.
     * @param bitmap  The Bitmap to save.
     * @param file    The file to save the Bitmap to.
     * @throws IOException if the Bitmap could not be saved.
     */
    public static void saveBitmap(Context context, Bitmap bitmap, File file) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }



    /**
     * Returns the device's unique identifier.
     *
     * @param context The context.
     * @return The device's unique identifier.
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }







    /*
    icons links:
    <a href="https://www.flaticon.com/free-icons/play-button" title="play button icons">Play button icons created by Maxim Basinski Premium - Flaticon</a>
    <a href="https://www.flaticon.com/free-icons/plus" title="plus icons">Plus icons created by srip - Flaticon</a>
    <a href="https://www.flaticon.com/free-icons/study" title="study icons">Study icons created by Freepik - Flaticon</a>


     */

}
