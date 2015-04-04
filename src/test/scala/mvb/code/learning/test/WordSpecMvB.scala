package mvb.code.learning.test

import org.scalamock.scalatest.MockFactory
import org.scalatest.{GivenWhenThen, Matchers, WordSpecLike}
/**
 * Base trait to extend whem making wordspec tests
 * Created by Mark on 4/4/2015.
 */
trait WordSpecMvB extends WordSpecLike with GivenWhenThen with Matchers with MockFactory {
      def provide = afterWord("provide")
      def given__ = afterWord("given")
}









