package HBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.hadoop.hbase.HConstants;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.BloomType;

 
public class HBaseDDLUtils {

	private final Connection connection = HBaseFactory.getInstance();

	public void createOrOverwrite(Admin admin, TableDescriptor table) throws IOException {

		if (admin.tableExists(table.getTableName())) {
			admin.disableTable(table.getTableName());
			admin.deleteTable(table.getTableName());
		}
		admin.createTable(table);
	}

	public void createSchemaTables(String strTableName, List<String> strFamilyNames) throws IOException {

		Admin admin = connection.getAdmin();

		Collection<ColumnFamilyDescriptor> families=new ArrayList<ColumnFamilyDescriptor>(); 
		ModifyableColumnFamilyDescriptor mcfd;
		for(String strFamilyName:strFamilyNames) {
			mcfd = new ModifyableColumnFamilyDescriptor(
					ColumnFamilyDescriptorBuilder.of(strFamilyName))
					.setCompactionCompressionType(Algorithm.NONE);
			families.add(mcfd);
		}
		
		
		TableDescriptor table = TableDescriptorBuilder
				.newBuilder(TableName.valueOf(strTableName))
//				.setColumnFamily(mcfd)
				.setColumnFamilies(families)
				.build();

 
		createOrOverwrite(admin, table);
 

	}

	public void addSchemaColumnFamily(String strTableName,String strFamilyName) throws IOException {
		Admin admin = connection.getAdmin();

		TableName tableName = TableName.valueOf(strTableName);
		if (!admin.tableExists(tableName)) {
			System.out.println("Table does not exist.");
			System.exit(-1);
		}
		TableDescriptor table = admin.getDescriptor(tableName);
		ModifyableColumnFamilyDescriptor columnFamilyNew = new ModifyableColumnFamilyDescriptor(
				ColumnFamilyDescriptorBuilder.of(strFamilyName))
				.setBloomFilterType(BloomType.ROWCOL)	//设置布隆过滤器
				.setCompactionCompressionType(Algorithm.GZ)
						.setMaxVersions(HConstants.ALL_VERSIONS);
		admin.addColumnFamily(tableName, columnFamilyNew);
	}
	
	public void dropSchemaColumnFamily(String strTableName,String strFamilyName) throws IOException {
		Admin admin = connection.getAdmin();

		TableName tableName = TableName.valueOf(strTableName);
		if (!admin.tableExists(tableName)) {
			System.out.println("Table does not exist.");
			System.exit(-1);
		}
		
		admin.deleteColumnFamily(tableName, strFamilyName.getBytes("UTF-8"));
	}
	
	public void modifySchemaColumnFamily(String strTableName,  String strFamilyName) throws IOException {

		Admin admin = connection.getAdmin();

		TableName tableName = TableName.valueOf(strTableName);
		if (!admin.tableExists(tableName)) {
			System.out.println("Table does not exist.");
			System.exit(-1);
		}
 

		ModifyableColumnFamilyDescriptor columnFamilyUpdate = new ModifyableColumnFamilyDescriptor(
				ColumnFamilyDescriptorBuilder.of(strFamilyName))
				.setCompactionCompressionType(Algorithm.GZ)
						.setMaxVersions(HConstants.ALL_VERSIONS);
		admin.modifyColumnFamily(tableName, columnFamilyUpdate);


	}

	
	
	
	
	public void dropSchemaTable(String strTableName) throws IOException {
		Admin admin = connection.getAdmin();
		TableName tableName = TableName.valueOf(strTableName);
		if (!admin.tableExists(tableName)) {
			System.out.println("Table does not exist.");
			return;
		}
		// Disable an existing table
		if(!admin.isTableDisabled(tableName))
			admin.disableTable(tableName);
		// Delete a table (Need to be disabled first)
		admin.deleteTable(tableName);
	}

}
