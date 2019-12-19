package com.panghu.appcommon.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.webkit.WebView;

import com.panghu.appcommon.utils.io.FileUtils;
import com.panghu.appcommon.utils.temp.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

/**
 * PhDeviceUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhDeviceUtil {


    private static final String TAG = PhDeviceUtil.class.getSimpleName();

    //Ethernet Mac Address
    private static final String ETH0_MAC_ADDRESS = "/sys/class/net/eth0/address";

    private static String mWebViewUA;

    /**
     * 获取WebView的User Agent信息
     *
     * @param context
     * @return
     */
    public static String getUserAgent(Context context) {
        if (StringUtils.isEmpty(mWebViewUA)) {
            final String system_ua = System.getProperty("http.agent");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mWebViewUA = new WebView(context).getSettings().getDefaultUserAgent(context)
                        + (!StringUtils.isEmpty(system_ua) ? "__" + system_ua : "");
                return mWebViewUA;
            } else {
                mWebViewUA = new WebView(context).getSettings().getUserAgentString()
                        + (!StringUtils.isEmpty(system_ua) ? "__" + system_ua : "");
                return mWebViewUA;
            }
        }
        return mWebViewUA;
    }

    /**
     * 获取 开机时间
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        PhLogUtil.i(TAG, h + ":" + m);
        return h + ":" + m;
    }


    private static  String mUUID;

    public synchronized static String getUUID(Context context) {
        if(mUUID==null){
            SharedPreferences preference = PreferenceManager
                    .getDefaultSharedPreferences(context);
            String identity = preference.getString("identity_android", null);
            if (identity == null) {
                identity = UUID.randomUUID().toString();
                preference.edit().putString("identity_android", identity).apply();
            }
            mUUID = identity;
        }
        return mUUID;

    }


    private static int phoneType;
    public static int getPhoneType(Context context) {
        try {
            if(phoneType==0){
                TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                phoneType = telephony.getPhoneType();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return phoneType;
    }

    /**
     * 获取手机型号
     * @return
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static String getSystemVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    public static String getHardware() {
        return Build.HARDWARE;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getRadioVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ? Build.getRadioVersion() : "";
    }


    public static String getSerial() {
        return Build.SERIAL;
    }

    /**
     * need permission : <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * 获取Sim序列号
     *
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getSIMSerial(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    public static String getMNC(Context ctx) {
        String providersName = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            providersName = telephonyManager.getSimOperator();
            providersName = providersName == null ? "" : providersName;
        }
        return providersName;
    }

    /**
     * 获取运营商名
     * @param ctx
     * @return
     */
    public static String getCarrier(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName().toLowerCase(Locale.getDefault());
    }


    public static String getModel() {
        return Build.MODEL;
    }

    public static String getBuildBrand() {
        return Build.BRAND;
    }

    public static String getBuildHost() {
        return Build.HOST;
    }

    public static String getBuildTags() {
        return Build.TAGS;
    }

    public static long getBuildTime() {
        return Build.TIME;
    }

    public static String getBuildUser() {
        return Build.USER;
    }

    public static String getBuildVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    public static String getBuildVersionCodename() {
        return Build.VERSION.CODENAME;
    }

    public static String getBuildVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    public static int getBuildVersionSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    public static String getBuildID() {
        return Build.ID;
    }

    public static String[] getSupportedABIS() {
        String[] result = new String[]{"-"};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = Build.SUPPORTED_ABIS;
        }
        if (result == null || result.length == 0) {
            result = new String[]{"-"};
        }
        return result;
    }

    /**
     * 获取厂商信息
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }


    public static String getBootloader() {
        return Build.BOOTLOADER;
    }

    public static String getScreenDisplayID(Context ctx) {
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        return String.valueOf(wm.getDefaultDisplay().getDisplayId());
    }

    public static String getDisplayVersion() {
        return Build.DISPLAY;
    }

    /**
     * need permission : <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * 获取手机IMEI
     * @param context
     * @return
     *
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        String id = "";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId().toLowerCase();
        } catch (Exception e) {
            //throw new UniqueException(e);
        }
        return "";
    }

    private static String mIMSI;

    /**
     * need permission : <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * 获取手机IMEI
     * @param mContext
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getImsi(Context mContext) {
        if(mIMSI == null){
            TelephonyManager mTelephonyManager = ((TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE));
            String IMSI = "";
            try {
                IMSI = mTelephonyManager.getSubscriberId();
            } catch (Exception localException) {

            }
            mIMSI = IMSI;
        }
        return mIMSI;
    }


    /**
     * 获取 ANDROID_ID
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        PhLogUtil.i(TAG, "ANDROID_ID ：" + androidId);
        return androidId;
    }


    /**
     * 获取 Wifi MAC 地址
     * need permission : <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     */
    public static String getMacAddress(Context context) {
        return getWifiMacAddress(context);
    }

    /**
     *
     * need permission : <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * 获取 Wifi MAC 地址
     *
     * @param context
     * @return
     */
    public static String getWifiMacAddress(Context context) {
        //wifi mac地址
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        PhLogUtil.i(TAG, "WIFI MAC：" + mac);
        return mac;
    }

    /**
     * 获取以太网Mac地址
     * @return
     */
    public static String getEthernetMacAddress() {
        try {
            String mac = FileUtils.readFileToString(new File(ETH0_MAC_ADDRESS));
            PhLogUtil.i(TAG, "Ethernet MAC：" + mac);
            return mac;
        } catch (IOException e) {
            PhLogUtil.e(TAG, "IO Exception when getting eth0 mac address", e);
            e.printStackTrace();
            return "unknown";
        }
    }

    /**
     * need to : <uses-permission android:name="android.permission.BLUETOOTH"/>
     * 获取蓝牙地址
     *
     * @param context
     * @return
     */
    @SuppressWarnings("MissingPermission")
    public static String getBluetoothMAC(Context context) {
        String result = null;
        try {
            BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
            result = bta.getAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * @param ctx
     * @return
     */
    public static String getIp(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled() ? getWifiIP(wifiManager) : getGPRSIP();
    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * @param wifiManager
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getWifiIP(WifiManager wifiManager) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ip = intToIp(wifiInfo.getIpAddress());
        return ip != null ? ip : "";
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    public static String getGPRSIP() {
        String ip = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                for (Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            ip = null;
        }
        return ip;
    }

    /**
     * 获取当前语言信息
     *
     * @return
     */
    public static String getLanguage(Context context) {
        String language = context.getResources().getConfiguration().locale.getLanguage();
        if (PhStringUtil.isEmpty(language)){
            return Locale.getDefault().getLanguage();
        }
        return language;
    }

    /**
     * 获取当前城市信息
     *
     * @param context
     * @return
     */
    public static String getCountry(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Locale locale = Locale.getDefault();
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY
                ? tm.getSimCountryIso().toLowerCase(Locale.getDefault())
                : locale.getCountry().toLowerCase(locale);
    }

    /**
     * 打印设备信息
     *
     * @return
     */
    public static String printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID);
        sb.append("\nBRAND              :").append(Build.BRAND);
        sb.append("\nMODEL              :").append(Build.MODEL);
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                :").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD);
        sb.append("\nPRODUCT            :").append(Build.PRODUCT);
        sb.append("\nDEVICE             :").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT);
        sb.append("\nHOST               :").append(Build.HOST);
        sb.append("\nTAGS               :").append(Build.TAGS);
        sb.append("\nTYPE               :").append(Build.TYPE);
        sb.append("\nTIME               :").append(Build.TIME);
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           :").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN);
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sb.append("\nSERIAL             :").append(Build.SERIAL);
        }
        PhLogUtil.i(TAG, sb.toString());
        return sb.toString();
    }

}
