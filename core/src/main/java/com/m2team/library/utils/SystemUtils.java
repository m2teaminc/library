package com.m2team.library.utils;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;


@SuppressWarnings("deprecation")
public class SystemUtils {


	public static final int DEFAULT_THREAD_POOL_SIZE = getSysDefaultThreadPoolSize();
	
	
	 public static String getSysClientOs() {
        String OsName = Build.ID;
        return OsName;
    }
	
	public static String getSysSdk() {
	    String sdkVersion = Build.VERSION.SDK;
	    AppLogMessageMgr.i("AppSysMgr-->>getSysLanguage", sdkVersion);
	    return sdkVersion;
	}
	
	public static String getSysLanguage() {
		String language = Locale.getDefault().getLanguage();
		AppLogMessageMgr.i("AppSysMgr-->>getSysLanguage",  language);
		return language;
	}

	
	public static String getSysModel() {
		String model = Build.MODEL;
		AppLogMessageMgr.i("AppSysMgr-->>getSysModel",  model);
		return model;
	}

	
	public static String getSysRelease() {
		String release = Build.VERSION.RELEASE;
		AppLogMessageMgr.i("AppSysMgr-->>getSysRelease",  release);
		return release;
	}

	
	public static String getSysSIMSerialNum(Context content) {
		String simSerialNumber = getSysTelephonyManager(content).getSimSerialNumber();
		AppLogMessageMgr.i("AppSysMgr-->>getSysSIMSerialNum",  simSerialNumber);
		return simSerialNumber;
	}


    public static String getSysCPUSerialNum() {
        String str = "", strCPU = "", cpuSerialNum = "0000000000000000";
        try {
            //??CPU??
            Process pp = Runtime.getRuntime().exec("cat/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            //??CPU???
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    //?????????
                    if (str.indexOf("Serial") > -1) {
                        //?????
                        strCPU = str.substring(str.indexOf(":") + 1,
                        str.length());
                        //???
                        cpuSerialNum = strCPU.trim();
                        break;
                    }
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppLogMessageMgr.e("AppSysMgr-->>getSysCPUSerialNum",  e.getMessage().toString());
        }
        	return cpuSerialNum;
    }
	

	private static TelephonyManager getSysTelephonyManager(Context content) {
		TelephonyManager telephonyManager = null;
		telephonyManager = (TelephonyManager) content.getSystemService(Context.TELEPHONY_SERVICE);
		AppLogMessageMgr.i("AppSysMgr-->>getSysTelephonyManager",  telephonyManager + "");
		return telephonyManager;
	}



	public static String getSysTelephoneSerialNum(Context content) {
		String deviceId = getSysTelephonyManager(content).getDeviceId();
		AppLogMessageMgr.i("AppSysMgr-->>getSysTelephoneSerialNum",  deviceId + "");
		return deviceId;
	}



	public static String getSysCarrier(Context content) {
		String moblieType = "";
		TelephonyManager telephonyManager = (TelephonyManager) content.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telephonyManager.getSubscriberId();
		if (imsi != null && imsi.length() > 0) {
			//????????46000??IMSI????????????46002???134/159????????
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				//????
				moblieType = "China Mobile";
			} else if (imsi.startsWith("46001")) {
				//????
				moblieType = "China Unicom";
			} else if (imsi.startsWith("46003")) {
				//????
				moblieType = "China Telecom";
			}
		}
		AppLogMessageMgr.i("AppSysMgr-->>getSysCarrier",  moblieType);
		return moblieType;
	}


	public static Integer getSysPhoneState(Context context) {
		Integer callState = getSysTelephonyManager(context).getCallState();
		AppLogMessageMgr.i("AppSysMgr-->>getSysPhoneState",  callState + "");
		return callState;
	}

	
	public static CellLocation getSysPhoneLoaction(Context context) {
		CellLocation cellLocation = getSysTelephonyManager(context).getCellLocation();
		AppLogMessageMgr.i("AppSysMgr-->>getSysPhoneLoaction",  cellLocation + "");
		return cellLocation;
	}

	
	public static String getSysDeviceSoftVersion(Context context) {
		String deviceSoftwareVersion = getSysTelephonyManager(context).getDeviceSoftwareVersion();
		AppLogMessageMgr.i("AppSysMgr-->>getSysDeviceSoftVersion",  deviceSoftwareVersion + "");
		return deviceSoftwareVersion;
	}


