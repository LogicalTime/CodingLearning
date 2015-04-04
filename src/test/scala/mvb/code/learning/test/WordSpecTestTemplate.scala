package mvb.code.learning.test

import mvb.code.learning.test.TestTags.{SlowTest, DbTest}


class WordSpecTestTemplate extends WordSpecMvB{



  "The ScalaTest Matchers DSL" when {
    "In a certain state" should provide {
      "provide an and operator," which {
        "does something" taggedAs(SlowTest, DbTest) in {
          assert(2+3 === 5)

          info("simple addition seems to work")
        }

      }
    }
  }

}

