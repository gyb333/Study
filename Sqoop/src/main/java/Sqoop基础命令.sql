/**
 * 本质就是迁移数据， 迁移的方式：就是把sqoop的迁移命令转换成MR程序
 * 
 */
sqoop version

sqoop list-databases --connect jdbc:mysql://Master:3306/?serverTimezone=UTC --username root -password root

sqoop list-tables --connect jdbc:mysql://Master:3306/hive3?serverTimezone=UTC --username root -password root







