package com.ai.agent;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class StockAgent {
	public static void main(String[] args) {
	    try {
	        // 1. Brain setup (Groq/OpenAI Bridge)
	        OpenAiChatModel model = OpenAiChatModel.builder()
	                .apiKey("GROQ_API_KEY") // Replace with your actual Groq API key
	                .baseUrl("https://api.groq.com/openai/v1")
	                .modelName("llama-3.3-70b-versatile")
	                .build();

	        System.out.println("Starting Deep Market Scan...");

	        // 2. Fetch all headlines regarding results from Google News
	        String discoveries = MarketScanner.getMarketWideDiscovery();

	        if (discoveries.startsWith("Discovery Error")) {
	            System.out.println(discoveries);
	            return;
	        }

	        // 3. The Instruction for the AI
	        /*String prompt = "Analyze these LATEST news headlines from the Indian Stock Market:\n\n" +
	                discoveries + "\n\n" +
	                "FILTERING RULES:\n" +
	                "1. IGNORE headlines that say 'Board to consider results' or 'Meeting scheduled'. These are NOT buy signals.\n" +
	                "2. ONLY look for companies that have ALREADY announced results today with words like 'Jumps', 'Doubles', 'Surges', or 'Rises by X%'.\n" +
	                "3. If a company reported a profit jump, check if it's a 'Beat' (above expectations).\n" +
	                "4. RANK the top candidate based on the STRENGTH of the profit growth (e.g., 100% growth is better than 10%).\n\n" +
	                "If no headline shows a CLEAR and MASSIVE profit jump, reply with: 'NO EXPLOSIVE OPPORTUNITIES FOUND'.";
	        */
	        String prompt = "You are an Indian Alpha-Seeker Bot. Analyze these headlines:\n\n" + discoveries + "\n\n" +
	        	    "FOR EACH VALID PROFIT JUMP, CALCULATE 'UPPER CIRCUIT PROBABILITY' (0-100%):\n\n" +
	        	    
	        	    "SCORING RULES:\n" +
	        	    "1. MARKET CAP FACTOR:\n" +
	        	    "   - Small/Mid Cap (e.g., < 10,000 Cr): +30% probability. They move fast.\n" +
	        	    "   - Large Cap (e.g., Nifty 50): +5% probability. They move slow.\n\n" +
	        	    
	        	    "2. SECTOR HOTNESS (2026 Sentiment):\n" +
	        	    "   - High Heat: Defense, Railways, Renewable Energy, AI/Tech, Jewellers. (+25%)\n" +
	        	    "   - Moderate: Banking, FMCG, Pharma. (+10%)\n\n" +
	        	    
	        	    "3. PROFIT STRENGTH:\n" +
	        	    "   - Profit Doubled (100%+): +40%\n" +
	        	    "   - Profit Up 20-50%: +15%\n\n" +

	        	    "4. PENALTY:\n" +
	        	    "   - If the headline says 'Revenue Down' despite Profit Up: -20% (Likely cost-cutting, not growth).\n\n" +

	        	    "OUTPUT FORMAT:\n" +
	        	    "--- POTENTIAL CIRCUIT ALERT ---\n" +
	        	    "Company: [Name]\n" +
	        	    "News: [Brief summary]\n" +
	        	    "Sector: [Sector Name]\n" +
	        	    "UC Probability: [X%]\n" +
	        	    "Strategy: [e.g., Buy in Pre-Open, Sell at 3:15 PM next day]\n" +
	        	    "Risk Level: [Low/Med/High]";
	        String analysis = model.generate(prompt);

	        System.out.println("\n--- DYNAMIC DISCOVERY REPORT ---");
	        System.out.println(analysis);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}

