package com.example.week6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public List<Wizard> getWizards(){
        List<Wizard> wizards = wizardService.retrieveWizard();
        return wizards;
    }
    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public ResponseEntity<?> createWizard(@RequestBody Wizard wizard){
        return ResponseEntity.ok(wizardService.createWizard(wizard));
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public boolean updateWizard(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        Wizard wizard = wizardService.retrieveWizardByName(d.get("nameold"));
        String id = wizard.get_id();
        String sex = d.get("sex");
        String name = d.get("namenew");
        String school = d.get("school");
        String house = d.get("house");
        Integer money = Integer.parseInt(d.get("money"));
        String position = d.get("position");
        if(wizard != null){
            wizardService.updateWizard(new Wizard(id, sex, name, school, house, money, position));
            return true;
        }else {
            return false;
        }

    }
    @RequestMapping(value = "/delWizard", method = RequestMethod.POST)
    public Boolean delWizard(@RequestBody MultiValueMap<String, String> n){
        Map<String, String> d = n.toSingleValueMap();
        Wizard wizard = wizardService.retrieveWizardByName(d.get("name"));

        boolean status = wizardService.deleteWizard(wizard);
        return status;
    }

}
