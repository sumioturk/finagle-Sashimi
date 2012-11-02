package infrastructure

import domain.User
import com.twitter.util.Future
import com.sumioturk.sashimi.common.exception.EntityNotFoundException
import org.jboss.netty.buffer.ChannelBuffers._
import com.twitter.finagle.redis.Client

class UserFutureRepository(client: Client) extends FutureRepository[User] {
  val redis = newRedisClient

  def resolve(id: String) = {
    redis.hGet(
      copiedBuffer(RedisKeys.Users),
      copiedBuffer(id.getBytes)
    ) flatMap {
      case Some(userCB) =>
        Future(User.fromJsonString(new String(userCB.array())))
      case None =>
        throw new EntityNotFoundException
    }
  }

  def resolveByName(name: String) = {
    resolveAll flatMap {
      users =>
        val user = users.filter(u => u.name == name)
        Future(user)
    }
  }

  def resolveAll = {
    redis.hGetAll(
      copiedBuffer(RedisKeys.Users)
    ) flatMap {
      seq =>
        Future(
          seq map {
            userCB =>
              User.fromJsonString(new String(userCB._2.array()))
          } toList
        )
    }
  }

  def resolveAllActive = {
      resolveAll flatMap {
        users =>
          Future(users.filter(u => u.isActive == 1))
      }
  }

  def store(user: User) = {
    redis.hSet(
      copiedBuffer(RedisKeys.Users),
      copiedBuffer(user.id.getBytes),
      copiedBuffer(user.toJsonString.getBytes)
    )
  }

  def purge(id: String) = {
    redis.hDel(
      copiedBuffer(RedisKeys.Users),
      Seq(copiedBuffer(id.getBytes))
    )
  }

  def exists(user: User) = {
    redis.hGet(
      copiedBuffer(RedisKeys.Users),
      copiedBuffer(user.id.getBytes)
    ) flatMap {
      case Some(sth) =>
        Future(true)
      case None =>
        Future(false)
    }
  }

  // Not a good idea for performance sake.
  def isUniqueName(name: String) = {
    resolveAll flatMap {
      users =>
        users.filter(u => u.name == name).length match {
          case 0 => Future(true)
          case _ => Future(false)
        }
    }
  }

  def newRedisClient = client
}
