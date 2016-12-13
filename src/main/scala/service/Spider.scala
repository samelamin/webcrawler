package service

import java.net.URL
import org.htmlcleaner.{HtmlCleaner, TagNode}
import scala.collection.mutable.ListBuffer

/**
  * Created by sam.elamin on 13/12/2016.
  */
class Spider (cleaner: HtmlCleaner) {
  def crawlDomain(domain: String): List[String] = {
    getUniqueLinks(domain)
  }

  private def getUniqueLinks(domain: String) : List[String] = {
    val visitedPages = new ListBuffer[String]
    val absoluteUrl = getAbsoluteURLPath(domain, domain)
    var foundLinks = getDomainLinks(domain, absoluteUrl)
    visitedPages.append(absoluteUrl)
      while(visitedPages.length < foundLinks.length) {
        for (link <- foundLinks) {
          if(!visitedPages.contains(link)) {
            foundLinks = (foundLinks ++ getDomainLinks(domain, link)).distinct
            visitedPages += link
          }
        }
      }
    visitedPages.toList
  }

  private def getDomainLinks(domain: String, url: String): ListBuffer[String] = {
    val foundLinks = new ListBuffer[String]
    val absoluteURL = getAbsoluteURLPath(domain, url)
    val rootNode: TagNode = cleaner.clean(new URL(absoluteURL))
    val elements = rootNode.getElementsByName("a", true)
    for (elem <- elements) {
      val link = cleanURL(getAbsoluteURLPath(domain, elem.getAttributeByName("href")))
      if(isDomainLink(domain, link)) {
        foundLinks += link
      }
    }
    foundLinks
  }

  private def isDomainLink(domain: String, url: String) : Boolean = {
    url.startsWith(s"http://$domain") || url.startsWith(s"https://$domain")
  }
  private def getAbsoluteURLPath(domain: String, url: String): String ={
    if(url.startsWith("/")){
      s"http://$domain$url"
    } else if(!url.startsWith("http") ){
      s"http://$url"
    }else  {
      url
    }
  }
  private def cleanURL(url: String): String ={
    var cleanedURL = url.replaceAll("#disqus_thread","")
    cleanedURL.split('?')(0)
  }
}
