package com.lsl.nature.common.db.mapper.condition;

import com.lsl.nature.common.db.mapper.pojo.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询条件类
 */
@Slf4j
public class QueryFilter {

    private static final String OR_COMPLEX_FIELD_NAME = "OR_COMPLEX_FIELD";

    @Getter
    private final Table table;

    @Getter
    private List<String> fieldNames;

    @Getter
    private List<CondDBField> condFields;

    @Getter
    private List<SortDBField> sortFields;

    @Getter
    private List<String> groupFields;

    @Getter
    private DBPage pager;

    public QueryFilter(Table table) {
        if (table == null) {
            log.error("invalid table: 传入的参数table（表）为null");
            throw new IllegalArgumentException("invalid table");
        }
        this.table = table;
        setFieldNames(table.getFields());
    }

    /**
     * 添加一个排序字段，默认升序
     *
     * @param filedName
     */
    public void addSortField(String filedName) {
        addSortField(filedName, true);
    }

    /**
     * 添加一个排序字段
     *
     * @param filedName
     * @param isAsc
     */
    public void addSortField(String filedName, boolean isAsc) {
        if (StringUtils.isEmpty(filedName)) {
            return;
        }

        this.addSortField(new SortDBField(filedName.trim(), isAsc));
    }

    /**
     * 添加一个排序字段
     *
     * @param sortField
     *
     */
    public void addSortField(SortDBField sortField) {
        if (sortField == null) {
            return;
        }

        checkFieldName(sortField.getFieldName());

        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }
        sortFields.add(sortField);
    }

    /**
     * 添加或者的复合条件
     *
     * @param cond
     * @return
     */
    public CondDBField addOrConds(OrCondDBFields cond) {
        return addCond(OR_COMPLEX_FIELD_NAME, cond);
    }

    /**
     * 添加某个字段的查询条件
     *
     * @param fieldName
     * @param value
     * @return
     */
    public CondDBField addCond(String fieldName, Object value) {
        if (StringUtils.isEmpty(fieldName) || value == null) {
            return null;
        }

        return addCond(new CondDBField(fieldName, value));
    }

    /**
     * 删除某个字段的查询条件
     *
     * @param fieldName
     * @return
     */
    public boolean removeCond(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }

        boolean isRemoved = false;
        for (int index = condFields.size() - 1; index >= 0; index--) {
            if (condFields.get(index).getFieldName().equals(fieldName)) {
                condFields.remove(index);
                isRemoved = true;
            }
        }

        return isRemoved;
    }

    /**
     * 检查字段名是否合法
     *
     * @param fieldName
     */
    private void checkFieldName(String fieldName) {
        if (!fieldName.equals(OR_COMPLEX_FIELD_NAME) && !table.containsField(fieldName)) {
            log.error("invalid field name: 没有当前表并且fieldName与OR_COMPLEX_FIELD_NAME常量也不相等", new RuntimeException());
            throw new IllegalArgumentException("invalid field name " + fieldName + " of table " + table.getTableName());
        }
    }

    /**
     * 添加一个查询条件
     *
     * @param field
     * @return
     */
    public CondDBField addCond(CondDBField field) {
        if (field == null) {
            return null;
        }

        checkFieldName(field.getFieldName());

        if (condFields == null) {
            condFields = new ArrayList<>();
        }
        condFields.add(field);
        return field;
    }

    /**
     * 由外面的分页转换为数据库分页
     *
     * @param pager
     */
    public void setPager(Page pager) {
        if (pager != null) {
            this.pager = new DBPage((pager.getCurrPage() - 1) * pager.getPageSize(), pager.getPageSize());
        }
    }

    /**
     * 设置分页
     * @param pager
     */
    public void setPager(DBPage pager) {
        this.pager = pager;
    }

    /**
     * 设置表中的字段
     *
     * @param nameList
     */
    private void setFieldNames(List<String> nameList) {
        if (nameList != null) {
            this.fieldNames = nameList;
        }
    }

    /**
     * 添加分组字段
     *
     * @param fieldName
     */
    public void addGroupField(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return;
        }
        checkFieldName(fieldName);

        if (this.groupFields == null) {
            this.groupFields = new ArrayList<>();
        }
        this.groupFields.add(fieldName);
    }

}
