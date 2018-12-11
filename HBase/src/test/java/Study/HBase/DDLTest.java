package Study.HBase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DDLTest {

    static final String strTableName="t_user";
    static final String strFamilyName="FamilyName";
    static final String strBaseFamilyName="base_info";
    static final String strExtraFamilyName="extra_info";

    HBaseDDLUtils utils;

    List<String> strFamilyNames = new ArrayList<String>();



    @Before
    public void init() throws Exception {
        utils = new HBaseDDLUtils();
        strFamilyNames = new ArrayList<String>();
        strFamilyNames.add(strBaseFamilyName);
        strFamilyNames.add(strExtraFamilyName);
    }

    @Test
    public void TestCreate() throws IOException {
        utils.createSchemaTables(strTableName, strFamilyNames);
        Assert.assertTrue(true);
    }

    @Test
    public void TestAddColumn() throws IOException {
        utils.addSchemaColumnFamily(strTableName, strFamilyName);
        Assert.assertTrue(true);
    }

    @Test
    public  void TestModifyColumn() throws IOException {
        utils.modifySchemaColumnFamily(strTableName, strFamilyName);
        Assert.assertTrue(true);
    }

    @Test
    public void TestDrop() throws IOException {
        utils.dropSchemaTable(strTableName);
        Assert.assertTrue(true);
    }
}
