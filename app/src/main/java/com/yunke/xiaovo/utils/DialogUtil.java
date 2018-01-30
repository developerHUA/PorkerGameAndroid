package com.yunke.xiaovo.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunke.xiaovo.R;


public class DialogUtil {

    private static boolean _waitDialogIsVisible = false;
    private static Dialog _waitDialog;


    public static Dialog showLoadingDialog(Context context, boolean cancelable) {
        if (_waitDialogIsVisible) {
            hideWaitDialog();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_progress_dialog, null);
        ImageView ivProgress = view.findViewById(R.id.iv_loading);
        AnimationDrawable drawable = (AnimationDrawable) ivProgress.getDrawable();
        drawable.start();
        Dialog waitDialog = new Dialog(context, R.style.progress_dialog);
        waitDialog.setContentView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        hideBottomUIMenu(waitDialog.getWindow());
        _waitDialog = waitDialog;
        _waitDialog.setCanceledOnTouchOutside(false);
        _waitDialog.setCancelable(cancelable);
        _waitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                _waitDialog = null;
                _waitDialogIsVisible = false;
            }
        });
        _waitDialog.show();
        _waitDialogIsVisible = true;
        return _waitDialog;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    private static void hideBottomUIMenu(Window window) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 14 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = window.getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = window.getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    public static void hideWaitDialog() {
        try {
            if (_waitDialogIsVisible && _waitDialog != null) {
                _waitDialog.dismiss();
                _waitDialog = null;
                _waitDialogIsVisible = false;
            }
        } catch (Exception e) {

        } finally {
            _waitDialog = null;
            _waitDialogIsVisible = false;
        }
    }


    /**
     * 可输入文本的dialog
     *
     * @param cancelable
     * @param context
     * @param title
     * @param negativeText
     * @param positiveText
     * @param negativeClickListener
     * @param positiveClickListener
     */
    public static void showEditDialog(boolean cancelable, final Context context, EditText editText,
                                      String title, String negativeText, String positiveText,
                                      DialogInterface.OnClickListener negativeClickListener,
                                      DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setView(editText)
                .setNegativeButton(negativeText, negativeClickListener)
                .setPositiveButton(positiveText, positiveClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    /**
     * 警告Dialog
     *
     * @param context
     * @param message
     * @return
     */
    public static void showAlertDialog(final Context context, String message) {
        showAlertDialog(context, message, context.getString(R.string.make_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 警告Dialog
     *
     * @param context
     * @param messageId
     * @param buttonTextId
     */
    public static void showAlertDialog(final Context context, int messageId, int buttonTextId) {
        showAlertDialog(context, context.getString(messageId), context.getString(buttonTextId));
    }

    /**
     * 警告Dialog
     *
     * @param context
     * @param message
     * @return
     */
    public static void showAlertDialog(final Context context, String message, String buttonText) {
        showAlertDialog(context, message, buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 警告Dialog
     *
     * @param context
     * @param messageId
     * @param positiveTextId
     * @param positiveClickListener
     */
    public static void showAlertDialog(final Context context, int messageId, int positiveTextId, DialogInterface.OnClickListener positiveClickListener) {
        showAlertDialog(context, context.getString(messageId), context.getString(positiveTextId), positiveClickListener);
    }

    /**
     * 警告Dialog
     *
     * @param context
     * @param message
     * @param positiveText
     * @param positiveClickListener
     * @return
     */
    public static void showAlertDialog(final Context context, String message, String positiveText, DialogInterface.OnClickListener positiveClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_alert_dialog, null);
        TextView tvMessage = (TextView) contentView.findViewById(R.id.tv_message);
        tvMessage.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(contentView)
                .setPositiveButton(positiveText, positiveClickListener);
        builder.create().show();
    }

    /**
     * 确认Dialog
     *
     * @param context
     * @param title
     * @param message
     * @param negativeText
     * @param negativeClickListener
     * @return
     */
    public static Dialog showConfirmDialog(final Context context, String title, String message, String negativeText, DialogInterface.OnClickListener negativeClickListener) {
        return showConfirmDialog(true, context, title, message, negativeText, context.getString(R.string.cancel), negativeClickListener, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 确认Dialog
     *
     * @param context
     * @param title
     * @param message
     * @param negativeText
     * @param positiveText
     * @param negativeClickListener
     * @param positiveClickListener
     * @return
     */
    public static Dialog showConfirmDialog(boolean cancelable, final Context context,
                                           String title, String message, String negativeText,
                                           String positiveText, DialogInterface.
                                                   OnClickListener negativeClickListener,
                                           DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeText, negativeClickListener)
                .setPositiveButton(positiveText, positiveClickListener);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }


}
