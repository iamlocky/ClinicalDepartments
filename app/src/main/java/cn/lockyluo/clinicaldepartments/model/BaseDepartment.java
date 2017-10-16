package cn.lockyluo.clinicaldepartments.model;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lockyluo on 2017/10/10.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDepartment extends DataSupport{
    private int id;
    private String uid;
    private String baseDepartment;
    private String content;
    private List<DetailDepartment> detailDepartments=new ArrayList<>();
}
