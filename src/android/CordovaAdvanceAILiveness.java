package com.cordova.plugin.AdvanceAI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ai.advance.liveness.lib.Detector;
import ai.advance.liveness.lib.GuardianLivenessDetectionSDK;
import ai.advance.liveness.lib.LivenessResult;
import ai.advance.liveness.lib.Market;
import ai.advance.liveness.sdk.activity.LivenessActivity;

public class CordovaAdvanceAILiveness extends CordovaPlugin {
    //针对AdvanceAI 1.2.4版本的接入
    public static final int REQUEST_CODE_LIVENESS = 1422114;    //advan 1 4 22 1 14
    public static CallbackContext m_startLiveness_Callback = null;
    /**
     * 获取检测结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LIVENESS) {
            Log.i("Advance", "onActivityResult REQUEST_CODE_LIVENESS");
            if (LivenessResult.isSuccess()) {// 活体检测成功
                String livenessId = LivenessResult.getLivenessId();// 本次活体id
                String livenessBase64Str = LivenessResult.getLivenessBase64Str();// 本次活体图片
                if (null != m_startLiveness_Callback){
                    JSONObject js_obj = new JSONObject();
                    try {
                        js_obj.put("livenessId", livenessId);
                        js_obj.put("livenessBase64Str", livenessBase64Str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    m_startLiveness_Callback.success(js_obj);
                }
            } else {// 活体检测失败
                String errorCode = LivenessResult.getErrorCode();// 失败错误码
                String errorMsg = LivenessResult.getErrorMsg();// 失败原因
                JSONObject js_obj = new JSONObject();
                try {
                    js_obj.put("errorCode", errorCode);
                    js_obj.put("errorMsg", errorMsg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                m_startLiveness_Callback.success(js_obj);
            }
        }
    }

    public static String initLiveness(Activity activity, String market) {
        //market支持的值包括Indonesia印尼 India印度 Philippines菲律宾 Vietnam越南 Malaysia马来西亚 Thailand泰国 BPS CentralData
        GuardianLivenessDetectionSDK.init(activity.getApplication(), Market.valueOf(market));
        return "";
    }
    public static String setLicenseAndCheck(Activity activity, String license){
        String checkResult = GuardianLivenessDetectionSDK.setLicenseAndCheck(license);//返回值是"SUCCESS"才是有效
        if ("SUCCESS".equals(checkResult)){
            ////有效的情况下，自动申请权限
            GuardianLivenessDetectionSDK.letSDKHandleCameraPermission();
        }
        return checkResult;
    }
    public static void startLivenessActivity(Activity activity, CallbackContext callbackContext){
        m_startLiveness_Callback = callbackContext;
        Intent intent = new Intent(activity, LivenessActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE_LIVENESS);
    }

    public static String setActionSequence(Activity activity, boolean isRandom, String[] actions){
        Detector.DetectionType[] arr = new Detector.DetectionType[actions.length];
        for (int i = 0; i< actions.length; i++){
            arr[i] = Detector.DetectionType.valueOf(actions[i]);
        }
        GuardianLivenessDetectionSDK.setActionSequence(isRandom, arr);
        return "";
    }
    public static String bindUser(Activity activity, String userId) {
        //绑定用户id
        GuardianLivenessDetectionSDK.bindUser(userId);
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
