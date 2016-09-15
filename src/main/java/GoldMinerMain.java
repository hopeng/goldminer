import static java.util.Arrays.asList;

public class GoldMinerMain {

    public static void main(String args[]) {
//        TwitterStreamConnector audConnector = new TwitterStreamConnector()
//                .setContentFilters(asList("AUDUSD"))
//                .addListener(new CurrencyTwitListener())
//                .connect();

        TwitterStreamConnector gbpConnector = new TwitterStreamConnector()
                .setContentFilters(asList("GBPUSD"))
                .addListener(new CurrencyTwitListener())
                .connect();

//        TwitterStreamConnector eurConnector = new TwitterStreamConnector()
//                .setContentFilters(asList("EURUSD"))
//                .addListener(new CurrencyTwitListener())
//                .connect();

//        audConnector.waitForDone(Long.MAX_VALUE);
        gbpConnector.waitForDone(Long.MAX_VALUE);
//        eurConnector.waitForDone(Long.MAX_VALUE);
    }
}
