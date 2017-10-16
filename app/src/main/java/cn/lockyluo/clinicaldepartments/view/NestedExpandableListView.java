package cn.lockyluo.clinicaldepartments.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by LockyLuo on 2017/10/13.
 */

public class NestedExpandableListView extends ExpandableListView {
    public NestedExpandableListView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sxpandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, sxpandSpec);
    }
}
