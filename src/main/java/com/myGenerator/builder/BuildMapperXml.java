package com.myGenerator.builder;

import com.myGenerator.bean.Constants;
import com.myGenerator.bean.FieldInfo;
import com.myGenerator.bean.TableInfo;
import com.myGenerator.utils.StringUtils;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildMapperXml {
    public static Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo table, List<FieldInfo> extendList) {
        File file = new File(Constants.PATH_MAPPER_MXL);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file, table.getBeanName() + ".xml");
        try {
            BufferedWriter bs = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));

            String clazzName = Constants.PACKAGE_MAPPER + "." + table.getBeanName();
            String poName = Constants.PACKAGE_PO + "." + table.getBeanName();
            List<FieldInfo> fieldList = table.getFieldList();
            String columnList = null;

            // 头部
            bs.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bs.newLine();
            bs.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bs.newLine();
            bs.write("\t\t\"https://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bs.newLine();
            bs.write("<mapper namespace=\"" + clazzName + "Mapper" + "\">");
            bs.newLine();
            bs.newLine();
            //

            //实体映射
            bs.write("\t<!--实体映射-->");
            bs.newLine();
            bs.write("\t<resultMap id=\"" + table.getTableName() + "\" type=\"" + poName + "\">");
            bs.newLine();

            StringBuilder sb = new StringBuilder();

            for (FieldInfo fieldInfo : fieldList) {
                // 为使用查询结果列做前置准备
                sb.append(fieldInfo.getFieldName());
                sb.append(",");

                bs.write("\t\t<!--" + fieldInfo.getComment() + "-->");
                bs.newLine();
                if (fieldInfo.getAutoIncrement()) {
                    bs.write("\t\t<id column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                    bs.newLine();
                    continue;
                }
                bs.write("\t\t<result column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                bs.newLine();
            }
            bs.write("\t</resultMap>");
            bs.newLine();
            bs.newLine();
            //

            //通用查询结果列 base_column_list
            String strColumn = sb.toString();
            strColumn = strColumn.substring(0, strColumn.lastIndexOf(","));

            bs.write("\t<!--通用查询结果列-->");
            bs.newLine();
            bs.write("\t<sql id=\"base_column_list\">");
            bs.newLine();
            bs.write("\t\t" + strColumn);
            bs.newLine();
            bs.write("\t</sql>");
            bs.newLine();
            bs.newLine();

            //

            // 基本条件查询 base_condition

            bs.write("\t<!--基本条件查询-->");
            bs.newLine();
            bs.write("\t<sql id=\"base_condition\">");
            bs.newLine();
            for (FieldInfo fieldInfo : fieldList) {
                bs.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null\">");
                bs.newLine();
                bs.write("\t\t\tand " + fieldInfo.getFieldName() + " = #{query." + fieldInfo.getPropertyName() + "}");
                bs.newLine();
                bs.write("\t\t</if>");
                bs.newLine();
            }
            bs.write("\t</sql>");
            bs.newLine();
            bs.newLine();

            //

            // 扩展条件查询 extend_condition
            bs.write("\t<!--扩展条件查询-->");
            bs.newLine();
            bs.write("\t<sql id=\"extend_condition\">");
            bs.newLine();

            for (FieldInfo fieldInfo : extendList) {
                bs.write("\t\t<if test=\"query." + fieldInfo.getPropertyName() + " != null and query." + fieldInfo.getPropertyName() + "!=\'\'\">");
                bs.newLine();
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPE, fieldInfo.getSqlType())) {
                    bs.write("\t\t\tand " + fieldInfo.getFieldName() + " like concat(\'%\', #{query." + fieldInfo.getPropertyName() + "}, \'%\')");
                } else {
                    if (fieldInfo.getPropertyName().contains("Start")) {
                        bs.write("\t\t\t<![CDATA[and " + fieldInfo.getFieldName() + ">=str_to_date(#{query." + fieldInfo.getPropertyName() + "}, \'%Y-%m-%d\')]]>");
                    } else {
                        bs.write("\t\t\t<![CDATA[and " + fieldInfo.getFieldName() + "<date_sub(str_to_date(#{query." + fieldInfo.getPropertyName() + "}, \'%Y-%m-%d\'),interval -1 day)]]>");
                    }
                }
                bs.newLine();
                bs.write("\t\t</if>");
                bs.newLine();
            }
            bs.write("\t</sql>");
            bs.newLine();
            bs.newLine();
            //

            // 通用查询条件 将 base_condition 和 extend_condition 结合起来 base_query
            bs.write("\t<!--通用查询条件-->");
            bs.newLine();
            bs.write("\t<sql id=\"base_query\">");
            bs.newLine();
            bs.write("\t\t<where>");
            bs.newLine();
            bs.write("\t\t\t<include refid=\"base_condition\"/>");
            bs.newLine();
            bs.write("\t\t\t<include refid=\"extend_condition\"/>");
            bs.newLine();
            bs.write("\t\t</where>");
            bs.newLine();
            bs.write("\t</sql>");
            bs.newLine();
            bs.newLine();


            //

            // 查询集合 selectList
            bs.write("\t<select id=\"selectList\" resultMap=\"" + table.getTableName() + "\">");
            bs.newLine();
            bs.write("\t\tSELECT");
            bs.newLine();
            bs.write("\t\t<include refid=\"base_column_list\"/>");
            bs.newLine();
            bs.write("\t\tFROM " + table.getTableName());
            bs.newLine();
            bs.write("\t\t<include refid=\"base_query\"/>");
            bs.newLine();
            bs.write("\t\t<if test=\"query.orderBy!=null\">");
            bs.newLine();
            bs.write("\t\t\torder by ${query.orderBy}");
            bs.newLine();
            bs.write("\t\t</if>");
            bs.newLine();
            bs.write("\t\t<if test=\"query.simplePage!=null\">");
            bs.newLine();
            bs.write("\t\t\tlimit #{query.simplePage.start},#{query.simplePage.end}");
            bs.newLine();
            bs.write("\t\t</if>");
            bs.newLine();
            bs.write("\t</select>");
            bs.newLine();
            bs.newLine();
            //


            // 查询数量 selectCount
            bs.write("\t<!--查询数量-->");
            bs.newLine();
            bs.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
            bs.newLine();
            bs.write("\t\tSELECT count(1) FROM " + table.getTableName());
            bs.newLine();
            bs.write("\t\t<include refid=\"base_query\"/>");
            bs.newLine();
            bs.write("\t</select>");
            bs.newLine();
            bs.newLine();

            //

            // 插入（匹配有值的字段）
            bs.write("\t<!--插入数据-->");
            bs.newLine();
            bs.write("\t<insert id=\"insert\" parameterType=\"" + poName + "\">");
            bs.newLine();

            for (FieldInfo fieldInfo : fieldList) {
                if (fieldInfo.getAutoIncrement()) {
                    bs.write("\t\t<selectKey keyProperty=\"bean." + fieldInfo.getPropertyName() + "\" resultType=\"Integer\" order=\"AFTER\">");
                    bs.newLine();
                    bs.write("\t\t\tSELECT LAST_INSERT_ID()");
                    bs.newLine();
                    bs.write("\t\t</selectKey>");
                    bs.newLine();
                    break;
                }
            }

            bs.write("\t\tINSERT INTO " + table.getTableName());
            bs.newLine();
            bs.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bs.newLine();

            for (FieldInfo fieldInfo : fieldList) {
                bs.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!= null\">");
                bs.newLine();
                bs.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bs.newLine();
                bs.write("\t\t\t</if>");
                bs.newLine();
            }

            bs.write("\t\t</trim>");
            bs.newLine();

            bs.write("\t\t<trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\">");
            bs.newLine();
            for (FieldInfo fieldInfo : fieldList) {
                bs.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!= null\">");
                bs.newLine();
                bs.write("\t\t\t\t#{bean." + fieldInfo.getPropertyName() + "},");
                bs.newLine();
                bs.write("\t\t\t</if>");
                bs.newLine();
            }
            bs.write("\t\t</trim>");
            bs.newLine();


            bs.write("\t</insert>");
            bs.newLine();
            bs.newLine();
            //

            // 插入或者更新 insertOrUpdate
            bs.write("\t<!--插入或者更新-->");
            bs.newLine();
            bs.write("\t<insert id=\"insertOrUpdate\" parameterType=\"" + poName + "\">");
            bs.newLine();
            bs.write("\t\tINSERT INTO " + table.getTableName());
            bs.newLine();
            bs.write("\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
            bs.newLine();

            for (FieldInfo fieldInfo : fieldList) {
                bs.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!= null\">");
                bs.newLine();
                bs.write("\t\t\t\t" + fieldInfo.getFieldName() + ",");
                bs.newLine();
                bs.write("\t\t\t</if>");
                bs.newLine();
            }
            bs.write("\t\t</trim>");
            bs.newLine();

            bs.write("\t\ton DUPLICATE key update");
            bs.newLine();
            bs.write("\t\t<trim prefix=\"\" suffix=\"\" suffixOverrides=\",\">");
            bs.newLine();
            for (FieldInfo fieldInfo : fieldList) {
                bs.write("\t\t\t<if test=\"bean." + fieldInfo.getPropertyName() + "!= null\">");
                bs.newLine();
                bs.write("\t\t\t\t" + fieldInfo.getFieldName() + " = VALUES(" + fieldInfo.getFieldName() + "),");
                bs.newLine();
                bs.write("\t\t\t</if>");
                bs.newLine();
            }
            bs.write("\t\t</trim>");
            bs.newLine();


            bs.write("\t</insert>");
            bs.newLine();
            bs.newLine();

            //

            // 批量添加 insertBatch
            bs.write("\t<!--批量添加-->");
            bs.newLine();
            bs.write("\t<insert id=\"insertBatch\" parameterType=\"" + poName + "\">");
            bs.newLine();
            bs.write("\t\tINSERT INTO " + table.getTableName() + "(" + strColumn + ")");
            bs.newLine();
            bs.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bs.newLine();
            bs.write("\t\t\t(");
            bs.newLine();

            StringBuilder sb1 = new StringBuilder();
            for (FieldInfo fieldInfo : fieldList) {
                sb1.append("#{item." + fieldInfo.getPropertyName() + "}");
                sb1.append(",");
            }
            String columnInsertBatch = sb1.toString();
            columnInsertBatch = columnInsertBatch.substring(0, columnInsertBatch.lastIndexOf(","));

            bs.write("\t\t\t" + columnInsertBatch);
            bs.newLine();
            bs.write("\t\t\t)");
            bs.newLine();
            bs.write("\t\t</foreach>");
            bs.newLine();
            bs.write("\t</insert>");
            bs.newLine();
            bs.newLine();
            //

            // 批量新增或修改 insertOrUpdateBatch
            bs.write("\t<!--批量新增或修改-->");
            bs.newLine();
            bs.write("\t<insert id=\"insertOrUpdateBatch\" parameterType=\"" + poName + "\">");
            bs.newLine();
            bs.write("\t\tINSERT INTO " + table.getTableName() + "(" + strColumn + ")");
            bs.newLine();
            bs.write("\t\t<foreach collection=\"list\" item=\"item\" separator=\",\">");
            bs.newLine();
            bs.write("\t\t\t(");
            bs.newLine();


            bs.write("\t\t\t" + columnInsertBatch);
            bs.newLine();
            bs.write("\t\t\t)");
            bs.newLine();
            bs.write("\t\t</foreach>");
            bs.newLine();

            bs.write("\t\ton DUPLICATE key update");
            bs.newLine();
            for (int i = 0; i < fieldList.size(); i++) {
                bs.write("\t\t" + fieldList.get(i).getFieldName() + " = " + "VALUES(" + fieldList.get(i).getFieldName() + ")");
                if (i < fieldList.size()-1) {
                    bs.write(",");
                    bs.newLine();
                }

            }

            bs.newLine();
            bs.write("\t</insert>");
            bs.newLine();
            bs.newLine();
            //

            // 自定义增删改
            for (Map.Entry<String, List<FieldInfo>> entry : table.getKeyIndexMap().entrySet()) {
                List<FieldInfo> fieldInfos = entry.getValue();
                if(fieldInfos.size()==1){
                    FieldInfo fieldInfo = fieldInfos.get(0);
                    String firstLetter = StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName());
                    // 删除
                    bs.write("\t<!--根据"+firstLetter+"删除-->");
                    bs.newLine();
                    bs.write("\t<delete id=\"deleteBy"+firstLetter+"\">");
                    bs.newLine();
                    bs.write("\t\tdelete from "+table.getTableName()+" where "+fieldInfo.getFieldName()+"=#{"+fieldInfo.getPropertyName()+"}");
                    bs.newLine();
                    bs.write("\t</delete>");
                    bs.newLine();
                    bs.newLine();
                    //
                    // 查询
                    bs.write("\t<!--根据"+firstLetter+"查询对象-->");
                    bs.newLine();
                    bs.write("\t<select id=\"selectBy"+firstLetter+"\" resultMap=\""+table.getTableName()+"\">");
                    bs.newLine();
                    bs.write("\t\tselect");
                    bs.newLine();
                    bs.write("\t\t<include refid=\"base_column_list\"/>");
                    bs.newLine();
                    bs.write("\t\tfrom "+table.getTableName()+" where "+fieldInfo.getFieldName()+"=#{"+fieldInfo.getPropertyName()+"}");
                    bs.newLine();
                    bs.write("\t</select>");
                    bs.newLine();
                    bs.newLine();
                    //
                    // 修改
                    bs.write("\t<!--根据"+firstLetter+"修改-->");
                    bs.newLine();
                    bs.write("\t<update id=\"updateBy"+firstLetter+"\" parameterType=\""+poName+"\">");
                    bs.newLine();
                    bs.write("\t\tUPDATE "+table.getTableName());
                    bs.newLine();
                    bs.write("\t\t<set>");
                    bs.newLine();

                    for (FieldInfo fieldInfo1 : fieldList) {
                        bs.write("\t\t\t<if test=\"bean." + fieldInfo1.getPropertyName() + "!= null\">");
                        bs.newLine();
                        bs.write("\t\t\t\t"+fieldInfo1.getFieldName()+" = #{bean." + fieldInfo1.getPropertyName() + "},");
                        bs.newLine();
                        bs.write("\t\t\t</if>");
                        bs.newLine();
                    }
                    bs.write("\t\t</set>");
                    bs.newLine();
                    bs.write("\t\twhere "+fieldInfo.getFieldName()+"=#{"+fieldInfo.getPropertyName()+"}");
                    bs.newLine();
                    bs.write("\t</update>");
                    bs.newLine();
                    bs.newLine();
                    //
                }else {
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    for (FieldInfo fieldInfo : fieldInfos) {
                        sb2.append(StringUtils.uppercaseFirstLetter(fieldInfo.getPropertyName()));
                        sb2.append("And");
                        sb3.append(fieldInfo.getFieldName()+"=#{"+fieldInfo.getPropertyName()+"}");
                        sb3.append(" and ");
                    }
                    String columnName = sb2.toString();;
                    columnName = columnName.substring(0,columnName.lastIndexOf("And"));

                    String columList1 = sb3.toString();

                    columList1 = columList1.substring(0,columList1.lastIndexOf("}")+1);

                    bs.write("\t<!--根据"+columnName+"删除-->");
                    bs.newLine();
                    bs.write("\t<delete id=\"deleteBy"+columnName+"\">");
                    bs.newLine();
                    bs.write("\t\tdelete from "+table.getTableName()+" where "+columList1);
                    bs.newLine();
                    bs.write("\t</delete>");
                    bs.newLine();
                    bs.newLine();
                    //
                    bs.write("\t<!--根据"+columnName+"查询对象-->");
                    bs.newLine();
                    bs.write("\t<select id=\"selectBy"+columnName+"\" resultMap=\""+table.getTableName()+"\">");
                    bs.newLine();
                    bs.write("\t\tselect");
                    bs.newLine();
                    bs.write("\t\t<include refid=\"base_column_list\"/>");
                    bs.newLine();
                    bs.write("\t\tfrom "+table.getTableName()+" where "+columList1);
                    bs.newLine();
                    bs.write("\t</select>");
                    bs.newLine();
                    bs.newLine();
                    //
                    bs.write("\t<!--根据"+columnName+"修改-->");
                    bs.newLine();
                    bs.write("\t<update id=\"updateBy"+columnName+"\" parameterType=\""+poName+"\">");
                    bs.newLine();
                    bs.write("\t\tUPDATE "+table.getTableName());
                    bs.newLine();
                    bs.write("\t\t<set>");
                    bs.newLine();

                    for (FieldInfo fieldInfo1 : fieldList) {
                        bs.write("\t\t\t<if test=\"bean." + fieldInfo1.getPropertyName() + "!= null\">");
                        bs.newLine();
                        bs.write("\t\t\t\t"+fieldInfo1.getFieldName()+" = #{bean." + fieldInfo1.getPropertyName() + "},");
                        bs.newLine();
                        bs.write("\t\t\t</if>");
                        bs.newLine();
                    }
                    bs.write("\t\t</set>");
                    bs.newLine();
                    bs.write("\t\twhere "+columList1);
                    bs.newLine();
                    bs.write("\t</update>");
                    bs.newLine();
                    bs.newLine();
                }
            }
            //


            bs.write("</mapper>");
            bs.newLine();


            bs.flush();
        } catch (Exception e) {
            logger.error("创建xml失败,{}", e);
        }
    }
}
