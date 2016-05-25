package baajna.scroll.owner.mobioapp.customviews;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

public class SidraCustomProgressDialog extends ProgressDialog {
    private AnimationDrawable animation;

    public static ProgressDialog creator(Context context) {
        SidraCustomProgressDialog dialog = new SidraCustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog creator2(Context context) {
        SidraCustomProgressDialog dialog = new SidraCustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }

    public SidraCustomProgressDialog(Context context) {
        super(context);
    }

    public SidraCustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.view_custom_progress_dialog);
//
//		ImageView la = (ImageView) findViewById(R.id.animation);
//		la.setBackgroundResource(R.fadein.loading_frame);
//		animation = (AnimationDrawable) la.getBackground();
    }

    @Override
    public void show() {
        super.show();
        animation.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        animation.stop();
    }
}
