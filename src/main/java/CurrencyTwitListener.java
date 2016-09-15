import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by hopeng on 11/09/2016.
 */
public class CurrencyTwitListener implements TwitListener {
    private static final Logger log = LoggerFactory.getLogger(CurrencyTwitListener.class);

    private static enum Bias {Long, Short};

//    todo persisted collection, hazelcast?
    List<String> longTwits;
    List<String> shortTwits;

    public void onTwit(Map<String, Object> message) {
        String text = (String) message.get("text");
        String createdAt = (String) message.get("created_at");
//                        System.out.println(msg);
        log.info("{}: {}", createdAt, text.replaceAll("\n", "\t"));

        Bias bias = analyseBias(text);

    }

    // todo LingPipe
    private Bias analyseBias(String text) {
        return null;
    }
}
