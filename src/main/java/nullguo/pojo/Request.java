package nullguo.pojo;

import java.io.Serializable;

public class Request implements Serializable {
private static final long serialVersionUID = 1L;
public int type;//1为请求P2P列表,2为发送消息
public String address;
public String data;
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}

}
