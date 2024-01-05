package com.myGenerator.builder;

import com.myGenerator.bean.Constants;
import com.myGenerator.bean.FieldInfo;
import com.myGenerator.bean.TableInfo;
import com.myGenerator.utils.StringUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildPo {

    public static Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo)  {
        File file = new File(Constants.PATH_PO);
        if(!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(file,tableInfo.getBeanName()+".java");
        try {
            BufferedWriter bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
            // 包路径
            bs.write("package "+Constants.PACKAGE_PO +";");
            bs.newLine();
            bs.newLine();
            //

            bs.write("import java.io.Serializable;");
            bs.newLine();
            // 导包
            if(tableInfo.getHaveDate() || tableInfo.getHaveDateTime()){
                bs.write("import java.util.Date;");
                bs.newLine();
                bs.write("import java.text.SimpleDateFormat;");
                bs.newLine();
                bs.write(Constants.DATE_FORMAT_CLASS+";");
                bs.newLine();
                bs.write(Constants.DATE_UNFORMAT_CLASS+";");
                bs.newLine();
            }
            if(tableInfo.getHaveBigDecimal()){
                bs.write("import java.math.BigDecimal;");
                bs.newLine();
            }

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                if(ArrayUtils.contains(Constants.IGNORE_FILED,fieldInfo.getPropertyName())){
                    bs.write(Constants.IGNORE_CLASS+";");
                    bs.newLine();
                    break;
                }
            }


            bs.newLine();

            // 注释
            bs.write("/**");
            bs.newLine();
            bs.write(" * @author "+Constants.AUTHOR_NAME);
            bs.newLine();
            bs.write(" * @date "+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
            bs.newLine();
            bs.write(" * @desc "+tableInfo.getComment());
            bs.newLine();
            bs.write(" */");
            bs.newLine();
            //

            bs.write("public class "+tableInfo.getBeanName()+" implements Serializable {");
            bs.newLine();



            // 遍历字段
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {

                bs.write("\t/**");
                bs.newLine();
                bs.write("\t * "+fieldInfo.getComment());
                bs.newLine();
                bs.write("\t */");
                bs.newLine();

                if(ArrayUtils.contains(Constants.IGNORE_FILED,fieldInfo.getPropertyName())){
                    bs.write("\t"+Constants.IGNORE_EXPRESSION);
                    bs.newLine();
                }
                if(fieldInfo.getJavaType().equals("Date")){
                    bs.write("\t"+Constants.DATE_FORMAT_EXPRESSION);
                    bs.newLine();
                    bs.write("\t"+Constants.DATE_UNFORMAT_EXPRESSION);
                    bs.newLine();
                }
                bs.write("\tpublic "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+";");
                bs.newLine();
            }
            bs.newLine();

            // 生成 get set 方法
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bs.write("\tpublic "+fieldInfo.getJavaType()+" get"+ StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName())+"() {");
                bs.newLine();
                bs.write("\t\treturn "+fieldInfo.getPropertyName()+";");
                bs.newLine();
                bs.write("\t}");
                bs.newLine();
                bs.newLine();

                bs.write("\tpublic void set"+StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName())+"("+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName()+") {");
                bs.newLine();
                bs.write("\t\tthis."+fieldInfo.getPropertyName()+" = "+fieldInfo.getPropertyName()+";");
                bs.newLine();
                bs.write("\t}");
                bs.newLine();
                bs.newLine();
            }

            // 重写 toString 方法
            bs.write("\t@Override");
            bs.newLine();
            bs.write("\tpublic String toString(){");
            bs.newLine();
            bs.write("\t\treturn \""+tableInfo.getBeanName()+"{\""+" +");
            bs.newLine();

            for (int i = 0; i < tableInfo.getFieldList().size(); i++) {
                FieldInfo fieldInfo = tableInfo.getFieldList().get(i);

                bs.write("\t\t\t\t\""+(i!=0?", ":"")+fieldInfo.getPropertyName()+" = \""+" + "+(!fieldInfo.getJavaType().equals("Date")?fieldInfo.getPropertyName():"new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format("+fieldInfo.getPropertyName()+")")+" + ");
                bs.newLine();
            }
            bs.write("\t\t\t\t\'}\';");
            bs.newLine();
            bs.write("\t}");

            bs.newLine();
            //
            bs.write("}");

            bs.flush();
        } catch (Exception e) {
            logger.error("生成{}错误,{}",tableInfo.getBeanName(),e);
        }

    }
}
