package com.boyuanitsm.zhetengba.http.manager;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.boyuanitsm.zhetengba.MyApplication;
import com.boyuanitsm.zhetengba.chat.db.UserDao;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.util.VolleyErrorHelper;
import com.boyuanitsm.zhetengba.utils.GsonUtils;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.utils.SpUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.http.client.multipart.content.FileBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 总的请求接口管理类
 * Created by wangbin on 16/5/31.
 */
public class RequestManager {

    private Gson mGson = new Gson();
    private static UserManager userManager;//用户管理
    private static TalkManager talkManager;//圈子管理

    private static ScheduleManager scheduleManager;//档期管理

    private static MessManager messManager;//消息接口管理

    public static UserManager getUserManager() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public static TalkManager getTalkManager() {
        if (talkManager == null) {
            talkManager = new TalkManager();
        }
        return talkManager;
    }

    public static ScheduleManager getScheduleManager() {
        if (scheduleManager == null) {
            scheduleManager = new ScheduleManager();
        }
        return scheduleManager;
    }

    public static MessManager getMessManager() {
        if (messManager == null) {
            messManager = new MessManager();
        }
        return messManager;
    }


    /* Volley Post请求
     * @param url
     * @param callback
     */
    public void doPost(final String url, final Map<String, String> params, final ResultCallback callback) {
        MyLogUtils.info("地址:" + url);
        MyLogUtils.info("传入参数：" + GsonUtils.bean2Json(params));
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        MyLogUtils.info(result);
                        if (callback.mType == String.class) {
                            callback.onResponse(result);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int status = jsonObject.getInt("status");
                                if (status == 200) {//成功
                                    Object o = mGson.fromJson(result, callback.mType);
                                    callback.onResponse(o);
                                } else if(status==601){
                                    callback.onError(status, result);
                                }else {//失败
                                    callback.onError(status, jsonObject.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(-1, VolleyErrorHelper.getMessage(error));
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                if (!TextUtils.isEmpty(SpUtils.getCookie(MyApplication.getInstance())))
                    localHashMap.put("Cookie", SpUtils.getCookie(MyApplication.getInstance()));
                if (UserInfoDao.getUser()!=null){
                    localHashMap.put("userName", UserInfoDao.getUser().getUsername());
                }
//                MyLogUtils.info("cookie======="+SpUtils.getCookie(MyApplication.getInstance()));
                return localHashMap;
            }

            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {
                // TODO Auto-generated method stub
                try {
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    String dataString = new String(response.data, "UTF-8");
                    if (!TextUtils.isEmpty(rawCookies))
                        SpUtils.setCooike(MyApplication.getInstance(), rawCookies);
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        // 设定超时时间
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1.0f));
        MyApplication.getInstance().addToRequestQueue(request);
        MyApplication.getInstance().getRequestQueue().start();
    }


    /**
     * 上传图片
     *
     * @param url
     * @param
     * @param fileMaps
     * @param callback
     */
    public void submitFujian(final String url, final Map<String, FileBody> fileMaps, final ResultCallback callback) {
        final HttpManager manager = new HttpManager();
        final Map<String, String> par = new HashMap<String, String>();
        MyLogUtils.info("地址：" + url);
        par.put("type", "img");
        par.put("uploadLableName", "file");

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return manager.upLoadFile(url, par, fileMaps);
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(String result) {
                MyLogUtils.info("上传图片：" + result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.getInt("status");
                        if (status == 200) {//成功
                            Object o = mGson.fromJson(result, callback.mType);
                            callback.onResponse(o);
                        } else {//失败
                            callback.onError(status, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    callback.onError(-1, "亲，网络不给力");

                }

            }
        }.execute();

    }



}
