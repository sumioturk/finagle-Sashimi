package infrastructure

import com.twitter.util.{Future, Time}

trait SortedFutureRepository[T] {
  def sRange(min: Time, max: Time, maxRetries: Int): Future[List[T]]

  def sAdd(entity: T): Future[_]

  def sRem(entity: T): Future[_]
}
