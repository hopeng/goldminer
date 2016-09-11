import java.util.List;
import java.util.Map;

/**
 * Created by hopeng on 11/09/2016.
 */
public class CurrencyTwitListener implements TwitListener {

//    todo persisted collection, hazelcast?
    List<String> buySignals;
    List<String> sellSignals;

    public void onTwit(Map<String, String> message) {
        String text = message.get("text");
        String createdAt = message.get("created_at");
//                        System.out.println(msg);
        System.out.println(String.format("%s: %s", createdAt, text.replaceAll("\n", "\t")));

    }
}
