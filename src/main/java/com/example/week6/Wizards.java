package com.example.week6;

import com.example.week6.Wizard;

import java.util.ArrayList;
import java.util.List;

public class Wizards {
    private List<Wizard> model = new ArrayList<>();

    public Wizards(List<Wizard> model) {
        this.model = model;
    }

    public Wizards() {

    }

    public List<Wizard> getModel() {
        return model;
    }

    public void setModel(List<Wizard> model) {
        this.model = model;
    }
}