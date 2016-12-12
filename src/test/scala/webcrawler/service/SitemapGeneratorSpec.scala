package webcrawler.service

import java.net.URL

import org.htmlcleaner.{HtmlCleaner, TagNode}
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar
import service.{SitemapGenerator, Spider}

/**
  * Created by sam.elamin on 10/12/2016.
  */
class SitemapGeneratorSpec extends FeatureSpec with MockitoSugar {
  feature("Parsing Links from String") {
    scenario("Generate Sitemap from set of links") {
      val links = List("http://www.test.com","http://www.test.com/about","http://www.test.com/blog")
      val sitemap = SitemapGenerator.generateSiteMap(links)

      val expectedSitemap = """<?xml version="1.0" encoding="UTF-8"?><urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"><url><loc>http://www.test.com</loc></url><url><loc>http://www.test.com/about</loc></url><url><loc>http://www.test.com/blog</loc></url></urlset>"""
      sitemap should be (expectedSitemap)
    }
  }
  }

