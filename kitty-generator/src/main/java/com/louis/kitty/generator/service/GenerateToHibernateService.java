package com.louis.kitty.generator.service;

import com.louis.kitty.dbms.model.Table;
import com.louis.kitty.dbms.vo.ConnParam;
import com.louis.kitty.generator.vo.GenerateModel;

import java.io.IOException;
import java.util.List;

/**
 * hibernate代码生成服务
 * @author Louis
 * @date Nov 9, 2018
 */
public interface GenerateToHibernateService {
	/**
	 * 生成hibernate代码文件
	 * @param generateModel
	 * @return
	 * @throws IOException
	 */
	public boolean generateModelsToHibernate(GenerateModel generateModel) throws Exception;


}
