package Study.HBase;

public class HBaseFamily {

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
