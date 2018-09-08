--时间处理函数：
from_unixtime(21938792183,'yyyy-MM-dd HH:mm:ss')  -->   '2017-06-03 17:50:30'

--类型转换函数：
from_unixtime(cast('21938792183' as bigint),'yyyy-MM-dd HH:mm:ss')

--字符串截取和拼接
substr("abcd",1,3)  -->   'abc'
concat('abc','def')  -->  'abcdef'

--Json数据解析函数
get_json_object('{\"key1\":3333,\"key2\":4444}' , '$.key1')  -->  3333

json_tuple('{\"key1\":3333,\"key2\":4444}','key1','key2') as(key1,key2)  --> 3333, 4444

--url解析函数
parse_url_tuple('http://www.edu360.cn/bigdata/baoming?userid=8888','HOST','PATH','QUERY','QUERY:userid')
--->     www.edu360.cn      /bigdata/baoming     userid=8888   8888

--时间戳函数
select current_date from dual;
select current_timestamp from dual;

select unix_timestamp() from dual;
1491615665

select unix_timestamp('2011-12-07 13:01:03') from dual;
1323234063

select unix_timestamp('20111207 13:01:03','yyyyMMdd HH:mm:ss') from dual;
1323234063

select from_unixtime(1323234063,'yyyy-MM-dd HH:mm:ss') from dual;



--获取日期、时间
select year('2011-12-08 10:03:01') from dual;
2011
select year('2012-12-08') from dual;
2012
select month('2011-12-08 10:03:01') from dual;
12
select month('2011-08-08') from dual;
8
select day('2011-12-08 10:03:01') from dual;
8
select day('2011-12-24') from dual;
24
select hour('2011-12-08 10:03:01') from dual;
10
select minute('2011-12-08 10:03:01') from dual;
3
select second('2011-12-08 10:03:01') from dual;
1

--日期增减
select date_add('2012-12-08',10) from dual;
2012-12-18

date_sub (string startdate, int days) : string
例：
select date_sub('2012-12-08',10) from dual;
2012-11-28



--json函数
create table t_rate 
as 
select uid,movie,rate,year(from_unixtime(cast(ts as bigint))) as year,month(from_unixtime(cast(ts as bigint))) as month,day(from_unixtime(cast(ts as bigint))) as day,hour(from_unixtime(cast(ts as bigint))) as hour,
minute(from_unixtime(cast(ts as bigint))) as minute,from_unixtime(cast(ts as bigint)) as ts
from 
(select 
json_tuple(rateinfo,'movie','rate','timeStamp','uid') as(movie,rate,ts,uid)
from t_json) tmp
;



--分组topn
select *,row_number() over(partition by uid order by rate desc) as rank from t_rate;

select uid,movie,rate,ts
from 
(select uid,movie,rate,ts,row_number() over(partition by uid order by rate desc) as rank from t_rate) tmp
where rank<=3;



--网页URL数据解析函数：parse_url_tuple
select parse_url_tuple("http://www.edu360.cn/baoming/youhui?cookieid=20937219375",'HOST','PATH','QUERY','QUERY:cookieid') 
from dual;
+----------------+------------------+-----------------------+--------------+--+
|       c0       |        c1        |          c2           |      c3      |
+----------------+------------------+-----------------------+--------------+--+
| www.edu360.cn  | /baoming/youhui  | cookieid=20937219375  | 20937219375  |
+----------------+------------------+-----------------------+--------------+--+
