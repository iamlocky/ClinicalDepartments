package cn.lockyluo.clinicaldepartments.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.lockyluo.clinicaldepartments.R;
import cn.lockyluo.clinicaldepartments.Utils.FileUtils;
import cn.lockyluo.clinicaldepartments.Utils.StartActivityUtil;
import cn.lockyluo.clinicaldepartments.Utils.StringUtils;
import cn.lockyluo.clinicaldepartments.Utils.ToastUtils;
import cn.lockyluo.clinicaldepartments.model.BaseDepartment;
import cn.lockyluo.clinicaldepartments.presenter.LoadDataAsync;
import cn.lockyluo.clinicaldepartments.presenter.OnLoadListener;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.iv_mine_head)
    CircleImageView ivMineHead;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.exListview)
    NestedExpandableListView exListview;


    private RxPermissions rxPermissions;
    private String dbPath;
    private String key;
    private ExpandAdapter expandAdapter;
    private LoadDataAsync loadDataAsync;
    private OnLoadListener onLoadListener;
    private Boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//-----------
        ButterKnife.bind(this);
        dbPath = getApplicationContext().getFilesDir().getParentFile().getAbsolutePath() + "/databases";

        initView();
        getPermission();
        progressbar.setVisibility(View.VISIBLE);
        onLoadListener = new OnLoadListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish(List<BaseDepartment> data) {
                final int size;
                if (data == null) {
                    size = 0;
                    data = new ArrayList<>();
                } else {
                    size = data.size();
                }
                expandAdapter = new ExpandAdapter(getApplicationContext(), data);
                exListview.setAdapter(expandAdapter);

                progressbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setVisibility(View.GONE);
                        if (isResult && size != 0) {
                            isResult = false;
                            exListview.expandGroup(0);
                        }
                        fab.show();
                    }
                }, 250);
            }
        };

        initData(onLoadListener);
    }


    void getPermission() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            ToastUtils.show("无sd权限");
                            finish();
                        }
                    }
                });

    }


    void initView() {
        setSupportActionBar(toolbar);

        setTitle("一附院科室查询");
        exListview.setDividerHeight(1);
        Glide.with(this)
                .load("http://www.gztcm.com.cn/Portals/0/GZTCM/IMG_7690.jpg")
                .crossFade(1500)
                .error(R.drawable.gzucm_first)
                .into(ivMineHead);

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                progressbar.setVisibility(View.VISIBLE);
                initData(onLoadListener);
                return true;
            }
        });
    }

    void initData(OnLoadListener onLoadListener) {
        progressbar.setVisibility(View.VISIBLE);
        loadDataAsync = new LoadDataAsync();
        loadDataAsync.initTask(this, LoadDataAsync.DEFAULT, null);
        loadDataAsync.execute(onLoadListener);
    }

    File newFile;

    void checkSDPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.show("无sd权限");
            getPermission();
            return;
        }
    }

    void saveDB() {
        checkSDPermission();
        String fileName = "/departments.db";
        try {
            Logger.d(dbPath);
            newFile = new File(FileUtils.getSaveFolder("departments").getAbsoluteFile() + fileName);
            if (newFile.exists())
                newFile.delete();
            FileUtils.copyFile(new File(dbPath + fileName), newFile);
            ToastUtils.show("已复制数据库到文件夹" + newFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.show("复制失败，请重试");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            if (data != null) {
                try {
                    key = data.getStringExtra("data");
                    progressbar.setVisibility(View.VISIBLE);
                    loadDataAsync = new LoadDataAsync();
                    if (key.equals(""))
                        throw new Exception("result is null string");

                    if (StringUtils.isNumeric(key))
                        loadDataAsync.initTask(getApplicationContext(), LoadDataAsync.FIND_BY_ID, key);
                    else
                        loadDataAsync.initTask(getApplicationContext(), LoadDataAsync.FIND_BY_STRING, key);

                    loadDataAsync.execute(onLoadListener);
                    isResult = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ToastUtils.show("Powered by 2015082009 骆庭蔚");
            return true;
        }
        if (id == R.id.action_search) {
            StartActivityUtil.startForResult(this, SearchActivity.class, 1001);
            return true;
        }
        if (id == R.id.action_save) {
            saveDB();
            return true;
        }
        if (id == R.id.action_default) {
            initData(onLoadListener);
            return true;
        }
        if (id == R.id.action_delete) {
            FileUtils.deleteAllFiles(new File(dbPath));
            ToastUtils.show("请重启app");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.iv_mine_head)
    public void onIvMineHeadClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gztcm.com.cn/"));
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        StartActivityUtil.startForResult(MainActivity.this, SearchActivity.class, 1001);
    }

//-------------------------------------双击退出函数

    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            ToastUtils.show(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

}
