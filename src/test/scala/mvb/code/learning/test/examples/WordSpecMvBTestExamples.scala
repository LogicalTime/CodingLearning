package mvb.code.learning.test.examples


import mvb.code.learning.test.TestTags._
import mvb.code.learning.test.WordSpecMvB

class WordSpecMvBTestExamples extends WordSpecMvB{




  "The ScalaTest Matchers DSL" when {

    "In a certain state" should provide {
      "provide an and operator," which {
        "returns silently when evaluating true and true" taggedAs(SlowTest, DbTest) in {
          val x = 2
          val y = 3
          When("they are added")
          val sum = x + y
         assert(sum === 5)

        }
        "allow tests to be pending" in {
          pending
        }

        "intercept an  exceptions" in {
            intercept[NoSuchElementException] {
              throw new NoSuchElementException
            }
          }
        }

      }
    }


  "Scala Mock" when {
    "given a trait" should provide {
      trait PlayerDatabase {
        def getPlayerById(playerId: Int): Player
        def givePlayerNickname(potentialNickname:String)
      }
      case class Player(id: Int, nickname: String)
      case class TestException(message:String) extends Exception(message:String)
      "stubbing" taggedAs(Stub, DbTest) in {
        // create fakeDb stub that implements PlayerDatabase trait
        val playerDbStub = stub[PlayerDatabase]

        // configure fakeDb behavior
        playerDbStub.getPlayerById _ when 222 returns Player(222, "boris")
        playerDbStub.getPlayerById _ when 333 returns Player(333, "hans")

        info(s" player = ${playerDbStub.getPlayerById(222)}")
        // use fakeDb
        assert(playerDbStub.getPlayerById(222).nickname == "boris")
      }

      "mocking" taggedAs(SlowTest, DbTest) in {
        // create fakeDb mock that implements PlayerDatabase trait
        val playerDbMock = mock[PlayerDatabase]
        playerDbMock.givePlayerNickname _ expects "Bilbo"
        playerDbMock.givePlayerNickname _ expects "Frodo" throws new IllegalArgumentException("The Ring!") repeated 3 times()

        playerDbMock.givePlayerNickname("Bilbo")
        an [IllegalArgumentException] should be thrownBy playerDbMock.givePlayerNickname("Frodo")

        the [IllegalArgumentException] thrownBy playerDbMock.givePlayerNickname("Frodo") should have message "The Ring!"

        intercept[IllegalArgumentException] {
          playerDbMock.givePlayerNickname("Frodo")
        }



      }
    }
  }



}
