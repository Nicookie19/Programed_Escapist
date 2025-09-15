# TODO: Align Hero Classes with Programmer Theme

## Overview
Update all hero classes, skills, weapons, names, factions, and quests to match the programmer/computer escape theme. Locations and enemies are already themed.

## Steps
1. Rename class files and update class names:
   - Knight.java -> Debugger.java (class Debugger)
   - Mage.java -> Hacker.java (class Hacker)
   - Archer.java -> Tester.java (class Tester)
   - Rook.java -> Architect.java (class Architect)
   - Assasin.java -> PenTester.java (class PenTester)
   - Cleric.java -> Support.java (class Support)

2. Update skills for each class:
   - Debugger: Debug, Refactor, Patch, Optimize, Secure
   - Hacker: Exploit, Inject, DDoS, Rootkit, Firewall
   - Tester: Scan, Verify, Bug Report, Regression, Penetration
   - Architect: Design, Build, Rally, Demolish, Fortify
   - PenTester: Probe, Breach, Exploit Chain, Recon, Stealth
   - Support: Patch, Heal, Buffer, Restore, Bless

3. Update allowed weapons and armors to match Hero.weaponTable and themed items:
   - Weapons: Basic Exploit, Advanced Exploit, Zero-Day Exploit, etc.
   - Armors: Firewall Vest, Encryption Cloak, etc. (update Hero.itemTable if needed)

4. Update character names in TurnBased_RPG.java and GameManager.java:
   - Nikolaos Ironmark -> Alex Codebreaker (Debugger)
   - Aurelion Thalor -> Maya Firewall (Hacker)
   - Etc.

5. Update factions in Faction.java and references:
   - Companions -> Open Source Community
   - Thieves Guild -> Hackers Guild
   - Etc.

6. Update quests in Quest.java and QuestManager.java:
   - Relic Hunt -> Code Hunt
   - Etc.

7. Update all instanceof checks and references in Hero.java, Heroes.java, etc.

8. Compile and test the game.

## Progress
- [ ] Read all relevant files
- [ ] Create new class files with updated names and content
- [ ] Update TurnBased_RPG.java
- [ ] Update GameManager.java
- [ ] Update Heroes.java
- [ ] Update Hero.java
- [ ] Update Faction.java
- [ ] Update Quest.java and QuestManager.java
- [ ] Delete old class files
- [ ] Compile with mvn compile
- [ ] Run and verify theme alignment
