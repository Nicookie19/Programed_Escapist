package com.mycompany.programmer_escape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mage extends Hero {
    private int fireballCooldown = 0;
    private int iceStormCooldown = 0;
    private int arcaneBarrageCooldown = 0;

    public Mage() {
        super(new Random());
        this.maxHP = 180;
        this.hp = 100;
        this.minDmg = 10;
        this.maxDmg = 25;
        this.maxMana = 120;
        this.mana = 120;
        this.attackNames = new String[]{"Fireball", "Ice Bolt", "Heal", "Ice Storm", "Arcane Barrage"};
    }

    @Override
    public String getClassName() {
        return "Mage";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storms", "Staff of Healing", "Wand of Lightning", "Orb of Elements");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Robe of Protection", "Cloak of Shadows");
    }

    @Override
    public void decrementCooldowns() {
        if (fireballCooldown > 0) fireballCooldown--;
        if (iceStormCooldown > 0) iceStormCooldown--;
        if (arcaneBarrageCooldown > 0) arcaneBarrageCooldown--;
    }

    @Override
    public void applyPassiveEffects() {
        // Existing passive: Mana regeneration
        int manaRegen = (int)(maxMana * 0.1);
        mana = Math.min(mana + manaRegen, maxMana);
        System.out.println("Mage regenerates " + manaRegen + " mana.");

        // New passive: Mana Surge
        if (mana < maxMana * 0.2) {
            int surgeMana = (int)(maxMana * 0.1);
            mana = Math.min(mana + surgeMana, maxMana);
            System.out.println("Mana Surge activates, restoring " + surgeMana + " mana!");
        }
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        switch (skillIdx) {
            case 0: // Fireball
                if (fireballCooldown == 0 && mana >= 25) {
                    int baseDamage = minDmg + random.nextInt(maxDmg);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You cast Fireball and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 25;
                    fireballCooldown = 5;
                } else {
                    System.out.println("Fireball is on cooldown or insufficient mana! Casting normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Ice Bolt
                super.useSkill(1, enemy);
                break;
            case 2: // Heal
                if (mana >= 20) {
                    int heal = (int)(maxHP * 0.3 * multiplier);
                    hp = Math.min(hp + heal, maxHP);
                    System.out.println("You cast Heal and restore " + heal + " HP!");
                    mana -= 20;
                } else {
                    System.out.println("Insufficient mana for Heal! Casting normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Ice Storm
                if (iceStormCooldown == 0 && mana >= 30) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 1.5 * multiplier);
                    System.out.println("You cast Ice Storm and deal " + damage + " damage, slowing enemies!");
                    enemy.receiveDamage(damage);
                    mana -= 30;
                    iceStormCooldown = 5;
                } else {
                    System.out.println("Ice Storm is on cooldown or insufficient mana! Casting normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Arcane Barrage
                if (arcaneBarrageCooldown == 0 && mana >= 30) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2.5 * multiplier);
                    System.out.println("You cast Arcane Barrage and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 30;
                    arcaneBarrageCooldown = 5;
                } else {
                    System.out.println("Arcane Barrage is on cooldown or insufficient mana! Casting normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            default:
                super.useSkill(1, enemy);
                break;
        }
    }
}
