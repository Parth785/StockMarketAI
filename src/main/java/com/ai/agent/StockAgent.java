package com.ai.agent;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.jsoup.Jsoup;

public class StockAgent {

    // Telegram hard-safe limit (keep buffer)
    private static final int TELEGRAM_LIMIT = 3500;

    public static void main(String[] args) {

        try {
            String groqKey = System.getenv("GROQ_API_KEY");
            String telegramToken = System.getenv("TELEGRAM_TOKEN");
            String telegramChatId = System.getenv("TELEGRAM_CHAT_ID");

            if (groqKey == null || telegramToken == null || telegramChatId == null) {
                throw new RuntimeException("Missing environment variables");
            }

            OpenAiChatModel model = OpenAiChatModel.builder()
                    .apiKey(groqKey)
                    .baseUrl("https://api.groq.com/openai/v1")
                    .modelName("llama-3.1-8b-instant")
                    .temperature(0.1)
                    .build();

            System.out.println("🚀 Morning Market Scan Started...");

            String newsData = MarketScanner.getMarketWideDiscovery();

            if (newsData.startsWith("Error") || newsData.equals("NO_VALID_HEADLINES")) {
                sendTelegram("☕ <b>Morning Update</b>\nNo valid earnings news found today.");
                return;
            }

            String prompt =
                    "You are a professional Indian equity research analyst.\n\n" +

                    "TASK:\n" +
                    "Analyze the following headlines and find companies with MASSIVE profit growth (50%+).\n\n" +
                    "RULES:\n" +
                    "- Ignore board meetings or future announcements\n" +
                    "- Only ACTUAL reported results\n" +
                    "FORMAT STRICTLY LIKE THIS:\n\n" +
                    "<b>🚀 COMPANY NAME</b>\n" +
                    "📈 News: Short 1-line summary\n" +
                    "💰 Profit Growth: X%\n" +
                    "🎯 UC Probability: X%\n" +
                    "<a href=\"https://www.google.com/finance/quote/SYMBOL:NSE\">View Chart</a>\n\n" +

                    "RULES:\n" +
                    "- Use ONLY <b> and <a> HTML tags\n" +
                    "- Keep output concise and professional\n" +
                    "- Max 5 stocks\n" +
                    "- If nothing found, reply exactly: NO_NEWS\n\n" +

                    "HEADLINES:\n" + newsData;

            String analysis = model.generate(prompt).trim();

            if (analysis.equalsIgnoreCase("NO_NEWS")) {
                sendTelegram("☕ <b>Morning Update</b>\nNo explosive profit results found today.");
            } else {
                sendTelegramInChunks(analysis);
            }

        } catch (Exception e) {
            sendTelegram("❌ <b>Bot Error</b>\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends long messages safely by splitting on stock blocks
     */
    private static void sendTelegramInChunks(String message) {

        // Split by blank line between stocks
        String[] blocks = message.split("\n\n");
        StringBuilder currentMessage = new StringBuilder();

        for (String block : blocks) {

            // If adding this block exceeds Telegram limit → send current chunk
            if (currentMessage.length() + block.length() > TELEGRAM_LIMIT) {
                sendTelegram(currentMessage.toString());
                currentMessage.setLength(0);
            }

            currentMessage.append(block).append("\n\n");
        }

        // Send remaining content
        if (currentMessage.length() > 0) {
            sendTelegram(currentMessage.toString());
        }
    }

    /**
     * Sends a single Telegram message (HTML-safe, no escaping)
     */
    private static void sendTelegram(String message) {
        try {
            String token = System.getenv("TELEGRAM_TOKEN");
            String chatId = System.getenv("TELEGRAM_CHAT_ID");

            String url = "https://api.telegram.org/bot" + token + "/sendMessage";

            Jsoup.connect(url)
                    .ignoreContentType(true)
                    .data("chat_id", chatId)
                    .data("text", message)
                    .data("parse_mode", "HTML")
                    .method(org.jsoup.Connection.Method.POST)
                    .execute();

            System.out.println("✅ Telegram alert sent");

        } catch (Exception e) {
            System.out.println("❌ Telegram send failed: " + e.getMessage());
        }
    }
}
