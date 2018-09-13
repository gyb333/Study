/**
 * array数据类型示例 
1       a1,b1,c1
2       a2,b2,c2
3       a3,b3,c3
4       物理,化学
5       语文,英语,数学
6       天文
 */
--建表：
create table t_stu_subjects(id int,subjects array<string>)
row format delimited
fields terminated by '\t'
collection items terminated by ',';

--导入数据
load data local inpath '/usr/local/BigData/hivedata/array.txt' into table t_stu_subjects;

select  id,array_contains(subjects,'语文') from t_stu-subjects;

--查询列值拆分
	select id,subjects[0] from t_stu_subjects;

	select id,subjects[0],subjects[1],subjects[2] from t_stu_subjects;
/*
id      _c1     _c2     _c3
1       a1      b1      c1
2       a2      b2      c2
3       a3      b3      c3
4       物理    化学    NULL
5       语文    英语    数学
6       天文    NULL    NULL
*/
	
--使用explode —— 行转列
	select id,explode(subjects) from t_stu_subjects;
  	
--配合lateral view 列转行方便统计
select id, tmp.* from t_stu_subjects
lateral view explode(subjects) tmp as sub;
/*
1       a1
1       b1
1       c1
2       a2
2       b2
2       c2
3       a3
3       b3
3       c3
4       物理
4       化学
5       语文
5       英语
5       数学
6       天文
*/
--统计，选修了天文学的学生
select t_lt.id,t_lt.sub from 
(
select id, tmp.sub as sub from t_stu_subjects 
lateral view explode(subjects) tmp as sub
) t_lt
where t_lt.sub='天文';

--explode函数应用示例2： wordcount
Create table if not exists dual (dummy string);		#hive中构建dual虚表
insert into table dual values('');
--load data local inpath '/usr/local/BigData/hivedata/dual' overwrite into table dual;
select 1+1 from dual;	
	
select words.word,count(1) as counts
from
(select explode(split("a b c d e f a b c d e f g"," ")) as word 
from dual
) words
group by word
order by counts desc;

--利用explode和lateral view 实现hive版的wordcount
/*
a b c d e f g
a b c
e f g a
b c d b
 */
create table tb_wc(line string) row format delimited;
load data local inpath '/usr/local/BigData/hivedata/words' into table tb_wc;
select a.word,count(1) cnt
from 
(select tmp.* from tb_wc lateral view explode(split(line,' ')) tmp as word) a
group by a.word
order by cnt desc;
/*结果
 * a.word  cnt
b       4
c       3
a       3
g       2
f       2
e       2
d       2
*/

--row_number() over 常用于求分组TOPN
/*
有如下数据：
zhangsan,kc1,90
zhangsan,kc2,95
zhangsan,kc3,68
lisi,kc1,88
lisi,kc2,95
lisi,kc3,98
*/
create table tb_kc(name string,kcId string,score int)
row format delimited
fields terminated by ',';

load data local inpath '/usr/local/BigData/hivedata/kc' into table tb_kc;

select *,row_number() over(partition by name order by score desc) as rank from tb_kc;
/*
tb_kc.name      tb_kc.kcid      tb_kc.score     rank
lisi    kc3     98      1
lisi    kc2     95      2
lisi    kc1     88      3
zhangsan        kc2     95      1
zhangsan        kc1     90      2
zhangsan        kc3     NULL    3 
 */
select name,kcid,score
from
(select *,row_number() over(partition by name order by score desc) as rank from tb_kc) tmp
where rank<3;

/*
lisi    kc3     98
lisi    kc2     95
zhangsan        kc2     95
zhangsan        kc1     90
 */

