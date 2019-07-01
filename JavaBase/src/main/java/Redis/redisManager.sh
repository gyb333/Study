usage="Usage: $0 (start|stop)"
if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi
behave=$1
echo "$behave redis cluster..."
#主机名称
for i in Master Second Slave
do
#使用ssh进行启动
script=""
if [ $1 == 'start' ];then	
script="source /etc/profile;/usr/local/redis5/create-cluster start" 
fi
if [ $1 ==  'stop' ];then
script="source /etc/profile;/usr/local/redis5/create-cluster stop"
ssh root@$i "$script"
sleep 3
script="source /etc/profile;/usr/local/redis5/create-cluster clean"
fi
ssh root@$i "$script"
done
 