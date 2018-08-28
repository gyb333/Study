package Study.MapReduce.Beans;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

 

/**
 * 
 * ¶©µ¥ΚµΜε
 *
 */
public  class OrderBean implements WritableComparable<OrderBean> {

	private String orderId;
	private String userId;
	private String productName;
	private float price;
	private int number;
	private float amount;

	public void set(String orderId, String userId, String productName, float price, int number) {
		this.orderId = orderId;
		this.userId = userId;
		this.productName = productName;
		this.price = price;
		this.number = number;
		this.amount = this.price * this.number;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(orderId);
		out.writeUTF(userId);
		out.writeUTF(productName);
		out.writeFloat(price);
		out.writeInt(number);

	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.orderId = in.readUTF();
		this.userId = in.readUTF();
		this.productName = in.readUTF();
		this.price = in.readFloat();
		this.number = in.readInt();
		this.amount = this.number * this.price;

	}

	
	@Override
	public int compareTo(OrderBean o) {
		 
		return this.orderId.compareTo(o.getOrderId())==0
				?Float.compare(o.getAmount(), this.getAmount())
				:this.orderId.compareTo(o.getOrderId());
	}

	
	@Override
	public String toString() {
		return "OrderBean [orderId=" + orderId + ", userId=" + userId + ", productName=" + productName + ", price="
				+ price + ", number=" + number + ", amount=" + amount + "]";
	}

	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}

