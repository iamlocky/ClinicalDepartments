package cn.lockyluo.clinicaldepartments.model;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.lockyluo.clinicaldepartments.R;

/**
 * Created by LockyLuo on 2017/10/10.
 */

public class InitDataModel {
    public static List<BaseDepartment> initData(Context context) {
        List<String[]> detailList = new ArrayList<>();
        detailList.add(context.getResources().getStringArray(R.array.d0));
        detailList.add(context.getResources().getStringArray(R.array.d1));
        detailList.add(context.getResources().getStringArray(R.array.d2));
        detailList.add(context.getResources().getStringArray(R.array.d3));
        detailList.add(context.getResources().getStringArray(R.array.d4));
        detailList.add(context.getResources().getStringArray(R.array.d5));
        detailList.add(context.getResources().getStringArray(R.array.d6));

        List<BaseDepartment> baseList = new ArrayList<>();
        List<String> baseDepartStrList = new ArrayList<>();
        List<DetailDepartment> detailDepartList;
        baseDepartStrList.add("内科");
        baseDepartStrList.add("妇产科");
        baseDepartStrList.add("骨科");
        baseDepartStrList.add("肿瘤科");
        baseDepartStrList.add("外科");
        baseDepartStrList.add("独立科室");
        baseDepartStrList.add("辅助部门");
        for (int i = 0; i < baseDepartStrList.size(); i++) {
            BaseDepartment base = new BaseDepartment();
            detailDepartList = new ArrayList<>();
            for (int j = 0; j < detailList.get(i).length; j++) {
                DetailDepartment detail = new DetailDepartment();
                detail.setDepartment(detailList.get(i)[j]);
                detail.setContent("科室介绍"+String.format(Locale.getDefault(),"%02d", j+1));
                detail.setUid(String.format(Locale.getDefault(),"%02d", i+1)+String.format(Locale.getDefault(),"%02d", j+1));//小科

                detailDepartList.add(detail);
            }
            DataSupport.saveAll(detailDepartList);
            base.setUid(String.format(Locale.getDefault(),"%02d", i+1));//大科
            base.setBaseDepartment(baseDepartStrList.get(i));
            base.setDetailDepartments(detailDepartList);
            base.setContent("分科介绍"+String.format(Locale.getDefault(),"%02d", i+1));

            baseList.add(base);
            Logger.d(baseList.get(i));
        }
        return baseList;
    }
}
