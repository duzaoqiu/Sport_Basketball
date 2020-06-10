package org.opencv.samples.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.util.TimeUtils;

import org.apache.commons.lang3.StringUtils;
import org.opencv.samples.facedetect.R;

import java.util.Calendar;
import java.util.Locale;

public class DateSelectView extends LinearLayout {

    private TextView startDateTxt;
    private TextView endDateTxt;
    private TextView searchBtn;
    private OnSearchListener listener;
    private String mStartDate;
    private String mEndDate;

    public DateSelectView(Context context) {
        super(context);
        init();
    }

    public DateSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DateSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DateSelectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_date_selector, null);
        LayoutParams llps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(llps);
        startDateTxt = view.findViewById(R.id.startDateTxt);
        endDateTxt = view.findViewById(R.id.endDateTxt);
        searchBtn = view.findViewById(R.id.searchBtn);
        addView(view);
        initListener();
    }

    public void setStartDateRange(String mStartDate,String mEndDate) {
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        startDateTxt.setText(mStartDate);
        endDateTxt.setText(mEndDate);
    }


    private void initListener() {
        //初始时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -7);

        //startDate
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int day = instance.get(Calendar.DAY_OF_MONTH);

        mStartDate = formatDay(year, month, day);
        startDateTxt.setText(mStartDate);

        mEndDate = formatDay(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        endDateTxt.setText(mEndDate);

        startDateTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal;
                if (StringUtils.isBlank(mStartDate)) {
                    cal = Calendar.getInstance(Locale.CHINA);
                } else {
                    cal = TimeUtils.stringToCalendar(mStartDate, TimeUtils.DATEFORMAT);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mStartDate = formatDay(year, month, dayOfMonth);
                                startDateTxt.setText(mStartDate);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        endDateTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal;
                if (StringUtils.isBlank(mStartDate)) {
                    cal = Calendar.getInstance(Locale.CHINA);
                } else {
                    cal = TimeUtils.stringToCalendar(mStartDate, TimeUtils.DATEFORMAT);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mEndDate = formatDay(year, month, dayOfMonth);
                                endDateTxt.setText(mEndDate);
                            }
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(mEndDate) && !StringUtils.isEmpty(mStartDate)) {
                    long startDate = Long.parseLong(mStartDate.replace("-", ""));
                    long endDate = Long.parseLong(mEndDate.replace("-", ""));
                    if (endDate < startDate) {
                        ToastUtils.show("结束时间不能小于开始时间");
                        return;
                    }
                    listener.onSearch(mStartDate, mEndDate);
                } else {
                    ToastUtils.show("请选择开始时间和结束时间");
                }
            }
        });
    }

    private String formatDay(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append("-");
        if (month + 1 < 10) {
            sb.append("0");
        }
        sb.append(month + 1);
        sb.append("-");
        if (day < 10) {
            sb.append("0");
        }
        sb.append((day));
        return sb.toString();
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.listener = listener;
    }


    public static interface OnSearchListener {
        void onSearch(String startDate, String endDate);
    }
}
