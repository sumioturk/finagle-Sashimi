package infrastructure

import com.twitter.util.Future
import com.twitter.finagle.redis.Client

trait FutureRepository[T] {
  def resolve(id: String): Future[T]

  def resolveAll: Future[Seq[T]]

  def store(entity: T): Future[Unit]

  def purge(id: String): Future[_]

  def newRedisClient: Client

  def exists(entity: T): Future[Boolean]
}
