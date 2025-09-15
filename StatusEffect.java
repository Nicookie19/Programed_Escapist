/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.programmer_escape;
import java.io.Serializable;

public class StatusEffect implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int duration;
    private double modifier;
    private String targetStat;
    private int damagePerTurn; // For effects like Burn

    public StatusEffect(String name, int duration, double modifier, String targetStat, int damagePerTurn) {
        this.name = name;
        this.duration = duration;
        this.modifier = modifier;
        this.targetStat = targetStat;
        this.damagePerTurn = damagePerTurn;
    }

    public void apply(Hero hero) {
        if (targetStat.equals("damage")) {
            hero.minDmg = (int)(hero.minDmg * modifier);
            hero.maxDmg = (int)(hero.maxDmg * modifier);
        } else if (targetStat.equals("speed")) {
            // Placeholder: Hero lacks speed field; implement if added
        }
        System.out.println(Color.colorize(hero.getClassName() + " is affected by " + name + "!", Color.YELLOW));
    }

    public void apply(Enemy enemy) {
        if (targetStat.equals("damage")) {
            enemy.minDmg = (int)(enemy.minDmg * modifier);
            enemy.maxDmg = (int)(enemy.maxDmg * modifier);
        } else if (targetStat.equals("speed")) {
            // Placeholder: Enemy lacks speed field; implement if added
        }
        System.out.println(Color.colorize(enemy.getDisplayName() + " is affected by " + name + "!", Color.YELLOW));
    }

    public void tick(Hero hero) {
        duration--;
        if (damagePerTurn > 0 && hero != null) {
            hero.hp = Math.max(0, hero.hp - damagePerTurn);
            System.out.println(Color.colorize(hero.getClassName() + " takes " + damagePerTurn + " damage from " + name + "!", Color.RED));
        }
    }

    public void tick(Enemy enemy) {
        duration--;
        if (damagePerTurn > 0 && enemy != null) {
            enemy.hp = Math.max(0, enemy.hp - damagePerTurn);
            System.out.println(Color.colorize(enemy.getDisplayName() + " takes " + damagePerTurn + " damage from " + name + "!", Color.RED));
        }
    }

    public boolean isActive() {
        return duration > 0;
    }

    public String getTargetStat() {
        return targetStat;
    }

    public String getName() {
        return name;
    }
}
