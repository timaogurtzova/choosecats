package com.cat.controller;

import com.cat.model.Cat;
import com.cat.sevice.interfaces.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class CatController {

    @Autowired
    private CatService catService;

    /* Возвращает страницу голосования.
    Если для данного пользователя закончились пары по типу "каждый с каждым", вернет топ-10
    * */
    @GetMapping()
    public String votePage(ModelMap model) {
        List<Cat> randomCat = catService.getRandomCat();
        if (randomCat.isEmpty()) {
            return "redirect:/top";
        }
        model.addAttribute("catFirst", randomCat.get(0));
        model.addAttribute("catLast", randomCat.get(1));
        return "main-page";
    }

    /*
        Когда пользователь голосует за кота, рейтинг кота должен быть изменен.
    */
    @GetMapping("/voteCat/{winner}/{notwinner}")
    public ResponseEntity<String> voteForCat(@PathVariable("winner") Long winner, @PathVariable("notwinner") Long notWinner) {
        catService.updateVoteCounter(winner, notWinner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/top")
    public String ratingPage(ModelMap model) {
        List<Cat> topTen = catService.findTopCats();
        model.addAttribute("topTen", topTen);
        return "rating";
    }

}
