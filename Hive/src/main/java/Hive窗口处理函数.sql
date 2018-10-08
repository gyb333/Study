/** 		级联报表查询
有如下数据：

A,2015-01,5
A,2015-01,15
B,2015-01,5
A,2015-01,8
B,2015-01,25
A,2015-01,5
C,2015-01,10
C,2015-01,20
A,2015-02,4
A,2015-02,6
C,2015-02,30
C,2015-02,10
B,2015-02,10
B,2015-02,5
A,2015-03,14
A,2015-03,6
B,2015-03,20
B,2015-03,25
C,2015-03,10
C,2015-03,20

需要要开发hql脚本，来统计出如下累计报表：
用户 月份 月总额 累计到当月的总额
A 2015-01 33 33
A 2015-02 10 43
A 2015-03 30 73
B 2015-01 30 30
B 2015-02 15 45
*/
create table t_access(username string,month string,counts int)
row format delimited 
fields terminated by ',';

load data local inpath '/usr/local/BigData/hivedata/t_access.dat' into table t_access;

select username,month,counts,
sum(counts) over(partition by username order by month 
rows between unbounded preceding and current row) as  grandtotal
from (select username,month,sum(counts) counts from t_access group by username,month) tmp;

/* username        month   counts  grandtotal
		A       2015-01 33      33
		A       2015-02 10      43
		A       2015-03 20      63
		B       2015-01 30      30
		B       2015-02 15      45
		B       2015-03 45      90
		C       2015-01 30      30
		C       2015-02 40      70
		C       2015-03 30      100
*/
  
select username,month,counts,
row_number() over(partition by username,month order by counts) as rank
,lag(counts,1,0) over(partition by username,month order by counts) as lag_counts
,lead(counts,1,0) over(partition by username,month order by counts)as lead_counts
,first_value(counts) over(partition by username,month order by counts)as first_counts
,last_value(counts) over(partition by username,month order by counts)as last_counts
from t_access;

/*	username        month   counts  rank    lag_counts      lead_counts     first_counts    last_counts
		A       2015-01 5       1       0       5       5       5
		A       2015-01 5       2       5       8       5       5
		A       2015-01 8       3       5       15      5       8
		A       2015-01 15      4       8       0       5       15
		A       2015-02 4       1       0       6       4       4
		A       2015-02 6       2       4       0       4       6
		A       2015-03 6       1       0       14      6       6
		A       2015-03 14      2       6       0       6       14
		B       2015-01 5       1       0       25      5       5
		B       2015-01 25      2       5       0       5       25
		B       2015-02 5       1       0       10      5       5
		B       2015-02 10      2       5       0       5       10
		B       2015-03 20      1       0       25      20      20
		B       2015-03 25      2       20      0       20      25
		C       2015-01 10      1       0       20      10      10
		C       2015-01 20      2       10      0       10      20
		C       2015-02 10      1       0       30      10      10
		C       2015-02 30      2       10      0       10      30
		C       2015-03 10      1       0       20      10      10
		C       2015-03 20      2       10      0       10      20
 */




   