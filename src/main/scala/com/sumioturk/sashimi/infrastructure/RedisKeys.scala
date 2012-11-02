package infrastructure

object RedisKeys {
  def Users = "Users".getBytes()

  def UserSessions = "UserSessions".getBytes()

  def UserAccessTokens = "UserAccessTokens".getBytes()

  def UserAccessTokenSecrets = "UserAccessTokenSecrets".getBytes()

  def Sashimis = "Sashimis".getBytes()

  def Last = "Last".getBytes()

  def UserRequestTokens = "UserRequestTokens".getBytes()

  def UserRequestTokenSecrets = "UserRequestTokenSecrets".getBytes()
}
