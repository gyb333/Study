package Activiti;


import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;

public class HelloActiviti {
    private static  ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
    //取得流程引擎，且自动创建Activiti涉及的数据库和表

    public static void main(String[] args){
//        createProcessEngine();
//        deploy();
//        startProcess();
//        queryTask();
        compileTask();
    }

    public static void createProcessEngine(){

        /**1.通过代码形式创建
         *  - 取得ProcessEngineConfiguration对象
         *  - 设置数据库连接属性
         *  - 设置创建表的策略 （当没有表时，自动创建表）
         *  - 通过ProcessEngineConfiguration对象创建 ProcessEngine 对象

         //取得ProcessEngineConfiguration对象
         ProcessEngineConfiguration engineConfiguration= ProcessEngineConfiguration.
         createStandaloneProcessEngineConfiguration();
         //设置数据库连接属性
         engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
         engineConfiguration.setJdbcUrl("jdbc:mysql://Hadoop:3307/activitiDB?createDatabaseIfNotExist=true"
         + "&useUnicode=true&characterEncoding=utf8");
         engineConfiguration.setJdbcUsername("root");
         engineConfiguration.setJdbcPassword("qwer.1234");
         // 设置创建表的策略 （当没有表时，自动创建表）
         //		  public static final java.lang.String DB_SCHEMA_UPDATE_FALSE = "false";//不会自动创建表，没有表，则抛异常
         //		  public static final java.lang.String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";//先删除，再创建表
         //		  public static final java.lang.String DB_SCHEMA_UPDATE_TRUE = "true";//假如没有表，则自动创建
         engineConfiguration.setDatabaseSchemaUpdate("true");
         //通过ProcessEngineConfiguration对象创建 ProcessEngine 对象
         ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
         System.out.println("流程引擎创建成功!");
         */
        /**2. 通过加载 activiti.cfg.xml 获取 流程引擎 和自动创建数据库及表

         ProcessEngineConfiguration engineConfiguration=
         ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
         //从类加载路径中查找资源  activiti.cfg.xm文件名可以自定义
         ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
         System.out.println("使用配置文件Activiti.cfg.xml获取流程引擎");
         **/

        /**3. 通过ProcessEngines 来获取默认的流程引擎
         *
         */
        //默认会加载类路径下的 activiti.cfg.xml
		ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
		System.out.println("通过ProcessEngines 来获取流程引擎");
    }


    //部署流程定义
    public static void deploy(){
        //取得流程引擎对象
//		ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        //获取仓库服务 ：管理流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()//创建一个部署的构建器
                .addClasspathResource("diagrams/HelloActiviti.bpmn")//从类路径中添加资源,一次只能添加一个资源
                .addClasspathResource("diagrams/HelloActiviti.png")//从类路径中添加资源,一次只能添加一个资源
                .name("请求单流程")//设置部署的名称
                .category("办公类别")//设置部署的类别
                .deploy();

        System.out.println("部署的id"+deploy.getId());
        System.out.println("部署的名称"+deploy.getName());

    }


    //执行流程
    public static void startProcess(){
        String processDefiKey="Hello";
        //取运行时服务
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //取得流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefiKey);//通过流程定义的key 来执行流程
        System.out.println("流程实例id:"+pi.getId());//流程实例id
        System.out.println("流程定义id:"+pi.getProcessDefinitionId());//输出流程定义的id
    }
    //查询任务
    public static void queryTask(){
        //任务的办理人
        String assignee="申请人";
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        //办理人的任务列表
        List<Task> list = taskQuery.taskAssignee(assignee)//指定办理人
                .list();

        //遍历任务列表
        if(list!=null&&list.size()>0){
            for(Task task:list){
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务的id："+task.getId());
                System.out.println("任务的名称："+task.getName());

            }
        }

    }

    //完成任务
    public static void compileTask(){
        String taskId="9";
        //taskId：任务id
        processEngine.getTaskService().complete(taskId);
        System.out.println("当前任务执行完毕");
    }



}
