package com.panghu.appcommon.utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.File;
import java.lang.reflect.Method;
/**
 * PhNetWorkStatusUtils
 * 网络情况获取类
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhNetWorkStatusUtils {

    /**
     * 中国移动cmwap
     */
    public static final String CMWAP = "cmwap";
    /**
     * 中国移动cmnet
     */
    public static final String CMNET = "cmnet";
    /**
     * 3G wap 中国联通3gwap APN
     */
    public static final String GWAP_3 = "3gwap";
    /**
     * 3G net 中国联通3gnet APN
     */
    public static final String GNET_3 = "3gnet";
    /**
     * uni wap 中国联通uni wap APN
     */
    public static final String UNIWAP = "uniwap";
    /**
     * uni net 中国联通uni net APN
     */
    public static final String UNINET = "uninet";
    /**
     * 中国电信 ctwap
     */
    public static final String CTWAP = "ctwap";
    /**
     * 中国电信 ctnet
     */
    public static final String CTNET = "ctnet";

    // 中国联通3GWAP设置 中国联通3G因特网设置 中国联通WAP设置 中国联通因特网设置
    // 3gwap 3gnet uniwap uninet
    public static final String CTC = "CTC";
    private static final int BLUETOOTH = 4;
    private static final int BRIGHTNESS = 1;
    private static final int GPS = 3;
    private static final int SYNC = 2;
    private static final int WIFI = 0;
    private static final String TAG = "NetWorkStatusUtil";

    /**
     * Private constructor to forbid instantiation.
     */
    private PhNetWorkStatusUtils() {
    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     * 是否有网络
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean getNetworkConnectionStatus(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) {
                //Log.w(TAG, "getNetworkConnectionStatus,1,false");
                return false;
            }

            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null) {
                //Log.w(TAG, "getNetworkConnectionStatus,2,false");
                return false;
            }

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm == null) {
                //Log.w(TAG, "getNetworkConnectionStatus,3,false");
                return false;
            }

            if ((tm.getDataState() == TelephonyManager.DATA_CONNECTED || tm.getDataState() == TelephonyManager.DATA_ACTIVITY_NONE) && info != null) {
                //Log.w(TAG, "getNetworkConnectionStatus,true");
                return true;
            } else {
                //Log.w(TAG, "getNetworkConnectionStatus,false");
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return 如果是中国联通uninet网络返回true，否则返回false
     */
    @SuppressLint("MissingPermission")
    public static boolean isUNINet(Context context) {

        ConnectivityManager conManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(UNINET) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return 如果是中国联通uniwap网络返回true，否则返回false
     */
    @SuppressLint("MissingPermission")
    public static boolean isUNIWap(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(UNIWAP) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return 如果是中国移动cmwap网络返回true，否则返回false
     */
    @SuppressLint("MissingPermission")
    public static boolean isCMWap(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(CMWAP) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return 如果是中国联通3gwap网络返回true，否则返回false
     */
    @SuppressLint("MissingPermission")
    public static boolean is3GWAP(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(GWAP_3) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * need permission : <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return 如果是中国联通3gnet网络返回true，否则返回false
     */
    @SuppressLint("MissingPermission")
    public static boolean is3GNet(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(GNET_3) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    public static boolean is2G(Context context) {
        if (getNetType(context.getApplicationContext()) == NETWORK_CLASS_2_G) {
            return true;
        }
        return false;
    }

    public static boolean is3G(Context context) {
        if (getNetType(context.getApplicationContext()) == NETWORK_CLASS_3_G) {
            return true;
        }
        return false;
    }

    public static boolean is4G(Context context) {
        if (getNetType(context.getApplicationContext()) == NETWORK_CLASS_4_G) {
            return true;
        }
        return false;
    }


    /**
     * @param context
     * @return 如果是中国联通cmnet网络返回true，否则返回false
     */
    @SuppressLint("MissingPermission")
    public static boolean isCMNet(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(CMNET) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    @SuppressLint("MissingPermission")
    public static boolean isCTNet(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null) {
                if (!extraInfo.equals(CTNET) ? true : false) {
                    return (extraInfo.equals(CTC) ? true : false);
                }
            } else {
                return false;
            }

        } else {
            return false;
        }
        return false;

    }

    @SuppressLint("MissingPermission")
    public static boolean isCTWap(Context context) {

        ConnectivityManager conManager = (ConnectivityManager) context.

                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null) {
            if (ni.getTypeName().compareToIgnoreCase("MOBILE") != 0) {
                return false;
            }
            String extraInfo = ni.getExtraInfo();
            if (extraInfo != null)
                return (extraInfo.equals(CTWAP) ? true : false);
            else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * 是否WIFI网络
     *
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifi(final Context ctx) {
        ConnectivityManager conManager = (ConnectivityManager) ctx.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = conManager.getActiveNetworkInfo();

        if (ni != null && ni.getTypeName() != null) {
            if (ni.getTypeName().compareToIgnoreCase("WIFI") != 0) {
                return false;
            }
            return true;

        } else {
            return false;
        }
    }


    /**
     * Checks if is umts.
     *
     * @param ctx the ctx
     * @return true, if is umts
     */
    public static boolean isUmts(final Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType() >= TelephonyManager.NETWORK_TYPE_UMTS;
    }

    /**
     * Checks if is edge.
     *
     * @param ctx the ctx
     * @return true, if is edge
     */
    public static boolean isEdge(final Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE;
    }

    /**
     * 检查是否有网络(支持连接的网络)
     * @param context
     * @return
     */
    public static boolean hasNetWork(Context context) {
        try {
            boolean bConnection = PhNetWorkStatusUtils.getNetworkConnectionStatus(context);
            if (!bConnection) {
                return false;
            }
        } catch (Exception e) {
            return true;
        }

        return true;
    }

    public static boolean isSupportNetWork(final Context ctx) {
        try {
            boolean bCMWap = PhNetWorkStatusUtils.isCMWap(ctx);
            boolean b3GWAP = PhNetWorkStatusUtils.is3GWAP(ctx);
            boolean bCTWap = PhNetWorkStatusUtils.isCTWap(ctx);
            boolean bUNIWap = PhNetWorkStatusUtils.isUNIWap(ctx);
            if (b3GWAP || bCMWap || bCTWap || bUNIWap) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 是否处于飞行模式
     *
     * @param context
     * @return
     */
    public static boolean isAirplaneMode(Context context) {
        int modeIdx = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        if (modeIdx == 1)
            return true;
        else
            return false;
    }

    /**
     * 设置飞行模式
     *
     * @param bOpen
     */
    public static void setAirplaneMode(Context context, boolean bOpen) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, bOpen ? 1 : 0);
        // 广播飞行模式信号的改变，让相应的程序可以处理。
        // 不发送广播时，在非飞行模式下，Android 2.2.1上测试关闭了Wifi,不关闭正常的通话网络(如GMS/GPRS等)。
        // 不发送广播时，在飞行模式下，Android 2.2.1上测试无法关闭飞行模式。
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        context.sendBroadcast(intent);
    }

    /**
     * 获取运营商名字
     */
    public static String getOperatorName(Context context) {
        String ProvidersName = null;
        try {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            /**
             * 返回移动终端
             *
             * SIM_STATE_ABSENT SIM卡未找到
             * SIM_STATE_NETWORK_LOCKED SIM卡网络被锁定，需要Network PIN解锁
             * SIM_STATE_PIN_REQUIRED SIM卡PIN被锁定，需要User PIN解锁
             * SIM_STATE_PUK_REQUIRED SIM卡PUK被锁定，需要User PUK解锁
             * SIM_STATE_READY SIM卡可用
             * SIM_STATE_UNKNOWN SIM卡未知
             */
            String operator = telephonyManager.getSimOperator();
            if (operator != null) {
                if (!operator.equals("")) {
                    if (operator.equals("46000") || operator.equals("46002") || operator.equals("46004")
                            || operator.equals("46007") || operator.equals("46008") || operator.equals("46013")) {
                        ProvidersName = "中国移动";
                    } else if (operator.equals("46001") || operator.equals("46006") || operator.equals("46009")
                            || operator.equals("46010")) {
                        //中国联通
                        ProvidersName = "中国联通";
                    } else if (operator.equals("46003") || operator.equals("46011") || operator.equals("46012")) {
                        //中国电信
                        ProvidersName = "中国电信";
                    }
                } else {
                    ProvidersName = "";
                }

            } else {
                ProvidersName = "";
            }

            if (ProvidersName == null || ProvidersName.equals("")) {
                ////Log.e("运营商==============", "未找到SIM卡........");
            } else {
                // //Log.e("运营商==============", ProvidersName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ProvidersName;

    }

    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;


    /**
     * Unknown network class. {@hide}
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_4_G = 3;
    public static final int NETWORK_CLASS_WIFI = 4;


    private static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    /**
     * 2G 1 3G 2 4G 3 WIFI 4
     * <p/>
     * <p/>
     * int getNetworkType()
     * 常用的有这几个：
     * int NETWORK_TYPE_CDMA 网络类型为CDMA
     * int NETWORK_TYPE_EDGE 网络类型为EDGE
     * int NETWORK_TYPE_EVDO_0 网络类型为EVDO0
     * int NETWORK_TYPE_EVDO_A 网络类型为EVDOA
     * int NETWORK_TYPE_GPRS 网络类型为GPRS
     * int NETWORK_TYPE_HSDPA 网络类型为HSDPA
     * int NETWORK_TYPE_HSPA 网络类型为HSPA
     * int NETWORK_TYPE_HSUPA 网络类型为HSUPA
     * int NETWORK_TYPE_UMTS 网络类型为UMTS
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     * 获取网络信号类型
     */
    public static int getNetType(Context context) {

        try {
            if (isWifi(context)) {
                ////Log.e("网络类型:", "wifi");
                return NETWORK_CLASS_WIFI;
            } else {
                TelephonyManager telmng = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                /**
                 * 获取数据连接状态
                 *
                 * DATA_CONNECTED 数据连接状态：已连接
                 * DATA_CONNECTING 数据连接状态：正在连接
                 * DATA_DISCONNECTED 数据连接状态：断开
                 * DATA_SUSPENDED 数据连接状态：暂停
                 */
//                int dataState = telmng.getDataState();
//				if(dataState != TelephonyManager.DATA_DISCONNECTED){
                //数据连接无断开的情况
                switch (telmng.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        //Log.e("网络类型:", "2G");
                        return NETWORK_CLASS_2_G;

                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        //Log.e("网络类型:", "3G");
                        return NETWORK_CLASS_3_G;
                    case NETWORK_TYPE_LTE:
                        //Log.e("网络类型:", "4G");
                        return NETWORK_CLASS_4_G;
                    default:
                        //Log.e("网络类型:", "无网络连接...");
                        return NETWORK_CLASS_UNKNOWN;
                }

//				}else {
//					//Log.e("网络类型:", "无网络连接...");
//					return NETWORK_CLASS_UNKNOWN;
//				}

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return NETWORK_CLASS_UNKNOWN;
    }


    /**
     * 获取网络类型
     * <p/>
     * 获取网络类型：int getNetworkType()
     * 常用的有这几个：
     * int NETWORK_TYPE_CDMA 网络类型为CDMA
     * int NETWORK_TYPE_EDGE 网络类型为EDGE
     * int NETWORK_TYPE_EVDO_0 网络类型为EVDO0
     * int NETWORK_TYPE_EVDO_A 网络类型为EVDOA
     * int NETWORK_TYPE_GPRS 网络类型为GPRS
     * int NETWORK_TYPE_HSDPA 网络类型为HSDPA
     * int NETWORK_TYPE_HSPA 网络类型为HSPA
     * int NETWORK_TYPE_HSUPA 网络类型为HSUPA
     * int NETWORK_TYPE_UMTS 网络类型为UMTS
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getNetworkType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isAvailable() && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    if (info.getTypeName().equals("WIFI")) {
                        return "wifi";
                    } else {
                        TelephonyManager telmng = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        if (telmng.getDataState() == TelephonyManager.DATA_CONNECTED) {
                            //LogUtils.d("net work type: " + telmng.getNetworkType());
                            switch (telmng.getNetworkType()) {
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                    return "gprs";

                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                    return "edge";


                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                    return "cdma";


                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                    return "evdo";

                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                    return "umts";


                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                    return "hsdpa";


                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                    return "hspa";

                            }
                        }
                    }
                }
            }


            Cursor mCursor = context.getContentResolver()
                    .query(Uri.parse("content://telephony/carriers"), new String[]{"name"}, "current=1", null, null);
            if (mCursor != null && mCursor.moveToFirst()) {
                String name = mCursor.getString(0);
                if (name != null) {
                    if (name.equalsIgnoreCase("cmnet"))
                        return "cmnet";
                    else if (name.equalsIgnoreCase("cmwap"))
                        return "cmwap";
                }
            }
            if (null != mCursor && !mCursor.isClosed()) {
                mCursor.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "exception_network_type";
    }

    public static int getNetTypeDes(Context context) {

        try {
            if (isWifi(context)) {
                //Log.e("网络类型:", "wifi");
                return 1;
            } else {
                TelephonyManager telmng = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                /**
                 * 获取数据连接状态
                 *
                 * DATA_CONNECTED 数据连接状态：已连接
                 * DATA_CONNECTING 数据连接状态：正在连接
                 * DATA_DISCONNECTED 数据连接状态：断开
                 * DATA_SUSPENDED 数据连接状态：暂停
                 */
//                int dataState = telmng.getDataState();
//				if(dataState != TelephonyManager.DATA_DISCONNECTED){
                //数据连接无断开的情况
                switch (telmng.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        //Log.e("网络类型:", "2G");
                        return 3;

                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        //Log.e("网络类型:", "3G");
                        return 4;
                    case NETWORK_TYPE_LTE:
                        //Log.e("网络类型:", "4G");
                        return 5;
                    default:
                        //Log.e("网络类型:", "无网络连接...");
                        return 2;
                }

//				}else {
//					//Log.e("网络类型:", "无网络连接...");
//					return NETWORK_CLASS_UNKNOWN;
//				}

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 2;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {
        boolean root = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
        }
        return root;
    }
}
