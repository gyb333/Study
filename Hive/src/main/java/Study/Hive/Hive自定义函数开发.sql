/**
 * 自定义函数的步骤：
a、先开发一个java程序
public class UserInfoParser extends UDF {
	public String evaluate(String field, int index) {

		String replaceAll = field.replaceAll("\\|", ":");

		String[] split = replaceAll.split(":");

		return split[index-1];

	}
}

b、将java程序打成jar包，上传到hive所在的机器上
c、在hive的命令行中输入以下命令，将程序jar包添加到hive运行时的classpath中：
hive> add jar /root/udf.jar;
d、在hive中创建一个函数名，映射到自己开发的java类：
hive> create temporary function uinfo_parse as 'com.doit.hive.udf.UserInfoParser'
e、接下来就可以使用自定义的函数uinfo_parse，用函数拆解原来的字段，然后将结果保存到一张明细表中：
create table t_u_info as
select id,
uinf_parse(user_info,1) as uname,
uinf_parse(user_info,2) as age,
uinf_parse(user_info,3) as addr,
uinf_parse(user_info,4) as sexual,
uinf_parse(user_info,5) as hangye,salary
from  t_user_info;

 */