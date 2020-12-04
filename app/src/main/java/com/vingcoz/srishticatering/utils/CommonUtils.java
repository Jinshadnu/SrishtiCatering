package com.vingcoz.srishticatering.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.vingcoz.srishticatering.R;

import java.util.Objects;

import es.dmoral.toasty.BuildConfig;
import es.dmoral.toasty.Toasty;

public class CommonUtils {

    Dialog mDialogProgress;
    TextView txtLoadingMessage;
    private Context mCtx;
    private AlertDialog mAlert;
    private boolean blnReturnValue = false;

    public CommonUtils(Context context) {
        mCtx = context;

        mDialogProgress = new Dialog(mCtx, R.style.mytheme);
        View views = LayoutInflater.from(mCtx).inflate(R.layout.dialog_loading, null);
        mDialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(mDialogProgress.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogProgress.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        mDialogProgress.setContentView(views);
        mDialogProgress.setCancelable(false);
        txtLoadingMessage = mDialogProgress.findViewById(R.id.txtLoadingMessage);
        // MaterialDialog mDialog = new MaterialDialog.Builder(mCtx)
    }

    public void showSuccess(String strMessage) {
        Toasty.success(mCtx, strMessage, Toast.LENGTH_SHORT, true).show();
    }

    public void showError(String strMessage) {
        Toasty.error(mCtx, strMessage, Toast.LENGTH_LONG, true).show();
    }

    public void debugMe(Exception e, Thread t) {

        if (BuildConfig.DEBUG) {

            String strMessage = e.getMessage();
            strMessage += "\n";
            strMessage += e.getStackTrace();
            strMessage += "\n";
            // strMessage += t.getStackTrace()[2].getFileName();
            strMessage += "\n";
            //  strMessage += t.getStackTrace()[2].getMethodName();
            strMessage += "\n";
            //  strMessage += t.getStackTrace()[2].getLineNumber();

            Toasty.error(mCtx, strMessage, Toast.LENGTH_LONG, true).show();
        }
    }

    public void showWarning(String strMessage) {
        Toasty.warning(mCtx, strMessage, Toast.LENGTH_SHORT, true).show();
    }

    public void showInfo(String strMessage) {
        Toasty.info(mCtx, strMessage, Toast.LENGTH_SHORT, true).show();
    }

    public void alertMe(String strMessage) {

        if (BuildConfig.DEBUG) {
            new AlertDialog.Builder(mCtx)
                    .setTitle("Debug Alert Error:")
                    .setMessage(strMessage)
                    .setCancelable(false)
                    .setPositiveButton("ok", (dialog, which) -> {
                    }).show();
        }
    }

    public void progress(String strMessage, boolean blnAction) {
        if (blnAction) {

            if (strMessage.trim().length() == 0) {
                strMessage = "Loading..Please wait";
            }
            txtLoadingMessage.setText(strMessage);
            if (!mDialogProgress.isShowing()) {
                mDialogProgress.show();
            }
        } else {
            if (mDialogProgress.isShowing()) {
                mDialogProgress.dismiss();
            }
        }

    }

    public boolean isNetworkAvailable() {

        ConnectivityManager cm =
                (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}
