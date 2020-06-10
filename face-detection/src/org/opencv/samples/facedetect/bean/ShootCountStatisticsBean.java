package org.opencv.samples.facedetect.bean;

import com.bench.android.core.util.bean.BaseResponse;

/**
 * @author zhouyi
 */
public class  ShootCountStatisticsBean extends BaseResponse {

    /**
     * scoreAllShootCount : 979
     * scoreAllShootWin : 616
     * score2ShootCount : 605
     * score2ShootWin : 374
     * score3ShootCount : 374
     * score3ShootWin : 242
     * pointLocationData : [{"winCount":55,"shootCount":190},{"winCount":153,"shootCount":220},{"winCount":122,"shootCount":218},{"winCount":113,"shootCount":190},{"winCount":85,"shootCount":179},{"winCount":81,"shootCount":184},{"winCount":147,"shootCount":216},{"winCount":129,"shootCount":201},{"winCount":103,"shootCount":183},{"winCount":148,"shootCount":236},{"winCount":106,"shootCount":213},{"winCount":87,"shootCount":209},{"winCount":94,"shootCount":181},{"winCount":71,"shootCount":132}]
     */

    private int scoreAllShootCount;
    private int scoreAllShootWin;
    private int score2ShootCount;
    private int score2ShootWin;
    private int score3ShootCount;
    private int score3ShootWin;
    private String pointLocationData;

    public int getScoreAllShootCount() {
        return scoreAllShootCount;
    }

    public void setScoreAllShootCount(int scoreAllShootCount) {
        this.scoreAllShootCount = scoreAllShootCount;
    }

    public int getScoreAllShootWin() {
        return scoreAllShootWin;
    }

    public void setScoreAllShootWin(int scoreAllShootWin) {
        this.scoreAllShootWin = scoreAllShootWin;
    }

    public int getScore2ShootCount() {
        return score2ShootCount;
    }

    public void setScore2ShootCount(int score2ShootCount) {
        this.score2ShootCount = score2ShootCount;
    }

    public int getScore2ShootWin() {
        return score2ShootWin;
    }

    public void setScore2ShootWin(int score2ShootWin) {
        this.score2ShootWin = score2ShootWin;
    }

    public int getScore3ShootCount() {
        return score3ShootCount;
    }

    public void setScore3ShootCount(int score3ShootCount) {
        this.score3ShootCount = score3ShootCount;
    }

    public int getScore3ShootWin() {
        return score3ShootWin;
    }

    public void setScore3ShootWin(int score3ShootWin) {
        this.score3ShootWin = score3ShootWin;
    }

    public String getPointLocationData() {
        return pointLocationData;
    }

    public void setPointLocationData(String pointLocationData) {
        this.pointLocationData = pointLocationData;
    }
}
