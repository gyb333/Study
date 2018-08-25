package Study.RPC.HTTP;

public enum Encode {
	UTF8((byte)00),
	GBK((byte)1);
	
	 private byte value;
	 
	    private Encode(byte value) {
	        this.value = value;
	    }

	public byte getValue() {
		// TODO Auto-generated method stub
		
		return value;
	}
	 
}
