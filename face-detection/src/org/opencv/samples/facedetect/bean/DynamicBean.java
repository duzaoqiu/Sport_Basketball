package org.opencv.samples.facedetect.bean;

import com.google.gson.annotations.SerializedName;

import org.opencv.samples.facedetect.model.VideoDetailModel;
import org.opencv.samples.model.BaseResp;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态bean
 *
 * @author zhouyi
 */
public class DynamicBean extends BaseResp {

    public Data data;

    public static class Data{

        public StatsBean stats;
        public List<ListBean> list;

        public StatsBean getStats() {
            return stats;
        }

        public void setStats(StatsBean stats) {
            this.stats = stats;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
    }

    public static class ListBean {
        /**
         * analyzeVideos : [{"actionId":6673520547365992449,"createTime":"2020-05-20 19:16:05","id":6668831645094983681,"imgUrl":"http://183.136.222.58:18080/artemis-app/file/6668831936464893953","title":"","type":1,"videoUrl":"http://183.136.222.58:18080/artemis-app/file/6668831944572483585"}]
         * areaId : 1
         * createTime : 2020-06-02 17:48:06
         * id : 6673520547365992449
         * orgVideos : [{"actionId":6673520547365992449,"createTime":"2020-05-20 19:16:03","id":6668831638182770689,"imgUrl":"http://183.136.222.58:18080/artemis-app/file/6668831638618978305","type":0,"videoUrl":"http://183.136.222.58:18080/artemis-app/file/6668831645627660289"}]
         * result : 1
         * stadiumId : 6061680407697765401
         * type : 1
         * userId : 6061680407697766402
         */

        public long id;
        public long userId;
        public long stadiumId;
        public String imgUrl;
        public ArrayList<VideoDetailModel> orgVideos;
        public ArrayList<VideoDetailModel> analyzeVideos;
        public int type;
        public int result;
        public int areaId;
        public String createTime;
    }

    public static class StatsBean {
        public TotalBean total;

        @SerializedName("3point")
        public PointBean point3;

        @SerializedName("2point")
        public PointBean point2;

        public List<DetailBean> detail;

        public static class TotalBean {
            public int tryCount;
            public int sucCount;
            public double sucRate;
        }

        public static class PointBean {
            public int tryCount;
            public int sucCount;
            public double sucRate;
        }

        public static class DetailBean {
            public int tryCount;
            public int sucCount;
            public double sucRate;
        }
    }
}
