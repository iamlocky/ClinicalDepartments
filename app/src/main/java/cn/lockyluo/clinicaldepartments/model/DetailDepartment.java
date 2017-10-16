package cn.lockyluo.clinicaldepartments.model;

import org.litepal.crud.DataSupport;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lockyluo on 2017/10/10.
 */
@Data
@EqualsAndHashCode(callSuper = false)

public class DetailDepartment extends DataSupport {
    private int id;
    private String uid;
    private String department;
    private String content;
}
