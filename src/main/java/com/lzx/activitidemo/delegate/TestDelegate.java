package com.lzx.activitidemo.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class TestDelegate implements JavaDelegate
{
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("test");
        ((ExecutionEntity) execution).getProcessInstance().getVariables().get("name");
    }
}
