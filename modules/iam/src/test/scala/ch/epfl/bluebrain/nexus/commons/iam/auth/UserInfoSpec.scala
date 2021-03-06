package ch.epfl.bluebrain.nexus.commons.iam.auth

import ch.epfl.bluebrain.nexus.commons.iam.identity.Identity
import ch.epfl.bluebrain.nexus.commons.iam.identity.Identity.{AuthenticatedRef, GroupRef, UserRef}
import io.circe.syntax._
import org.scalatest._

class UserInfoSpec extends WordSpecLike with Matchers with Inspectors {

  private val json =
    s"""{"sub":"sub","name":"name","preferred_username":"preferredUsername","given_name":"givenName","family_name":"familyName","email":"email@example.com","groups":["group1","group2"]}"""

  private val model = UserInfo("sub",
                               "name",
                               "preferredUsername",
                               "givenName",
                               "familyName",
                               "email@example.com",
                               Set("group1", "group2"))

  "A UserInfo" should {

    "be encoded to Json properly" in {
      model.asJson.noSpaces shouldEqual json
    }

    "convert userinfo to user" in {
      val realm = "realm"
      model.toUser(realm).identities should contain allElementsOf AuthenticatedUser(
        Set[Identity](AuthenticatedRef(Some(realm))) ++ model.groups.map(GroupRef(realm, _)) + UserRef(realm, model.sub)
      ).identities
      model.toUser(realm) shouldBe a[AuthenticatedUser]
    }
  }
}
