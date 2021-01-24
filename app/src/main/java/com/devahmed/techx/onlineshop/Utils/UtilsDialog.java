package com.devahmed.techx.onlineshop.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.devahmed.techx.onlineshop.BuildConfig;
import com.devahmed.techx.onlineshop.R;

public class UtilsDialog {

    private static Dialog Dialog;
    private Activity activity;
    private Context context;
    public UtilsDialog(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public UtilsDialog(Context context) {
        this.context = context;
    }

    public static void showAddToCartDialog(Context context){
        Dialog = new Dialog(context);
        Dialog.setContentView(R.layout.add_to_cart_pop_up_dialog);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        Dialog.findViewById(R.id.container).setAnimation(AnimationUtils.loadAnimation(context , R.anim.products_image_fade_scale_animation));
        Dialog.setCancelable(true);

    }

    public void showExitDialog(){
        //Dialog Init
        Dialog = new Dialog(activity);
        Dialog.setContentView(R.layout.dialog_extite_app);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        Dialog.findViewById(R.id.container).setAnimation(AnimationUtils.loadAnimation(activity , R.anim.products_image_fade_scale_animation));
        Dialog.setCancelable(true);
        final Button exit , share , rate;
        exit = Dialog.findViewById(R.id.exit);
        share = Dialog.findViewById(R.id.shareapp);
        rate = Dialog.findViewById(R.id.reteus);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finishAndRemoveTask();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMyApp();
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateMyApp();
            }
        });
        Dialog.show();
    }
    private void RateMyApp() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
    public void showFullImageDialog(String imageUrl){
        //Dialog Init
        Dialog = new Dialog(activity);
        Dialog.setContentView(R.layout.full_image_dialog);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Dialog.findViewById(R.id.container).setAnimation(AnimationUtils.loadAnimation(activity , R.anim.products_image_fade_scale_animation));
        Dialog.setCancelable(true);
        ImageView fullViewImage = Dialog.findViewById(R.id.fullViewImage);

        Glide.with(activity)
                .load(imageUrl)
                .placeholder(R.drawable.images_placeholder)
                .into(fullViewImage);
        Dialog.show();
    }

    public void showFullImageDialog(BillCanvasView billCanvasView){
        //Dialog Init
        Dialog = new Dialog(activity);
        Dialog.setContentView(R.layout.bill_image_dialog);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        Dialog.findViewById(R.id.container).setAnimation(AnimationUtils.loadAnimation(activity , R.anim.products_image_fade_scale_animation));
        Dialog.setCancelable(true);
        final LinearLayout linearLayout = Dialog.findViewById(R.id.container);
        linearLayout.addView(billCanvasView);
        Dialog.show();
        Dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                linearLayout.removeAllViews();
            }
        });
    }

    public void showTextMessage(String message){
        //Dialog Init
        Dialog = new Dialog(context);
        Dialog.setContentView(R.layout.dialog_text_message);
        Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        Dialog.findViewById(R.id.container).setAnimation(AnimationUtils.loadAnimation(activity , R.anim.products_image_fade_scale_animation));
        Dialog.setCancelable(true);
        TextView textView = Dialog.findViewById(R.id.textMessage);
        textView.setText(message);
        Dialog.show();
    }

    public void dismiss(){
        Dialog.dismiss();
    }

    public void addCancelListener(DialogInterface.OnCancelListener cancelListener){
        Dialog.setOnCancelListener(cancelListener);
    }

    private void shareMyApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        activity.startActivity(shareIntent);
    }
}
