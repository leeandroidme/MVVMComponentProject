package com.newland.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * @author: leellun
 * @data: 18/6/2021.
 *
 */
object DeviceInfo {
    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    fun isMobileConnected(context: Context?): Boolean {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE) return networkInfo.isAvailable
        }
        return false
    }

    /**
     * 是否有可用网络（WIFI和移动网络）
     *
     * @return true 网络可用
     */
    fun isNetworkConnected(context: Context): Boolean {
        val result = false
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo?.isAvailable ?: result
    }

    fun getIp(context: Context): String? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiState = wifiNetworkInfo?.state
        val mobileState = mobileNetworkInfo?.state
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress) {
                            return inetAddress.hostAddress
                        }
                    }
                }
            } catch (ex: SocketException) {
            }
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            //获取wifi服务
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            //判断wifi是否开启
            if (wifiManager.isWifiEnabled) {
                val wifiInfo = wifiManager.connectionInfo
                val ipAddress = wifiInfo.ipAddress
                return (ipAddress and 0xFF).toString() + "." +
                        (ipAddress shr 8 and 0xFF) + "." +
                        (ipAddress shr 16 and 0xFF) + "." +
                        (ipAddress shr 24 and 0xFF)
            }
        }
        return ""
    }


    /**
     * 打开移动数据
     *
     * @param context
     */
    fun openMobileData(context: Context): Int {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var i = 0
        var conMgrClass: Class<*>? = null // ConnectivityManager类
        var iConMgrField: Field? = null // ConnectivityManager类中的字段
        var iConMgr: Any? = null // IConnectivityManager类的引用
        var iConMgrClass: Class<*>? = null // IConnectivityManager类
        var setMobileDataEnabledMethod: Method? = null // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.javaClass.name)
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService")
            // 设置mService可访问
            iConMgrField.isAccessible = true
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField[conMgr]
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.javaClass.name)
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod =
                iConMgrClass.getDeclaredMethod("setMobileDataEnabled", java.lang.Boolean.TYPE)
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.isAccessible = true
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, true)
            i = 1
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return i
    }

    /**
     * 定位是否开启
     *
     * @param context
     * @return true 开启
     */
    fun isLocationEnable(context: Context): Boolean {
        var result = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            val network =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) // 网络定位
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) // GPS定位
            if (network || gps) {
                result = true
            }
        }
        return result
    }

    /**
     * GPS定位是否开启
     *
     * @param context
     * @return true 开启
     */
    fun isGpsEnable(context: Context): Boolean {
        var result = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) // GPS定位
        }
        return result
    }

    /**
     * 打开或关闭GPS
     *
     * @param context
     */
    fun openGPS(context: Context?) {
        val GPSIntent = Intent()
        GPSIntent.setClassName(
            "com.android.settings",
            "com.android.settings.widget.SettingsAppWidgetProvider"
        )
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
        GPSIntent.data = Uri.parse("custom:3")
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send()
        } catch (e: CanceledException) {
            e.printStackTrace()
        }
    }

    fun settingGPS(context: Context) {
        val alm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            context.startActivity(intent) // 此为设置完成后返回到获取界面
        }
    }

    /**
     * 获取当前连接WIFI的SSID
     */
    fun getSSID(context: Context): String? {
        val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wm != null) {
            val winfo = wm.connectionInfo
            if (winfo != null) {
                val s = winfo.ssid
                if (s.length > 2 && s[0] == '"' && s[s.length - 1] == '"') {
                    return s.substring(1, s.length - 1)
                }
            }
        }
        return ""
    }

    /**
     * WIFI是否可用
     *
     * @param context
     * @return true 可用
     */
    fun isWifiEnable(context: Context): Boolean {
        var result = false
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager != null) {
            result = wifiManager.isWifiEnabled
        }
        return result
    }

    /**
     * 打开WIFI
     *
     * @param context
     */
    fun openWifi(context: Context) {
        if (!isWifiEnable(context)) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager != null) {
                wifiManager.isWifiEnabled = true
            }
        }
    }

    /**
     * 得到用户手机号
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getPhoneNumber(context: Context): String? {
        var result = ""
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        result = telephonyManager.line1Number
        if (!TextUtils.isEmpty(result)) {
            result = result.replace("+86", "")
        }
        return result
    }

    /**
     * 得到用户手机串号
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getPhoneDeviceId(context: Context): String? {
        var result = ""
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            result = telephonyManager.deviceId
        } catch (e: Exception) {
//            CmLog.printStackTrace(e);
        }
        return result
    }

    /**
     * 获取媒体音量
     *
     * @param context
     * @param type    音量类型
     * @return
     */
    fun getCurrentVolume(context: Context, type: Int): Int {
        val mAudioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return mAudioManager.getStreamVolume(type)
    }

    fun getProcessName(context: Context): String? {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return ""
    }

    /**
     * 进程是否在运行
     *
     * @param context
     * @param processName
     * @return
     */
    fun isProcessRunning(context: Context, processName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (processName == appProcess.processName) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    fun isApplicationBroughtToBackground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            return topActivity!!.packageName != context.packageName
        }
        return false
    }

    /**
     * 是否在后台
     *
     * @param context
     * @return
     */
    fun isBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                //                    CmLog.i("后台", appProcess.processName);
//                    CmLog.i("前台", appProcess.processName);
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND
            }
        }
        return false
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.applicationContext
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = context.applicationContext.packageName
        /**
         * 获取Android设备中所有正在运行的App
         */
        /**
         * 获取Android设备中所有正在运行的App
         */
        val appProcesses = activityManager
            .runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    /**
     * 获取当前手机状态
     *
     * @param context
     * @return
     */
    fun getDeviceStatus(context: Context): Int {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isScreenOn
        val mKeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val flag = mKeyguardManager.inKeyguardRestrictedInputMode()
        return if (isScreenOn) {
            if (!flag) {
                1 //未锁屏
            } else {
                2 //锁屏
            }
        } else {
            //黑屏
            3
        }
    }

    /**
     * 点亮屏幕
     *
     * @param context
     */
    fun accquireScreenOn(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //获取电源管理器对象
        @SuppressLint("InvalidWakeLockTag") val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        wl.acquire()
        //点亮屏幕
        //重新启用自动加锁
        wl.release()
    }

    /**
     * 获得系统亮度
     */
    fun getSystemBrightness(context: Context): Int {
        var systemBrightness = 0
        try {
            systemBrightness =
                Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: SettingNotFoundException) {
            e.printStackTrace()
        }
        return systemBrightness
    }

    /**
     * 解锁
     *
     * @param context
     */
    fun accquireKeyguardLock(context: Context) {
        //得到键盘锁管理器对象
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        //参数是LogCat里用的Tag
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()

        //自动锁
        kl.reenableKeyguard()
    }

    /**
     * 隐藏输入法
     */
    fun showKeyBord(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 2)
    }

    /**
     * 开关输入法
     */
    fun hideKeyBord(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!activity.isFinishing && imm != null && activity.currentFocus != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }
}