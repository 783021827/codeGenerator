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

public class BuildServiceImpl {

    public static Logger logger = LoggerFactory.getLogger(BuildPo.class);
    public static void execute(TableInfo tableInfo)  {
        File file = new File(Constants.PATH_SERVICE_IMPL);
        if(!file.exists()){
            file.mkdirs();
        }
        File file1 = new File(file,tableInfo.getBeanName()+"ServiceImpl.java");
        try {
            BufferedWriter bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
            // 包路径
            bs.write("package "+Constants.PACKAGE_SERVICE_IMPL +";");
            bs.newLine();
            bs.newLine();
            //

            bs.write("import java.util.List;");
            bs.newLine();
            bs.write("import org.springframework.stereotype.Service;");
            bs.newLine();
            bs.write("import javax.annotation.Resource;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_PO+"."+tableInfo.getBeanName()+";");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_QUERY+"."+tableInfo.getBeanName()+"Query;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_VO+".PaginationResultVo;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_SERVICE+"."+tableInfo.getBeanName()+"Service;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_MAPPER+"."+tableInfo.getBeanName()+"Mapper;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_QUERY+".SimplePage;");
            bs.newLine();
            bs.write("import "+Constants.PACKAGE_ENUM+".PageSize;");
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

            String clazzName = tableInfo.getBeanName()+"ServiceImpl";
            String queryName = tableInfo.getBeanName()+"Query";
            String name = tableInfo.getBeanName();
            String mapperName = name +"Mapper";
            mapperName = StringUtils.lowercaseFirstLetter(mapperName);
            bs.write("@Service(\""+name+"Service\")");
            bs.newLine();
            bs.write("public class "+clazzName+" implements "+name+"Service{");
            bs.newLine();
            bs.newLine();

            bs.write("\t@Resource");
            bs.newLine();
            bs.write("\tprivate "+name+"Mapper<"+name+","+queryName+">"+" "+mapperName+";");
            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 根据条件查询列表");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\tpublic List<"+tableInfo.getBeanName()+"> findListByQuery("+tableInfo.getBeanName()+"Query query){");
            bs.newLine();
            bs.write("\t\treturn "+mapperName+".selectList(query);");
            bs.newLine();
            bs.write("\t}");
            bs.newLine();
            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 根据条件查询数量");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\tpublic Integer findCountByQuery("+tableInfo.getBeanName()+"Query query){");
            bs.newLine();
            bs.write("\t\treturn "+mapperName+".selectCount(query);");
            bs.newLine();
            bs.newLine();
            bs.write("\t}");
            bs.newLine();
            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 分页查询");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\tpublic PaginationResultVo<"+tableInfo.getBeanName()+"> findListByPage("+tableInfo.getBeanName()+"Query query){");
            bs.newLine();
            bs.write("\t\tint count = this.findCountByQuery(query);");
            bs.newLine();
            bs.write("\t\tint pageSize = query.getPageSize()==null?PageSize.SIZE15.getSize():query.getPageSize();");
            bs.newLine();
            bs.newLine();
            bs.write("\t\tSimplePage page = new SimplePage(query.getPageNo(),count,pageSize);");
            bs.newLine();
            bs.write("\t\tquery.setSimplePage(page);");
            bs.newLine();
            bs.write("\t\tList<"+name+"> list = this.findListByQuery(query);");
            bs.newLine();
            bs.write("\t\tPaginationResultVo<"+name+"> result = new PaginationResultVo(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);");
            bs.newLine();
            bs.write("\t\treturn result;");
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

            bs.write("\tpublic Integer add("+tableInfo.getBeanName()+" bean){");
            bs.newLine();
            bs.write("\t\treturn "+mapperName+".insert(bean);");
            bs.newLine();
            bs.write("\t}");
            bs.newLine();
            bs.newLine();

            bs.write("\t/**");
            bs.newLine();
            bs.write("\t * 批量新增对象");
            bs.newLine();
            bs.write("\t */");
            bs.newLine();

            bs.write("\tpublic Integer addBatch(List<"+tableInfo.getBeanName()+"> listBean){");
            bs.newLine();
            bs.write("\t\tif(listBean == null || listBean.isEmpty()) {");
            bs.newLine();
            bs.write("\t\t\treturn 0;");
            bs.newLine();
            bs.write("\t\t}");
            bs.newLine();
            bs.write("\t\treturn "+mapperName+".insertBatch(listBean);");
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

            bs.write("\tpublic Integer addOrUpdateBatch(List<"+tableInfo.getBeanName()+"> listBean){");
            bs.newLine();
            bs.write("\t\tif(listBean == null || listBean.isEmpty()) {");
            bs.newLine();
            bs.write("\t\t\treturn 0;");
            bs.newLine();
            bs.write("\t\t}");
            bs.newLine();
            bs.write("\t\treturn "+mapperName+".insertOrUpdateBatch(listBean);");
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
                    bs.write("\tpublic "+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By"+methodName+"(");

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
                    bs.write("\t\treturn "+mapperName+".selectBy"+methodName+"("+paramStr+");");

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

                    bs.write("\tpublic Integer update"+tableInfo.getBeanName()+"By"+methodName+"("+tableInfo.getBeanName()+" bean,");
                    bs.write(tmp);
                    bs.write("){");

                    bs.newLine();
                    bs.write("\t\treturn "+mapperName+".updateBy"+methodName+"(bean ,"+paramStr+");");

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
                    bs.write("\tpublic Integer delete"+tableInfo.getBeanName()+"By"+methodName+"(");

                    bs.write(tmp);
                    bs.write("){");

                    bs.newLine();
                    bs.write("\t\treturn "+mapperName+".deleteBy"+methodName+"("+paramStr+");");

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
                    bs.write("\tpublic "+tableInfo.getBeanName()+" get"+tableInfo.getBeanName()+"By"+paramName+"("+info.getJavaType()+" "+info.getPropertyName()+"){");
                    bs.newLine();
                    bs.write("\t\treturn "+mapperName+".selectBy"+paramName+"("+info.getPropertyName()+");");
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
                    bs.write("\tpublic Integer update"+tableInfo.getBeanName()+"By"+paramName+"("+tableInfo.getBeanName()+" bean, "+info.getJavaType()+" "+info.getPropertyName()+"){");
                    bs.newLine();
                    bs.write("\t\treturn "+mapperName+".updateBy"+paramName+"(bean ,"+info.getPropertyName()+");");
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
                    bs.write("\tpublic Integer delete"+tableInfo.getBeanName()+"By"+paramName+"("+info.getJavaType()+" "+info.getPropertyName()+"){");
                    bs.newLine();
                    bs.write("\t\treturn "+mapperName+".deleteBy"+paramName+"("+info.getPropertyName()+");");
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
