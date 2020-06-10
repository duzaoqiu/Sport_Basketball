package org.opencv.samples.facedetect.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bench.android.core.app.dialog.BaseDialog;
import com.bench.android.core.app.dialog.holder.ViewHolder;

import org.opencv.samples.facedetect.R;

/**
 * 分数描述Dialog
 *
 * @author zhouyi
 */
public class PointDescDialog extends BaseDialog {
    public PointDescDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_point_desc;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        TextView tv1 = holder.getView(R.id.tv1);
        TextView tv2 = holder.getView(R.id.tv2);
        TextView tv3 = holder.getView(R.id.tv3);
        TextView tv4 = holder.getView(R.id.tv4);

        tv1.setText("70<命中率<=100");
        tv2.setText("40<命中率<=70");
        tv3.setText("10<命中率<=40");
        tv4.setText("0<命中率<=10");
    }
}