	public static String getSysPhoneNumber(Context context) {
		String phoneNumber = getSysTelephonyManager(context).getLine1Number();
		AppLogMessageMgr.i("AppSysMgr-->>getSysPhoneNumber",  phoneNumber + "");
		return phoneNumber;
	}

	
	public static String getSysSimCode(Context context) {
		String code = "";
		if (getSysTelephonyManager(context).getSimState() == 5) {
			code = getSysTelephonyManager(context).getSimOperator();
		}
		AppLogMessageMgr.i("AppSysMgr-->>getSysSimCode",  code + "");
		return code;
	}


	public static String getSysSimPrivatorName(Context context) {
		String simOperatorName = "";
		if (getSysTelephonyManager(context).getSimState() == 5) {
			simOperatorName = getSysTelephonyManager(context).getSimOperatorName();
		}
		AppLogMessageMgr.i("AppSysMgr-->>getSysSimPrivatorName",  simOperatorName);
		return simOperatorName;
	}



	public static String getSysUserPhoneId(Context context) {
		String subscriberId = getSysTelephonyManager(context).getSubscriberId();
		AppLogMessageMgr.i("AppSysMgr-->>getSysUserPhoneId",  subscriberId);
		return subscriberId;
	}
	

	public static WindowManager getWindowManager(Context context){
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}
	
	
	public static DisplayMetrics getSysDisplayMetrics(Activity activity) {
		DisplayMetrics displayMetrics = null;
		if (displayMetrics == null) {
			displayMetrics = new DisplayMetrics();
		}
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		AppLogMessageMgr.i("AppSysMgr-->>getSysDisplayMetrics",  "??????????" + displayMetrics);
		return displayMetrics;
	}
	
	
	public static int[] getScreenDispaly(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		//???????
		int width = wm.getDefaultDisplay().getWidth();
		//???????
		int height = wm.getDefaultDisplay().getHeight();
		AppLogMessageMgr.i("AppSysMgr-->>getScreenDispaly-->>width",  "????????" + width);
		AppLogMessageMgr.i("AppSysMgr-->>getScreenDispaly-->>height",  "????????" + height);
		int result[] = { width, height };
		return result;
	}
	
	
    
	public static int[] getScreenDispaly8(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		//???????
		int width = wm.getDefaultDisplay().getWidth() /10 * 8;
		//???????
		int height = wm.getDefaultDisplay().getHeight() /10 * 8;
		AppLogMessageMgr.i("AppSysMgr-->>getScreenDispaly-->>width",  "????????" + width);
		AppLogMessageMgr.i("AppSysMgr-->>getScreenDispaly-->>height",  "????????" + height);
		int result[] = { width, height };
		return result;
	}
	

