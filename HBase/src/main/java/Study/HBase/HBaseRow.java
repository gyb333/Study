package Study.HBase;


 

import org.apache.hadoop.hbase.util.Bytes;

public class HBaseRow {

	private byte[] rowKey;
	
	private byte[] family;
	
	
	private byte[] qualifier;
	
	private byte[] value;

	public HBaseRow(String rowKey, String family, String qualifier, String value) {
 
		this.rowKey = Bytes.toBytes(rowKey);
		this.family =  Bytes.toBytes(family);
		this.qualifier =  Bytes.toBytes(qualifier);
		this.value =  Bytes.toBytes(value);
	}

	public byte[] getRowKey() {
		return rowKey;
	}

	public void setRowKey(byte[] rowKey) {
		this.rowKey = rowKey;
	}

	public byte[] getFamily() {
		return family;
	}

	public void setFamily(byte[] family) {
		this.family = family;
	}

	public byte[] getQualifier() {
		return qualifier;
	}

	public void setQualifier(byte[] qualifier) {
		this.qualifier = qualifier;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
	
	 

	 

	 
	
	
	
	
	
}
