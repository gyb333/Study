/** SELECT
	JOIN | inner join
	left join | left outer join
	right join  | right outer join
	full outer join
* 
* 
	t_a			t_b
	a,1			b,22
	b,2			c,33
	c,3			d,44
	
	select a.*,b.*
	from t_a a join t_b b
	on a.id=b.id;
	
	b,2,b,22
	c,3,c,33
	
	select a.*,b.*
	from t_a a left join t_b b
	on a.id=b.id;
	
	a,1,null,null
	b,2,b,22
	c,3,c,33
	
	select a.*,b.*
	from t_a a right join t_b b
	on a.id=b.id;
	
	b,2,b,22
	c,3,c,33
	null,null,d,44
	
	select a.*,b.*
	from t_a a full outer join t_b b
	on a.id=b.id;
	
	a,1,null,null
	b,2,b,22
	c,3,c,33
	null,null,d,44

 * 
 * left semi join   可以提高exist |  in 这种查询需求的效率
	select a.* from t_a a left semi join t_b b on a.id=b.id; 	#本查询中，无法取到右表的数据
 * 
 * 老版本中，不支持非等值的join，在新版中：1.2.0后，都支持非等值join，不过写法应该如下：
	select a.*,b.* from t_a a,t_b b where a.id>b.id;
 * 
 * 不支持的语法：  select a.*,b.* from t_a a join t_b b on a.id>b.id;
 * 
 * 
 */















