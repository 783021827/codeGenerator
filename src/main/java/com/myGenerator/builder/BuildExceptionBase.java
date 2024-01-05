package com.myGenerator.builder;

import com.myGenerator.bean.Constants;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.*;

public class BuildExceptionBase {
    private static Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(String fileName, String... args) {
        File file = new File(Constants.PATH_EXCEPTION);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file, fileName + ".java");
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1)));
            String path = BuildQueryBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

            bw.write("package " + Constants.PACKAGE_EXCEPTION + ";");
            bw.newLine();
            bw.newLine();

            if (args.length > 0 && args[0].equals("base")) {
                bw.write("import " + Constants.PACKAGE_ENUM + ".ResponseCode;");
                bw.newLine();
//
            }

            String lineInfo = null;
            while ((lineInfo = br.readLine()) != null) {
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {

            logger.error("生成基本类失败,{},{}", e.getMessage(),"haha");
        }
    }
}
