package Study.HBase;

import java.util.ArrayList;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.sun.tools.javac.util.List;

public class HBaseDMLUtils {

	private final Connection connection = HBaseFactory.getInstance();

	/**
	 * 增 改:put来覆盖
	 * 
	 * @throws Exception
	 */

	public void PutData(String strTableName, List<HBaseRow> rows) throws Exception {

		// 获取一个操作指定表的table对象,进行DML操作
		Table table = connection.getTable(TableName.valueOf("user_info"));

		// 构造要插入的数据为一个Put类型(一个put对象只能对应一个rowkey)的对象
		// put.add(byte[] family , byte[] qualifier , byte[] value) ;

		ArrayList<Put> puts = new ArrayList<Put>();
		for (HBaseRow row : rows) {
			Put put = new Put(row.getRow());
			for (HBaseFamily family : row.getFamilies()) {
				put.addColumn(family.getFamily(), family.getQualifier(), family.getValue());
			}
			puts.add(put);

		}

		// 插进去
		table.put(puts);

		table.close();
		connection.close();

	}

	private Delete getDeleteRow(String strRow) {
		return new Delete(Bytes.toBytes(strRow));
	}

	private Delete getDeleteRow(String strRow, List<HBaseFamily> families) {
		Delete delete = new Delete(Bytes.toBytes(strRow));
		for (HBaseFamily family : families) {
			delete.addColumn(family.getFamily(), family.getQualifier());
		}

		return delete;
	}

	/**
	 * 删
	 * 
	 * @throws Exception
	 */

	public void DeleteData(String strTableName, List<Delete> dels) throws Exception {
		Table table = connection.getTable(TableName.valueOf(strTableName));
		table.delete(dels);

		table.close();
		connection.close();
	}

	public byte[] getValueBykey(Result result, String family, String qualifier) {

		// 从结果中取用户指定的某个key的value
		byte[] value = result.getValue(family.getBytes(), qualifier.getBytes());
		System.out.println(new String(value));
		return value;
	}

	/**
	 * 查
	 * 
	 * @throws Exception
	 */

	public void GetData(String strTableName, String strRow) throws Exception {

		Table table = connection.getTable(TableName.valueOf(strTableName));

		Get get = new Get(strRow.getBytes());

		Result result = table.get(get);

		System.out.println("-------------------------");

		// 遍历整行结果中的所有kv单元格
		CellScanner cellScanner = result.cellScanner();
		while (cellScanner.advance()) {
			Cell cell = cellScanner.current();

			byte[] rowArray = cell.getRowArray(); // 本kv所属的行键的字节数组
			byte[] familyArray = cell.getFamilyArray(); // 列族名的字节数组
			byte[] qualifierArray = cell.getQualifierArray(); // 列名的字节数据
			byte[] valueArray = cell.getValueArray(); // value的字节数组

			System.out.println("行键: " + new String(rowArray, cell.getRowOffset(), cell.getRowLength()));
			System.out.println("列族名: " + new String(familyArray, cell.getFamilyOffset(), cell.getFamilyLength()));
			System.out
					.println("列名: " + new String(qualifierArray, cell.getQualifierOffset(), cell.getQualifierLength()));
			System.out.println("value: " + new String(valueArray, cell.getValueOffset(), cell.getValueLength()));

		}

		table.close();
		connection.close();

	}

}
