package nullguo.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Response implements Serializable {
private static final long serialVersionUID = 1L;
public int type;//1为响应P2P列表,2为响应消息成功
public Set<String> addressList;
public Response() {
	addressList=new HashSet<String>();
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public Set<String> getAddressList() {
	return addressList;
}
public void setAddressList(Set<String> addressList) {
	this.addressList = addressList;
}
public void addAddress(String address) {
	addressList.add(address);
}
}
