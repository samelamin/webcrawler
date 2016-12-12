package service

import java.net.URL

import org.htmlcleaner.{HtmlCleaner, TagNode}

import scala.collection.mutable.ListBuffer

/**
  * Created by sam.elamin on 10/12/2016.
  */
class Spider (cleaner: HtmlCleaner) {


  def crawlDomain(domain: String): List[String] = {
    val uniqueLinks = getUniqueLinks(domain)
    uniqueLinks
  }

  private def getUniqueLinks(domain: String) : List[String] = {
    var uniqueLinks = new ListBuffer[String]
    val foundLinks = getDomainLinks(domain, domain, uniqueLinks)
    for (link <- foundLinks) {
      val pageLinks = getDomainLinks(domain, link, uniqueLinks)
    }
    uniqueLinks.toList
  }

  private def getDomainLinks(domain: String, url: String, foundLinks: ListBuffer[String]): ListBuffer[String] = {

    val props = cleaner.getProperties
    val absoluteURL = getAbsoluteURLPath(domain, url)

    val rootNode: TagNode = cleaner.clean(new URL(absoluteURL))
    val elements = rootNode.getElementsByName("a", true)
    for (elem <- elements) {
      val link = cleanURL(getAbsoluteURLPath(domain, elem.getAttributeByName("href")))
      if(isDomainLink(domain, link)  && !foundLinks.contains(link)) {
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
