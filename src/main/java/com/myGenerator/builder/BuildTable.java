package com.myGenerator.builder;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.myGenerator.bean.Constants;
import com.myGenerator.bean.FieldInfo;
import com.myGenerator.bean.TableInfo;
import com.myGenerator.utils.JsonUtil;
import com.myGenerator.utils.PropertiesUtil;
import com.myGenerator.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.myGenerator.utils.PropertiesUtil.getString;

public class BuildTable {
    public static Connection connection = null;
    public static final Logger logger = LoggerFactory.getLogger(BuildTable.class);

    public static String SQL_SHOW_TABLES = "show table status";

    public static String SQL_TABLE_INFO = "show full fields from ";

    public static String SHOW_INDEX = "show index from ";

    // 表前缀
    private static String prefix;

    // 初始化
    static {
        // 初始化数据库信息
        String driverName = getString("db.driverName");
        String url = getString("db.url");
        String username = getString("db.username");
        String password = getString("db.password");

        // 初始化表名前缀
        prefix = getString("ignore.table.prefixInfo");
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("数据库连接失败");
        }
    }

    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TableInfo> tableInfos = new ArrayList<>();
        try {
            ps = connection.prepareStatement(SQL_SHOW_TABLES);
            rs = ps.executeQuery();
            while (rs.next()) {
                // 表的基本信息

                String tableName = rs.getString("name");
                String comment = rs.getString("comment");

                Boolean haveDate = false;
                Boolean haveDateTime = false;
                Boolean havaBigDecimal = false;

                TableInfo tableInfo = new TableInfo();

                tableInfo.setTableName(tableName);
                tableInfo.setComment(comment);

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX && beanName.indexOf(prefix) != -1) {
                    beanName = beanName.substring(prefix.length());
                }
                beanName = upperCaseLetter(beanName, true);

                tableInfo.setBeanName(beanName);


                // 表的字段 读取 ///

                HashMap<String, FieldInfo> map = new HashMap<>();


                List<FieldInfo> fieldInfos = new ArrayList<>();

                PreparedStatement ps1 = connection.prepareStatement(SQL_TABLE_INFO + tableName);

                ResultSet rs1 = ps1.executeQuery();

                while (rs1.next()) {
                    FieldInfo fieldInfo = new FieldInfo();
                    String field1 = rs1.getString("field");
                    String type1 = rs1.getString("type");
                    String comment1 = rs1.getString("comment");
                    String isAuto = rs1.getString("extra");

                    if (type1.indexOf("(") > 0) {
                        type1 = type1.substring(0, type1.indexOf("("));
                    }

                    if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type1)) {
                        haveDate = true;
                    } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, type1)) {
                        haveDateTime = true;
                    } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type1)) {
                        havaBigDecimal = true;
                    }

                    fieldInfo.setFieldName(field1);
                    fieldInfo.setPropertyName(upperCaseLetter(field1, false));
                    fieldInfo.setComment(comment1);
                    fieldInfo.setSqlType(type1);
                    fieldInfo.setJavaType(setJavaType(type1));
                    fieldInfo.setAutoIncrement(isAuto.equals("auto_increment") ? true : false);

                    map.put(field1, fieldInfo);

                    fieldInfos.add(fieldInfo);
                }


                //////////


                tableInfo.setFieldList(fieldInfos);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAMS);

                tableInfo.setHaveDate(haveDate);
                tableInfo.setHaveDateTime(haveDateTime);
                tableInfo.setHaveBigDecimal(havaBigDecimal);

                //// 表的索引

                PreparedStatement ps2 = connection.prepareStatement(SHOW_INDEX + tableName);

                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    String keyName = rs2.getString("key_name");
                    Integer nonUnique = rs2.getInt("non_unique");
                    String columnName = rs2.getString("column_name");
                    if (nonUnique == 1) {
                        continue;
                    }
                    List<FieldInfo> infos = tableInfo.getKeyIndexMap().get(keyName);

                    if (infos == null) {
                        infos = new ArrayList<>();
                        tableInfo.getKeyIndexMap().put(keyName, infos);
                    }
                    infos.add(map.get(columnName));
//                    for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
//                        if (fieldInfo.getFieldName().equals(columnName)) {
//                            infos.add(fieldInfo);
//                        }
//                    }
                }

                ///
//                logger.info(JsonUtil.coverObject2Json(tableInfo));

                tableInfos.add(tableInfo);
            }


        } catch (SQLException e) {
            logger.error("读取表失败");
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {

                }
            }

        }
        return tableInfos;

    }

    public static String upperCaseLetter(String filed, Boolean isUpper) {
        String[] split = filed.split("_");
        StringBuffer sb = new StringBuffer();
        sb.append(isUpper ? StringUtils.uppercaseFirstLetter(split[0]) : split[0]);
        for (int i = 1; i < split.length; i++) {
            sb.append(StringUtils.uppercaseFirstLetter(split[i]));
        }
        return sb.toString();
    }

    public static String setJavaType(String str) {
        if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, str)) {
            return "String";
        } else if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, str)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, str) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, str)) {
            return "Date";
        } else if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, str)) {
            return "BigDecimal";
        } else if (ArrayUtils.contains(Constants.SQL_LONG_TYPE, str)) {
            return "Long";
        } else {
            throw new RuntimeException("无法识别的类型:" + str);
        }
    }
}
