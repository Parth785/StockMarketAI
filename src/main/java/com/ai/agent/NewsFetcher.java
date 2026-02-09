package com.ai.agent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsFetcher {
    public static String getLatestNews(String stockName) {
        StringBuilder newsFeed = new StringBuilder();
        try {
            // 1. Construct the Search URL for last 24 hours
            String url = "https://news.google.com/rss/search?q=" + stockName.replace(" ", "+") + "+when:24h";
            
            // 2. Fetch the XML (RSS)
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select("item");

            for (Element item : items) {
                String title = item.select("title").text();
                String date = item.select("pubDate").text();
                newsFeed.append("- ").append(title).append(" (").append(date).append(")\n");
            }
        } catch (Exception e) {
            return "Error fetching news: " + e.getMessage();
        }
        return newsFeed.length() > 0 ? newsFeed.toString() : "No news found in last 24 hours.";
    }
    
    public static String scanMarketResults() {
        // This query looks for general result news across the whole Indian market
        String query = "quarterly+results+NSE+OR+BSE+when:24h";
        return getLatestNews(query); 
    }

}