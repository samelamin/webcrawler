package service

/**
  * Created by root on 12/12/16.
  */
object SitemapGenerator {

  def generateSiteMap(links: List[String]): String = {
    val sitemap = new StringBuilder("""<?xml version="1.0" encoding="UTF-8"?><urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">""")
    for(link <- links)
      {
        val element = s"<url><loc>$link</loc></url>"
        sitemap.append(element)
      }
    sitemap.append("</urlset>")
    sitemap.toString()
  }

}
