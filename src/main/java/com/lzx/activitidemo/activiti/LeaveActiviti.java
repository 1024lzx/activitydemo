package com.lzx.activitidemo.activiti;

import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveActiviti {
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
        queryEmpTask();
        queryManagerTask();
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
     * 根据配置文件activiti.cfg.xml创建ProcessEngine
     */
    public void testCreateProcessEngineByCfgXml() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("BpmnXml/activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    /**
     * 发布流程
     * RepositoryService
     */
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("BpmnFile/leave.bpmn");
        builder.deploy();
    }

    /**
     * 启动流程
     * RuntimeService
     */
    public void startProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //可根据id、key、message启动流程
        Map<String,Object> map=new HashMap<>();
        map.put("taskid",1);
        Map<String,Object> map2=new HashMap<>();
        map.put("taskid",2);
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("leaveProcess",map);
        runtimeService.startProcessInstanceByKey("leaveProcess",map2);
    }

    /**
     * 查看任务
     * TaskService
     */
    public void queryEmpTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String assignee = "emp";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).includeProcessVariables().list();

        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
            //get arguments
            //((TaskEntity)task).getProcessVariables()
            taskService.complete(task.getId());
        }
    }

    public void queryManagerTask(){
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String assignee = "manager";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();

        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
            taskService.complete(task.getId());
        }
    }
}