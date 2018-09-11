/** 
 * 启动hbase集群： start-hbase.sh
 * 启动完后，还可以在集群中找任意一台机器启动一个备用的master
 * hbase-daemon.sh start master 		#新启的这个master会处于backup状态

 * 启动hbase的命令行客户端:hbase shell
	Hbase> list     // 查看表
	Hbase> status   // 查看集群状态
	Hbase> version  // 查看集群版本

 * hbase表模型的要点：
	有表名,分为多个列族（不同列族的数据会存储在不同文件中）
	每一行有一个“行键rowkey”，而且行键在表中不能重复
	表中的每一对kv数据称作一个cell
	对数据存储多个历史版本（历史版本数量可配置）
	整张表由于数据量过大，会被横向切分成若干个region（用rowkey范围标识），不同region的数据也存储在不同文件中
	hbase会对插入的数据按顺序存储：
		要点一：首先会按行键排序
		要点二：同一行里面的kv会按列族排序，再按k排序

 */
--			表名      列族名   列族名
create 't_user_info','base_info','extra_info'
--查看表结构 describe
describe 't_user_info'

put 't_user_info','001','base_info:username','zhangsan'
put 't_user_info','001','base_info:age','18'
put 't_user_info','001','base_info:sex','female'
put 't_user_info','001','extra_info:career','it'
put 't_user_info','002','extra_info:career','actoress'
put 't_user_info','002','base_info:username','liuyifei'

--#scan 扫描
scan 't_user_info'	

--#get 单行Key数据
get 't_user_info','001'			

--#删除一个kv数据
delete 't_user_info','001','base_info:sex'

--#删除整行数据
deleteall 't_user_info','001'

--删除整个表：
disable 't_user_info'
drop 't_user_info'





