package com.m2team.library.utils;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.regex.Pattern;


@SuppressLint("SimpleDateFormat")
@SuppressWarnings("rawtypes")
public class ValidationUtils {

	
	//?????
	private final static Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
	
	//??????
	private final static Pattern phone_pattern = Pattern.compile("^(13|15|18)\\d{9}$");
	
	//???????
	private final static Pattern bankNo_pattern = Pattern.compile("^[0-9]{16,19}$");
	
	//???????
	private final static Pattern plane_pattern = Pattern.compile("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$");  
	
	//?????
	private final static Pattern notZero_pattern = Pattern.compile("^\\+?[1-9][0-9]*$");
	
	//?????
	private final static Pattern number_pattern = Pattern.compile("^[0-9]*$");
	
	//???????
	private final static Pattern upChar_pattern = Pattern.compile("^[A-Z]+$");
	
	//???????
	private final static Pattern lowChar_pattern = Pattern.compile("^[a-z]+$");

	//????????
	private final static Pattern letter_pattern = Pattern.compile("^[A-Za-z]+$");
	
	//???????
	private final static Pattern chinese_pattern = Pattern.compile("^[\u4e00-\u9fa5],{0,}$");
	
	//??????
	private final static Pattern onecode_pattern = Pattern.compile("^(([0-9])|([0-9])|([0-9]))\\d{10}$");
	
	//???????
	private final static Pattern postalcode_pattern = Pattern.compile("([0-9]{3})+.([0-9]{4})+"); 
	
	//IP?????
	private final static Pattern ipaddress_pattern = Pattern.compile("[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"); 
	
	//URL?????
	private final static Pattern url_pattern = Pattern.compile("(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?"); 
	
	//??????
	private final static Pattern username_pattern = Pattern.compile("^[A-Za-z0-9_]{1}[A-Za-z0-9_.-]{3,31}"); 
	
	//???????

	//??HTML??,?????????????HTML???????
	private final static Pattern html_patter = Pattern.compile("<\\\\/?\\\\w+((\\\\s+\\\\w+(\\\\s*=\\\\s*(?:\".*?\"|'.*?'|[\\\\^'\">\\\\s]+))?)+\\\\s*|\\\\s*)\\\\/?>");

	//????,???????HMTL????????????????
	private final static Pattern notes_patter = Pattern.compile("<!--(.*?)-->");

	//??CSS??,??????????????????CSS???
	private final static Pattern css_patter = Pattern.compile("^\\\\s*[a-zA-Z\\\\-]+\\\\s*[:]{1}\\\\s[a-zA-Z0-9\\\\s.#]+[;]{1}");

	//???????,??html??????
	private final static Pattern hyperlink_patter = Pattern.compile("(<a\\\\s*(?!.*\\\\brel=)[^>]*)(href=\"https?:\\\\/\\\\/)((?!(?:(?:www\\\\.)?'.implode('|(?:www\\\\.)?', $follow_list).'))[^\"]+)\"((?!.*\\\\brel=)[^>]*)(?:[^>]*)>");

	//??????,???????????????????????????
	private final static Pattern image_patter = Pattern.compile("\\\\< *[img][^\\\\\\\\>]*[src] *= *[\\\\\"\\\\']{0,1}([^\\\\\"\\\\'\\\\ >]*)");

	//??Color Hex Codes,??????????????????????????
	private final static Pattern color_patter = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

	//??????????,??windows?????????????????.txt???
	private final static Pattern route_patter = Pattern.compile("^([a-zA-Z]\\\\:|\\\\\\\\)\\\\\\\\([^\\\\\\\\]+\\\\\\\\)*[^\\\\/:*?\"<>|]+\\\\.txt(l)?$");

	//??URL??,???????????????????URL
	// ^(f|ht){1}(tp|tps):\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- ./?%&=]*)?
	//??URL???,????????????????HTTPS??HTTP???????????????url???????????
//if (!s.match(/^[a-zA-Z]+:\\/\\//))
//	{
//		s = 'http://' + s;
//	}
	//??IP-v6??
//	(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))
//??IP-v4??
//	\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b
//	??IE???
//	^.*MSIE [5-8](?:\\.[0-9]+)?(?!.*Trident\\/[5-9]\\.0).*$
//	????
//^[0-9]+(.[0-9]{2})?$
//	??????
//^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$

