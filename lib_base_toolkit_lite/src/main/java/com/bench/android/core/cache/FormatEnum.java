package com.bench.android.core.cache;

/************************************************************************
 *@Project: thirdsParty
 *@Package_Name: com.bench.android.core.cache
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/8/15 
 *************************************************************************/
public enum FormatEnum {
    //图片格式
    IMG("img",  "jpg", "jpeg", "gif", "png", "bmp", "tiff"),

    //文本格式
    TXT("txt", "txt"),

    //文档格式
    WORD("word", "docx", "dotx", "doc", "dot", "pagers"),

    //电子表格
    EXCEL("excel", "xls", "xlsx", "xlt", "xltx"),

    //ppt
    PPT("ppt", "ppt", "pptx"),

    //pdf
    PDF("pdf", "pdf"),

    //音频格式
    MP3("audio", "mp3", "wav", "wma","amr","awb","ogg"),

    //视频格式
    VIDEO("video", "avi", "flv", "mpg", "mpeg", "mp4", "3gp", "mov", "rmvb", "mkv"),

    //网页格式
    HTML("html",  "html"),

    //cad
    CAD("cad", "dwg","dxf","dwt"),

    //ps
    PS("ps", "psd", "pdd"),

    //max
    MAX3D("3DMax",  "max"),

    //压缩包
    ZIP("zip", "zip", "jar", "rar", "7z"),

    //未知格式
    UNKNOWN("unknown");

    private static final String TAG = "FormatEnum";
    public String type;
    public String[] formats;

    /**
     * @param type    文件类型
     * @param formats 包含格式
     */
    FormatEnum(String type, String... formats) {
        this.type = type;
        this.formats = formats;
    }

    /**
     * 通过文件类型获取对应枚举
     *
     * @param extension 文件扩展名
     * @return 文件对应的枚举信息，如果没有，返回未知
     */
    public static FormatEnum getFormat(String extension) {
        for (FormatEnum format : FormatEnum.values()) {
            for (String extend : format.formats) {
                if (extend.equalsIgnoreCase(extension)) {
                    return format;
                }
            }
        }
        return UNKNOWN;
    }
}
