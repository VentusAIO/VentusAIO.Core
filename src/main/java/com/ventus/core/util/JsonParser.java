package com.ventus.core.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonParser {

    //Возвращает первое вхождение из Json
    public static String getValue(String fullStr, String subStr){
        boolean flag = false;
        int count = 0;
        StringBuilder stringBuilder = null;

        try {
            stringBuilder = new StringBuilder();
            for (int i = fullStr.indexOf(subStr); count != 3; i++) {
                if (fullStr.charAt(i) == 34) {
                    count++;
                    flag = true;
                }
                if (flag){
                    i++;
                    flag = false;
                }
                if (count == 2){
                    stringBuilder.append(fullStr.charAt(i));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
//            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    //Возвращает Map
    public static Map<String, String> getMap(String fullStr, String subStr){
        if(fullStr.contains(subStr)) {
            int index = fullStr.indexOf(subStr) + subStr.length() + 2;
            if(fullStr.charAt(index) != '{') return null;
            Map<String, String> result = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder();

            String name = "";

            boolean isName = true;
            boolean v = false;

            try {
                for (int i = index+1; i < fullStr.length(); i++) {
                    char c = fullStr.charAt(i);
                    if(c == '}') break;
                    if(isName){
                        if(c == '"'){
                            if(v) {
                                name = stringBuilder.toString();
                                stringBuilder = new StringBuilder();
                                isName = false;
                            }
                            v = !v;
                            continue;
                        }
                        if(v){
                            stringBuilder.append(c);
                        }
                    }
                    else{
                        if(c == '"'){
                            if(v) {
                                result.put(name, stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                isName = true;
                            }
                            v = !v;
                            continue;
                        }
                        if(v){
                            stringBuilder.append(c);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        else{
            return null;
        }
    }

    public static int getIntValue(String fullStr, String subStr){
        boolean flag = false;
        byte[] fullStrBytes;
        fullStrBytes = fullStr.getBytes(StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int i = fullStr.indexOf(subStr) + subStr.length(); i < fullStrBytes.length; i++){
                if (fullStrBytes[i] == ','){
                    return Integer.parseInt((stringBuilder.toString()));
                }
                if (flag){
                    stringBuilder.append((char) fullStrBytes[i]);
                }
                if (fullStrBytes[i] == ':') {
                    flag = true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }
}
