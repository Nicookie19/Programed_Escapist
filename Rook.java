package com.mycompany.programmer_escape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Rook extends Hero {
    private int earthCooldown = 0;
    private int warCryCooldown = 0;
    private int stoneWallCooldown = 0;
    private boolean stoneWallActive = false;
    private boolean unyieldingSpiritTriggered = false;

    public Rook() {
        super(new Random());
        this.maxHP = 320;
        this.hp = this.maxHP;
        this.minDmg = 25;
        this.maxDmg = 40;
        this.maxMana = 50;
        this.mana = this.maxMana;
        this.attackNames = new String[]{"Earthquake", "Slam", "War Cry", "Pulverize", "Stone Wall"};
    }

    @Override
    public String getClassName() {
        return "Rook";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Warhammer", "Battleaxe", "Mace", "Flail");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Chainmail", "Plate Armor");
    }

    @Override
    public void decrementCooldowns() {
        if (earthCooldown > 0) earthCooldown--;
        if (warCryCooldown > 0) warCryCooldown--;
        if (stoneWallCooldown > 0) {
            stoneWallCooldown--;
            if (stoneWallCooldown == 0) {
                stoneWallActive = false;
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
            case 0: // Earthquake
                if (earthCooldown == 0 && mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * multiplier);
                    System.out.println("You use Earthquake and deal " + damage + " damage to all enemies!");
                    enemy.receiveDamage(damage);
                    if (random.nextInt(100) < 20) {
                        System.out.println("Enemy stunned!");
                        enemy.stunnedForNextTurn = true;
                    }
                    mana -= 20;
                    earthCooldown = 5;
                } else {
                    System.out.println("Earthquake is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Slam
                super.useSkill(1, enemy);
                break;
            case 2: // War Cry
                if (warCryCooldown == 0 && mana >= 15) {
                    System.out.println("You use War Cry, boosting your damage for the next attack!");
                    minDmg = (int)(minDmg * 1.5 * multiplier);
                    maxDmg = (int)(maxDmg * 1.5 * multiplier);
                    mana -= 15;
                    warCryCooldown = 4;
                } else {
                    System.out.println("War Cry is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Pulverize
                if (mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You use Pulverize and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                } else {
                    System.out.println("Insufficient mana for Pulverize! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Stone Wall
                if (stoneWallCooldown == 0 && mana >= 25) {
                    System.out.println("You use Stone Wall, absorbing all damage for one turn!");
                    stoneWallActive = true;
                    mana -= 25;
                    stoneWallCooldown = 5;
                } else {
                    System.out.println("Stone Wall is on cooldown or insufficient mana! Using normal attack.");
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
        if (stoneWallActive) {
            System.out.println("Stone Wall absorbs all damage!");
            stoneWallActive = false;
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
