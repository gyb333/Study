package Activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;

//流程实例与流程任务
public class ProcessInstanceAndTask {
    private ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
    //部署流程定义  ，采用buyBill
    //执行流程，开始跑流程
    public void startProcess(){
        String processDefiKey="BuyBill";//bpmn 的 process id属性
        ProcessInstance pi = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefiKey);

        System.out.println("流程执行对象的id："+pi.getId());//Execution 对象
        System.out.println("流程实例的id："+pi.getProcessInstanceId());//ProcessInstance 对象
        System.out.println("流程定义的id："+pi.getProcessDefinitionId());//默认执行的是最新版本的流程定义
    }

    //查询正在运行任务
    public void queryTask(){
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.list();
        //遍历任务列表
        if(list!=null&&list.size()>0){
            for(Task task:list){
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务的id："+task.getId());
                System.out.println("任务的名称："+task.getName());
                System.out.println("任务的流程实例ID："+task.getProcessInstanceId());
            }
        }

    }
    //完成任务
    public void compileTask(){
        String taskId="10005";
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }
    //获取流程实例的状态
    public void getProcessInstanceState(){
        String processDefiKey="BuyBill";
        String processInstanceId="10001";
        ProcessInstance pi = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult()
                ;//返回的数据要么是单行，要么是空 ，其他情况报错

            //判断流程实例的状态
            if(pi!=null){
                System.out.println("该流程实例"+processInstanceId+"正在运行...  "+"当前活动的任务:"+pi.getActivityId());
            }else{
                System.out.println("当前的流程实例"+processInstanceId+" 已经结束！");
            }



    }
    //查看历史执行流程实例信息
    public void queryHistoryProcInst(){
        List<HistoricProcessInstance> list = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .list();
        if(list!=null&&list.size()>0){
            for(HistoricProcessInstance temp:list){
                System.out.println("历史流程实例id:"+temp.getId());
                System.out.println("历史流程定义的id:"+temp.getProcessDefinitionId());
                System.out.println("历史流程实例开始时间--结束时间:"+temp.getStartTime()+"-->"+temp.getEndTime());
            }
        }
    }
    //查看历史执行流程任务信息

    public void queryHistoryTask(){
        String processInstanceId="10001";
        List<HistoricTaskInstance> list = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if(list!=null&&list.size()>0){
            for(HistoricTaskInstance temp:list){
                System.out.print("历史流程实例任务id:"+temp.getId());
                System.out.print("历史流程定义的id:"+temp.getProcessDefinitionId());
                System.out.print("历史流程实例任务名称:"+temp.getName());
                System.out.println("历史流程实例任务处理人:"+temp.getAssignee());
            }
        }
    }


    public  static  void main(String[] args){
        ProcessInstanceAndTask task= new ProcessInstanceAndTask();
//        task.startProcess();
//        task.queryTask();
//        task.compileTask();
        task.getProcessInstanceState();
//        task.queryHistoryProcInst();
        task.queryHistoryTask();
    }

}

