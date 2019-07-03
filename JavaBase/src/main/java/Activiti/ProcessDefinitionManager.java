package Activiti;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

//流程定义管理
public class ProcessDefinitionManager {

    private ProcessEngine processEngine= ProcessEngines.getDefaultProcessEngine();
    //部署流程定义，资源来在bpmn格式

    public void deployProcessDefi(){
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("采购流程")
                .addClasspathResource("diagrams/BuyBill.bpmn")
                .addClasspathResource("diagrams/BuyBill.png")
                .deploy();

        System.out.println("部署名称:"+deploy.getName());
        System.out.println("部署id:"+deploy.getId());
    }
    //部署流程定义,资源来自zip格式
    public void deployProcessDefiByZip(){
        InputStream in=getClass().getClassLoader().getResourceAsStream("diagrams/BuyBill.zip");
        Deployment deploy = processEngine.getRepositoryService()
                .createDeployment()
                .name("采购流程")
                .addZipInputStream(new ZipInputStream(in))
                .deploy();

        System.out.println("部署名称:"+deploy.getName());
        System.out.println("部署id:"+deploy.getId());
    }
    //查看流程定义
    public void queryProcessDefination(){
        String processDefiKey="BuyBill";//流程定义key
        //获取流程定义列表
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery()
                //查询 ，好比where
//		.processDefinitionId(proDefiId) //流程定义id
                // 流程定义id  ： buyBill:2:704   组成 ： proDefikey（流程定义key）+version(版本)+自动生成id
                .processDefinitionKey(processDefiKey)//流程定义key 由bpmn 的 process 的  id属性决定
//		.processDefinitionName(name)//流程定义名称  由bpmn 的 process 的  name属性决定
//		.processDefinitionVersion(version)//流程定义的版本
                .latestVersion()//最新版本

                //排序
                .orderByProcessDefinitionVersion().desc()//按版本的降序排序

                //结果
//		.count()//统计结果
//		.listPage(arg0, arg1)//分页查询
                .list();


        //遍历结果
        if(list!=null&&list.size()>0){
            for(ProcessDefinition temp:list){
                System.out.print("流程定义的id: "+temp.getId());
                System.out.print("流程定义的key: "+temp.getKey());
                System.out.print("流程定义的版本: "+temp.getVersion());
                System.out.print("流程定义部署的id: "+temp.getDeploymentId());
                System.out.println("流程定义的名称: "+temp.getName());
            }
        }
    }

    //查看bpmn 资源图片
    public void viewImage() throws Exception{
        String deploymentId="7501";
        String imageName=null;
        //取得某个部署的资源的名称  deploymentId
        List<String> resourceNames = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
        // buybill.bpmn  buybill.png
        if(resourceNames!=null&&resourceNames.size()>0){
            for(String temp :resourceNames){
                if(temp.indexOf(".png")>0){
                    imageName=temp;
                }
            }
        }

        /**
         * 读取资源
         * deploymentId:部署的id
         * resourceName：资源的文件名
         */
        InputStream resourceAsStream = processEngine.getRepositoryService()
                .getResourceAsStream(deploymentId, imageName);

        //把文件输入流写入到文件中
        File file=new File("d:/"+imageName);
        FileUtils.copyInputStreamToFile(resourceAsStream, file);

    }

    //删除流程定义
    public void deleteProcessDefi(){
        //通过部署id来删除流程定义
        String deploymentId="7501";
        processEngine.getRepositoryService().deleteDeployment(deploymentId);
    }


    public static void main(String[] args){

        ProcessDefinitionManager manager=new ProcessDefinitionManager();
//        manager.deployProcessDefi();
//        manager.deployProcessDefiByZip();
//        manager.queryProcessDefination();
        try {
            //manager.viewImage();
            manager.deleteProcessDefi();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
