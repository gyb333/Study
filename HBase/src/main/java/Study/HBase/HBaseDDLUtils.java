package Study.HBase;

import java.io.IOException;

 
import org.apache.hadoop.hbase.HConstants;
 
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
 
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
 

public class HBaseDDLUtils {

	private final Connection connection = HBaseFactory.getInstance();

	public void createOrOverwrite(Admin admin, TableDescriptor table) throws IOException {

		if (admin.tableExists(table.getTableName())) {
			admin.disableTable(table.getTableName());
			admin.deleteTable(table.getTableName());
		}
		admin.createTable(table);
	}

	public void createSchemaTables(String strTableName, String strFamilyName) throws IOException {

		Admin admin = connection.getAdmin();

//		HTableDescriptor table = new HTableDescriptor(TableName.valueOf(strTableName));
//		table.addFamily(new HColumnDescriptor(strFamilyName).setCompressionType(Algorithm.NONE));

		ModifyableColumnFamilyDescriptor mcfd = new ModifyableColumnFamilyDescriptor(
				ColumnFamilyDescriptorBuilder.of(strFamilyName)).setCompactionCompressionType(Algorithm.NONE);
		TableDescriptor table = TableDescriptorBuilder.newBuilder(TableName.valueOf(strTableName))
				.modifyColumnFamily(mcfd).build();

		System.out.print("Creating table. ");
		createOrOverwrite(admin, table);
		System.out.println(" Done.");

	}

	public void modifySchema(String strTableName, String strFamilyName, String strFamilyNameUpdate) throws IOException {

		Admin admin = connection.getAdmin();

		TableName tableName = TableName.valueOf(strTableName);
		if (!admin.tableExists(tableName)) {
			System.out.println("Table does not exist.");
			System.exit(-1);
		}

//		HTableDescriptor table = admin.getTableDescriptor(tableName);	 

		TableDescriptor table = admin.getDescriptor(tableName);

		// Update existing table
//		HColumnDescriptor newColumn = new HColumnDescriptor("NEWCF");
//		newColumn.setCompactionCompressionType(Algorithm.GZ);
//		newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
//		admin.addColumn(tableName, newColumn);

		ModifyableColumnFamilyDescriptor columnFamilyNew = new ModifyableColumnFamilyDescriptor(
				ColumnFamilyDescriptorBuilder.of(strFamilyName)).setCompactionCompressionType(Algorithm.GZ)
						.setMaxVersions(HConstants.ALL_VERSIONS);
		admin.addColumnFamily(tableName, columnFamilyNew);

		// Update existing column family
//		HColumnDescriptor existingColumn = new HColumnDescriptor(strFamilyNameUpdate);
//		existingColumn.setCompactionCompressionType(Algorithm.GZ);
//		existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
//		table.modifyFamily(existingColumn);
//		admin.modifyTable(tableName, table);

		ModifyableColumnFamilyDescriptor columnFamilyUpdate = new ModifyableColumnFamilyDescriptor(
				ColumnFamilyDescriptorBuilder.of(strFamilyNameUpdate)).setCompactionCompressionType(Algorithm.GZ)
						.setMaxVersions(HConstants.ALL_VERSIONS);
		admin.modifyColumnFamily(tableName, columnFamilyUpdate);

		// Disable an existing table
		admin.disableTable(tableName);

		// Delete an existing column family
//		admin.deleteColumn(tableName, strFamilyName.getBytes("UTF-8"));

		admin.deleteColumnFamily(tableName, strFamilyNameUpdate.getBytes("UTF-8"));

		// Delete a table (Need to be disabled first)
		admin.deleteTable(tableName);

	}

	
}
