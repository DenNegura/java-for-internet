package com.example.restaurant.controller;

import com.example.restaurant.model.DAO.DishDAO;
import com.example.restaurant.model.DAO.IngredientDAO;
import com.example.restaurant.model.entity.Dish;
import com.example.restaurant.model.entity.Ingredient;
import com.example.restaurant.search.DishSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/")
public class DishController {
    @Autowired
    DishDAO dishDAO;

    @Autowired
    IngredientDAO ingredientDAO;

    @GetMapping
    public String main(Model model) {
        List<Dish> dishes = dishDAO.getAll();
        model.addAttribute("dishes", dishes);
        model.addAttribute("dishSearch", new DishSearch());
        model.addAttribute("dish", new Dish());
        model.addAttribute("ingredient", new Ingredient());
//        model.addAttribute("idDish", "1");
        return "main";
    }
    @PostMapping("/search-dish")
    public String search(Model model, @ModelAttribute("dishSearch") DishSearch dishSearch) {
        System.out.println(dishSearch);
        List<Dish> dishes = dishDAO.getSearch(dishSearch);
        model.addAttribute("dishes", dishes);
        return "main";
    }
    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getName());
        return "redirect:/";
    }
//    @PostMapping("/get-details")
//    public String getDetails(Model model, @RequestParam("idDish") long idDish) {
//        System.out.println(idDish);
//        Dish dish = dishDAO.get(idDish);
//        System.out.println(dish.getIngredients());
//        model.addAttribute("ingredients", dish.getIngredients());
//        return "main";
//    }
}
