package Study.SpringCloud.GYB.result;

public enum CodeMessageEnum implements CodeMessage {
    SUCCESS(0, "success"),
    SERVER_ERROR(500100, "服务端异常"),
    BIND_ERROR(500101, "参数校验异常：%s"),
    REQUEST_ILLEGAL(500102, "请求非法"),
    ACCESS_LIMIT_REACHED(500104, "访问太频繁！"),

    SESSION_ERROR(500210, "Session不存在或者已经失效"),
    PASSWORD_EMPTY(500211, "登录密码不能为空"),
    MOBILE_EMPTY(500212, "手机号不能为空"),
    MOBILE_ERROR(500213, "手机号格式错误"),
    MOBILE_NOT_EXIST(500214, "手机号不存在"),
    PASSWORD_ERROR(500215, "密码错误"),

    ORDER_NOT_EXIST(500400, "订单不存在"),
    MIAO_SHA_OVER(500500, "商品已经秒杀完毕"),
    REPEATE_MIAOSHA(500501, "不能重复秒杀"),
    MIAOSHA_FAIL(500502, "秒杀失败");


    private final int code;
    private final String message;

    private CodeMessageEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", message=" + message + "]";
    }


//    public CodeMsg fillArgs(Object... args) {
//        int code = this.code;
//        String message = String.format(this.msg, args);
//        return new CodeMsg(code, message);
//    }


}
