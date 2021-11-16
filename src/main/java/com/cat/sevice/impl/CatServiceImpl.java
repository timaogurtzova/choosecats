package com.cat.sevice.impl;

import com.cat.dao.CatRepository;
import com.cat.model.Cat;
import com.cat.sevice.interfaces.CatService;
import com.cat.util.CombinationHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    private CatRepository catRepository;
    private CombinationHolder combinationHolder;

    @Autowired
    public CatServiceImpl(CatRepository catRepository,
                          CombinationHolder combinationHolder) {
        this.catRepository = catRepository;
        this.combinationHolder = combinationHolder;
    }

    @Lookup
    public CombinationHolder getCombinationHolderForThisSession() {
        return null;
    }

    @Override
    public List<Cat> findTopCats() {
        return catRepository.findTop10ByOrderByVoteCounterDesc();
    }

    @Transactional
    @Override
    public void updateVoteCounter(Long winner, Long notWinner) {
        Cat catInDB = catRepository.findById(winner)
                .orElseThrow(() -> new EntityNotFoundException());
        long vote = catInDB.getVoteCounter();
        catInDB.setVoteCounter(++vote);
        combinationHolder.removePair(new Long[]{winner, notWinner});
    }

    @Override
    public List<Cat> getRandomCat() {
        Long[] randomIds = combinationHolder.getNextRandomIds();
        List<Cat> cats = new ArrayList<>();
        if (randomIds != null) {
            Cat cat = catRepository.findById(randomIds[0]).get();
            Cat cat2 = catRepository.findById(randomIds[1]).get();
            cats.add(cat);
            cats.add(cat2);
        }
        return cats;
    }


}

