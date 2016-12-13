package webcrawler.service

import java.net.URL

import org.htmlcleaner.{HtmlCleaner, TagNode}
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import service.Spider
import org.mockito.Matchers.any
/**
  * Created by sam.elamin on 13/12/2016.
  */
class SpiderSpec extends FeatureSpec with BeforeAndAfterEach with MockitoSugar {
  val DOMAIN = "test.com"
  var SPIDER = new Spider(null)
  val CLEANERMOCK =  mock[HtmlCleaner]

  override def beforeEach() {
    val rootNode = mock[TagNode]
    val link1 = mock[TagNode]
    val link2 = mock[TagNode]
    val link3 = mock[TagNode]
    val link4 = mock[TagNode]
    val link5 = mock[TagNode]
    when(link1.getAttributeByName("href")).thenReturn("http://test.com/about")
    when(link2.getAttributeByName("href")).thenReturn("http://twitter.com/about")
    when(link3.getAttributeByName("href")).thenReturn("http://test.com/about#disqus_thread")
    when(link4.getAttributeByName("href")).thenReturn("http://test.com/about?query=timestamp")
    when(link5.getAttributeByName("href")).thenReturn("http://test.com/blog")
    val array = Array(link1, link2, link3, link4, link5)

    when(rootNode.getElementsByName("a", true)).thenReturn(array)

    when(CLEANERMOCK.clean(new URL(s"http://$DOMAIN"))).thenReturn(rootNode)
    when(CLEANERMOCK.clean(new URL(s"http://$DOMAIN/about"))).thenReturn(rootNode)
    when(CLEANERMOCK.clean(new URL(s"http://$DOMAIN/blog"))).thenReturn(rootNode)
    SPIDER = new Spider(CLEANERMOCK)
  }


  feature("Parsing links from a web page") {
    scenario("A page with 5 links but only 3 unique ones") {
      val uniqueLinks = SPIDER.crawlDomain(DOMAIN)
      uniqueLinks.length should be(3)
    }

    scenario("Spider should ignore scroll links and query parameters ") {
      val uniqueLinks = SPIDER.crawlDomain(DOMAIN)
      uniqueLinks should be (List("http://test.com", "http://test.com/about", "http://test.com/blog"))
    }

    scenario("Each page is visited only once") {
      SPIDER.crawlDomain(DOMAIN)
      verify(CLEANERMOCK.clean(new URL(s"http://$DOMAIN/blog")),times(1))
    }
  }
}

