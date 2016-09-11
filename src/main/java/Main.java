import static java.util.Arrays.asList;

public class Main {

    public static void main(String args[]) {
        TwitterStreamConnector twitterStreamConnector = new TwitterStreamConnector()
                .setContentFilters(asList("AUDUSD", "EURUSD", "GBPUSD"))
                .addListener(new CurrencyTwitListener());

        twitterStreamConnector.connect();
        twitterStreamConnector.waitForDone(Long.MAX_VALUE);
    }
}
