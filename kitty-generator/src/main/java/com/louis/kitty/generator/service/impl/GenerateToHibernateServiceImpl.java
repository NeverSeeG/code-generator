package com.louis.kitty.generator.service.impl;

import com.louis.kitty.dbms.service.DatabaseService;
import com.louis.kitty.dbms.utils.StringUtils;
import com.louis.kitty.generator.config.ParamConfig;
import com.louis.kitty.generator.service.GenerateToHibernateService;
import com.louis.kitty.generator.vo.GenerateModel;
import com.louis.kitty.generator.vo.TableModel;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;

/**
 * hibernate代码生成服务实现
 * @author Louis
 * @date Nov 9, 2018
 */
@Service
public class GenerateToHibernateServiceImpl implements GenerateToHibernateService {

	public static final String TABLE = "table";

	public static final String TEMPLATE_MODEL = "/hibernateTemplates/model.btl";
	public static final String TEMPLATE_DAO = "/hibernateTemplates/dao.btl";
	public static final String TEMPLATE_SERVICE_MANAGER = "/hibernateTemplates/serviceManager.btl";
	public static final String TEMPLATE_CONTROLLER = "/hibernateTemplates/controller.btl";
	public static final String TEMPLATE_VIEW = "/hibernateTemplates/view.btl";

	public static final String PACKAGE_MODEL = "model";
	public static final String PACKAGE_DAO = "service";
	public static final String PACKAGE_SERVICE_MANAGER = "service";
	public static final String PACKAGE_CONTROLLER = "controller";
	public static final String PACKAGE_VIEW = "view";

	public static final String MODEL_SUFFIX = ".java";
	public static final String DAO_SUFFIX = "Dao.java";
	public static final String SERVICE_MANAGER_SUFFIX = "ServiceManager.java";
	public static final String CONTROLLER_SUFFIX = "Controller.java";
	public static final String VIEW_SUFFIX = ".vue";

	@Autowired
	private ParamConfig config;

	@Override
	public boolean generateModelsToHibernate(GenerateModel generateModel) throws Exception {
		String outPutFolderPath = config.getOutPutFolderPath();
		if(outPutFolderPath != null) {
			generateModel.setOutPutFolderPath(outPutFolderPath);
			ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("templates");
			Configuration configuration = Configuration.defaultConfiguration();
			GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, configuration);
			for(TableModel tableModel:generateModel.getTableModels()) {
				// 设置各类代码包名
				tableModel.setModelPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_MODEL));
				tableModel.setDaoPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_DAO));
				tableModel.setServiceImplPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_SERVICE_MANAGER));
				tableModel.setControllerPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_CONTROLLER));
				tableModel.setViewPackageName(getPakcageName(generateModel.getBasePackage(), PACKAGE_VIEW));
				// generate model
				generateModel(groupTemplate, tableModel, TEMPLATE_MODEL, generateModel.getOutPutFolderPath());
				// generate mapper
				generateModel(groupTemplate, tableModel, TEMPLATE_DAO, generateModel.getOutPutFolderPath());
				// generate serviceImpl
				generateModel(groupTemplate, tableModel, TEMPLATE_SERVICE_MANAGER, generateModel.getOutPutFolderPath());
				// generate controller
				generateModel(groupTemplate, tableModel, TEMPLATE_CONTROLLER, generateModel.getOutPutFolderPath());
				// generate view
				generateModel(groupTemplate, tableModel, TEMPLATE_VIEW, generateModel.getOutPutFolderPath());
			}
			return true;
		}else{
			return false;
		}
	}

	private String getPakcageName(String basePackage, String subPackage) {
		return basePackage + "." + subPackage;
	}

	/**
	 * 生成代码
	 * @param groupTemplate
	 * @param tableModel
	 * @param templatePath
	 * @param outPutFolderPath
	 * @throws
	 * @throws Exception
	 */
	private void generateModel(GroupTemplate groupTemplate, TableModel tableModel, String templatePath, String outPutFolderPath) throws Exception {
		Template template = groupTemplate.getTemplate(templatePath);
		template.binding(TABLE, tableModel);
		FileOutputStream os = new FileOutputStream(getOutputFile(tableModel, outPutFolderPath, templatePath));
		template.renderTo(os);
		os.close();
	}

	/**
	 * 获取要生成的文件
	 * @param tableModel
	 * @param outPutFolderPath
	 * @param templatePath
	 * @return
	 */
	private String getOutputFile(TableModel tableModel, String outPutFolderPath, String templatePath) {
		String packageName = tableModel.getModelPackageName();
		String suffix = MODEL_SUFFIX;
		if(TEMPLATE_DAO.equals(templatePath)) {
			packageName = tableModel.getDaoPackageName();
			suffix = DAO_SUFFIX;
		} else if(TEMPLATE_SERVICE_MANAGER.equals(templatePath)) {
			packageName = tableModel.getServiceImplPackageName();
			suffix = SERVICE_MANAGER_SUFFIX;
		} else if(TEMPLATE_CONTROLLER.equals(templatePath)) {
			packageName = tableModel.getControllerPackageName();
			suffix = CONTROLLER_SUFFIX;
		} else if(TEMPLATE_VIEW.equals(templatePath)) {
			packageName = tableModel.getViewPackageName();
			suffix = VIEW_SUFFIX;
		}
		outPutFolderPath = outPutFolderPath + File.separatorChar + packageName.replaceAll("\\.", "/");
		File outPutFolder = new File(outPutFolderPath);
		if(!outPutFolder.exists()) {
			outPutFolder.mkdirs();
		}
		String filePath = outPutFolderPath + File.separatorChar + tableModel.getClassName() + suffix;
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
		return filePath;
	}

	/**
	 * 下划线转驼峰式
	 * @param str
	 * @return
	 */
	public String lineToHump(String str) {
		return StringUtils.lineToHump(str);
	}
}
