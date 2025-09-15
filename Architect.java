package com.mycompany.programmer_escape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Architect extends Hero {
    private int designCooldown = 0;
    private int rallyCooldown = 0;
    private int fortifyCooldown = 0;
    private boolean fortifyActive = false;
    private boolean unyieldingSpiritTriggered = false;

    public Architect() {
        super(new Random());
        this.maxHP = 320;
        this.hp = this.maxHP;
        this.minDmg = 25;
        this.maxDmg = 40;
        this.maxMana = 50;
        this.mana = this.maxMana;
        this.attackNames = new String[]{"Design", "Build", "Rally", "Demolish", "Fortify"};
    }

    @Override
    public String getClassName() {
        return "Architect";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Basic Exploit", "Advanced Exploit", "Zero-Day Exploit", "Kernel Exploit", "Buffer Overflow Exploit");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Firewall Vest", "Encryption Cloak", "Antivirus Shield");
    }

    @Override
    public void decrementCooldowns() {
        if (designCooldown > 0) designCooldown--;
        if (rallyCooldown > 0) rallyCooldown--;
        if (fortifyCooldown > 0) {
            fortifyCooldown--;
            if (fortifyCooldown == 0) {
                fortifyActive = false;
            }
        }
    }

    @Override
    public void applyPassiveEffects() {
        // Unyielding Spirit handled in receiveDamage
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        switch (skillIdx) {
            case 0: // Design
                if (designCooldown == 0 && mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * multiplier);
                    System.out.println("You design a flaw and deal " + damage + " damage to all enemies!");
                    enemy.receiveDamage(damage);
                    if (random.nextInt(100) < 20) {
                        System.out.println("Enemy stunned!");
                        enemy.stunnedForNextTurn = true;
                    }
                    mana -= 20;
                    designCooldown = 5;
                } else {
                    System.out.println("Design is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Build
                super.useSkill(1, enemy);
                break;
            case 2: // Rally
                if (rallyCooldown == 0 && mana >= 15) {
                    System.out.println("You rally the team, boosting your damage for the next attack!");
                    minDmg = (int)(minDmg * 1.5 * multiplier);
                    maxDmg = (int)(maxDmg * 1.5 * multiplier);
                    mana -= 15;
                    rallyCooldown = 4;
                } else {
                    System.out.println("Rally is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Demolish
                if (mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You demolish defenses and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                } else {
                    System.out.println("Insufficient mana for Demolish! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Fortify
                if (fortifyCooldown == 0 && mana >= 25) {
                    System.out.println("You fortify your position, absorbing all damage for one turn!");
                    fortifyActive = true;
                    mana -= 25;
                    fortifyCooldown = 5;
                } else {
                    System.out.println("Fortify is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            default:
                super.useSkill(1, enemy);
                break;
        }
    }

    @Override
    public void receiveDamage(int dmg) {
        if (fortifyActive) {
            System.out.println("Fortify absorbs all damage!");
            fortifyActive = false;
            return;
        }
        if (!unyieldingSpiritTriggered && hp - dmg <= 0) {
            hp = 1;
            unyieldingSpiritTriggered = true;
            System.out.println("Unyielding Spirit prevents your death, leaving you with 1 HP!");
        } else {
            super.receiveDamage(dmg);
        }
    }
}
