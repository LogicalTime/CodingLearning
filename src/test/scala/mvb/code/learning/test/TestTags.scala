package mvb.code.learning.test

import org.scalatest.Tag

object TestTags{
  object SlowTest extends Tag("com.mycompany.tags.SlowTest")
  object DbTest extends Tag("com.mycompany.tags.DbTest")
  object Stub extends Tag("Stubbing")
}
