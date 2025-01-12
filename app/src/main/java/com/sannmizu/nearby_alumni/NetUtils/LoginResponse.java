package com.sannmizu.nearby_alumni.NetUtils;

import com.google.gson.annotations.SerializedName;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class LoginResponse extends MyResponse<LoginResponse.LoginData>{
    public static interface LoginService {
        @FormUrlEncoded
        @POST("app/login")
        Observable<LoginResponse> login(@Field("value") String value);
    }
    public static class LoginData {
        @SerializedName("userId")
        private  int id;
        @SerializedName("logToken")
        private String logToken;
        @SerializedName("expiretime")
        private String expire_time;

        public String getLogToken() {
            return logToken;
        }

        public void setLogToken(String logToken) {
            this.logToken = logToken;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
