package com.louis.kitty.generator.controller;

import com.louis.kitty.generator.service.GenerateToHibernateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.louis.kitty.core.http.HttpResult;
import com.louis.kitty.dbms.vo.ConnParam;
import com.louis.kitty.generator.service.GenerateService;
import com.louis.kitty.generator.vo.GenerateModel;

/**
 * 代码生成控制器
 * @author Louis
 * @date Nov 9, 2018
 */
@RestController
@RequestMapping("")
public class GenerateController {

	@Autowired
	GenerateService generatorService;

	@Autowired
	GenerateToHibernateService generateToHibernateService;


	@GetMapping("/dockertest")
	public HttpResult dockertest() {
		return HttpResult.ok("接口调用成功！");
	}

	@PostMapping("/testConnection")
	public HttpResult testConnection(@RequestBody ConnParam connParam) {
		boolean success = generatorService.testConnection(connParam);
		if(success) {
			return HttpResult.ok(generatorService.testConnection(connParam));
		}
		return HttpResult.error("连接失败,请检查数据库及连接。");
	}

	@PostMapping("/getTables")
	public HttpResult getTables(@RequestBody ConnParam connParam) {
		return HttpResult.ok(generatorService.getTables(connParam));
	}

	@PostMapping("/getGenerateModel")
	public HttpResult getGenerateModel(@RequestBody GenerateModel generateModel) {
		return HttpResult.ok(generatorService.getGenerateModel(generateModel));
	}

	/**
	 * 前端默认默认生成mybatis
	 * @param generateModel
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/generateModels")
	public HttpResult generateModels(@RequestBody GenerateModel generateModel) throws Exception {
		return HttpResult.ok(generatorService.generateModels(generateModel));
	}

	/**
	 * 生成hibernate
	 * @param generateModel
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/generateModelsToHibernate")
	public HttpResult generateModelsToHibernate(@RequestBody GenerateModel generateModel) throws Exception {
		return HttpResult.ok(generateToHibernateService.generateModelsToHibernate(generateModel));
	}
}
