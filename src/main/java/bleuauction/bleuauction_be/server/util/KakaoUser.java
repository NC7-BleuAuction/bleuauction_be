package bleuauction.bleuauction_be.server.util;


import java.util.Map;

public class KakaoUser implements OAuthUserInfo{

  private Map<String, Object> attribute;

  public KakaoUser(Map<String, Object> attribute) {
    this.attribute = attribute;
  }

  @Override
  public String getProviderId() {
    return (String)attribute.get("userToken");
  }

  @Override
  public String getEmail() {
    return (String)attribute.get("email");
  }

  @Override
  public String getName() {
    return (String)attribute.get("fullName");
  }

}