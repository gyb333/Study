package Study.MapReduce.DataType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class FlowBean implements Writable {

	private int upFlow;
	private int downFlow;
	private int amountFlow;
	private String phone;
	
	
	
	
	public FlowBean() {
		 
	}

	public FlowBean(int upFlow, int downFlow, String phone) {
		this.upFlow = upFlow;
		this.downFlow = downFlow;
		this.amountFlow=upFlow+downFlow;
		this.phone = phone;
	}
	
	@Override
	public String toString() {
		return upFlow + ", " + downFlow + ", " + amountFlow ;
	}
	
	
	public int getAmountFlow() {
		return amountFlow;
	}

	public int getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(int upFlow) {
		this.upFlow = upFlow;
	}
	public int getDownFlow() {
		return downFlow;
	}
	public void setDownFlow(int downFlow) {
		this.downFlow = downFlow;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	public void write(DataOutput out) throws IOException {

		out.writeInt(upFlow);
		out.writeInt(downFlow);
		out.writeInt(amountFlow);
		out.writeUTF(phone);
		
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		 this.upFlow=in.readInt();
		 this.downFlow=in.readInt();
		 this.amountFlow=in.readInt();
		 this.phone=in.readUTF();
	}
	
	
	
	
	
	
	
}
