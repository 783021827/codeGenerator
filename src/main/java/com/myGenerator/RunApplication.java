package com.myGenerator;

import com.myGenerator.bean.FieldInfo;
import com.myGenerator.bean.TableInfo;
import com.myGenerator.builder.*;
import org.apache.ibatis.builder.BuilderException;

import java.util.List;

/**
 * Hello world!
 *
 */
public class RunApplication
{
    public static void main( String[] args )
    {
        for (TableInfo table : BuildTable.getTables()) {
            BuildPo.execute(table);

            BuildQueryBase.execute("BaseParam");
            BuildQueryBase.execute("SimplePage");

            BuildMapperBase.execute("BaseMapper");

            BuildVoBase.execute("PaginationResultVo");
            BuildVoBase.execute("ResponseVo");

            BuildControllerBase.execute("ABaseController","base");
            BuildControllerBase.execute("AGlobalExceptionHandlerController","base");

            BuildController.execute(table);

            BuildEnumBase.execute("PageSize");
            BuildEnumBase.execute("ResponseCode");

            BuildExceptionBase.execute("BusinessException","base");


            List<FieldInfo> extendList = BuildQuery.execute(table);
            BuildMapper.execute(table);

            BuildMapperXml.execute(table,extendList);

            BuildService.execute(table);

            BuildServiceImpl.execute(table);
        }
    }
}
