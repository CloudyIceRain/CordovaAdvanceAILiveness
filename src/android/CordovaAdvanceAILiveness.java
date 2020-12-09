package com.cordova.plugin.AdvanceAI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CordovaAdvanceAILiveness extends CordovaPlugin {
    //针对AdvanceAI 1.2.4版本的接入


    public static String initLiveness(Activity activity, String market) {
        return "";
    }
    public static String setLicenseAndCheck(Activity activity, String license){
        return "";
    }
    public static void startLivenessActivity(Activity activity, CallbackContext callbackContext){
        
    }
    public static String setActionSequence(Activity activity, boolean isRandom, String[] actions){
        return "";
    }
    public static String bindUser(Activity activity, String userId) {
        return "";
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        Activity activity = this.cordova.getActivity();
        Context context = this.cordova.getContext();

        if ("initLiveness".equals(action)){//初始化活体检测
            String market = args.getString(0);
            String ret = initLiveness(activity, market);
            callbackContext.success(ret);
            return true;
        }else if ("setLicenseAndCheck".equals(action)){//设置License
            String license = args.getString(0);
            String ret = setLicenseAndCheck(activity, license);
            callbackContext.success(ret);
            return true;
        }else if ("startLivenessActivity".equals(action)){//启动活体认证
            startLivenessActivity(activity, callbackContext);
            return true;
        }else if ("setActionSequence".equals(action)){//设置动作顺序
            boolean isRandom = args.getBoolean(0);
            JSONArray json_actions = args.getJSONArray(1);
            ArrayList<String> stringList = new ArrayList<String>();
            for (int i =0; i< json_actions.length(); i++){
                stringList.add(json_actions.getString(i));
            }
            String[] actions = (String[])stringList.toArray(new String[stringList.size()]) ;

            String ret = setActionSequence(activity, isRandom, actions);
            callbackContext.success(ret);
            return true;
        }else if ("bindUser".equals(action)){//绑定用户
            String userId = args.getString(0);
            String ret = bindUser(activity, userId);
            callbackContext.success(ret);
            return true;
        }

        callbackContext.error(action + " is not a supported action");
        return false;
    }
}
