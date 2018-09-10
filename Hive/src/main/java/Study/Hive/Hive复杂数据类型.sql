/**							数据类型
数字类型
TINYINT (1-byte signed integer, from -128 to 127)
SMALLINT (2-byte signed integer, from -32,768 to 32,767)

INT/INTEGER (4-byte signed integer, from -2,147,483,648 to 2,147,483,647)

BIGINT (8-byte signed integer, from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807)
FLOAT (4-byte single precision floating point number)
DOUBLE (8-byte double precision floating point number)
*/
create table t_dataType(a string ,b int,c bigint,d float,e double,f tinyint,g smallint)

/**
日期时间类型
TIMESTAMP (Note: Only available starting with Hive 0.8.0)
DATE (Note: Only available starting with Hive 0.12.0)

示例，假如有以下数据文件：
1,zhangsan,1985-06-302,lisi,1986-07-103,wangwu,1985-08-09
那么，就可以建一个表来对数据进行映射
*/
create table t_customer(id int,name string,birthday date)
row format delimited fields terminated by ',';
--然后导入数据
load data local inpath '/root/customer.dat' into table t_customer;

/*
字符串类型
STRING
VARCHAR (Note: Only available starting with Hive 0.12.0)
CHAR (Note: Only available starting with Hive 0.13.0)

混杂类型
BOOLEAN
BINARY (Note: Only available starting with Hive 0.8.0)

复合类型
array数组类型
arrays: ARRAY<data_type> (Note: negative values and non-constant expressions are allowed as of Hive 0.14.)

示例：array类型的应用
假如有如下数据需要用hive的表去映射：
战狼2,吴京:吴刚:龙母,2017-08-16三生三世十里桃花,刘亦菲:痒痒,2017-08-20
设想：如果主演信息用一个数组来映射比较方便

*/
 
create table t_movie(moive_name string,actors array<string>,first_show date)
row format delimited fields terminated by ','
collection items terminated by ':';

--导入数据：
load data local inpath '/root/movie.dat' into table t_movie;

--查询：
select * from t_movie;
select moive_name,actors[0] from t_movie;
select moive_name,actors from t_movie where array_contains(actors,'吴刚');
select moive_name,size(actors) from t_movie;

/*		map类型
maps: MAP<primitive_type, data_type> (Note: negative values and non-constant expressions are allowed as of Hive 0.14.)

假如有以下数据：
1,zhangsan,father:xiaoming#mother:xiaohuang#brother:xiaoxu,282,lisi,father:mayun#mother:huangyi#brother:guanyu,223,wangwu,father:wangjianlin#mother:ruhua#sister:jingtian,294,mayun,father:mayongzhen#mother:angelababy,26

可以用一个map类型来对上述数据中的家庭成员进行描述

*/
create table t_person(id int,name string,family_members map<string,string>,age int)
row format delimited fields terminated by ','
collection items terminated by '#'
map keys terminated by ':';

select * from t_person;

--## 取map字段的指定key的值
select id,name,family_members['father'] as father from t_person;

--## 取map字段的所有key
select id,name,map_keys(family_members) as relation from t_person;

--## 取map字段的所有value
select id,name,map_values(family_members) from t_person;
select id,name,map_values(family_members)[0] from t_person;

--## 综合：查询有brother的用户信息
select id,name,father from (select id,name,family_members['brother'] as father from t_person) tmpwhere father is not null;


/**
struct类型
structs: STRUCT<col_name : data_type, ...>

假如有如下数据：
1,zhangsan,18:male:beijing2,lisi,28:female:shanghai

其中的用户信息包含：年龄：整数，性别：字符串，地址：字符串
设想用一个字段来描述整个用户信息，可以采用struct

*/
create table t_person_struct(id int,name string,info struct<age:int,sex:string,addr:string>)
row format delimited fields terminated by ','
collection items terminated by ':';


select * from t_person_struct;
select id,name,info.age from t_person_struct;


