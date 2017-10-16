package cn.lockyluo.clinicaldepartments.view;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.lockyluo.clinicaldepartments.App;
import cn.lockyluo.clinicaldepartments.R;
import cn.lockyluo.clinicaldepartments.Utils.StartActivityUtil;
import cn.lockyluo.clinicaldepartments.Utils.ToastUtils;
import cn.lockyluo.clinicaldepartments.model.BaseDepartment;
import cn.lockyluo.clinicaldepartments.model.DetailDepartment;

/**
 * Created by LockyLuo on 2017/10/12.
 */

public class ExpandAdapter extends BaseExpandableListAdapter {
    private List<BaseDepartment> data = new ArrayList<>();
    private List<ViewHolder> viewHolders=new ArrayList<>();
    private List<ViewHolderChild> viewHolderChildren=new ArrayList<>();

    private Context context;
    private ViewHolder viewHolder;
    private ViewHolderChild viewHolderChild;

    public void setContext(Context context) {
        this.context = context;
    }

    public List<BaseDepartment> getData() {
        return data;
    }

    public void setData(List<BaseDepartment> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ExpandAdapter(Context context, List<BaseDepartment> data) {
        setContext(context);
        setData(data);
    }



    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public int getChildrenCount(int i) {
        return data.get(i).getDetailDepartments().size();
    }

    @Override
    public Object getGroup(int i) {
        return data.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return data.get(i).getDetailDepartments().get(i1);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//        if (view == null) {
            final BaseDepartment base=data.get(i);
            Logger.d(base);
            view = LayoutInflater.from(context).inflate(R.layout.group_item, null);
            viewHolder=new ViewHolder(view);
            viewHolder.tvItem1.setText(base.getUid());
            viewHolder.tvItem2.setText(base.getBaseDepartment());
            viewHolder.tvItem2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.show(base.getBaseDepartment());
                }
            });
//            viewHolders.add(viewHolder);
//        } else {
//            viewHolder=viewHolders.get(i);
//        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
//        if (view == null) {
            final DetailDepartment detailDepartment=data.get(i).getDetailDepartments().get(i1);
            Logger.d(detailDepartment);

            view = LayoutInflater.from(context).inflate(R.layout.child_item, null);
            viewHolderChild=new ViewHolderChild(view);
            viewHolderChild.tvItem1c.setText(detailDepartment.getUid());
            viewHolderChild.tvItem2c.setText(detailDepartment.getDepartment());
            viewHolderChild.tvItem2c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtils.show(detailDepartment.getDepartment());
                    StringBuffer sb=new StringBuffer();
                    sb.append(WebActivity.urlHead);
                    sb.append("广中医一附院");
                    sb.append(detailDepartment.getDepartment());
                    WebActivity.url=sb.toString();
                    StartActivityUtil.start(context,WebActivity.class);
                }
            });
        viewHolderChild.tvItem2c.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager) App.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(detailDepartment.getDepartment());
                ToastUtils.show("已复制科室名到剪切板");
                return true;
            }
        });
//            view.setTag(viewHolderChild);
//        } else {
//            viewHolderChild=(ViewHolderChild) view.getTag();
//        }
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item1)
        TextView tvItem1;
        @BindView(R.id.tv_item2)
        TextView tvItem2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderChild {
        @BindView(R.id.tv_item1c)
        TextView tvItem1c;
        @BindView(R.id.tv_item2c)
        TextView tvItem2c;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
