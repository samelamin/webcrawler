package webcrawler.service

import java.net.URL

import org.htmlcleaner.{HtmlCleaner, TagNode}
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import service.Spider
/**
  * Created by sam.elamin on 10/12/2016.
  */
class SpiderSpec extends FeatureSpec with MockitoSugar {
  feature("Parsing Links from String") {
    scenario("An HTML Page with 5 links") {
      val domain = "test.com"
      val cleanerMock = mock[HtmlCleaner]
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


      val array = Array(link1,link2,link3,link4,link5)

      when(rootNode.getElementsByName("a", true)).thenReturn(array)

      when(cleanerMock.clean(new URL(s"http://$domain"))).thenReturn(rootNode)
      when(cleanerMock.clean(new URL(s"http://$domain/about"))).thenReturn(rootNode)
      when(cleanerMock.clean(new URL(s"http://$domain/blog"))).thenReturn(rootNode)

      val spider = new Spider(cleanerMock)
      val uniqueLinks = spider.crawlDomain(domain)
      uniqueLinks.length should be (2)
    }
  }
  }

