package com.bench.android.core.view.webview;

import java.util.List;

/**
 * Created by zaozao on 2018/8/15.
 * 详情页webView显示内容
 */
public class SubjectDetailWebShowModel {

    public SubjectShowContent subjectContent;

    /**
     * 话题内容,资讯内容
     *
     * @param newsContent
     */
    public SubjectDetailWebShowModel(String newsContent) {

        if (subjectContent == null) {
            subjectContent = new SubjectShowContent();
        }

        subjectContent.sourceContent = newsContent;
    }

    public SubjectShowContent getSubjectContent() {
        return subjectContent;
    }

    public void setSubjectContent(SubjectShowContent subjectContent) {
        this.subjectContent = subjectContent;
    }

    public static class SubjectShowContent {

        private String sourceContent;//原话题内容（转发的时候取source里面的content，非转发直接取外面的content）

        private String sourceTitle;//"heiren"

        private String sourceAuthorName;//"09",

        private List<SupplementBean> sourceSupplementList;//原话题追加（转发的时候取source里面的list，非转发直接取外面的追加list）

        private List<SupplementBean> transmitSupplementList;//转发的追加

        public void setSourceContent(String sourceContent) {
            this.sourceContent = sourceContent;
        }

        public void setSourceTitle(String sourceTitle) {
            this.sourceTitle = sourceTitle;
        }

        public void setSourceAuthorName(String sourceAuthorName) {
            this.sourceAuthorName = sourceAuthorName;
        }

        public void setSourceSupplementList(List<SupplementBean> sourceSupplementList) {
            this.sourceSupplementList = sourceSupplementList;
        }

        public void setTransmitSupplementList(List<SupplementBean> transmitSupplementList) {
            this.transmitSupplementList = transmitSupplementList;
        }

        public String getSourceTitle() {
            return sourceTitle;
        }

        public String getSourceAuthorName() {
            return sourceAuthorName;
        }

        public List<SupplementBean> getSourceSupplementList() {
            return sourceSupplementList;
        }

        public List<SupplementBean> getTransmitSupplementList() {
            return transmitSupplementList;
        }

        public String getSourceContent() {
            return sourceContent;
        }
    }

    /**
     * 追加内容,kk业务相关
     */
    public static class SupplementBean {

        public String gmtCreate;

        public String content;

    }
}
