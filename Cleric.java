package com.mycompany.programmer_escape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Cleric extends Hero {
    private int holySmiteCooldown = 0;
    private int divineShieldCooldown = 0;
    private int blessedLightCooldown = 0;
    private boolean divineShieldActive = false;

    public Cleric() {
        super(new Random());
        this.maxHP = 240;
        this.hp = this.maxHP;
        this.minDmg = 15;
        this.maxDmg = 30;
        this.maxMana = 100;
        this.mana = this.maxMana;
        this.attackNames = new String[]{"Holy Smite", "Light Strike", "Divine Shield", "Restoration", "Blessed Light"};
    }

    @Override
    public String getClassName() {
        return "Cleric";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Mace", "Flail", "Staff of Healing", "Holy Scepter");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Robe of Protection", "Chainmail");
    }

    @Override
    public void decrementCooldowns() {
        if (holySmiteCooldown > 0) holySmiteCooldown--;
        if (divineShieldCooldown > 0) {
            divineShieldCooldown--;
            if (divineShieldCooldown == 0) {
                divineShieldActive = false;
            }
        }
        if (blessedLightCooldown > 0) blessedLightCooldown--;
    }

    @Override
    public void applyPassiveEffects() {
        int heal = (int)(maxHP * 0.05);
        hp = Math.min(hp + heal, maxHP);
        if (heal > 0) {
            System.out.println("Cleric regenerates " + heal + " HP.");
        }
    }

    private void triggerDivineGrace() {
        if (random.nextInt(100) < 20) {
            String[] abilities = {"Holy Smite", "Divine Shield", "Blessed Light"};
            int[] cooldowns = {holySmiteCooldown, divineShieldCooldown, blessedLightCooldown};
            List<Integer> activeCooldowns = new ArrayList<>();
            for (int i = 0; i < cooldowns.length; i++) {
                if (cooldowns[i] > 0) {
                    activeCooldowns.add(i);
                }
            }
            if (!activeCooldowns.isEmpty()) {
                int idx = activeCooldowns.get(random.nextInt(activeCooldowns.size()));
                if (idx == 0) holySmiteCooldown = 0;
                else if (idx == 1) divineShieldCooldown = 0;
                else if (idx == 2) blessedLightCooldown = 0;
                System.out.println("Divine Grace resets the cooldown of " + abilities[idx] + "!");
            }
        }
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        switch (skillIdx) {
            case 0: // Holy Smite
                if (holySmiteCooldown == 0 && mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 1.5 * multiplier);
                    System.out.println("You cast Holy Smite and deal " + damage + " damage to all enemies!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                    holySmiteCooldown = 4;
                } else {
                    System.out.println("Holy Smite is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Light Strike
                super.useSkill(1, enemy);
                break;
            case 2: // Divine Shield
                if (divineShieldCooldown == 0 && mana >= 15) {
                    System.out.println("You cast Divine Shield, reducing damage taken next turn!");
                    divineShieldActive = true;
                    mana -= 15;
                    divineShieldCooldown = 3;
                } else {
                    System.out.println("Divine Shield is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Restoration
                if (mana >= 25) {
                    int heal = (int)(maxHP * 0.4 * multiplier);
                    hp = Math.min(hp + heal, maxHP);
                    System.out.println("You cast Restoration and restore " + heal + " HP!");
                    mana -= 25;
                    triggerDivineGrace();
                } else {
                    System.out.println("Insufficient mana for Restoration! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Blessed Light
                if (blessedLightCooldown == 0 && mana >= 20) {
                    int heal = (int)(maxHP * 0.25 * multiplier);
                    hp = Math.min(hp + heal, maxHP);
                    System.out.println("You cast Blessed Light and restore " + heal + " HP!");
                    Set<String> undeadEnemies = new HashSet<>(Arrays.asList("Draugr", "Vampire", "Skeleton", "Wraith", "Mummy"));
                    if (undeadEnemies.contains(enemy.getCurrentName())) {
                        int damage = (int)(minDmg * 1.2 * multiplier);
                        System.out.println("Blessed Light deals " + damage + " damage to the undead " + enemy.getCurrentName() + "!");
                        enemy.receiveDamage(damage);
                    }
                    mana -= 20;
                    blessedLightCooldown = 4;
                    triggerDivineGrace();
                } else {
                    System.out.println("Blessed Light is on cooldown or insufficient mana! Using normal attack.");
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
        if (divineShieldActive) {
            dmg = (int)(dmg * 0.5);
            System.out.println("Divine Shield absorbs half the damage!");
        }
        super.receiveDamage(dmg);
    }
}
