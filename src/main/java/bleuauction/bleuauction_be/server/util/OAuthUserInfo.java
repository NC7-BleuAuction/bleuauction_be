package bleuauction.bleuauction_be.server.util;

public interface OAuthUserInfo {
  String getProviderId();
  String getEmail();
  String getName();
}