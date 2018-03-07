package edu.sjsu.prashant.whatsfordinner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Prashant on 3/4/2018.
 *
 */

public class NewDish implements Serializable {


    private String dish_name;
    private String dish_description;
    private String dish_image_path;
    private List<String> ingredient_list = new ArrayList<>();

    public NewDish(){
        this.dish_name = "";
        this.dish_description = "";
        this.dish_image_path = "";
        this.ingredient_list = new ArrayList<>();
    }
    public NewDish(String dish_name, String dish_description, String dish_image_path, List<String> ingredient_list ){
     this.dish_name = dish_name;
     this.dish_description = dish_description;
     this.dish_image_path = dish_image_path;
     this.ingredient_list = ingredient_list;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }


    public String getDish_description() {
        return dish_description;
    }

    public void setDish_description(String dish_description) {
        this.dish_description = dish_description;
    }

    public String getDish_image_path() {
        return dish_image_path;
    }

    public void setDish_image_path(String dish_image_path) {
        this.dish_image_path = dish_image_path;
    }

    public List<String> getIngredient_list() {
        return ingredient_list;
    }

    public void setIngredient_list(List<String> ingredient_list) {
        this.ingredient_list = ingredient_list;
    }

}
