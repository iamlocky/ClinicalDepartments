package cn.lockyluo.clinicaldepartments.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import cn.lockyluo.clinicaldepartments.model.BaseDepartment;
import cn.lockyluo.clinicaldepartments.model.DetailDepartment;
import cn.lockyluo.clinicaldepartments.model.InitDataModel;

/**
 * Created by LockyLuo on 2017/10/13.
 */

public class LoadDataAsync extends AsyncTask<OnLoadListener, Integer, List<BaseDepartment>> {
    private static String dbPath;
    private static OnLoadListener onLoadListener;
    private static List<BaseDepartment> data;
    private static File dbFile;
    private static int condition = -1;
    private static String param;


    public static SQLiteDatabase database;
    public static Context context;

    public static final int DEFAULT = 0;
    public static final int FIND_BY_ID = 1;
    public static final int FIND_BY_STRING = 2;

    @IntDef({DEFAULT, FIND_BY_ID, FIND_BY_STRING})
    @Retention(RetentionPolicy.SOURCE)
    @interface Condition {
    }

    public void initTask(Context context, @Condition int condition, String param) {
        this.context = context;
        this.condition = condition;
        this.param = param;
    }

    public void find() {
        if (TextUtils.isEmpty(param))
            return;
        switch (condition) {
            case FIND_BY_ID: {
                try {
                    data = DataSupport.where("uid like\'%" + param+"%\'").find(BaseDepartment.class);
                    if (data.isEmpty() || data == null) {
                        List<DetailDepartment> details = DataSupport.where("uid like\'%" + param+"%\'").find(DetailDepartment.class);
                        if (!details.isEmpty()&&details != null) {
                            data=new ArrayList<>();
                            BaseDepartment base=new BaseDepartment();
                            base.setDetailDepartments(details);
                            base.setBaseDepartment("按id查找");
                            base.setContent("按id查找");
                            base.setUid("");
                            base.setId(0);
                            data.add(base);
                        }
                    }else {
                        getDetailList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.d(data);
                break;
            }

            case FIND_BY_STRING: {
                try {
                    data = DataSupport.where("baseDepartment like\'%" + param+"%\'").find(BaseDepartment.class);
                    if (data.isEmpty() || data == null) {
                        List<DetailDepartment> details = DataSupport.where("department like\'%" + param+"%\'").find(DetailDepartment.class);
                        if (!details.isEmpty()&&details != null) {
                            data=new ArrayList<>();
                            BaseDepartment base=new BaseDepartment();
                            base.setDetailDepartments(details);
                            base.setBaseDepartment("按科室名查找");
                            base.setContent("按科室名查找");
                            base.setUid("");
                            base.setId(0);
                            data.add(base);
                        }
                    }else {
                        getDetailList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.d(data);
                break;
            }
        }
    }

    private void getDetailList(){
        if (data==null)
        {
            Logger.e("data is null");
            return;
        }
        for (int i = 0; i <data.size() ; i++) {
            List<DetailDepartment> details = DataSupport.where("basedepartment_id=" + data.get(i).getId()).find(DetailDepartment.class);
            if (!details.isEmpty()&&details != null) {
                data.get(i).setDetailDepartments(details);
            }
        }
    }

    @Override
    protected List<BaseDepartment> doInBackground(OnLoadListener... onLoadListeners) {
        if (onLoadListeners[0] != null && context != null) {
            onLoadListener = onLoadListeners[0];
        } else {
            return null;
        }

        if (condition == DEFAULT) {
            dbPath = context.getFilesDir().getParentFile().getAbsolutePath() + "/databases";
            dbFile = new File(dbPath + "/departments.db");
            try {
                if (!dbFile.exists()) {
                    database = Connector.getDatabase();
                    data = InitDataModel.initData(context);
                    DataSupport.saveAll(data);
                    Logger.i("---new db---");

                } else {
                    data = DataSupport.findAll(BaseDepartment.class);
                    for (int i = 0; i < data.size(); i++) {
                        List<DetailDepartment> details = DataSupport.where("basedepartment_id=" + (i + 1)).find(DetailDepartment.class);
                        data.get(i).setDetailDepartments(details);
                    }
                    Logger.i("---db exist---");

                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            find();
        }


        Logger.d("initData finish");
        return data;
    }

    @Override
    protected void onPostExecute(List<BaseDepartment> baseDepartments) {
        if (onLoadListener != null) {
            onLoadListener.onFinish(baseDepartments);
        } else {
            Logger.d("onLoadListener is null");
        }
        super.onPostExecute(baseDepartments);
    }
}
