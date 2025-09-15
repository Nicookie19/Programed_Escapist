package com.mycompany.programmer_escape;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Assasin extends Hero {
    private int backstabCooldown = 0;
    private int smokeBombCooldown = 0;
    private int shadowStepCooldown = 0;
    boolean smokeBombActive = false;
    private boolean shadowStepActive = false;

    public Assasin() {
        super(new Random());
        this.maxHP = 200;
        this.hp = this.maxHP;
        this.minDmg = 20;
        this.maxDmg = 40;
        this.maxMana = 70;
        this.mana = this.maxMana;
        this.attackNames = new String[]{"Backstab", "Quick Strike", "Smoke Bomb", "Assassinate", "Shadow Step"};
    }

    @Override
    public String getClassName() {
        return "Assassin";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger", "Daedric Dagger");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Leather Armor", "Cloak of Shadows");
    }

    @Override
    public void decrementCooldowns() {
        if (backstabCooldown > 0) backstabCooldown--;
        if (smokeBombCooldown > 0) {
            smokeBombCooldown--;
            if (smokeBombCooldown == 0) {
                smokeBombActive = false;
            }
        }
        if (shadowStepCooldown > 0) {
            shadowStepCooldown--;
            if (shadowStepCooldown == 0) {
                shadowStepActive = false;
            }
        }
        super.decrementCooldowns();
    }

    @Override
    public void applyPassiveEffects() {
        // Night Stalker handled in useSkill for damage bonus
        // Existing passive: 10% dodge chance handled in encounter
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        double damageMultiplier = (enemy.hp < enemy.maxHP * 0.5) ? 1.15 : 1.0; // Night Stalker: +15% damage if enemy HP < 50%
        switch (skillIdx) {
            case 0: // Backstab
                if (backstabCooldown == 0 && mana >= 15) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    boolean isCrit = random.nextInt(100) < 30;
                    int damage = (int)(baseDamage * (isCrit ? 3 : 2) * multiplier * damageMultiplier);
                    System.out.println("You use Backstab and deal " + damage + (isCrit ? " critical" : "") + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 15;
                    backstabCooldown = 4;
                } else {
                    System.out.println("Backstab is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Quick Strike
                int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                int damage = (int)(baseDamage * multiplier * damageMultiplier);
                System.out.println("You use Quick Strike and deal " + damage + " damage!");
                enemy.receiveDamage(damage);
                break;
            case 2: // Smoke Bomb
                if (smokeBombCooldown == 0 && mana >= 10) {
                    System.out.println("You use Smoke Bomb, reducing enemy accuracy!");
                    smokeBombActive = true;
                    mana -= 10;
                    smokeBombCooldown = 3;
                } else {
                    System.out.println("Smoke Bomb is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Assassinate
                if (mana >= 25) {
                    baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                     damage = (int)(baseDamage * 2.5 * multiplier * damageMultiplier);
                    if (enemy.hp < enemy.maxHP * 0.3 && random.nextInt(100) < 20) {
                        damage = enemy.hp;
                        System.out.println("You use Assassinate and instantly kill the enemy!");
                    } else {
                        System.out.println("You use Assassinate and deal " + damage + " damage!");
                    }
                    enemy.receiveDamage(damage);
                    mana -= 25;
                } else {
                    System.out.println("Insufficient mana for Assassinate! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Shadow Step
                if (shadowStepCooldown == 0 && mana >= 15) {
                    System.out.println("You use Shadow Step, guaranteeing a dodge for the next enemy attack!");
                    shadowStepActive = true;
                    mana -= 15;
                    shadowStepCooldown = 4;
                } else {
                    System.out.println("Shadow Step is on cooldown or insufficient mana! Using normal attack.");
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
        if (shadowStepActive) {
            System.out.println("Shadow Step allows you to dodge the attack!");
            shadowStepActive = false;
            return;
        }
        super.receiveDamage(dmg);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.random = new Random();
    }
}
