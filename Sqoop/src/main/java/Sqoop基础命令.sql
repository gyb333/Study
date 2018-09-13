/**
 * 本质就是迁移数据， 迁移的方式：就是把sqoop的迁移命令转换成MR程序
 * 
 */
sqoop version

--列出mysql数据库中的所有数据库
sqoop list-databases --connect jdbc:mysql://Master:3306/?serverTimezone=UTC --username root -password root

--连接mysql并列出hive3数据库中的表
sqoop list-tables --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC --username root -password root


sqoop eval --connect jdbc:mysql://Master:3306/sqoop2 \
--username root --password root \
--query "SELECT * FROM student  "

--将关系型数据的表结构复制到hive中,只是复制表的结构，表中的内容没有复制过去
sqoop create-hive-table --connect jdbc:mysql://Master:3306/sqoop2 \
--table student --username root --password root \
--hive-table hive3.student


--导入mysql整表数据到hive表中
sqoop  import  --connect jdbc:mysql://Master:3306/sqoop2?zeroDateTimeBehavior=EXCEPTION \
--username root --password root \
--table student  --hive-import  --delete-target-dir \
--hive-database hive3 --hive-table student -m 1

--从关系数据库导入整表和数据到hive中
sqoop  import  --connect jdbc:mysql://Master:3306/sqoop2?zeroDateTimeBehavior=EXCEPTION \
--username root --password root \
--table student --fields-terminated-by '\t' \
--hive-import  --delete-target-dir \
--hive-database hive3 --hive-table student1 -m 1


--从关系数据库查询数据导入到hive表中
sqoop  import  --connect jdbc:mysql://Master:3306/sqoop2 \
--username root --password root \
--hive-database hive3 --hive-table student \
--split-by id --hive-import -m 1 \
--target-dir  /hive3/warehouse/hive3.db/student --delete-target-dir  \
--query 'SELECT * FROM student  where id=1 and $CONDITIONS LIMIT 100'  

sqoop  import  --connect jdbc:mysql://Master:3306/sqoop2 \
--username root --password root \
--query "SELECT * FROM student  where id>0 and \$CONDITIONS LIMIT 100"  \
--split-by id --num-mappers 2 --hive-import -m 1 \
--delete-target-dir --target-dir  /hive3/warehouse/hive3.db/student \
--hive-database hive3 --hive-table student


-- 将hive中的表数据导入到mysql中,在进行导入之前，mysql中的表hive_test必须已经提起创建好
CREATE TABLE IF NOT EXISTS tb_kc(
  name nvarchar(100),
  kcId nvarchar(100),
  score int
  );
  
sqoop export  --connect jdbc:mysql://Master:3306/sqoop2 \
--username root --password root \
--table tb_kc --export-dir /hive3/warehouse/hive3.db/tb_kc/kc \
--fields-terminated-by ',' \
--input-null-string 'null' --input-null-non-string 'null'
#--input-null-string '\\N' --input-null-non-string '\\N'





