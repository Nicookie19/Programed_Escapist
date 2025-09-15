/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.programmer_escape;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Quest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private List<String> objectives;
    private int currentObjective;
    private Map<String, Integer> rewards;
    private String faction;
    private boolean isComplete;

    public Quest(String name, String description, List<String> objectives, Map<String, Integer> rewards, String faction) {
        this.name = name;
        this.description = description;
        this.objectives = objectives;
        this.rewards = rewards;
        this.faction = faction;
        this.currentObjective = 0;
        this.isComplete = false;
    }

    public void progress() {
        if (currentObjective < objectives.size() - 1) {
            currentObjective++;
            System.out.println(Color.colorize("Quest updated: " + objectives.get(currentObjective), Color.YELLOW));
        } else {
            complete();
        }
    }

    private void complete() {
        isComplete = true;
        System.out.println(Color.colorize("Quest completed: " + name, Color.GREEN));
    }

    public String getName() {
        return name;
    }

    public String getCurrentObjective() {
        return objectives.get(currentObjective);
    }

    public Map<String, Integer> getRewards() {
        return rewards;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getDescription() {
        return description;
    }
}
