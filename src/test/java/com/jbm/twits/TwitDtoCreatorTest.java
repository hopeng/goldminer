package com.jbm.twits;

import com.jbm.hazelcast.sample.JsonUtils;
import com.jbm.hazelcast.util.HazelcastMapFactory;
import com.jbm.model.Twit;
import com.jbm.model.TwitDto;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.jbm.twits.TrendBias.Long;
import static com.jbm.twits.TrendBias.Neutral;
import static com.jbm.twits.TrendBias.Short;
import static org.junit.Assert.assertEquals;

/**
 * Created by hopeng on 18/09/2016.
 */
public class TwitDtoCreatorTest {


    @Test
    public void testBias() {

        TwitDtoCreator creator = new TwitDtoCreator();

        assertEquals(Long, creator.analyseBias("this is going up!"));
        assertEquals(Short, creator.analyseBias("it's a bearish day today!"));
        assertEquals(Neutral, creator.analyseBias("flat really"));
        assertEquals(Neutral, creator.analyseBias("no update today"));
        assertEquals(Neutral, creator.analyseBias("download my magic strategy"));
    }

    @Test
    public void testLoad() throws IOException {
        Path file = Files.createFile(Paths.get("gbp_dump.csv"));
        List<TwitDto> lines = new LinkedList<>();

        final TwitDtoCreator creator = new TwitDtoCreator();
        Map<String, Twit> twitsMap = HazelcastMapFactory.getMap("TWITS_GBPUSD");
        List<Twit> twits = new ArrayList<>(twitsMap.values());
        Collections.sort(twits, (o1, o2) -> o1.getId().compareTo(o2.getId())); // todo sort during getMap()?
        twits.forEach(twit -> lines.add(creator.createDto(twit)));

        Files.write(file, JsonUtils.toJson(lines).getBytes());
    }

}