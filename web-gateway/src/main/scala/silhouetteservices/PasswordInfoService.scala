package silhouetteservices

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO

/**
 * Created by shubham.k on 28-02-2017.
 */
class PasswordInfoService() extends DelegableAuthInfoDAO[PasswordInfo] {
  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo) = ???

  override def find(loginInfo: LoginInfo) = ???

  override def remove(loginInfo: LoginInfo) = ???

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo) = ???

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo) = ???
}
