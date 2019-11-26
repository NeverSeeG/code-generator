package com.louis.kitty.generator.vo;

import java.util.ArrayList;
import java.util.List;

import com.louis.kitty.dbms.vo.ConnParam;
import lombok.Data;

/**
 * 代码生成数据模型
 * @author Louis
 * @date Nov 10, 2018
 */
@Data
public class GenerateModel {

	private String outPutFolderPath;
	private String basePackage = "com.app.test";
	private ConnParam connParam;
	private List<TableModel> tableModels = new ArrayList<>();

}
