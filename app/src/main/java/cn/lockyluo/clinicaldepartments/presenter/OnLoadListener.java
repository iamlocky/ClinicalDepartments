package cn.lockyluo.clinicaldepartments.presenter;

import android.content.Context;

import java.util.List;

import cn.lockyluo.clinicaldepartments.model.BaseDepartment;

/**
 * Created by LockyLuo on 2017/10/14.
 */

public interface OnLoadListener {
    void onStart();
    void onFinish(List<BaseDepartment> data);

}
