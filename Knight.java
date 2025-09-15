package com.mycompany.programmer_escape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Knight extends Hero {
    private int cleaveCooldown = 0;
    private int shieldCooldown = 0;
    private int fortifiedStanceCooldown = 0;
    private boolean fortifiedStanceActive = false;
    private int fortifiedStanceTurns = 0;
    private boolean ironResolveTriggered = false;

    public Knight() {
        super(new Random());
        this.maxHP = 280;
        this.hp = this.maxHP;
        this.minDmg = 20;
        this.maxDmg = 35;
        this.maxMana = 60;
        this.mana = 40;
        this.attackNames = new String[]{"Cleave", "Slash", "Shield Bash", "Dragon Strike", "Fortified Stance"};
    }

    @Override
    public String getClassName() {
        return "Knight";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword", "Daedric Sword");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Plate Armor", "Chainmail");
    }

    @Override
    public void decrementCooldowns() {
        if (cleaveCooldown > 0) cleaveCooldown--;
        if (shieldCooldown > 0) shieldCooldown--;
        if (fortifiedStanceCooldown > 0) {
            fortifiedStanceCooldown--;
            if (fortifiedStanceCooldown == 0) {
                fortifiedStanceActive = false;
            }
        }
        if (fortifiedStanceActive && fortifiedStanceTurns > 0) {
            fortifiedStanceTurns--;
            if (fortifiedStanceTurns == 0) {
                fortifiedStanceActive = false;
            }
        }
    }

    @Override
    public void applyPassiveEffects() {
        if (hp < maxHP * 0.3 && !ironResolveTriggered) {
            int tempHP = (int)(maxHP * 0.1);
            hp = Math.min(hp + tempHP, maxHP);
            System.out.println("Iron Resolve activates, granting " + tempHP + " temporary HP!");
            ironResolveTriggered = true;
        }
    }

    @Override
    public void useSkill(int skillIdx, Enemy enemy) {
        double multiplier = getSkillMultiplier();
        switch (skillIdx) {
            case 0: // Cleave
                if (cleaveCooldown == 0 && mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You use Cleave and deal " + damage + " damage to all enemies!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                    cleaveCooldown = 4;
                } else {
                    System.out.println("Cleave is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Slash
                super.useSkill(1, enemy);
                break;
            case 2: // Shield Bash
                if (shieldCooldown == 0 && mana >= 15) {
                    int baseDamage = minDmg + random.nextInt(10);
                    int damage = (int)(baseDamage * multiplier);
                    System.out.println("You use Shield Bash and deal " + damage + " damage, stunning the enemy!");
                    enemy.receiveDamage(damage);
                    enemy.stunnedForNextTurn = true;
                    mana -= 15;
                    shieldCooldown = 3;
                } else {
                    System.out.println("Shield Bash is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Dragon Strike
                if (mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2.5 * multiplier);
                    System.out.println("You use Dragon Strike and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                } else {
                    System.out.println("Insufficient mana for Dragon Strike! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Fortified Stance
                if (fortifiedStanceCooldown == 0 && mana >= 15) {
                    System.out.println("You enter Fortified Stance, reducing damage taken by 30% for 2 turns!");
                    fortifiedStanceActive = true;
                    fortifiedStanceTurns = 2;
                    mana -= 15;
                    fortifiedStanceCooldown = 4;
                } else {
                    System.out.println("Fortified Stance is on cooldown or insufficient mana! Using normal attack.");
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
        if (fortifiedStanceActive && fortifiedStanceTurns > 0) {
            dmg = (int)(dmg * 0.7);
            System.out.println("Fortified Stance reduces damage by 30%!");
        }
        super.receiveDamage(dmg);
    }
}
