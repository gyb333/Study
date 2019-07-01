package HBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;

public class App {
	static HBaseDDLUtils utils = new HBaseDDLUtils();
	static HBaseDMLUtils dmlUtils=new HBaseDMLUtils();
	
	 static final String strTableName="t_user";
	 static final String strFamilyName="FamilyName";
	 static final List<String> strFamilyNames = new ArrayList<String>();
	 static final String strBaseFamilyName="base_info";
	 static final String strExtraFamilyName="extra_info";
	 
	 
	 static {
		 strFamilyNames.add(strBaseFamilyName);
		 strFamilyNames.add(strExtraFamilyName);
	 }
	 
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DDLTest() ;
		DMLTest();
	}
	
	
	
	public static void DDLTest() {
		 try {
			 utils.dropSchemaTable(strTableName);
			 utils.createSchemaTables(strTableName, strFamilyNames);
			 utils.addSchemaColumnFamily(strTableName, strFamilyName);
			 utils.modifySchemaColumnFamily(strTableName, strFamilyName);
			 utils.dropSchemaColumnFamily(strTableName, strFamilyName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void DMLTest() {
		List<HBaseRow> rows = new ArrayList<HBaseRow>();
		String  rowKey="001";
		rows.add(new HBaseRow(rowKey,strBaseFamilyName,"username", "zhangsan"));
		rows.add(new HBaseRow(rowKey,strBaseFamilyName,"age", "18"));
		rows.add(new HBaseRow(rowKey,strBaseFamilyName,"sex", "female"));
		rows.add(new HBaseRow(rowKey,strExtraFamilyName,"career", "it"));
		
		rowKey="002";
		rows.add(new HBaseRow(rowKey,strExtraFamilyName, "career", "actoress"));
		rows.add(new HBaseRow(rowKey,strBaseFamilyName, "username", "liuyifei"));
 
		
		
		try {
			String timestamp="1536651890123";
			String time=HBaseUtils.GetTimeByStamp(timestamp);
			timestamp=HBaseUtils.GetStampByTime(time);

			rowKey="003";
			dmlUtils.PutData(strTableName, rows);
			dmlUtils.addRecord(strTableName, rowKey, strBaseFamilyName, "qualifier", "value");
			dmlUtils.addRecord(strTableName, rowKey, strBaseFamilyName, "qualifier", "value2");
			dmlUtils.addRecord(strTableName, rowKey, strBaseFamilyName, "qualifier1", "value1");
			dmlUtils.addRecord(strTableName, rowKey, strExtraFamilyName, "qualifier", "value");
			dmlUtils.addRecord(strTableName, rowKey, strExtraFamilyName, "qualifier1", "value1");
			
			
			dmlUtils.GetRowData(strTableName,"001");
			
			dmlUtils.getRecordByRow(strTableName, "001");
			dmlUtils.getRangeRecord(strTableName, "001", "002");
			dmlUtils.getRecordByTableName(strTableName);
			
			dmlUtils.getValueByQualifier(strTableName, rowKey, strBaseFamilyName, "qualifier");
			
			dmlUtils.deleteRecordByQualifier(strTableName, rowKey, strBaseFamilyName, "qualifier1");
			System.out.println("deleteRecordByQualifier success");
			
			dmlUtils.deleteRecordByFamily(strTableName, rowKey, strExtraFamilyName);
			System.out.println("deleteRecordByFamily success");
			
			List<String> list =new ArrayList<String>();
			list.add(strBaseFamilyName);
			list.add(strExtraFamilyName);
			dmlUtils.deleteRecordByFamilys(strTableName, rowKey,list);
			System.out.println("deleteRecordByFamilys success");
			
			dmlUtils.deleteRecordByRow(strTableName, "002");
			System.out.println("deleteRecordByRow success");
			
			dmlUtils.truncateTable(strTableName);
			System.out.println("truncateTable success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
