package com.segnities007.stylishui.r8sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/** Minimal Java consumer used only to prove a release/minified Android package links. */
public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        TextView text = new TextView(this);
        text.setText("Stylish UI R8 smoke");
        setContentView(text);
    }
}
