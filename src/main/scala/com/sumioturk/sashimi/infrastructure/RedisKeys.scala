package infrastructure

object RedisKeys {
  def Users(id: String) = ("Users-" + id).getBytes()

  def UserSessions = "UserSessions".getBytes()

  def UserAccessTokens = "UserAccessTokens".getBytes()

  def UserAccessTokenSecrets = "UserAccessTokenSecrets".getBytes()

  def Sashimis = "Sashimis".getBytes()

  def Last = "Last".getBytes()

}
