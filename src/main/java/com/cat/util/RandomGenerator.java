package com.cat.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomGenerator{

    public Long getRandom(Set<Long> set) {
        int randomInt = ThreadLocalRandom.current().nextInt(0, set.size());
        Long[] arrayKeys = set.toArray(new Long[set.size()]);
        return arrayKeys[randomInt];
    }

}
