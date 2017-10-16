package cn.lockyluo.clinicaldepartments;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.litepal.LitePalApplication;

/**
 * Created by lockyluo on 2017/10/10.
 */

public class App extends LitePalApplication{
    private static App instance;
    public static AndroidLogAdapter androidLogAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if(androidLogAdapter==null){
            androidLogAdapter=new AndroidLogAdapter(getFormatStrategy());
            Logger.addLogAdapter(androidLogAdapter);
        }
    }

    private FormatStrategy getFormatStrategy(){
        FormatStrategy formatStrategy= PrettyFormatStrategy
                .newBuilder()
                .tag("locky Log").build();
        return  formatStrategy;
    }

    public static App getInstance(){
        return instance;
    }
}
