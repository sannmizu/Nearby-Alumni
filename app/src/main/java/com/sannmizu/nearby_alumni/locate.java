package com.sannmizu.nearby_alumni;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.sannmizu.nearby_alumni.NetUtils.MyResponse;
import com.sannmizu.nearby_alumni.NetUtils.Net;
import com.sannmizu.nearby_alumni.NetUtils.locateResponse;
import com.sannmizu.nearby_alumni.utils.locateback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class locate{
    private LocationClient mLocationClient;
    private String latitude,longitude;
    //private SharedPreferences pref;
    //private SharedPreferences.Editor editor;
    private Context applicationContext;
    private LocationClient locationClient;

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    //public static locateActivity instance=null;

    public LocationClient getmLocationClient() {
        return mLocationClient;
    }
    public void setmLocationClient(LocationClient mLocationClient) {
        this.mLocationClient = mLocationClient;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    /*String latitude,longitude;
                    @Override
                    protected void onCreate(@Nullable Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        instance=this;
                        mLocationClient=new LocationClient(getApplicationContext());
                        mLocationClient.registerLocationListener(new MyLocationListener());
                        SDKInitializer.initialize(getApplicationContext());
                        List<String> permissionList=new ArrayList<>();
                        if (ContextCompat.checkSelfPermission(locateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                                PackageManager.PERMISSION_GRANTED){
                            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                        if (ContextCompat.checkSelfPermission(locateActivity.this,Manifest.permission.READ_PHONE_STATE)!=
                                PackageManager.PERMISSION_GRANTED){
                            permissionList.add(Manifest.permission.READ_PHONE_STATE);
                        }
                        if (ContextCompat.checkSelfPermission(locateActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                                PackageManager.PERMISSION_GRANTED){
                            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        if (!permissionList.isEmpty()){
                            String[]permissions=permissionList.toArray(new String[permissionList.size()]);
                            ActivityCompat.requestPermissions(locateActivity.this,permissions,1);
                        }else {
                            requesLocation();
                        }
                    }*/
    public void requesLocation(locateback locateback){
        mLocationClient=new LocationClient(getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(new BDAbstractLocationListener(){
            @Override
            public void onReceiveLocation(final BDLocation bdLocation) {
                StringBuilder currentPosition=new StringBuilder();
                //pref=PreferenceManager.getDefaultSharedPreferences(locateActivity.this);
                currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                latitude= String.valueOf(bdLocation.getLatitude());
                setLatitude(latitude);
                //editor=pref.edit();
                //editor.putString("latitude",latitude);
                currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
                longitude= String.valueOf(bdLocation.getLongitude());
                locateback.onReceiveLocation(latitude, longitude);
                setLongitude(longitude);
                //editor.putString("longitude",longitude);
                //editor.apply();
                currentPosition.append("国家:").append(bdLocation.getCountry()).append("\n");
                currentPosition.append("省:").append(bdLocation.getProvince()).append("\n");
                currentPosition.append("市:").append(bdLocation.getCity()).append("\n");
                currentPosition.append("区:").append(bdLocation.getDistrict()).append("\n");
                currentPosition.append("街道:").append(bdLocation.getStreet()).append("\n");
                currentPosition.append("定位方式:");
                if (bdLocation.getLocType()== BDLocation.TypeGpsLocation){
                    currentPosition.append("GPS");
                }
                else if (bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                    currentPosition.append("网络");
                }
                //getlocate();
                //positonText.setText(currentPosition);
            }
            public void onConnentHotSpotMessage(String s,int i){
                Log.d("", s);
            }
        });
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requesLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }*/

   private static class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    StringBuilder currentPosition=new StringBuilder();
                    //pref=PreferenceManager.getDefaultSharedPreferences(locateActivity.this);
                    currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                    String latitude= String.valueOf(bdLocation.getLatitude());
                    //editor=pref.edit();
                    //editor.putString("latitude",latitude);
                    currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
                    String longitude= String.valueOf(bdLocation.getLongitude());
                    //editor.putString("longitude",longitude);
                    //editor.apply();
                    currentPosition.append("国家:").append(bdLocation.getCountry()).append("\n");
                    currentPosition.append("省:").append(bdLocation.getProvince()).append("\n");
                    currentPosition.append("市:").append(bdLocation.getCity()).append("\n");
                    currentPosition.append("区:").append(bdLocation.getDistrict()).append("\n");
                    currentPosition.append("街道:").append(bdLocation.getStreet()).append("\n");
                    currentPosition.append("定位方式:");
                    if (bdLocation.getLocType()== BDLocation.TypeGpsLocation){
                        currentPosition.append("GPS");
                    }
                    else if (bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                        currentPosition.append("网络");
                    }
                    //getlocate();
                    //positonText.setText(currentPosition);
                }
            }).start();
        }
        public void onConnentHotSpotMessage(String s,int i){

        }
    }
   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }*/
   /* public void getlocate(){
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        String logToken = pref.getString("logToken", "null");
        String latitude=pref.getString("latitude","null");
        String longitude=pref.getString("longitude","null");
        if(logToken == "null") {    //其实还要判断logToken是否失效
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Net.BaseHost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            MyResponse.locateService service=retrofit.create(MyResponse.locateService.class);
            Call<locateResponse> call=service.locate(latitude,longitude,logToken);
            call.enqueue(new Callback<locateResponse>() {
                @Override
                public void onResponse(Call<locateResponse> call, Response<locateResponse> response) {
                    if(response.body().getCode()==0){
                        Log.d("shangchuan","上传成功");
                    }
                    else {
                        Log.d("shangchuan","上传失败");
                    }
                }

                @Override
                public void onFailure(Call<locateResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }*/
}
