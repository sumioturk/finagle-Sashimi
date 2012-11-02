package infrastructure

import com.twitter.util.Future
import com.twitter.finagle.redis.Client

trait FutureRepository[T] {
  def resolve(id: String): Future[T]

  def resolveAll: Future[List[T]]

  def store(entity: T): Future[_]

  def purge(id: String): Future[_]

  def newRedisClient: Client

  def exists(entity: T): Future[Boolean]
}
