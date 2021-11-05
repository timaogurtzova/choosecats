package com.cat.sevice.interfaces;

import com.cat.model.Cat;

import java.util.List;

public interface CatService {

    List<Cat> findTopCats();

    void updateVoteCounter(Long winner, Long notWinner);

    List<Cat> getRandomCat();

}
