package com.yangyxd.flutterumpush;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.PushAgent;

import java.util.List;

import io.flutter.app.FlutterApplication;

public class MainApplication extends FlutterApplication {
    private static final String TAG = "MainApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            init(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Context context) {
        UMConfigure.setLogEnabled(true);
        Bundle bundle = null;
        try {
            bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (null != bundle) {
            Log.e("whh", "UMENG_APPKEY \t" + bundle.getString("UMENG_APPKEY")
                    + "UMENG_CHANNEL \t" + bundle.getString("UMENG_CHANNEL")
                    + "UMENG_MESSAGE_SECRET \t" + bundle.getString("UMENG_MESSAGE_SECRET")
            );
            UMConfigure.init(this, bundle.getString("UMENG_APPKEY"), bundle.getString("UMENG_CHANNEL"), UMConfigure.DEVICE_TYPE_PHONE, bundle.getString("UMENG_MESSAGE_SECRET"));
        } else {
            Log.e("whh", "bundle empty ");
        }
//        UMConfigure.init(this, "5d8b2f0d4ca35791a7000895", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "c9f1f7b585127da6a27c6311d2c081f8");
//获取消息推送代理示例
        Log.e(TAG, "onCreate:   " + UMConfigure.getTestDeviceInfo(this));
        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i("whh", "注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("whh", "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        FlutterUmPushPlugin.initUpush(context, PushAgent.getInstance(context));
    }

//    private boolean shouldInit() {
//
//        //通过ActivityManager我们可以获得系统里正在运行的activities
//        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
//        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
//        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
//        String mainProcessName = getPackageName();
//
//        //获取本App的唯一标识
//        int myPid = android.os.Process.myPid();
//        //利用一个增强for循环取出手机里的所有进程
//        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
//            //通过比较进程的唯一标识和包名判断进程里是否存在该App
//            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static String getMetaData(Context context, String name) {
//        try {
//            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
//                    PackageManager.GET_META_DATA);
//            return appInfo.metaData.getString("name");
//        } catch (PackageManager.NameNotFoundException e) {
//            return null;
//        }
//    }
}
