import service.{SitemapGenerator, Spider}
import org.htmlcleaner.HtmlCleaner

/**
  * Created by sam.elamin on 13/12/2016.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val spider = new Spider(new HtmlCleaner())
    val domain = "tomblomfield.com"
    val uniqueLinks = spider.crawlDomain(domain)
    Console.print(SitemapGenerator.generateSiteMap(uniqueLinks))
  }
}
