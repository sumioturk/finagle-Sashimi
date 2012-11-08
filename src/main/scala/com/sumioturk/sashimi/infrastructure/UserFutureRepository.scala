package infrastructure

import domain.User
import com.twitter.util.Future
import com.sumioturk.sashimi.common.exception.EntityNotFoundException
import org.jboss.netty.buffer.ChannelBuffers._
import com.twitter.finagle.redis.{TransactionalClient, Client}
import com.twitter.finagle.redis.protocol._
import scala.Some

class UserFutureRepository(client: TransactionalClient) extends FutureRepository[User] {
  val redis = newRedisClient

  def resolve(id: String) = {
    redis.get(
      copiedBuffer(RedisKeys.Users(id))
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
    redis.keys(
      copiedBuffer(RedisKeys.Users("*"))
    ) flatMap {
      seq =>
        Future(
          seq map {
            userCB =>
              val l = new String(userCB.array())
              User.fromJsonString(new String(userCB.array()))
          } toList
        )
    }
  }

  def resolveAllActive = {
    resolveAll flatMap {
      users =>
        Future(users.filter(_.isActive))
    }
  }

  def store(user: User) = {
    redis.set(
      copiedBuffer(RedisKeys.Users(user.id)),
      copiedBuffer(user.toJsonString.getBytes)
    ) flatMap {
      _ =>
        Future.None
    }
  }

  def purge(id: String) = {
    redis.del(
      copiedBuffer(RedisKeys.Users(id)) :: Nil
    )
  }

  def exists(user: User) = {
    redis.get(
      copiedBuffer(RedisKeys.Users(user.id))
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
