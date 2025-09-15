/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.programmer_escape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Quest> activeQuests = new ArrayList<>();
    private List<Quest> completedQuests = new ArrayList<>();

    public void addQuest(String name, String description, List<String> objectives, Map<String, Integer> rewards, String faction) {
        Quest quest = new Quest(name, description, objectives, rewards, faction);
        activeQuests.add(quest);
        System.out.println(Color.colorize("New quest: " + quest.getName(), Color.YELLOW));
    }

    public void updateQuest(String objective, Hero hero) {
        for (Quest quest : new ArrayList<>(activeQuests)) { // Avoid ConcurrentModificationException
            if (quest.getCurrentObjective().equals(objective)) {
                quest.progress();
                if (quest.isComplete()) {
                    applyRewards(quest, hero);
                    completedQuests.add(quest);
                    activeQuests.remove(quest);
                }
                break;
            }
        }
    }

    private void applyRewards(Quest quest, Hero hero) {
        quest.getRewards().forEach((reward, amount) -> {
            if (reward.equals("gold")) hero.addGold(amount);
            else if (reward.equals("xp")) hero.addXP(amount);
            else if (reward.equals("item")) hero.addItem("Quest Reward Item", 1.0f); // Customize item
            System.out.println(Color.colorize("Received " + amount + " " + reward + "!", Color.GREEN));
        });
    }

    public List<Quest> getActiveQuests() {
        return activeQuests;
    }

    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }
}
