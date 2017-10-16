package cn.lockyluo.clinicaldepartments.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lockyluo.clinicaldepartments.R;
import scut.carson_ho.searchview.BackCallBack;
import scut.carson_ho.searchview.SearchCallBack;
import scut.carson_ho.searchview.SearchView;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    SearchView searchView;

    static SearchCallBack onSearch;
    static BackCallBack onBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle("关键字查询");
        initView();
    }

    private void initView() {
        onSearch = new SearchCallBack() {
            @Override
            public void SearchAciton(String string) {
                setResult(string);
            }
        };
        onBack = new BackCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        };
        searchView.setOnSearchClick(onSearch);
        searchView.setOnBackClick(onBack);

    }

    private void setResult(String result) {
        Intent intent = new Intent();
        intent.putExtra("data", result);
        setResult(1002, intent);
        finish();
    }


}
