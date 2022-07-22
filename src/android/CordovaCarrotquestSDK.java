package cordova.carrotquestSDK;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import android.content.Context;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.carrotquest_sdk.android.Carrot;
import io.carrotquest_sdk.android.Carrot.Callback;
import io.carrotquest_sdk.android.core.main.ThemeSdk;
import io.carrotquest_sdk.android.models.UserProperty;

public class CordovaCarrotquestSDK extends CordovaPlugin {

  private Context context;
  private String packageName;
  public static final String TAG = "CordovaCarrotquestSDK";

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    this.context = cordova.getContext();
    this.packageName = cordova.getActivity().getApplicationContext().getPackageName();
    Log.d(TAG, "==> CordovaCarrotquestSDK initialize");
  }


  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("setup")) {
      String apiKey = args.getString(0);
      String appId = args.getString(1);
      boolean debug = args.getBoolean(2);
      Carrot.setDebug(debug);
      this.init(apiKey, appId, callbackContext);
      return true;
    }
    if (action.equals("deInit")) {
      this.deInit(callbackContext);
      return true;
    }
    if (action.equals("openChat")) {
      this.openChat(callbackContext);
      return true;
    }
    if (action.equals("auth")) {
      String userId = args.getString(0);
      String userAuthKey = args.getString(1);
      this.auth(userId, userAuthKey, callbackContext);
      return true;
    }
    if (action.equals("setProperties")) {
      JSONArray jsonArray = args.getJSONArray(0);
      ArrayList<UserProperty> userProperties = new ArrayList<>();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject userProperty = jsonArray.getJSONObject(i);
        String key = userProperty.keys().next();
        String value = userProperty.getString(key);
        userProperties.add(new UserProperty(key, value));
      }
      this.setProperties(userProperties, callbackContext);
      return true;
    }
    if (action.equals("getUnreadConversations")) {
      this.getUnreadConversations(callbackContext);
      return true;
    }
    if (action.equals("getUnreadConversationsCallback")) {
      this.getUnreadConversationsCallback(callbackContext);
      return true;
    }
    return false;
  }

  private void setup(String apiKey, String appId, CallbackContext callbackContext) {
    try {
      Callback<Boolean> cb = new Callback<Boolean>() {
        @Override
        public void onResponse(Boolean aBoolean) {
          Carrot.setTheme(ThemeSdk.LIGHT);
          Carrot.setParentActivityClassName(packageName + ".MainActivity");
          callbackContext.success();
        }

        @Override
        public void onFailure(Throwable throwable) {
          callbackContext.error(throwable.getMessage());
        }
      };
      Carrot.setup(this.context, apiKey, appId, cb);
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  private void deInit(CallbackContext callbackContext) {
    try {
      Carrot.deInit();
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  private void auth(String userId, String userAuthKey, CallbackContext callbackContext) {
    try {
      Callback<Boolean> cb = new Callback<Boolean>() {
        @Override
        public void onResponse(Boolean aBoolean) {
          callbackContext.success();
        }

        @Override
        public void onFailure(Throwable throwable) {
          callbackContext.error(throwable.getMessage());
        }
      };
      Carrot.auth(userId, userAuthKey, cb);
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  private void openChat(CallbackContext callbackContext) {
    try {
      Carrot.openChat(this.context);
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  private void setProperties(ArrayList<UserProperty> userProperty, CallbackContext callbackContext) {
    try {
      Carrot.setUserProperty(userProperty);
      callbackContext.success();
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  private void getUnreadConversations(CallbackContext callbackContext) {
    try {
      List<String> list = Carrot.getUnreadConversations();
      JSONArray jsonArray = new JSONArray(list);
      callbackContext.success(jsonArray);
    } catch (Exception e) {
      callbackContext.error(e.getMessage());
    }
  }

  private void getUnreadConversationsCallback(CallbackContext callbackContext) {
    Callback<List<String>> cb = new Callback<List<String>>() {
      @Override
      public void onResponse(List<String> list) {
        JSONArray jsonArray = new JSONArray(list);
        PluginResult result = new PluginResult(PluginResult.Status.OK, jsonArray);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
      }

      @Override
      public void onFailure(Throwable throwable) {
        callbackContext.error(throwable.getMessage());
      }
    };
    Carrot.setUnreadConversationsCallback(cb);
  }

}
