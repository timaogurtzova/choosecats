package com.cat.util;

import com.cat.dao.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/*
Для каждой сессии создается отдельный экземпляр, который будет хранить ещё неиспользованные комбинации.
Комбинации хранятся в Map<Long, Set<Long>> combinationForSession
Пользователь не должен видеть повторения пар: как только комбинация будет использована, она удаляется из хранилища.
Комбинация считается использованной, только если пользователь проголосовал за один из вариантов
(например, если он перезагрузил страницу - комбинация не должна быть удалена)
Комбинация выбирается случайно
 */

@Component
@SessionScope
public class CombinationHolder {
    private CatRepository catRepository;
    private Map<Long, Set<Long>> combinationForSession;
    private RandomGenerator randomGenerator;

    @Autowired
    public CombinationHolder(CatRepository catRepository, RandomGenerator randomGenerator) {
        this.catRepository = catRepository;
        this.randomGenerator = randomGenerator;
    }

    //Отдает случайную пару
    public Long[] getNextRandomIds() {
        if (combinationForSession == null) {
            combinationForSession = createAllCombination();
        }

        if (combinationForSession.isEmpty()) { //Комбинации закончились
            return null;
        }
        Long randomKey = randomGenerator.getRandom(combinationForSession.keySet());
        Set<Long> valueForRandomKey = combinationForSession.get(randomKey);
        Long randomValue = randomGenerator.getRandom(valueForRandomKey);
        return new Long[]{randomKey, randomValue};
    }

    //Удаляет использованную пару
    public void removePair(Long[] pair) {
        Long keyMin = pair[0]; //Key всегда наименьшее
        Long valueMax = pair[1];
        for (Long num : pair) {
            if (num < keyMin) {
                keyMin = num;
            } else {
                valueMax = num;
            }
        }
        Set<Long> value = combinationForSession.get(keyMin);
        if (value.size() == 1) { //Если это была последняя комбинация, связанная с этим key, то удаляем пару
            combinationForSession.remove(keyMin);
        } else {
            combinationForSession.get(keyMin).remove(valueMax);
        }
    }

    /* Уникальные комбинации Cat по принципу "каждый с каждым".
    Комбинации были получены на основе id Cat.
    Ключом каждой из комбинаций является наименьшее значение id, а значением - оставшиеся id's.
    * */
    private Map<Long, Set<Long>> createAllCombination() {
        List<Long> ids = catRepository.getAllIds();
        ids.sort(Comparator.naturalOrder());
        combinationForSession = new HashMap<>();
        for (int index = 0; index < ids.size() - 1; index++) {
            Long key = ids.get(index);
            List<Long> values = new ArrayList<>(ids.subList(index + 1, ids.size()));
            combinationForSession.put(key, new HashSet<>(values));
        }
        return combinationForSession;
    }

    //Вернет количество возможных комбинаций "каждый с каждым"
    public static int getCountCombination(int size) {
        return getFactorial(size) / (getFactorial(2) * (getFactorial((size - 2))));
    }

    private static int getFactorial(int f) {
        if (f <= 1) {
            return 1;
        } else {
            return IntStream.rangeClosed(2, f).reduce((x, y) -> x * y).getAsInt();
        }
    }

}
