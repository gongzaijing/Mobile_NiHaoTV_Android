package com.china.cibn.bangtvmobile.bangtv.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2018/3/22.
 */

public class Utils {
    public static String getEthernetMac() {
        // return "C8:0E:77:30:77:62";
        String macAddr = null;
        macAddr = _getLocalEthernetMacAddress();
        if (macAddr == null)
            macAddr = getMac();
        if (TextUtils.isEmpty(macAddr)) {
            macAddr = _getEthMacAddress2();
            if (macAddr != null && macAddr.startsWith("0:")) {
                macAddr = "0" + macAddr;
            }
        }
//		  return "C8:0E:77:30:77:62";
        return macAddr;
//		 return "00:00:77:30:77:62";
    }
    /**
     * 获取当前系统连接网络的网卡的mac地址
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static String getMac() {
        byte[] mac = null;
        StringBuffer sb = new StringBuffer();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();

                while (address.hasMoreElements()) {
                    InetAddress ip = address.nextElement();
                    if (ip.isAnyLocalAddress() || !(ip instanceof Inet4Address)
                            || ip.isLoopbackAddress())
                        continue;
                    if (ip.isSiteLocalAddress())
                        mac = ni.getHardwareAddress();
                    else if (!ip.isLinkLocalAddress()) {
                        mac = ni.getHardwareAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                sb.append(parseByte(mac[i]));
            }
            return sb.substring(0, sb.length() - 1);
        }

        return null;
    }
    // 获取当前连接网络的网卡的mac地址
    private static String parseByte(byte b) {
        String s = "00" + Integer.toHexString(b) + ":";
        return s.substring(s.length() - 3);
    }
    /*
     * PRIVATE METHODS
     */
    @SuppressLint("NewApi")
    private static String _getLocalEthernetMacAddress() {
        String mac = null;
        try {
            Enumeration localEnumeration = NetworkInterface
                    .getNetworkInterfaces();

            while (localEnumeration.hasMoreElements()) {
                NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration
                        .nextElement();
                String interfaceName = localNetworkInterface.getDisplayName();

                if (interfaceName == null) {
                    continue;
                }

                if (interfaceName.equals("eth0")) {
                    mac = _convertToMac(localNetworkInterface
                            .getHardwareAddress());
                    if (mac != null && mac.startsWith("0:")) {
                        mac = "0" + mac;
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return mac;
    }
    private static String _convertToMac(byte[] mac) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            byte b = mac[i];
            int value = 0;
            if (b >= 0 && b < 16) {// Jerry(2013-11-6): if (b>=0&&b<=16) => if
                // (b>=0&&b<16)
                value = b;
                sb.append("0" + Integer.toHexString(value));
            } else if (b >= 16) {// Jerry(2013-11-6): else if (b>16) => else if
                // (b>=16)
                value = b;
                sb.append(Integer.toHexString(value));
            } else {
                value = 256 + b;
                sb.append(Integer.toHexString(value));
            }
            if (i != mac.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    private static String _getEthMacAddress2() {
        String mac = _loadFileAsString("/sys/class/net/eth0/address");
        if (mac == null) {
            mac = "";
        } else {
            mac = mac.toUpperCase();
            if (mac.length() > 17) {
                mac = mac.substring(0, 17);
            }
        }

        return mac;
    }

    private static String _loadFileAsString(String filePath) {
        try {
            if (new File(filePath).exists()) {
                StringBuffer fileData = new StringBuffer(1000);
                BufferedReader reader = new BufferedReader(new FileReader(
                        filePath));
                char[] buf = new char[1024];
                int numRead = 0;
                while ((numRead = reader.read(buf)) != -1) {
                    String readData = String.valueOf(buf, 0, numRead);
                    fileData.append(readData);
                }
                reader.close();
                return fileData.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * (设备id + sim串号 + androidID号 + CPU串号) MD5处理
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String currentDeviceMark(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String serial = "";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            serial = Build.SERIAL;
        }
        String m_szLongID = tmDevice + tmSerial + androidId + serial
                + m_szDevIDShortMaker();
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        return m_szUniqueID = m_szUniqueID.toUpperCase();

    }

    public static String m_szDevIDShortMaker() {
        String m_szDevIDShort = "35";

        m_szDevIDShort += Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                + Build.USER.length() % 10 + "";

        return m_szDevIDShort;
    }
}
