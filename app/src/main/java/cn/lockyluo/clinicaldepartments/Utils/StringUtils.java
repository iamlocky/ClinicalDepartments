package cn.lockyluo.clinicaldepartments.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LockyLuo on 2017/10/15.
 */

public class StringUtils {
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
