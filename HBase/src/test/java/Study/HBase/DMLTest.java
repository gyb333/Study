package Study.HBase;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DMLTest {
    static final String strTableName="t_user";
    static final String strFamilyName="FamilyName";
    static final String strBaseFamilyName="base_info";
    static final String strExtraFamilyName="extra_info";
    static final String rowKey="003";

    HBaseDMLUtils utils;
    List<HBaseRow> rows;

    @Before
    public void init() throws Exception {
        utils= new HBaseDMLUtils();
        rows = new ArrayList<HBaseRow>();
        String  rowKey="001";
        rows.add(new HBaseRow(rowKey,strBaseFamilyName,"username", "zhangsan"));
        rows.add(new HBaseRow(rowKey,strBaseFamilyName,"age", "18"));
        rows.add(new HBaseRow(rowKey,strBaseFamilyName,"sex", "female"));
        rows.add(new HBaseRow(rowKey,strExtraFamilyName,"career", "it"));

        rowKey="002";
        rows.add(new HBaseRow(rowKey,strExtraFamilyName, "career", "actoress"));
        rows.add(new HBaseRow(rowKey,strBaseFamilyName, "username", "liuyifei"));
    }

    @Test
    public  void PutData() throws IOException {
        utils.PutData(strTableName, rows);
    }

    @Test
    public  void TestAdd() throws IOException {
        utils.addRecord(strTableName, rowKey, strBaseFamilyName, "qualifier", "value");
        utils.addRecord(strTableName, rowKey, strBaseFamilyName, "qualifier", "value2");
        utils.addRecord(strTableName, rowKey, strBaseFamilyName, "qualifier1", "value1");
        utils.addRecord(strTableName, rowKey, strExtraFamilyName, "qualifier", "value");
        utils.addRecord(strTableName, rowKey, strExtraFamilyName, "qualifier1", "value1");
    }

    @Test
    public  void GetRowData() throws IOException {
        utils.GetRowData(strTableName,"001");
    }

    @Test
    public void getRecordByRow() throws IOException {
        utils.getRecordByRow(strTableName, "001");
    }

    @Test
    public  void getRangeRecord(){
        utils.getRangeRecord(strTableName, "001", "002");

    }

    @Test
    public void getRecordByTableName() throws IOException {
        utils.getRecordByTableName(strTableName);
    }

    @Test
    public void getValueByQualifier() throws IOException {
        utils.getValueByQualifier(strTableName, rowKey, strBaseFamilyName, "qualifier");
    }

    @Test
    public void deleteRecordByQualifier() throws IOException {
        utils.deleteRecordByQualifier(strTableName, rowKey, strBaseFamilyName, "qualifier1");
    }

    @Test
    public void deleteRecordByFamily() throws IOException {

        utils.deleteRecordByFamily(strTableName, rowKey, strExtraFamilyName);
    }

    @Test
    public void deleteRecordByFamilys() throws IOException {
        List<String> list =new ArrayList<String>();
        list.add(strBaseFamilyName);
        list.add(strExtraFamilyName);
        utils.deleteRecordByFamilys(strTableName, rowKey,list);
    }

    @Test
    public void deleteRecordByRow() throws IOException {
        utils.deleteRecordByRow(strTableName, "002");
    }

    @Test
    public void truncateTable() throws IOException {
        utils.truncateTable(strTableName);
    }
}
