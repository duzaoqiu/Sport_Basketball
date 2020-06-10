package org.opencv.samples.facedetect.activity.login;

import org.opencv.samples.model.BaseResp;

public class LoginBean extends BaseResp {

    public DataBean data;

    public static class DataBean{

        public UserBean user;

        public String token;

    }

    public static class UserBean {
        public long id;
        public String cellphone;
        public String name;
        public String favoriteStarName;
        public long favoriteStarId;
        public String avatar;
     }
}
