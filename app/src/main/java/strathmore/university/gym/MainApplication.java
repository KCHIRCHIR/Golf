package strathmore.university.gym;

import android.app.Application;
import android.content.Context;

import strathmore.university.gym.Helper.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.setLocale(base, "en"));
    }
}
