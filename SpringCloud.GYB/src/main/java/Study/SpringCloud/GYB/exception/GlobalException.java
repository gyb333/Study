package Study.SpringCloud.GYB.exception;


import Study.SpringCloud.GYB.result.CodeMessageEnum;


public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private CodeMessageEnum cm;
	
	public GlobalException(CodeMessageEnum cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMessageEnum getCm() {
		return cm;
	}

}
