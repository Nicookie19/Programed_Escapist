package com.mycompany.programmer_escape;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Hacker extends Hero {
    private int exploitCooldown = 0;
    private int ddosCooldown = 0;
    private int rootkitCooldown = 0;

    public Hacker() {
        super(new Random());
        this.maxHP = 180;
        this.hp = 100;
        this.minDmg = 10;
        this.maxDmg = 25;
        this.maxMana = 120;
        this.mana = 120;
        this.attackNames = new String[]{"Exploit", "Inject", "DDoS", "Rootkit"};
    }

    @Override
    public String getClassName() {
        return "Hacker";
    }

    @Override
    protected List<String> getAllowedWeapons() {
        return Arrays.asList("Exploit Kit", "Injection Tool", "DDoS Script", "Rootkit Module", "Firewall Bypass");
    }

    @Override
    protected List<String> getAllowedArmors() {
        return Arrays.asList("Firewall Robe", "Encryption Cloak");
    }

    @Override
    public void decrementCooldowns() {
        if (exploitCooldown > 0) exploitCooldown--;
        if (ddosCooldown > 0) ddosCooldown--;
        if (rootkitCooldown > 0) rootkitCooldown--;
    }

    @Override
    public void applyPassiveEffects() {
        // Existing passive: Mana regeneration
        int manaRegen = (int)(maxMana * 0.1);
        mana = Math.min(mana + manaRegen, maxMana);
        System.out.println("Hacker regenerates " + manaRegen + " mana.");

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
            case 0: // Exploit
                if (exploitCooldown == 0 && mana >= 25) {
                    int baseDamage = minDmg + random.nextInt(maxDmg);
                    int damage = (int)(baseDamage * 2 * multiplier);
                    System.out.println("You exploit a vulnerability and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 25;
                    exploitCooldown = 5;
                } else {
                    System.out.println("Exploit is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 1: // Inject
                super.useSkill(1, enemy);
                break;
            case 2: // DDoS
                if (ddosCooldown == 0 && mana >= 20) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 1.5 * multiplier);
                    System.out.println("You launch DDoS attack and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 20;
                    ddosCooldown = 4;
                } else {
                    System.out.println("DDoS is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            case 3: // Rootkit
                if (rootkitCooldown == 0 && mana >= 30) {
                    int baseDamage = minDmg + random.nextInt(maxDmg - minDmg + 1);
                    int damage = (int)(baseDamage * 2.5 * multiplier);
                    System.out.println("You deploy Rootkit and deal " + damage + " damage!");
                    enemy.receiveDamage(damage);
                    mana -= 30;
                    rootkitCooldown = 5;
                } else {
                    System.out.println("Rootkit is on cooldown or insufficient mana! Using normal attack.");
                    super.useSkill(1, enemy);
                }
                break;
            default:
                super.useSkill(1, enemy);
                break;
        }
    }
}
