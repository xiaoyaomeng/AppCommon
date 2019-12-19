package com.panghu.appcommon.utils;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * PhCpuUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @date 2019年12月19日
 *
 **/
public class PhCpuUtil {

    private static final String TAG = PhCpuUtil.class.getSimpleName();
    private static final String CPU_INFO_PATH = "/proc/cpuinfo";
    private static final String CPU_FREQ_NULL = "N/A";
    private static final String CMD_CAT = "/system/bin/cat";
    private static final String CPU_FREQ_CUR_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
    private static final String CPU_FREQ_MAX_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
    private static final String CPU_FREQ_MIN_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";

    private static String CPU_NAME;
    private static int CPU_CORES = 0;
    private static long CPU_MAX_FREQENCY = 0;
    private static long CPU_MIN_FREQENCY = 0;

    /**
     * 打印CPU信息 "/proc/cpuinfo"
     * @return
     */
    public static String printCpuInfo() {
        String info = PhFileUtil.readFile(CPU_INFO_PATH);
        PhLogUtil.i(TAG, "_______  CPU :   \n" + info);
        return info;
    }

    /**
     * 获取CPU可支持的进程数
     * @return
     */
    public static int getProcessorsCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取Cpu 核数
     *
     * "/sys/devices/system/cpu"
     *
     * @return
     */
    public static int getCpuCoresNumber() {
        if (CPU_CORES != 0) {
            return CPU_CORES;
        }
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            CPU_CORES = files.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CPU_CORES < 1) {
            CPU_CORES = Runtime.getRuntime().availableProcessors();
        }
        if (CPU_CORES < 1) {
            CPU_CORES = 1;
        }
        return CPU_CORES;
    }

    /**
     * 获取CPU名
     * @return
     */
    public static String getCpuName() {
        if (!PhStringUtil.isEmpty(CPU_NAME)) {
            return CPU_NAME;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(CPU_INFO_PATH), 8192);
            String line = bufferedReader.readLine();
            bufferedReader.close();
            String[] array = line.split(":\\s+", 2);
            if (array.length > 1) {
                PhLogUtil.i(TAG, array[1]);
                CPU_NAME = array[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CPU_NAME;
    }

    /**
     * 获取CPU当前频率
     * @return
     */
    public static long getCurrentFreqency() {
        try {
            return Long.parseLong(PhFileUtil.readFile(CPU_FREQ_CUR_PATH).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取CPU最小频率
     * @return
     */
    public static long getMinFreqency() {
        if (CPU_MIN_FREQENCY > 0) {
            return CPU_MIN_FREQENCY;
        }
        try {
            CPU_MIN_FREQENCY = Long.parseLong(getCMDOutputString(new String[]{CMD_CAT, CPU_FREQ_MIN_PATH}).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CPU_MIN_FREQENCY;
    }

    /**
     * 获取CPU最大频率
     * @return
     */
    public static long getMaxFreqency() {
        if (CPU_MAX_FREQENCY > 0) {
            return CPU_MAX_FREQENCY;
        }
        try {
            CPU_MAX_FREQENCY = Long.parseLong(getCMDOutputString(new String[]{CMD_CAT, CPU_FREQ_MAX_PATH}).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CPU_MAX_FREQENCY;
    }

    /**
     * Get command output string.
     */
    public static String getCMDOutputString(String[] args) {
        try {
            ProcessBuilder cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            StringBuilder sb = new StringBuilder();
            byte[] re = new byte[64];
            int len;
            while ((len = in.read(re)) != -1) {
                sb.append(new String(re, 0, len));
            }
            in.close();
            process.destroy();
            PhLogUtil.i(TAG, "CMD: " + sb.toString());
            return sb.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static final String ABI_X86 = "x86";

    public static final String ABI_MIPS = "mips";

    public static enum ARCH {
        Unknown, ARM, X86, MIPS, ARM64,
    }

    private static ARCH sArch = ARCH.Unknown;

    // see include/​uapi/​linux/​elf-em.h
    private static final int EM_ARM = 40;
    private static final int EM_386 = 3;
    private static final int EM_MIPS = 8;
    private static final int EM_AARCH64 = 183;

    // /system/lib/libc.so
    // XXX: need a runtime check
    public static synchronized ARCH getMyCpuArch() {
        byte[] data = new byte[20];
        File libc = new File(Environment.getRootDirectory(), "lib/libc.so");
        if (libc.canRead()) {
            RandomAccessFile fp = null;
            try {
                fp = new RandomAccessFile(libc, "r");
                fp.readFully(data);
                int machine = (data[19] << 8) | data[18];
                switch (machine) {
                    case EM_ARM:
                        sArch = ARCH.ARM;
                        break;
                    case EM_386:
                        sArch = ARCH.X86;
                        break;
                    case EM_MIPS:
                        sArch = ARCH.MIPS;
                        break;
                    case EM_AARCH64:
                        sArch = ARCH.ARM64;
                        break;
                    default:
                        Log.e("NativeBitmapFactory", "libc.so is unknown arch: " + Integer.toHexString(machine));
                        break;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fp != null) {
                    try {
                        fp.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sArch;
    }

    public static String get_CPU_ABI() {
        return Build.CPU_ABI;
    }

    public static String get_CPU_ABI2() {
        try {
            Field field = Build.class.getDeclaredField("CPU_ABI2");
            if (field == null)
                return null;

            Object fieldValue = field.get(null);
            if (!(fieldValue instanceof String)) {
                return null;
            }

            return (String) fieldValue;
        } catch (Exception e) {

        }

        return null;
    }

    public static boolean supportABI(String requestAbi) {
        String abi = get_CPU_ABI();
        if (!TextUtils.isEmpty(abi) && abi.equalsIgnoreCase(requestAbi))
            return true;

        String abi2 = get_CPU_ABI2();
        return !TextUtils.isEmpty(abi2) && abi.equalsIgnoreCase(requestAbi);

    }

    public static boolean supportX86() {
        return supportABI(ABI_X86);
    }

    public static boolean supportMips() {
        return supportABI(ABI_MIPS);
    }

    public static boolean isARMSimulatedByX86() {
        ARCH arch = getMyCpuArch();
        return !supportX86() && ARCH.X86.equals(arch);
    }

    public static boolean isMiBox2Device() {
        String manufacturer = Build.MANUFACTURER;
        String productName = Build.PRODUCT;
        return manufacturer.equalsIgnoreCase("Xiaomi")
                && productName.equalsIgnoreCase("dredd");
    }

    public static boolean isMagicBoxDevice() {
        String manufacturer = Build.MANUFACTURER;
        String productName = Build.PRODUCT;
        return manufacturer.equalsIgnoreCase("MagicBox")
                && productName.equalsIgnoreCase("MagicBox");
    }

    public static boolean isProblemBoxDevice() {
        return isMiBox2Device() || isMagicBoxDevice();
    }

    public static boolean isRealARMArch() {
        ARCH arch = getMyCpuArch();
        return (supportABI("armeabi-v7a") || supportABI("armeabi")) && ARCH.ARM.equals(arch);
    }

    public static boolean isRealX86Arch() {
        ARCH arch = getMyCpuArch();
        return supportABI(ABI_X86) || ARCH.X86.equals(arch);
    }
}
