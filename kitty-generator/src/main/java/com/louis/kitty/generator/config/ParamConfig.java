package com.louis.kitty.generator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("app")
@Data
public class ParamConfig {

	/** 输出地址 */
	private String outPutFolderPath;


}
