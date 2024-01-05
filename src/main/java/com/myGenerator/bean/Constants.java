package com.myGenerator.bean;

import com.myGenerator.utils.PropertiesUtil;

public class Constants {
    public static Boolean IGNORE_TABLE_PREFIX;

    public static String SUFFIX_BEAN_PARAMS;

    public static String PATH_BASE;

    public static String JAVA = "java";

    public static String RESOURCE = "resources";

    public static String PACKAGE_BASE;

    public static String PATH_PO;

    public static String PACKAGE_PO;

    public static String PACKAGE_QUERY;
    public static String PATH_QUERY;

    public static String PACKAGE_MAPPER;
    public static String PATH_MAPPER;

    public static String PATH_MAPPER_MXL;

    public static String PATH_VO;
    public static String PACKAGE_VO;

    public static String PATH_ENUM;
    public static String PACKAGE_ENUM;


    public static String PATH_CONTROLLER;
    public static String PACKAGE_CONTROLLER;

    public static String PATH_EXCEPTION;
    public static String PACKAGE_EXCEPTION;


    public static String PATH_SERVICE;
    public static String PACKAGE_SERVICE;

    public static String PATH_SERVICE_IMPL;
    public static String PACKAGE_SERVICE_IMPL;

    //


    public static String AUTHOR_NAME;

    // 忽视的属性
    public static String[] IGNORE_FILED;
    public static String IGNORE_EXPRESSION;
    public static String IGNORE_CLASS;
    //

    // 日期序列化
    public static String DATE_FORMAT_EXPRESSION;
    public static String DATE_FORMAT_CLASS;
    //

    //日期反序列化
    public static String DATE_UNFORMAT_EXPRESSION;

    public static String DATE_UNFORMAT_CLASS;
    //

    public static String SUFFIX_BEAN_PARAM_FUZZY;
    public static String SUFFIX_BEAN_PARAM_DATE_START;
    public static String SUFFIX_BEAN_PARAM_DATE_END;

    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtil.getString("ignore.table.prefix"));

        SUFFIX_BEAN_PARAMS = PropertiesUtil.getString("suffix.bean.param");

        //

        PATH_BASE = PropertiesUtil.getString("path.base");

        PACKAGE_BASE = PropertiesUtil.getString("package.base");

        PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtil.getString("package.mapper");

        PATH_MAPPER = PATH_BASE + JAVA + "/" + PACKAGE_MAPPER.replace(".", "/");


        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtil.getString("package.query");

        PATH_QUERY = PATH_BASE + JAVA + "/" + PACKAGE_QUERY.replace(".", "/");


        PATH_MAPPER_MXL = PATH_BASE + RESOURCE + "/" + PACKAGE_MAPPER.replace(".", "/");


        PACKAGE_VO = PACKAGE_BASE + "." + PropertiesUtil.getString("package.vo");

        PATH_VO = PATH_BASE + JAVA + "/" + PACKAGE_VO.replace(".", "/");


        PACKAGE_ENUM = PACKAGE_BASE + "." + PropertiesUtil.getString("package.enum");

        PATH_ENUM = PATH_BASE + JAVA + "/" + PACKAGE_ENUM.replace(".", "/");


        PACKAGE_CONTROLLER = PACKAGE_BASE + "." + PropertiesUtil.getString("package.controller");

        PATH_CONTROLLER = PATH_BASE + JAVA + "/" + PACKAGE_CONTROLLER.replace(".", "/");


        PACKAGE_EXCEPTION = PACKAGE_BASE + "." + PropertiesUtil.getString("package.exception");

        PATH_EXCEPTION = PATH_BASE + JAVA + "/" + PACKAGE_EXCEPTION.replace(".", "/");


        PACKAGE_SERVICE = PACKAGE_BASE + "." + PropertiesUtil.getString("package.service");

        PATH_SERVICE = PATH_BASE + JAVA + "/" + PACKAGE_SERVICE.replace(".", "/");



        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + PropertiesUtil.getString("package.service.impl");

        PATH_SERVICE_IMPL = PATH_BASE + JAVA + "/" + PACKAGE_SERVICE_IMPL.replace(".", "/");



        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtil.getString("package.po");

        PATH_BASE = PATH_BASE + JAVA + "/" + PACKAGE_BASE.replace(".", "/");

        PATH_PO = PATH_BASE + "/" + PropertiesUtil.getString("package.po").replace(".", "/");


        //

        AUTHOR_NAME = PropertiesUtil.getString("author.name");


        IGNORE_FILED = PropertiesUtil.getString("ignore.bean.tojson.filed").split(",");
        IGNORE_EXPRESSION = PropertiesUtil.getString("ignore.bean.tojson.expression");
        IGNORE_CLASS = PropertiesUtil.getString("ignore.bean.tojson.class");

        DATE_FORMAT_EXPRESSION = PropertiesUtil.getString("bean.date.format.expression");
        DATE_FORMAT_CLASS = PropertiesUtil.getString("bean.date.format.class");

        DATE_UNFORMAT_EXPRESSION = PropertiesUtil.getString("bean.date.unformat.expression");
        DATE_UNFORMAT_CLASS = PropertiesUtil.getString("bean.date.unformat.class");

        SUFFIX_BEAN_PARAM_FUZZY = PropertiesUtil.getString("suffix.bean.param.fuzzy");
        SUFFIX_BEAN_PARAM_DATE_START = PropertiesUtil.getString("suffix.bean.param.date.start");
        SUFFIX_BEAN_PARAM_DATE_END = PropertiesUtil.getString("suffix.bean.param.date.end");


    }

    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
    public final static String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};
    public final static String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public final static String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};
    public final static String[] SQL_LONG_TYPE = new String[]{"bigint "};


    public static void main(String[] args) {
        System.out.println(PATH_VO);
//        System.out.println(PACKAGE_MAPPER);
    }
}
