package Study.MapReduce.MapSideJoin;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public  class OrderDetailBean implements WritableComparable<OrderDetailBean> {

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(orderId);
		out.writeUTF(productName);
		out.writeFloat(price);
		out.writeInt(number);

	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.orderId = in.readUTF();
		this.productName = in.readUTF();
		this.price = in.readFloat();
		this.number = in.readInt();
		this.amount = this.number * this.price;

	}


	@Override
	public int compareTo(OrderDetailBean o) {

		return this.orderId.compareTo(o.getOrderId())==0
				?Float.compare(o.getAmount(), this.getAmount())
				:this.orderId.compareTo(o.getOrderId());
	}
	public OrderDetailBean() {
		this.orderId = "Null";
		this.productName = "Null";
		this.price = 0;
		this.number = 0;
		this.amount = 0;
	}

	public OrderDetailBean(String orderId, String productName, float price, int number) {
		set(orderId,productName,price,number);
	}

	public void set(String orderId, String productName, float price, int number) {
		this.orderId = orderId;
		this.productName = productName;
		this.price = price;
		this.number = number;
		this.amount = this.price * this.number;
	}

	private String orderId;
	private String productName;
	private float price;
	private int number;
	private float amount;


	@Override
	public String toString() {
		return "OrderDetailBean [orderId=" + orderId + ", productName=" + productName + ", price="
				+ price + ", number=" + number + ", amount=" + amount + "]";
	}

	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