	public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || str.length() == 0) {
        	 return true;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
            return false;
            }
        }
        	return true;
    }
 
    
	public static boolean isNotEmpty(String s){
		return s != null && !"".equals(s.trim());
	}
	
	public static boolean isNotZero(String str) {
		return notZero_pattern.matcher(str).matches();
	}

	
	public static boolean isNumber(String str) {
		return number_pattern.matcher(str).matches();
	}
	
	
	public static boolean isUpChar(String str) {
		return upChar_pattern.matcher(str).matches();
	}
	
	
	public static boolean isLowChar(String str) {
		return lowChar_pattern.matcher(str).matches();
	}
	
	
	public static boolean isLetter(String str) {
		return letter_pattern.matcher(str).matches();
	}
	
	
	public static boolean isChinese(String str) {
		return chinese_pattern.matcher(str).matches();
	}
	
	public static boolean isOneCode(String oneCode) {
		return onecode_pattern.matcher(oneCode).matches();
	}

	public static boolean isEmail(String email) {
		return email_pattern.matcher(email).matches();
	}
	
	
	
	public static boolean isPhone(String phone) {
		 return phone_pattern.matcher(phone).matches();
	}

	
	public static boolean isPlane(String plane) {
		 return plane_pattern.matcher(plane).matches();
	}
	
	
	
	
	public static boolean isPostalCode(String postalcode) {
		return postalcode_pattern.matcher(postalcode).matches();
	}
	

	public static boolean isIpAddress(String ipaddress){
        return ipaddress_pattern.matcher(ipaddress).matches();
	}
	
	
	
	public static boolean isURL(String url){
		 return url_pattern.matcher(url).matches();
	}
	
	
	
    
    public static boolean isInteger(String str){
		try{
			Integer.valueOf(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	public static boolean isPoint(String paramString){
		if(paramString.indexOf(".") > 0){
			if(paramString.substring(paramString.indexOf(".")).length() > 3){
				return false;
			}
		}
		return true;
	}
    
	
	public static boolean isBankNo(String bankNo){
		//????
		bankNo = bankNo.replaceAll(" ", "");
		//??????12???
		if(12 == bankNo.length()){
			return true;
		}
		//??????16-19???
		return bankNo_pattern.matcher(bankNo).matches();
	}

	public static boolean isPeculiarStr(String str){
		boolean flag = false;
		String regEx = "[^0-9a-zA-Z\u4e00-\u9fa5]+";
		if(str.length() != (str.replaceAll(regEx, "").length())) {
			flag = true;
		}
			return  flag;
	}
	
	
	public static boolean isUserName(String username) {
		return username_pattern.matcher(username).matches();
	}

	public static int chineseLength(String str) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* ???????????????????????????2????1 */
		if (!isEmpty(str)) {
			for (int i = 0; i < str.length(); i++) {
				/* ?????? */
				String temp = str.substring(i, i + 1);
				/* ????????? */
				if (temp.matches(chinese)) {
					valueLength += 2;
				}
			}
		}
		return valueLength;
	}

    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            // ???????????????????????????2????1
            for (int i = 0; i < str.length(); i++) {
                // ??????
                String temp = str.substring(i, i + 1);
                // ?????????
                if (temp.matches(chinese)) {
                    // ???????2
                    valueLength += 2;
                } else {
                    // ???????1
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        // ???????????????????????????2????1
        for (int i = 0; i < str.length(); i++) {
            // ??????
            String temp = str.substring(i, i + 1);
            // ?????????
            if (temp.matches(chinese)) {
                // ???????2
                valueLength += 2;
            } else {
                // ???????1
                valueLength += 1;
            }
            if (valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    public static Boolean isNumberLetter(String str) {
        Boolean isNoLetter = false;
        String expr = "^[A-Za-z0-9]+$";
        if (str.matches(expr)) {
            isNoLetter = true;
        }
        return isNoLetter;
    }

    public static Boolean isContainChinese(String str) {
        Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            // ???????????????????????????2????1
            for (int i = 0; i < str.length(); i++) {
                // ??????
                String temp = str.substring(i, i + 1);
                // ?????????
                if (temp.matches(chinese)) {
                    isChinese = true;
                } else {

                }
            }
        }
        return isChinese;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            // ????\n??
            if (sb.indexOf("\n") != -1
                && sb.lastIndexOf("\n") == sb.length() - 1) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String cutString(String str, int length) {
        return cutString(str, length, "");
    }

    public static String cutString(String str, int length, String dot) {
        int strBLen = strlen(str, "GBK");
        if (strBLen <= length) {
            return str;
        }
        int temp = 0;
        StringBuffer sb = new StringBuffer(length);
        char[] ch = str.toCharArray();
        for (char c : ch) {
            sb.append(c);
            if (c > 256) {
                temp += 2;
            } else {
                temp += 1;
            }
            if (temp >= length) {
                if (dot != null) {
                    sb.append(dot);
                }
                break;
            }
        }
        return sb.toString();
    }

    public static String cutStringFromChar(String str1, String str2, int offset) {
        if (isEmpty(str1)) {
            return "";
        }
        int start = str1.indexOf(str2);
        if (start != -1) {
            if (str1.length() > start + offset) {
                return str1.substring(start + offset);
            }
        }
        return "";
    }

    public static int strlen(String str, String charset) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int length = 0;
        try {
            length = str.getBytes(charset).length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    public static String getSizeDesc(long size) {
        String suffix = "B";
        if (size >= 1024) {
            suffix = "K";
            size = size >> 10;
            if (size >= 1024) {
                suffix = "M";
                // size /= 1024;
                size = size >> 10;
                if (size >= 1024) {
                    suffix = "G";
                    size = size >> 10;
                    // size /= 1024;
                }
            }
        }
        return size + suffix;
    }

    public static long ip2int(String ip) {
        ip = ip.replace(".", ",");
        String[] items = ip.split(",");
        return Long.valueOf(items[0]) << 24 | Long.valueOf(items[1]) << 16
            | Long.valueOf(items[2]) << 8 | Long.valueOf(items[3]);
    }

    public static String gainUUID() {
        String strUUID = UUID.randomUUID().toString();
        strUUID = strUUID.replaceAll("-", "").toLowerCase();
        return strUUID;
    }


	public static String phoneNoHide(String phone) {
		// ????????????$n???n????
		// ???????????????????????replace()????
		// ????????$n(n???)??????????????????
		// "(\d{3})\d{4}(\d{4})", "$1****$2"???????????
		// ??(?3???)??4???(??4???)???(??????????$1)(???*)(??????????$2)
		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
	}

	public static String cardIdHide(String cardId) {
		return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
	}

	public static String idHide(String id) {
		return id.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1** **** ****$2");
	}

	public static boolean checkVehicleNo(String vehicleNo) {
		Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$");
		return pattern.matcher(vehicleNo).find();

	}

//	/**
//	 * ????????
//	 *
//	 * @param postcode ????
//	 * @return ??????true???????false
//	 */
//	public static boolean checkPostcode(String postcode) {
//		String regex = "[1-9]\\d{5}";
//		return Pattern.matches(regex, postcode);
//	}



}
	
	
	
	
	
