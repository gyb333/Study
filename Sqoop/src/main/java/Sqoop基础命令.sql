/**
 * 本质就是迁移数据， 迁移的方式：就是把sqoop的迁移命令转换成MR程序
 * 修改库的编码：alter database db_name character set utf8;
 * 修改表的编码：ALTER TABLE table_name CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci; 
 	set global time_zone='+8:00';
 	执行 flush privileges;
 	
 	--csrutil disable
sudo ln -s /usr/local/jdk1.8/bin/java /usr/bin/java
/usr/bin/java -version 
 
 */
sqoop version

--列出mysql数据库中的所有数据库
sqoop list-databases --connect jdbc:mysql://Master:3306/?serverTimezone=UTC --username root -password root

--连接mysql并列出hive3数据库中的表
sqoop list-tables --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC --username root -password root

1gyb10false
2test20true

sqoop eval --connect jdbc:mysql://Master:3306/sqoop2 \
--username root --password root \
--query "SELECT * FROM student  "

--将关系型数据的表结构复制到hive中,只是复制表的结构，表中的内容没有复制过去
sqoop create-hive-table --connect jdbc:mysql://Master:3306/sqoop2 \
--table student --username root --password root \
--hive-database hive3 --hive-table student


--导入mysql整表数据到hive表中
sqoop import --connect jdbc:mysql://Master:3306/sqoop2?zeroDateTimeBehavior=EXCEPTION \
--username root --password root \
--table student --hive-import --delete-target-dir \
--hive-database hive3 --hive-table student -m 1 \
--null-string '\\N' --null-non-string '\\N'

--从关系数据库导入整表和数据到hive中
sqoop import  --connect jdbc:mysql://Master:3306/sqoop2?zeroDateTimeBehavior=EXCEPTION \
--driver com.mysql.cj.jdbc.Driver --username root --password root \
--table student --fields-terminated-by '\t' \
--hive-import --delete-target-dir \
--hive-database hive3 --hive-table student1 -m 1

--从关系数据库导入表的数据到HDFS上文件
sqoop import --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC \
--driver com.mysql.jdbc.Driver --username root --password root \
--table student -m 1 --target-dir /hive3/warehouse/student

--从关系数据库增量导入表数据到hdfs中
sqoop import --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC \
--driver com.mysql.jdbc.Driver --username root --password root \
--table student -m 1 --target-dir /hive3/warehouse/student \
--check-column id -incremental append --last-value 2

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

sqoop export --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC \
--driver com.mysql.jdbc.Driver --username root --password root \
--table hivestudent -m 1 \
--input-null-string '\\N' --input-null-non-string '\\N' \
--input-fields-terminated-by '\001' --input-lines-terminated-by '\n' \
--export-dir /hive3/warehouse/hive3.db/student \
;

-- 将hive中的表数据导入到mysql中,在进行导入之前，mysql中的表hive_test必须已经提起创建好
create table tb_kc(name string,kcId string,score int)
row format delimited
fields terminated by '\001';
load data local inpath '/usr/local/BigData/hivedata/kc.dat' into table tb_kc;


CREATE TABLE IF NOT EXISTS tb_kc(
  name nvarchar(100),
  kcId nvarchar(100),
  score int
  );
 
sqoop export --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC \
--driver com.mysql.jdbc.Driver --username root --password root \
--table tb_kc -m 1 \
--input-null-string '\\N' --input-null-non-string '\\N' \
--input-fields-terminated-by '\001' --input-lines-terminated-by '\n' \
--export-dir /hive3/warehouse/hive3.db/tb_kc \
;


create table kc(name string,kcId string,score int)
row format delimited
fields terminated by ',';
load data local inpath '/usr/local/BigData/hivedata/kc' into table kc;


sqoop export --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC \
--driver com.mysql.jdbc.Driver --username root --password root \
--table tb_kc -m 1 \
--input-null-string '\\N' --input-null-non-string '\\N' \
--input-fields-terminated-by ',' --input-lines-terminated-by '\n' \
--export-dir /hive3/warehouse/hive3.db/kc \
;

--update-mode allowinsert
--input-null-string 'null' --input-null-non-string 'null'
 

 

 
day=`date -d '-1 day' +'%Y-%m-%d'`
sqoop export --connect jdbc:mysql://Master:3306/sqoop2?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8 \
--driver com.mysql.jdbc.Driver --username root --password root \
--input-fields-terminated-by '\001' \
--table dim_day \
--export-dir /user/hive/warehouse/app.db/dim_day/day=${day} /
