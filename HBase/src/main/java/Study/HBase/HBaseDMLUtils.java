package Study.HBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.sun.org.apache.xml.internal.resolver.readers.TextCatalogReader;

public class HBaseDMLUtils {

	private final Connection connection = HBaseFactory.getInstance();

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if (connection != null)
			connection.close();
	}

	/**
	 * 批量插入数据
	 * 
	 * @param strTableName
	 * @param rows
	 * @throws Exception
	 */
	public void PutData(String strTableName, List<HBaseRow> rows) throws IOException {

		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			ArrayList<Put> puts = new ArrayList<Put>();
			Put put;
			for (HBaseRow row : rows) {
				put = new Put(row.getRowKey());
				put.addColumn(row.getFamily(), row.getQualifier(), row.getValue());
				puts.add(put);
			}
			// 插进去
			table.put(puts);
			table.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 添加单条记录
	 * 
	 * @param strTableName
	 * @param rowKey
	 * @param family
	 * @param qualifier
	 * @param value
	 * @throws Exception
	 */
	public void addRecord(String strTableName, String rowKey, String family, String qualifier, String value)
			throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Put put = new Put(Bytes.toBytes(rowKey));
			put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value));
			table.put(put);
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查
	 * 
	 * @throws Exception
	 */

	public void GetRowData(String strTableName, String strRow) throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Get get = new Get(strRow.getBytes());
			Result result = table.get(get);
			// 遍历整行结果中的所有kv单元格
			CellScanner cellScanner = result.cellScanner();
			while (cellScanner.advance()) {
				Cell cell = cellScanner.current();

				byte[] rowArray = cell.getRowArray(); // 本kv所属的行键的字节数组
				byte[] familyArray = cell.getFamilyArray(); // 列族名的字节数组
				byte[] qualifierArray = cell.getQualifierArray(); // 列名的字节数据
				byte[] valueArray = cell.getValueArray(); // value的字节数组

				System.out.print("行键: " + new String(rowArray, cell.getRowOffset(), cell.getRowLength()));
				System.out.print(" 列族名: " + new String(familyArray, cell.getFamilyOffset(), cell.getFamilyLength()));
				System.out.print(
						" 列名: " + new String(qualifierArray, cell.getQualifierOffset(), cell.getQualifierLength()));
				System.out.println(" value: " + new String(valueArray, cell.getValueOffset(), cell.getValueLength()));
			}

			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getRecordByTableName(String strTableName) throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			ResultScanner rs = table.getScanner(new Scan());

			for (Result r : rs) {
				System.out.println("\n row: " + new String(r.getRow()));

				for (Cell cell : r.rawCells()) {
					System.out.print(new String(CellUtil.cloneRow(cell), "utf-8") + " ");
					System.out.print(new String(CellUtil.cloneFamily(cell), "utf-8") + ":");
					System.out.print(new String(CellUtil.cloneQualifier(cell), "utf-8") + " ");
					System.out.print(cell.getTimestamp() + " ");
					System.out.println(new String(CellUtil.cloneValue(cell), "utf-8"));

				}
			}
			rs.close();
			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getRecordByRow(String strTableName, String rowKey) throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Get get = new Get(rowKey.getBytes());
			Result rs = table.get(get);
			for (Cell cell : rs.rawCells()) {
				System.out.print(new String(CellUtil.cloneRow(cell)) + " ");
				System.out.print(new String(CellUtil.cloneFamily(cell)) + ":");
				System.out.print(new String(CellUtil.cloneQualifier(cell)) + " ");
				System.out.print(cell.getTimestamp() + " ");
				System.out.println(new String(CellUtil.cloneValue(cell)));
			}
			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getRangeRecord(String strTableName, String startRowKey, String endRowKey) {
		try {

			Table table = connection.getTable(TableName.valueOf(strTableName));
			Scan s = new Scan().withStartRow(startRowKey.getBytes()).withStopRow(endRowKey.getBytes());
			ResultScanner rs = table.getScanner(s);
			for (Result r : rs) {
				for (Cell cell : r.rawCells()) {
					System.out.print(new String(CellUtil.cloneRow(cell)) + " ");
					System.out.print(new String(CellUtil.cloneFamily(cell)) + ":");
					System.out.print(new String(CellUtil.cloneQualifier(cell)) + " ");
					System.out.print(cell.getTimestamp() + " ");
					System.out.println(new String(CellUtil.cloneValue(cell)));
				}
			}
			rs.close();
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public NavigableMap<byte[], byte[]> getRecordByFamily(String strTableName, String rowKey, String family) {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Get get = new Get(rowKey.getBytes());
			Result result = table.get(get);
			// 从结果中取用户指定的某个key的value
			return result.getFamilyMap(Bytes.toBytes(family));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public byte[] getValueByQualifier(String strTableName, String rowKey, String family, String qualifier)
			throws IOException {
		byte[] value = null;
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Get get = new Get(rowKey.getBytes());
			Result result = table.get(get);
			// 从结果中取用户指定的某个key的value
			value = result.getValue(family.getBytes(), qualifier.getBytes());
			table.close();
			System.out.println(new String(value));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	
	
	public ResultScanner getRSByFilter(String strTableName,String family, String qualifier,CompareOperator co,String value) throws IOException {
		 Table table = connection.getTable(TableName.valueOf(strTableName));
		//创建查询器

        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(family), 
                Bytes.toBytes(qualifier), co, Bytes.toBytes(value));

        //创建扫描器
        Scan scan = new Scan();

        //将查询过滤器的加入到数据表扫描器对象
        scan.setFilter(filter);

        //执行查询操作，并获取查询结果
        ResultScanner scanner =table.getScanner(scan);
 
        return scanner;
	}
	
	
	
	public void truncateTable(String strTableName) throws IOException {
		Admin admin = connection.getAdmin();
		// 取得目标数据表的表明对象
		TableName tableName = TableName.valueOf(strTableName);
		// 设置表状态为无效
		admin.disableTable(tableName);
		// 清空指定表的数据
		admin.truncateTable(tableName, true);		
		admin.close();		
	}

	
	public void deleteRecordByRow(String strTableName, String rowKey) throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			List<Delete> list = new ArrayList<Delete>();
			Delete del = new Delete(rowKey.getBytes());
			list.add(del);
			table.delete(list);
			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteRecordByFamilys(String strTableName, String rowKey, List<String> familys) throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			List<Delete> list = new ArrayList<Delete>();
			Delete del = new Delete(rowKey.getBytes());
			for (String family : familys) {
				del.addFamily(family.getBytes());
				list.add(del);
			}
			table.delete(list);
			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteRecordByFamily(String strTableName, String rowKey, String family) throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Delete del = new Delete(rowKey.getBytes());
			del.addFamily(family.getBytes());
			table.delete(del);
			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deleteRecordByQualifier(String strTableName, String rowKey, String family, String qualifier)
			throws IOException {
		try {
			Table table = connection.getTable(TableName.valueOf(strTableName));
			Delete del = new Delete(rowKey.getBytes());
			del.addColumn(family.getBytes(), qualifier.getBytes());
			table.delete(del);
			table.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
