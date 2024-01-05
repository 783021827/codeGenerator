package com.myGenerator.builder;

import com.myGenerator.bean.Constants;
import com.myGenerator.bean.FieldInfo;
import com.myGenerator.bean.TableInfo;
import com.myGenerator.utils.StringUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {
    public static Logger logger = LoggerFactory.getLogger(BuildPo.class);



    public static List<FieldInfo> execute(TableInfo tableInfo) {

        List<FieldInfo> extendField = new ArrayList<>();
        File file = new File(Constants.PATH_QUERY);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file, tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAMS + ".java");
        try {
            BufferedWriter bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
            // 包路径
            bs.write("package " + Constants.PACKAGE_QUERY + ";");
            bs.newLine();
            bs.newLine();
            //

            List<FieldInfo> fieldList = tableInfo.getFieldList();


            // 导包
            if (tableInfo.getHaveDate() || tableInfo.getHaveDateTime()) {
                bs.write("import java.util.Date;");
                bs.newLine();
            }
            if (tableInfo.getHaveBigDecimal()) {
                bs.write("import java.math.BigDecimal;");
                bs.newLine();
            }


            bs.newLine();


            bs.write("public class " + tableInfo.getBeanName() + Constants.SUFFIX_BEAN_PARAMS + " extends BaseParam {");
            bs.newLine();

            // 遍历字段
            for (FieldInfo fieldInfo : fieldList) {

                bs.write("\t/**");
                bs.newLine();
                bs.write("\t * " + fieldInfo.getComment());
                bs.newLine();
                bs.write("\t */");
                bs.newLine();

                bs.write("\tpublic " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bs.newLine();

                if (fieldInfo.getJavaType().equals("String")) {
                    bs.newLine();
                    bs.write("\tpublic " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_FUZZY + ";");
                    bs.newLine();

                    FieldInfo fieldInfo1 = new FieldInfo();
                    fieldInfo1.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_FUZZY);
                    fieldInfo1.setFieldName(fieldInfo.getFieldName());
                    fieldInfo1.setSqlType(fieldInfo.getSqlType());
                    fieldInfo1.setJavaType(fieldInfo.getJavaType());


                    extendField.add(fieldInfo1);
                }
                if (fieldInfo.getJavaType().equals("Date")) {
                    bs.newLine();
                    bs.write("\tpublic String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_DATE_START + ";");
                    bs.newLine();

                    bs.newLine();
                    bs.write("\tpublic String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_DATE_END + ";");
                    bs.newLine();

                    FieldInfo fieldInfo1 = new FieldInfo();
                    fieldInfo1.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_DATE_START);
                    fieldInfo1.setSqlType(fieldInfo.getSqlType());
                    fieldInfo1.setFieldName(fieldInfo.getFieldName());

                    FieldInfo fieldInfo2 = new FieldInfo();
                    fieldInfo2.setPropertyName(fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_PARAM_DATE_END);
                    fieldInfo2.setSqlType(fieldInfo.getSqlType());
                    fieldInfo2.setFieldName(fieldInfo.getFieldName());


                    fieldInfo1.setJavaType("String");
                    fieldInfo2.setJavaType("String");
                    extendField.add(fieldInfo1);
                    extendField.add(fieldInfo2);
                }
            }
            bs.newLine();

            // 生成 get set 方法
            for (FieldInfo fieldInfo : fieldList) {
                bs.write("\tpublic " + fieldInfo.getJavaType() + " get" + StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName()) + "() {");
                bs.newLine();
                bs.write("\t\treturn " + fieldInfo.getPropertyName() + ";");
                bs.newLine();
                bs.write("\t}");
                bs.newLine();
                bs.newLine();

                bs.write("\tpublic void set" + StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName()) + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bs.newLine();
                bs.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                bs.newLine();
                bs.write("\t}");
                bs.newLine();
                bs.newLine();
            }

            for (FieldInfo fieldInfo : extendField) {
                bs.write("\tpublic " + fieldInfo.getJavaType() + " get" + StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName()) + "() {");
                bs.newLine();
                bs.write("\t\treturn " + fieldInfo.getPropertyName() + ";");
                bs.newLine();
                bs.write("\t}");
                bs.newLine();
                bs.newLine();

                bs.write("\tpublic void set" + StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName()) + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bs.newLine();
                bs.write("\t\tthis." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                bs.newLine();
                bs.write("\t}");
                bs.newLine();
                bs.newLine();
            }

            bs.write("}");

            bs.flush();
        } catch (Exception e) {
            logger.error("生成{}错误,{}", tableInfo.getBeanName(), e);
        }
        return extendField;
    }

}
