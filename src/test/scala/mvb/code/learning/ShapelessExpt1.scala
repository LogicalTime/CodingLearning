package mvb.code.learning

import mvb.code.learning.test.TestTags.{DbTest, SlowTest}
import mvb.code.learning.test.WordSpecMvB
import org.scalatest.WordSpec
import shapeless.{record, LabelledGeneric, HNil}


import shapeless.PolyDefns.~>
import shapeless.syntax.singleton._
import shapeless.syntax._
import shapeless.record._



/**
 * Testing out basic Shapeless functionality for 2.0.0
 * https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0
 * Created by Mark on 4/4/2015.
 */
class ShapelessExpt1 extends WordSpecMvB{
  object choose extends (Set ~> Option) {
    def apply[T](s : Set[T]) = s.headOption
  }


//Ordinary Scala function values are monomorphic. shapeless, however, provides an encoding of polymorphic function values.
  "Polymorphic function values" should {
    "support natural transformations, which are familiar from libraries like Scalaz" in {
      choose(Set(1, 2, 3)) should equal (Some(1))
      choose(Set('a', 'b', 'c')) should equal (Some('a'))
    }
    "be passed as arguments to functions or methods and then applied to values of different types within those functions" in {
      def pairApply(f: Set ~> Option) = (f(Set(1, 2, 3)), f(Set('a', 'b', 'c')))
      pairApply( choose) should equal ((Some(1),Some('a')))
    }
    "be interoperable with ordinary monomorphic function values" in{
      val x = List(Set(1, 3, 5), Set(2, 4, 6)) map choose
      x should equal (List(Some(1),Some(2)))

    }
    "be more general than natural transformations and are able to capture type-specific cases which makes them ideal for generic programming" in{
      import shapeless.Poly1
      object size extends Poly1 {
        implicit def caseInt = at[Int](x => 1)
        implicit def caseString = at[String](_.length)
        implicit def caseTuple[T, U]
        (implicit st : Case.Aux[T, Int], su : Case.Aux[U, Int]) =
          at[(T, U)](t => size(t._1)+size(t._2))
      }

      size(23) should equal (1)
      size("foo") should equal (3)
      size((23, "foo")) should equal (4)
      size(((23, "foo"), 13)) should equal (5)

    }


  }


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


  "Heterogenous lists" when given__{
    val sets = Set(1) :: Set("foo") :: HNil
    "a polymorphic function value" should provide {
      "a map operation, applying a polymorphic function value across its elements." in {
        val r = sets map choose
        println(r)
        r should equal (Some(1) :: Some("foo") :: HNil)
      }
    }
  }

  "Generic representation of sealed families of case classes" when given__{
    val bookGen = LabelledGeneric[Book]
    val tapl = Book("Benjamin Pierce", "Types and Programming Languages", 262162091, 44.11)
    val rec = bookGen.to(tapl) // Convert case class value to generic representation
    "should allow generating an extended class from another in a generic way" in{


      val bookExtGen = LabelledGeneric[ExtendedBook]
      val result = bookExtGen.from(rec + ('inPrint ->> true))
      println(s"result = $result")

      result.inPrint should equal (true)
    }

    "should allow generating another extended class from original in a generic way" in{
      println(s"rec = $rec")
      val bookExtGen2 = LabelledGeneric[ExtendedBook2]
      val p = rec('price)
      println( p)
      p should equal (44.11)
      val rec2 = rec.tail
      println(rec2)
      val result = bookExtGen2.from( rec.updateWith('author)(Peeps(_,"Mark")))
      println(result)
      result.author.name2 should equal ("Mark")
    }

  }

}

case class Book(author: String, title: String, id: Int, price: Double)
case class ExtendedBook(author: String, title: String, id: Int, price: Double, inPrint: Boolean)
case class ExtendedBook2(author: Peeps, title: String, id: Int, price: Double)
case class Peeps(name1:String, name2:String)