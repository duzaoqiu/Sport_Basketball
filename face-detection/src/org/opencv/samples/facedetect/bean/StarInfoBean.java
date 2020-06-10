package org.opencv.samples.facedetect.bean;

import org.opencv.samples.model.BaseResp;

import java.util.List;

public class StarInfoBean extends BaseResp {

    /**
     * status : true
     * msg :
     * code : 200
     * data : [{"id":6647184791051778049,"name":"詹姆斯","avatar":"http://183.136.222.58:18080/basketball-app/file/6669110798276966401","createTime":"2020-03-22 01:39:12"},{"id":6647687044875499521,"name":"科比","avatar":"http://183.136.222.58:18080/basketball-app/file/6669110798276966401","createTime":"2020-03-23 10:54:59"},{"id":6649135294484658177,"name":"kl","avatar":"http://183.136.222.58:18080/basketball-app/file/6669110798276966401","createTime":"2020-03-27 10:49:49"},{"id":6649190174377195521,"name":"kl","avatar":"http://183.136.222.58:18080/basketball-app/file/6669110798276966401","createTime":"2020-03-27 14:27:53"}]
     */

    public List<StarInfo> data;


    public static class StarInfo {
        /**
         * id : 6647184791051778049
         * name : 詹姆斯
         * avatar : http://183.136.222.58:18080/basketball-app/file/6669110798276966401
         * createTime : 2020-03-22 01:39:12
         */

        private long id;
        private String name;
        private String avatar;
        private String createTime;
        public boolean isSelected = false;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
