package Study.HBase;

import org.apache.hadoop.hbase.util.Bytes;

public class HBaseFamily {

	public HBaseFamily(byte[] family, byte[] qualifier, byte[] value) {
		this.family = family;
		this.qualifier = qualifier;
		this.value = value;
	}
	
	public HBaseFamily(String family, String qualifier, String value) {
		this.family = Bytes.toBytes(family);
		this.qualifier =Bytes.toBytes(qualifier); 
		this.value =Bytes.toBytes(value); 
	}

	private  byte[] family;
	
	private  byte[] qualifier;
	
	private  byte[] value;

	
	
	
	
	
	
	
	
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
