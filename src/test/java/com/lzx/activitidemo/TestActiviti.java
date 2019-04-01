package com.lzx.activitidemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestActiviti {
    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine;

    @Test
    public void test() {
        //以下两种方式选择一种创建引擎方式：1.配置写在程序里 2.读对应的配置文件
        //1
        testCreateProcessEngine();
        //2
        //testCreateProcessEngineByCfgXml();

        deployProcess();
        startProcess();
    }

    /**
     * 测试activiti环境
     */
    public void testCreateProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver("com.mysql.jdbc.Driver");
        cfg.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti");
        cfg.setJdbcUsername("root");
        cfg.setJdbcPassword("1994");
        //配置建表策略
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngine = cfg.buildProcessEngine();
    }

    /**
     * 发布流程
     * RepositoryService
     */
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("BpmnFile/test.bpmn");
        builder.deploy();
    }

    /**
     * 启动流程
     * RuntimeService
     */
    public void startProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String,Object> param = new HashMap<>();
        param.put("name","test");
        runtimeService.startProcessInstanceByKey("testProcess",param);
    }
}
