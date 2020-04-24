package com.china.cibn.bangtvmobile.bangtv.module.user.vip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.china.cibn.bangtvmobile.R;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ProtocolDialog extends Activity {
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protoco_bangtv);
        btnExit = (Button) findViewById(R.id.button1);
        btnExit.setOnClickListener(view -> finish());
    }

    public static void launch(Activity activity) {

        Intent mIntent = new Intent(activity, ProtocolDialog.class);
        activity.startActivity(mIntent);

    }
}
