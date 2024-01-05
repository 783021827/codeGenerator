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

public class BuildController {

    public static Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo)  {
        File file = new File(Constants.PATH_CONTROLLER);
        if(!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(file,tableInfo.getBeanName()+"Controller.java");
        try {
            BufferedWriter bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
            // 包路径
            bs.write("package "+Constants.PACKAGE_CONTROLLER +";");
            bs.newLine();
            bs.newLine();
            //

            bs.write("import java.util.List;");
            bs.newLine();
            bs.write("import javax.annotation.Resource;");
            bs.newLine();
            bs.write("import org.springframework.web.bind.annotation.RequestMapping;");
            bs.newLine();
            bs.write("import org.springframework.web.bind.annotation.RestController;");
            bs.newLine();

            bs.write("import org.springframework.web.bind.annotation.RequestBody;");
            bs.newLine();


            bs.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanName()+"Query;");
            bs.newLine();

            bs.write("import "+Constants.PACKAGE_SERVICE+"."+tableInfo.getBeanName()+"Service;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_VO+".ResponseVo;");
            bs.newLine();


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
            bs.newLine();
            //

            String clazzName = tableInfo.getBeanName()+"Controller";
            String queryName = tableInfo.getBeanName()+"Query";
            String name = tableInfo.getBeanName();
            String serviceName = name +"Service";
            serviceName = StringUtils.lowercaseFirstLetter(serviceName);
            bs.write("@RestController");
            bs.newLine();
            bs.write("@RequestMapping(\""+StringUtils.lowercaseFirstLetter(name)+"\")");
            bs.newLine();
            bs.write("public class "+clazzName+" extends ABaseController{");
            bs.newLine();
            bs.newLine();

            bs.write("\t@Resource");
            bs.newLine();
            bs.write("\tprivate "+name+"Service "+serviceName+";");
            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 根据条件分页查询列表");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\t@RequestMapping(\"loadDataList\")");
            bs.newLine();
            bs.write("\tpublic ResponseVo loadDataList("+queryName+" query){");
            bs.newLine();
            bs.write("\t\treturn getSuccessResponseVo("+serviceName+".findListByPage(query));");
            bs.newLine();
            bs.write("\t}");
            bs.newLine();
            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 新增");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\t@RequestMapping(\"add\")");
            bs.newLine();
            bs.write("\tpublic ResponseVo add("+tableInfo.getBeanName()+" bean){");
            bs.newLine();
            bs.write("\t\t"+serviceName+".add(bean);");
            bs.newLine();
            bs.write("\t\treturn getSuccessResponseVo(null);");

            bs.newLine();
            bs.write("\t}");

            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 批量新增");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\t@RequestMapping(\"addBatch\")");
            bs.newLine();
            bs.write("\tpublic ResponseVo addBatch(@RequestBody List<"+tableInfo.getBeanName()+"> listBean){");
            bs.newLine();
            bs.write("\t\t"+serviceName+".addBatch(listBean);");
            bs.newLine();
            bs.write("\t\treturn getSuccessResponseVo(null);");

            bs.newLine();
            bs.write("\t}");
            bs.newLine();
            bs.newLine();


            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 批量新增/修改对象");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\t@RequestMapping(\"addOrUpdateBatch\")");
            bs.newLine();
            bs.write("\tpublic ResponseVo addOrUpdateBatch(@RequestBody List<"+tableInfo.getBeanName()+"> listBean){");
            bs.newLine();
            bs.write("\t\t"+serviceName+".addOrUpdateBatch(listBean);");
            bs.newLine();
            bs.write("\t\treturn getSuccessResponseVo(null);");
            bs.newLine();
            bs.write("\t}");

            bs.newLine();
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
                    bs.write("\t * 根据"+methodName+"查询对象");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    StringBuilder sb1=new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();

                    bs.write("\t@RequestMapping(\"get"+name+"By"+methodName+"\")");
                    bs.newLine();
                    bs.write("\tpublic ResponseVo get"+tableInfo.getBeanName()+"By"+methodName+"(");

                    for (FieldInfo fieldInfo : fieldInfos) {
                        sb1.append(fieldInfo.getJavaType()+" "+fieldInfo.getPropertyName());
                        sb1.append(",");
                        sb2.append(fieldInfo.getPropertyName());
                        sb2.append(", ");
                    }
                    String tmp = sb1.toString();
                    tmp = tmp.substring(0,tmp.lastIndexOf(","));

                    String paramStr = sb2.toString();
                    paramStr = paramStr.substring(0,paramStr.lastIndexOf(", "));

                    bs.write(tmp);
                    bs.write("){");

                    bs.newLine();
                    bs.write("\t\treturn getSuccessResponseVo("+serviceName+".get"+name+"By"+methodName+"("+paramStr+"));");

                    bs.newLine();
                    bs.write("\t}");
                    bs.newLine();
                    bs.newLine();
                    //

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"更新对象");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    bs.write("\t@RequestMapping(\"update"+name+"By"+methodName+"\")");
                    bs.newLine();
                    bs.write("\tpublic ResponseVo update"+name+"By"+methodName+"("+tableInfo.getBeanName()+" bean,");
                    bs.write(tmp);
                    bs.write("){");

                    bs.newLine();
                    bs.write("\t\t"+serviceName+".update"+name+"By"+methodName+"(bean, "+paramStr+");");
                    bs.newLine();
                    bs.write("\t\treturn getSuccessResponseVo(null);");

                    bs.newLine();
                    bs.write("\t}");
                    bs.newLine();
                    bs.newLine();
                    //

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"删除对象");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    StringBuilder sb3=new StringBuilder();
                    bs.write("\t@RequestMapping(\"delete"+name+"By"+methodName+"\")");
                    bs.newLine();
                    bs.write("\tpublic ResponseVo delete"+tableInfo.getBeanName()+"By"+methodName+"(");

                    bs.write(tmp);
                    bs.write("){");

                    bs.newLine();
                    bs.write("\t\t"+serviceName+".delete"+name+"By"+methodName+"("+paramStr+");");
                    bs.newLine();
                    bs.write("\t\treturn getSuccessResponseVo(null);");


                    bs.newLine();
                    bs.write("\t}");
                    bs.newLine();

                }else {
                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"查询对象");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();

                    StringBuilder sb1=new StringBuilder();
                    String paramName = StringUtils.uppercaseFirstLetter(info.getPropertyName());

                    bs.write("\t@RequestMapping(\"get"+name+"By"+methodName+"\")");
                    bs.newLine();
                    bs.write("\tpublic ResponseVo get"+tableInfo.getBeanName()+"By"+paramName+"("+info.getJavaType()+" "+info.getPropertyName()+"){");
                    bs.newLine();
                    bs.write("\t\treturn getSuccessResponseVo("+serviceName+".get"+name+"By"+methodName+"("+StringUtils.lowercaseFirstLetter(methodName)+"));");
                    bs.newLine();
                    bs.write("\t}");
                    bs.newLine();
                    bs.newLine();

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"更新对象");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();
                    bs.write("\t@RequestMapping(\"update"+name+"By"+methodName+"\")");
                    bs.newLine();
                    bs.write("\tpublic ResponseVo update"+tableInfo.getBeanName()+"By"+paramName+"("+tableInfo.getBeanName()+" bean, "+info.getJavaType()+" "+info.getPropertyName()+"){");
                    bs.newLine();
                    bs.write("\t\t"+serviceName+".update"+tableInfo.getBeanName()+"By"+paramName+"(bean, "+StringUtils.lowercaseFirstLetter(methodName)+");");
                    bs.newLine();
                    bs.write("\t\treturn getSuccessResponseVo(null);");
                    bs.newLine();
                    bs.write("\t}");
                    bs.newLine();
                    bs.newLine();

                    bs.write("\t/**");
                    bs.newLine();
                    bs.write("\t * 根据"+methodName+"删除对象");
                    bs.newLine();
                    bs.write("\t */");
                    bs.newLine();
                    bs.write("\t@RequestMapping(\"delete"+name+"By"+methodName+"\")");
                    bs.newLine();
                    bs.write("\tpublic ResponseVo delete"+tableInfo.getBeanName()+"By"+paramName+"("+info.getJavaType()+" "+info.getPropertyName()+"){");
                    bs.newLine();
                    bs.write("\t\t"+serviceName+".delete"+tableInfo.getBeanName()+"By"+paramName+"("+StringUtils.lowercaseFirstLetter(methodName)+");");
                    bs.newLine();
                    bs.write("\t\treturn getSuccessResponseVo(null);");
                    bs.newLine();
                    bs.write("\t}");
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
