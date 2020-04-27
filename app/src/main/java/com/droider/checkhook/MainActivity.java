package com.droider.checkhook;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView)findViewById(R.id.textview);
        tv.setText(checkHook() ? "Yes!": "No!");
    }

    private boolean checkHook() {
        PackageManager packageManager = this.getPackageManager();
        List<ApplicationInfo> applicationInfoList =
                packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo applicationInfo: applicationInfoList) {
            if (applicationInfo.packageName.equals("de.robv.android.xposed.installer"))
                return true;
        }
        try {
            throw new Exception("checkhook");
        }
        catch (Exception e) {
            e.printStackTrace();
            for (StackTraceElement stackTraceElement: e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("main"))
                    return true;
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && stackTraceElement.getMethodName().equals("handleHookedMethod"))
                    return true;
            }
        }
        return false;
    }
}
