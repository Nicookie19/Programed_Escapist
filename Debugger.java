package com.mycompany.programmer_escape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Debugger extends Hero {
    private int debugCooldown = 0;
    private int patchCooldown = 0;
    private int secureCooldown = 0;
    private boolean secureActive = false;
    private int secureTurns = 0;
    private boolean ironResolveTriggered = false;

    public Debugger() {
        super(new Random());
        this.maxHP = 280;
        this.hp = this.maxHP;
        this.minDmg = 20;
        this.maxDmg = 35;
        this.maxMana = 60;
        this.mana = 40;
        this.attackNames = new String[]{"Debug", "Refactor", "Patch", "Optimize", "Secure"};
    }

    @Override
    public String getClassName() {
        return "Debugger";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Basic Exploit", "Advanced Exploit", "Zero-Day Exploit", "Kernel Exploit", "Buffer Overflow Exploit");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Firewall Vest", "Encryption Cloak");
    }

    @Override
    public void decrementCooldowns() {
        if (debugCooldown > 0) debugCooldown--;
        if (patchCooldown > 0) patchCooldown--;
        if (secureCooldown > 0) {
            secureCooldown--;
            if (secureCooldown == 0) {
                secureActive = false;
            }
        }
        if (secureActive && secureTurns > 0) {
            secureTurns--;
            if (secureTurns == 0) {
                secureActive = false;
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
            case 0: // Debug
                if (debugCooldown == 0 && mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You use Debug and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                    debugCooldown = 4;
                } else {
                    System.out.println("Debug is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Refactor
                super.useSkill(1, enemy);
                break;
            case 2: // Patch
                if (patchCooldown == 0 && mana >= 15) {
                    int baseDamage = minDmg + random.nextInt(10);
                    int damage = (int)(baseDamage * multiplier);
                    System.out.println("You use Patch and deal " + damage + " damage, stunning the enemy!");
                    enemy.receiveDamage(damage);
                    enemy.stunnedForNextTurn = true;
                    mana -= 15;
                    patchCooldown = 3;
                } else {
                    System.out.println("Patch is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Optimize
                if (mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2.5 * multiplier);
                    System.out.println("You use Optimize and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                } else {
                    System.out.println("Insufficient mana for Optimize! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 4: // Secure
                if (secureCooldown == 0 && mana >= 15) {
                    System.out.println("You enter Secure, reducing damage taken by 30% for 2 turns!");
                    secureActive = true;
                    secureTurns = 2;
                    mana -= 15;
                    secureCooldown = 4;
                } else {
                    System.out.println("Secure is on cooldown or insufficient mana! Using normal attack.");
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
        if (secureActive && secureTurns > 0) {
            dmg = (int)(dmg * 0.7);
            System.out.println("Secure reduces damage by 30%!");
        }
        super.receiveDamage(dmg);
    }
}
