package infrastructure

import domain.User
import com.twitter.util.Future
import com.sumioturk.sashimi.common.exception.EntityNotFoundException
import org.jboss.netty.buffer.ChannelBuffers._
import com.twitter.finagle.redis.TransactionalClient
import scala.Some

class UserFutureRepository(client: TransactionalClient) extends FutureRepository[User] {
  val redis = newRedisClient

  def resolve(id: String): Future[User] = {
    redis.get(
      copiedBuffer(RedisKeys.Users(id))
    ) flatMap {
      case Some(userCB) =>
        Future(User.fromJsonString(new String(userCB.array())))
      case None =>
        throw new EntityNotFoundException
    }
  }

  def resolveByName(name: String): Future[Seq[User]] = {
    resolveAll flatMap {
      users =>
        val user = users.filter(u => u.name == name)
        Future(user)
    }
  }

  def resolveAll: Future[Seq[User]] = {
    redis.keys(
      copiedBuffer(RedisKeys.Users("*"))
    ) flatMap {
      keys =>
        Future.collect(
          keys map (
            key =>
              redis.get(key) flatMap {
                userCB =>
                  Future(
                    User
                      .fromJsonString(
                      new String(userCB.getOrElse(throw new EntityNotFoundException).array())
                    )
                  )
              }
            )
        )
    }
  }

  def resolveAllActive: Future[Seq[User]] = {
    resolveAll flatMap {
      users =>
        Future(users.filter(_.isActive))
    }
  }

  def store(user: User): Future[Unit] = {
    redis.set(
      copiedBuffer(RedisKeys.Users(user.id)),
      copiedBuffer(user.toJsonString.getBytes)
    ) flatMap {
      _ =>
        Future.Unit
    }
  }

  def purge(id: String): Future[_] = {
    redis.del(
      copiedBuffer(RedisKeys.Users(id)) :: Nil
    )
  }

  def exists(user: User): Future[Boolean] = {
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
  def isUniqueName(name: String): Future[Boolean] = {
    resolveAll flatMap {
      users =>
        users.filter(u => u.name == name).length match {
          case 0 => Future(true)
          case _ => Future(false)
        }
    }
  }

  def update(id: String)(f: User => User): Future[User] = {
    redis.watch(
      copiedBuffer(RedisKeys.Users(id)) :: Nil
    ) flatMap {
      _ =>
        resolve(id) flatMap {
          readUser =>
            store(f(readUser)) flatMap {
              _ =>
                redis.unwatch() flatMap {
                  _: Unit =>
                    resolve(id) flatMap (Future(_))
                }
            }
        }
    }
  }

  def newRedisClient = client
}
