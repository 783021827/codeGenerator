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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BuildMapper {

    public static Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo)  {
        File file = new File(Constants.PATH_MAPPER);
        if(!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(file,tableInfo.getBeanName()+"Mapper"+".java");
        try {
            BufferedWriter bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
            // 包路径
            bs.write("package "+Constants.PACKAGE_MAPPER +";");
            bs.newLine();
            bs.newLine();
            //

            bs.write("import org.apache.ibatis.annotations.Param;");
            // 注释
            bs.newLine();
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

            bs.write("public interface "+tableInfo.getBeanName()+"Mapper<T, P>"+" extends BaseMapper {");
            bs.newLine();


            for (Map.Entry<String, List<FieldInfo>> entry : tableInfo.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fieldInfos = entry.getValue();
                StringBuilder sb = new StringBuilder();
                FieldInfo info = fieldInfos.get(0);

                String methodName = sb.toString();
                if(fieldInfos.size()>1){
                    for (FieldInfo fieldInfo : fieldInfos) {
                        sb.append(StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName()));
                        sb.append("And");
                    }
                    String str = sb.toString();
                    methodName = str.substring(0,str.lastIndexOf("And"));

                }else{
                    methodName = StringUtils.uppercaseFirstLetter(info.getPropertyName());
                }


                if(fieldInfos.size()>1){
                    //
                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"查询");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    StringBuilder sb1=new StringBuilder();
                    bs.write("\tT selectBy"+methodName+"(");

                    for (FieldInfo fieldInfo : fieldInfos) {
                        sb1.append("@Param(\""+fieldInfo.getPropertyName()+"\") "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                        sb1.append(",");
                    }
                    String tmp = sb1.toString();
                    bs.write(tmp.substring(0,tmp.lastIndexOf(",")));
                    bs.write(");");
                    bs.newLine();
                    bs.newLine();
                    //

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"更新");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    StringBuilder sb2=new StringBuilder();
                    bs.write("\tInteger updateBy"+methodName+"(@Param(\"bean\") T t, ");

                    for (FieldInfo fieldInfo : fieldInfos) {
                        sb2.append("@Param(\""+fieldInfo.getPropertyName()+"\") "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                        sb2.append(",");
                    }
                    String tmp1 = sb2.toString();
                    bs.write(tmp1.substring(0,tmp1.lastIndexOf(",")));
                    bs.write(");");
                    bs.newLine();
                    bs.newLine();
                    //

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"删除");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    StringBuilder sb3=new StringBuilder();
                    bs.write("\tInteger deleteBy"+methodName+"(");

                    for (FieldInfo fieldInfo : fieldInfos) {
                        sb3.append("@Param(\""+fieldInfo.getPropertyName()+"\") "+fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                        sb3.append(",");
                    }
                    String tmp2 = sb3.toString();
                    bs.write(tmp2.substring(0,tmp2.lastIndexOf(",")));
                    bs.write(");");
                    bs.newLine();

                }else {
                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"查询");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();
                    bs.write("\tT selectBy"+methodName+"(@Param(\""+info.getPropertyName()+"\") "+info.getJavaType()+" "+info.getPropertyName()+");");
                    bs.newLine();
                    bs.newLine();

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"更新");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();
                    bs.write("\tInteger updateBy"+methodName+"(@Param(\"bean\") T t, @Param(\""+info.getPropertyName()+"\") "+info.getJavaType()+" "+info.getPropertyName()+");");
                    bs.newLine();
                    bs.newLine();

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"删除");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();
                    bs.write("\tInteger deleteBy"+methodName+"(@Param(\""+info.getPropertyName()+"\") "+info.getJavaType()+" "+info.getPropertyName()+");");
                    bs.newLine();
                    bs.newLine();
                }

                bs.newLine();


            }


            bs.write("}");
            bs.flush();
        } catch (Exception e) {
            logger.error("生成{}错误,{}",tableInfo.getBeanName(),e);
        }

    }
}
