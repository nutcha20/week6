package com.example.week6;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

//import java.awt.*;


@Route(value = "index")
public class MainWizardView extends VerticalLayout {
    @Autowired
    private WizardService wizardService;
    private TextField name;
    private RadioButtonGroup<String> gender;
    private ComboBox<String> position;
    private TextField dollars;
    private ComboBox<String> school;
    private ComboBox<String> house;
    private Button pre;
    private Button create;
    private Button update;
    private Button delete;
    private Button next;

    private Integer number=0;
    private String nameold = "";
    public MainWizardView() {
        name = new TextField("Name");
        gender = new RadioButtonGroup<>("Gender");
        gender.setItems("Male", "Female");
        position = new ComboBox<>("Position");
        position.setItems("Teacher", "Student");
        dollars = new TextField("Dollars");
        school = new ComboBox<>("School");
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        house = new ComboBox<>("House");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slytherin");
        pre = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        next = new Button(">>");
        HorizontalLayout row = new HorizontalLayout();
        row.add(pre, create, update, delete, next);
        add(name, gender, position, dollars, school, house, row);

        pre.addClickListener(buttonClickEvent -> {
           List<Wizard> out = WebClient.create()
                   .get()
                   .uri("http://localhost:8080/wizards/")
                   .retrieve()
                   .bodyToMono(List.class)
                   .block();

            this.number -= 1;
           if(this.number < 0){
               this.number = 0;
           }
            ObjectMapper mapper = new ObjectMapper();
            Wizard wizard = mapper.convertValue(out.get(this.number), Wizard.class);
            this.nameold = wizard.getName();
            name.setValue(wizard.getName());
            if(wizard.getSex().equals("f")){
                gender.setValue("Female");
            }
            else{
                gender.setValue("Male");
            }
            position.setValue(wizard.getPosition());
            dollars.setValue(String.valueOf(wizard.getMoney()));
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());



        });
        next.addClickListener(buttonClickEvent -> {
            List<Wizard> out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/wizards/")
                    .retrieve()
                    .bodyToMono(List.class)
                    .block();

            this.number += 1;
            if(this.number > out.size()-1){
                this.number = out.size()-1;
            }
            ObjectMapper mapper = new ObjectMapper();
            Wizard wizard = mapper.convertValue(out.get(this.number), Wizard.class);
            this.nameold = wizard.getName();
            name.setValue(wizard.getName());
            if(wizard.getSex().equals("f")){
                gender.setValue("Female");
            }
            else{
                gender.setValue("Male");
            }
            position.setValue(wizard.getPosition());
            dollars.setValue(String.valueOf(wizard.getMoney()));
            school.setValue(wizard.getSchool());
            house.setValue(wizard.getHouse());



        });
        create.addClickListener(event ->{
            Wizard formData = new Wizard(null, gender.getValue(), name.getValue(), school.getValue(), house.getValue(), Integer.valueOf(dollars.getValue()), position.getValue());

            Wizard out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .body(Mono.just(formData), Wizard.class)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
            new Notification("Wizard has bee create", 5000).open();

        });
        update.addClickListener(buttonClickEvent -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("namenew", name.getValue());
            formData.add("nameold", this.nameold);
            formData.add("money", dollars.getValue());
            formData.add("sex", gender.getValue());
            formData.add("position", position.getValue());
            formData.add("school", school.getValue());
            formData.add("house", house.getValue());

            Boolean out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED) //?
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            new Notification("Wizard has bee update", 5000).open();
        });

        delete.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("name", name.getValue());
            formData.add("money", dollars.getValue());
            formData.add("sex", gender.getValue());
            formData.add("position", position.getValue());
            formData.add("school", school.getValue());
            formData.add("house", house.getValue());

            Boolean out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/delWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            new Notification("Wizard has bee Delete", 5000).open();
        });
    }
}
