package Study.HBase;


import java.util.List;

public class HBaseRow {

	private byte[] row;
	
	private List<HBaseFamily> families;

	public byte[] getRow() {
		return row;
	}

	public void setRow(byte[] row) {
		this.row = row;
	}

	public List<HBaseFamily> getFamilies() {
		return families;
	}

	public void setFamilies(List<HBaseFamily> families) {
		this.families = families;
	}
	
	
	
	
	
}