	public static Integer getSysScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        DisplayMetrics displayMetrics = new DisplayMetrics();  
        wm.getDefaultDisplay().getMetrics(displayMetrics);  
        AppLogMessageMgr.i("AppSysMgr-->>getSysScreenWidth",  "????????" + displayMetrics.widthPixels);
        return displayMetrics.widthPixels;  
    }  
	
    
	public static Integer getSysScreenHeight(Context context)  {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        DisplayMetrics displayMetrics = new DisplayMetrics();  
        wm.getDefaultDisplay().getMetrics(displayMetrics);  
        AppLogMessageMgr.i("AppSysMgr-->>getSysScreenHeight",  "????????" + displayMetrics.heightPixels);
        return displayMetrics.heightPixels;  
    }  
    
	

    public static Integer getSysScreenStatusHeight(Context context)  {
        int statusHeight = 0;  
        try {  
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");  
            Object object = clazz.newInstance();  
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());  
            statusHeight = context.getResources().getDimensionPixelSize(height);  
            AppLogMessageMgr.i("AppSysMgr-->>getSysScreenStatusHeight",  "???????????" + statusHeight);
        } catch (Exception e) {  
            e.printStackTrace();  
            AppLogMessageMgr.e("AppSysMgr-->>getSysScreenStatusHeight",  "????????????" + e.getMessage());
        }  
        	return statusHeight;  
    } 
    
    
	public static Integer getSysDefaultThreadPoolSize() {
		Integer availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
		availableProcessors = availableProcessors > 8 ? 8 : availableProcessors;
		AppLogMessageMgr.i("AppSysMgr-->>getSysDefaultThreadPoolSize",  availableProcessors + "");
		return availableProcessors;
	}

	
	
	

	public static Integer getSysSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
    	Integer initialSize = calculateSysInitialSampleSize(options, minSideLength, maxNumOfPixels);  
    	Integer roundedSize;  
        if (initialSize <= 8 ) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        }else{  
        		roundedSize = (initialSize + 7) / 8 * 8;  
        }  
        AppLogMessageMgr.i("AppSysMgr-->>getSysSampleSize",  roundedSize + "");
        return roundedSize;  
    }  
      
    
    private static Integer calculateSysInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;  
        double h = options.outHeight;  
        Integer lowerBound = (maxNumOfPixels == -1) ? 1 :  (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));  
        Integer upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
        if (upperBound < lowerBound) {  
            return lowerBound;  
        }  
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
            return 1;  
        } else if (minSideLength == -1) {  
            return lowerBound;  
        } else {  
            return upperBound;  
        }  
    }
    
    
    public static Vibrator getVibrator(Context context){
    	return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    
    public String getSysLocalIpAddress() {
    	String hostAddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                    	hostAddress = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        	e.printStackTrace();
        	AppLogMessageMgr.e("AppSysMgr-->>getSysLocalIpAddress",  e.getMessage().toString());
        }
        	AppLogMessageMgr.i("AppSysMgr-->>getSysLocalIpAddress",  hostAddress);
        	return hostAddress;
    }

	public static String getAndroidID(Context ctx) {
		return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	public static String getIMSI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId() != null ? tm.getSubscriberId() : null;
	}

	public static String getIP(Context ctx) {
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled() ? getWifiIP(wifiManager) : getGPRSIP();
	}

	public static String getWifiIP(WifiManager wifiManager) {
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String ip = intToIp(wifiInfo.getIpAddress());
		return ip != null ? ip : "";
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

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
	}

	public static String getSerial() {
		return Build.SERIAL;
	}

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

	public static int getBuildVersionSDK() {
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

	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static String getCountry(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		Locale locale = Locale.getDefault();
		return tm.getSimState() == TelephonyManager.SIM_STATE_READY ? tm.getSimCountryIso().toLowerCase(Locale.getDefault()) : locale.getCountry().toLowerCase(locale);
	}

	public static String getOSVersion() {
		return Build.VERSION.RELEASE;
	}

	//<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>
	public static String getGSFID(Context context) {
		String result;
		final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
		final String ID_KEY = "android_id";
		String[] params = {ID_KEY};
		Cursor c = context.getContentResolver().query(URI, null, null, params, null);
		if (c == null || !c.moveToFirst() || c.getColumnCount() < 2) {
			return null;
		} else {
			result = Long.toHexString(Long.parseLong(c.getString(1)));
		}
		c.close();
		return result;
	}

	//<uses-permission android:name="android.permission.BLUETOOTH"/>
	@SuppressWarnings("MissingPermission")
	public static String getBluetoothMAC(Context context) {
		String result = null;
		try {
			if (context.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH)
				== PackageManager.PERMISSION_GRANTED) {
				BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
				result = bta.getAddress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getPsuedoUniqueID() {
		String devIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			devIDShort += (Build.SUPPORTED_ABIS[0].length() % 10);
		} else {
			devIDShort += (Build.CPU_ABI.length() % 10);
		}
		devIDShort += (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
		String serial;
		try {
			serial = Build.class.getField("SERIAL").get(null).toString();
			return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
		} catch (Exception e) {
			serial = "ESYDV000";
		}
		return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
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

	public static String getUA(Context ctx) {
		final String system_ua = System.getProperty("http.agent");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return new WebView(ctx).getSettings().getDefaultUserAgent(ctx) + "__" + system_ua;
		} else {
			return new WebView(ctx).getSettings().getUserAgentString() + "__" + system_ua;
		}
	}

	public static String getDensity(Context ctx) {
		String densityStr = null;
		final int density = ctx.getResources().getDisplayMetrics().densityDpi;
		switch (density) {
			case DisplayMetrics.DENSITY_LOW:
				densityStr = "LDPI";
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				densityStr = "MDPI";
				break;
			case DisplayMetrics.DENSITY_TV:
				densityStr = "TVDPI";
				break;
			case DisplayMetrics.DENSITY_HIGH:
				densityStr = "HDPI";
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				densityStr = "XHDPI";
				break;
			case DisplayMetrics.DENSITY_400:
				densityStr = "XMHDPI";
				break;
			case DisplayMetrics.DENSITY_XXHIGH:
				densityStr = "XXHDPI";
				break;
			case DisplayMetrics.DENSITY_XXXHIGH:
				densityStr = "XXXHDPI";
				break;
		}
		return densityStr;
	}

	//<uses-permission android:name="android.permission.GET_ACCOUNTS"/>
	@SuppressWarnings("MissingPermission")
	public static String[] getGoogleAccounts(Context ctx) {
		if (ctx.checkCallingOrSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
			Account[] accounts = AccountManager.get(ctx).getAccountsByType("com.google");
			String[] result = new String[accounts.length];
			for (int i = 0; i < accounts.length; i++) {
				result[i] = accounts[i].name;
			}
			return result;
		}
		return null;
	}

}
