package com.mycompany.programmer_escape;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Color {

    public static final String RESET = "\u001B[0m";
    public static final String WHITE = "\u001B[37m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GRAY = "\u001B[90m";
    public static boolean USE_ANSI = true;

    public static String colorize(String text, String color) {
        if (!USE_ANSI) {
            return text;
        }
        return color + text + RESET;
    }
}

public class TurnBased_RPG {

    private static final int MENU_WIDTH = 60;
    Scanner scan = new Scanner(System.in);

    Hero player;
    Enemy enemy;

    Stack<Integer> lastPlayerHP = new Stack<>();
    Stack<Integer> lastEnemyHP = new Stack<>();

    int gameTimer = 2;
    String equippedWeapon = "Basic Sword";
    String equippedArmor = "Cloth Armor";
    private boolean useColor = true;
    private QuestManager questManager = new QuestManager();

    private Map<String, Location> worldMap;
    private Random random = new Random();
    private List<Faction> availableFactions;

    public static void main(String[] args) {
        System.out.println("Game starting...");
        try {
            TurnBased_RPG game = new TurnBased_RPG();
            game.displayMainMenu();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public TurnBased_RPG() {
        initializeWorld();
        initializeFactions();
    }

    private void printCenteredLine(String text, String color) {
        StringBuilder padded = new StringBuilder();
        padded.append(String.format("%-" + (MENU_WIDTH - 2) + "s", text));
        String line = "|" + padded.substring(0, Math.min(padded.length(), MENU_WIDTH - 2)) + "|";
        int padding = (MENU_WIDTH - 2 - text.trim().length()) / 2;
        line = "|" + " ".repeat(padding) + text.trim() + " ".repeat(MENU_WIDTH - 2 - padding - text.trim().length()) + "|";
        System.out.println(Color.colorize(line, color));
    }

    private void printBorder(String type) {
        if (type.equals("top")) {
            System.out.println(Color.colorize("+" + "=".repeat(MENU_WIDTH - 2) + "+", Color.WHITE));
        } else if (type.equals("bottom")) {
            System.out.println(Color.colorize("+" + "=".repeat(MENU_WIDTH - 2) + "+", Color.WHITE));
        } else if (type.equals("divider")) {
            System.out.println(Color.colorize("+" + "=".repeat(MENU_WIDTH - 2) + "+", Color.WHITE));
        }
    }

    private void initializeFactions() {
        availableFactions = new ArrayList<>();
        availableFactions.add(new Faction("Hackers Alliance"));
        availableFactions.add(new Faction("Cyber Thieves"));
        availableFactions.add(new Faction("Shadow Coders"));
        availableFactions.add(new Faction("Tech University"));
        availableFactions.add(new Faction("Firewall Guardians"));
    }

    private void initializeWorld() {
        worldMap = new HashMap<>();

        Location desktop = new Location("Desktop",
                "The main workspace of the computer, cluttered with icons and shortcuts.", 1, true,
                new String[]{"Adware", "Popup", "Shortcut Virus"});
        desktop.addFeature("Recycle Bin");
        desktop.addFeature("Taskbar");
        worldMap.put("Desktop", desktop);

        Location downloads = new Location("Downloads Folder",
                "A folder filled with downloaded files, often harboring malware.", 2, true,
                new String[]{"Trojan", "Spyware", "Phishing Scam"});
        downloads.addFeature("File Manager");
        downloads.addFeature("Antivirus Scanner");
        worldMap.put("Downloads Folder", downloads);

        Location system32 = new Location("System32 Directory",
                "Core system files directory, guarded by the operating system.", 3, false,
                new String[]{"Rootkit", "Kernel Panic", "System Crash"});
        system32.addFeature("Registry Editor");
        system32.addFeature("Command Prompt");
        worldMap.put("System32 Directory", system32);

        Location registry = new Location("Registry Hive",
                "The central database for configuration settings, full of hidden dangers.", 4, false,
                new String[]{"Registry Worm", "Corrupted Key", "Blue Screen"});
        registry.addFeature("Regedit Tool");
        registry.addFeature("Backup Restore");
        worldMap.put("Registry Hive", registry);

        Location hardDrive = new Location("Hard Drive Sector",
                "Storage sectors of the hard drive, prone to data corruption.", 3, false,
                new String[]{"Data Corruption", "Bad Sector", "File Fragmentation"});
        hardDrive.addFeature("Disk Defragmenter");
        hardDrive.addFeature("Data Recovery");
        worldMap.put("Hard Drive Sector", hardDrive);

        Location ram = new Location("RAM Bank",
                "Volatile memory banks, flickering with data streams.", 2, true,
                new String[]{"Memory Leak", "Buffer Overflow", "Stack Overflow"});
        ram.addFeature("Memory Diagnostic");
        ram.addFeature("Cache Cleaner");
        worldMap.put("RAM Bank", ram);

        Location network = new Location("Network Interface",
                "Connection point to the internet, vulnerable to external threats.", 3, true,
                new String[]{"Firewall Breach", "DDoS Attack", "Man-in-the-Middle"});
        network.addFeature("Firewall Control");
        network.addFeature("VPN Gateway");
        worldMap.put("Network Interface", network);

        Location browser = new Location("Web Browser Cache",
                "Cached web data and cookies, often infected with tracking scripts.", 2, true,
                new String[]{"Cookie Monster", "Tracking Script", "Malicious Ad"});
        browser.addFeature("Incognito Mode");
        browser.addFeature("Cookie Manager");
        worldMap.put("Web Browser Cache", browser);

        Location tempFiles = new Location("Temp Files Directory",
                "Temporary files accumulating junk and potential threats.", 1, true,
                new String[]{"Junk File", "Temp Virus", "Leftover Malware"});
        tempFiles.addFeature("Disk Cleanup");
        tempFiles.addFeature("Temp File Cleaner");
        worldMap.put("Temp Files Directory", tempFiles);

        Location startup = new Location("Startup Programs",
                "Programs that run at boot, some may be unwanted.", 2, false,
                new String[]{"Startup Malware", "Bloatware", "Autorun Worm"});
        startup.addFeature("Task Manager");
        startup.addFeature("Startup Manager");
        worldMap.put("Startup Programs", startup);

        Location cloud = new Location("Cloud Storage",
                "Remote storage in the cloud, accessible but potentially compromised.", 3, false,
                new String[]{"Cloud Breach", "Sync Malware", "Data Leak"});
        cloud.addFeature("Cloud Sync");
        cloud.addFeature("Encryption Tool");
        worldMap.put("Cloud Storage", cloud);

        Location bios = new Location("BIOS Firmware",
                "Low-level system firmware, critical and dangerous to tamper with.", 5, false,
                new String[]{"Firmware Virus", "Boot Sector Virus", "UEFI Rootkit"});
        bios.addFeature("BIOS Setup");
        bios.addFeature("Firmware Update");
        worldMap.put("BIOS Firmware", bios);

        Location gpu = new Location("GPU Memory",
                "Graphics processing unit memory, handling visual computations.", 3, false,
                new String[]{"Graphics Glitch", "Shader Virus", "Render Bug"});
        gpu.addFeature("Graphics Driver");
        gpu.addFeature("GPU Overclock");
        worldMap.put("GPU Memory", gpu);

        Location usb = new Location("USB Ports",
                "External device connection points, entry for portable threats.", 2, true,
                new String[]{"USB Worm", "Autorun Script", "Drive-by Download"});
        usb.addFeature("USB Scanner");
        usb.addFeature("Port Blocker");
        worldMap.put("USB Ports", usb);

        Location email = new Location("Email Inbox",
                "Digital mailbox filled with messages, some containing phishing.", 2, true,
                new String[]{"Phishing Email", "Spam Bot", "Email Worm"});
        email.addFeature("Spam Filter");
        email.addFeature("Email Client");
        worldMap.put("Email Inbox", email);

        Location firewall = new Location("Firewall Logs",
                "Security logs monitoring network traffic.", 3, false,
                new String[]{"Firewall Bypass", "Port Scan", "Intrusion Attempt"});
        firewall.addFeature("Log Analyzer");
        firewall.addFeature("Rule Editor");
        worldMap.put("Firewall Logs", firewall);

        Location antivirus = new Location("Antivirus Database",
                "Signature database for detecting known threats.", 3, false,
                new String[]{"Zero-Day Exploit", "Polymorphic Virus", "Signature Evasion"});
        antivirus.addFeature("Update Server");
        antivirus.addFeature("Quarantine Zone");
        worldMap.put("Antivirus Database", antivirus);

        Location backup = new Location("Backup Drive",
                "Archived data storage, hopefully safe from corruption.", 1, true,
                new String[]{"Backup Corruption", "Ransomware Lock", "Stale Backup"});
        backup.addFeature("Backup Software");
        backup.addFeature("Restore Point");
        worldMap.put("Backup Drive", backup);

        Location virtualMachine = new Location("Virtual Machine",
                "Isolated environment running simulated systems.", 4, false,
                new String[]{"VM Escape", "Hypervisor Bug", "Nested Exploit"});
        virtualMachine.addFeature("VM Manager");
        virtualMachine.addFeature("Snapshot Tool");
        worldMap.put("Virtual Machine", virtualMachine);

        Location darkWeb = new Location("Dark Web Gateway",
                "Hidden network access, filled with illicit data and threats.", 5, false,
                new String[]{"Dark Web Crawler", "Tor Exit Node", "Encrypted Malware"});
        darkWeb.addFeature("Tor Browser");
        darkWeb.addFeature("Encryption Suite");
        worldMap.put("Dark Web Gateway", darkWeb);

        Location aiCore = new Location("AI Processing Core",
                "Advanced AI algorithms processing data, potentially self-aware.", 5, false,
                new String[]{"AI Virus", "Neural Network Hack", "Deepfake Generator"});
        aiCore.addFeature("AI Trainer");
        aiCore.addFeature("Model Optimizer");
        worldMap.put("AI Processing Core", aiCore);

        Location quantum = new Location("Quantum Computer",
                "Experimental quantum processing, unstable and powerful.", 5, false,
                new String[]{"Quantum Entanglement", "Superposition Bug", "Decoherence Error"});
        quantum.addFeature("Quantum Simulator");
        quantum.addFeature("Error Correction");
        worldMap.put("Quantum Computer", quantum);

        Location iot = new Location("IoT Device Network",
                "Internet of Things devices, numerous and vulnerable.", 3, true,
                new String[]{"IoT Botnet", "Smart Device Hack", "Firmware Exploit"});
        iot.addFeature("Device Manager");
        iot.addFeature("Network Scanner");
        worldMap.put("IoT Device Network", iot);

        Location blockchain = new Location("Blockchain Ledger",
                "Decentralized data chain, immutable but hackable.", 4, false,
                new String[]{"51% Attack", "Smart Contract Bug", "Cryptojacking"});
        blockchain.addFeature("Wallet Manager");
        blockchain.addFeature("Mining Pool");
        worldMap.put("Blockchain Ledger", blockchain);

        Location satellite = new Location("Satellite Link",
                "Orbital communication link, high latency and exposure.", 4, false,
                new String[]{"Satellite Jammer", "GPS Spoof", "Orbital Malware"});
        satellite.addFeature("Satellite Dish");
        satellite.addFeature("Signal Booster");
        worldMap.put("Satellite Link", satellite);

        Location hologram = new Location("Holographic Display",
                "3D projection interface, visually stunning but glitchy.", 3, false,
                new String[]{"Hologram Glitch", "Projection Virus", "Depth Buffer Hack"});
        hologram.addFeature("Holo Projector");
        hologram.addFeature("Calibration Tool");
        worldMap.put("Holographic Display", hologram);

        Location neural = new Location("Neural Interface",
                "Direct brain-computer link, intimate and dangerous.", 5, false,
                new String[]{"Neural Hijack", "Mind Virus", "Thought Malware"});
        neural.addFeature("Neural Link");
        neural.addFeature("Brainwave Scanner");
        worldMap.put("Neural Interface", neural);
    }

    public void displayMainMenu() {
        while (true) {
            printBorder("top");
            printCenteredLine("Programmed Escapist", Color.PURPLE);
            printCenteredLine("Help Im Stuck in My Computer", Color.GRAY);
            printBorder("divider");
            printCenteredLine("1. Start New Game", Color.WHITE);
            printCenteredLine("2. Load Game", Color.WHITE);
            printCenteredLine("3. Exit", Color.WHITE);
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-3): ", Color.YELLOW));

            String input = scan.nextLine().trim();
            if (input.equals("1")) {
                startGame();
            } else if (input.equals("2")) {
                loadGame();
            } else if (input.equals("3")) {
                System.out.println(Color.colorize("Thank you for playing! Farewell, adventurer!", Color.GREEN));
                System.out.println(Color.colorize("NICO-chan", Color.GREEN));
                break;
            } else {
                System.out.println(Color.colorize("Invalid option. Please choose again.", Color.RED));
            }
        }
    }

    private void startGame() {
        printBorder("top");
        printCenteredLine("A New Legend Begins", Color.PURPLE);
        printBorder("divider");
        printCenteredLine("  Oh good, you're awake! Choose your path:  ", Color.YELLOW);
        System.out.println(Color.colorize("|" + " ".repeat(MENU_WIDTH - 2) + "|", Color.WHITE));
        System.out.println(Color.colorize("| 1. Debugger: Alex Codebreaker, The Bug Hunter            |", Color.BLUE));
        System.out.println(Color.colorize("|    - High health and defense, wields exploits and patches|", Color.GRAY));
        System.out.println(Color.colorize("| 2. Hacker: Neo Matrix, The Cyber Warrior                 |", Color.BLUE));
        System.out.println(Color.colorize("|    - Powerful exploits, low defense, uses scripts        |", Color.GRAY));
        System.out.println(Color.colorize("| 3. Tester: QA Master, The Quality Guardian               |", Color.BLUE));
        System.out.println(Color.colorize("|    - Agile, high critical hits, uses exploits            |", Color.GRAY));
        System.out.println(Color.colorize("| 4. Architect: DevOps Guru, The System Builder            |", Color.BLUE));
        System.out.println(Color.colorize("|    - Heavy builds, strong attacks, slow movement         |", Color.GRAY));
        System.out.println(Color.colorize("| 5. PenTester: Ethical Hacker, The Vulnerability Finder   |", Color.BLUE));
        System.out.println(Color.colorize("|    - Stealthy, high damage, uses exploits                |", Color.GRAY));
        System.out.println(Color.colorize("| 6. Support: Tech Support, The Helper                     |", Color.BLUE));
        System.out.println(Color.colorize("|    - Healing and support, uses tools                     |", Color.GRAY));
        System.out.println(Color.colorize("|" + " ".repeat(MENU_WIDTH - 2) + "|", Color.WHITE));
        printBorder("bottom");
        System.out.print(Color.colorize("Enter your choice (1-6): ", Color.YELLOW));

        String choice = scan.nextLine().trim();
        switch (choice) {
            case "1":
                player = new Debugger();
                break;
            case "2":
                player = new Hacker();
                break;
            case "3":
                player = new Tester();
                break;
            case "4":
                player = new Architect();
                break;
            case "5":
                player = new PenTester();
                break;
            case "6":
                player = new Support();
                break;
            default:
                System.out.println(Color.colorize("Invalid choice. Defaulting to Debugger.", Color.RED));
                player = new Debugger();
        }
        System.out.println(Color.colorize("You are now a " + player.getClassName() + "! The world awaits your legend.", Color.GREEN));
        questManager.addQuest(
                "Bug Hunt",
                "Seek a relic in Desktop.",
                Arrays.asList("Find relic in Desktop", "Return to RAM Bank"),
                Map.of("gold", 100, "xp", 50),
                null
        );
        questManager.addQuest(
                "Malware Hunt",
                "Defeat a virus in RAM Bank.",
                Arrays.asList("Defeat a Virus in RAM Bank"),
                Map.of("gold", 50, "xp", 20),
                null
        );
        inGameMenu();
    }

    private void inGameMenu() {
        while (true) {
            printBorder("top");
            printCenteredLine("Adventurer's Lodge", Color.PURPLE);
            printBorder("divider");
            printCenteredLine("1. Travel to New Lands", Color.WHITE);
            printCenteredLine("2. Manage Inventory and Equipment", Color.WHITE);
            printCenteredLine("3. View Gold", Color.WHITE);
            printCenteredLine("4. View Quest Log", Color.WHITE);
            printCenteredLine("5. View Player Stats", Color.WHITE);
            printCenteredLine("6. Rest at Inn", Color.WHITE);
            printCenteredLine("7. Faction Menu", Color.WHITE);
            printCenteredLine("8. Save Game", Color.WHITE);
            printCenteredLine("9. Toggle Color", Color.WHITE);
            printCenteredLine("10. Return to Main Menu", Color.WHITE);
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-10): ", Color.YELLOW));

            String input = scan.nextLine().trim();
            switch (input) {
                case "1":
                    travel();
                    break;
                case "2":
                    manageInventoryAndEquipment();
                    break;
                case "3":
                    viewGold();
                    break;
                case "4":
                    viewQuestLog();
                    break;
                case "5":
                    viewPlayerStats();
                    break;
                case "6":
                    restAtInn();
                    break;
                case "7":
                    factionMenu();
                    break;
                case "8":
                    saveGame();
                    break;
                case "9":
                    toggleColor();
                    break;
                case "10":
                    System.out.println(Color.colorize("Returning to Main Menu...", Color.GREEN));
                    return;
                default:
                    System.out.println(Color.colorize("Invalid option. Try again.", Color.RED));
            }
        }
    }

    private void toggleColor() {
        useColor = !useColor;
        Color.USE_ANSI = useColor;
        System.out.println(Color.colorize("Color " + (useColor ? "enabled" : "disabled"), Color.YELLOW));
    }

    private void manageInventoryAndEquipment() {
        while (true) {
            printBorder("top");
            printCenteredLine("Inventory and Equipment", Color.PURPLE);
            printBorder("divider");
            printCenteredLine("1. View Inventory", Color.WHITE);
            printCenteredLine("2. View Equipment", Color.WHITE);
            printCenteredLine("3. Equip or Use Item", Color.WHITE);
            printCenteredLine("4. Back to Main Menu", Color.WHITE);
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-4): ", Color.YELLOW));

            String input = scan.nextLine().trim();
            switch (input) {
                case "1":
                    viewInventory();
                    break;
                case "2":
                    viewEquipment();
                    break;
                case "3":
                    equipOrUseItem();
                    break;
                case "4":
                    return;
                default:
                    System.out.println(Color.colorize("Invalid option. Try again.", Color.RED));
            }
        }
    }

    private void factionMenu() {
        while (true) {
            printBorder("top");
            printCenteredLine("Faction Hall", Color.PURPLE);
            printBorder("divider");
            printCenteredLine("1. Join a Faction", Color.WHITE);
            printCenteredLine("2. View Faction Status", Color.WHITE);
            printCenteredLine("3. Undertake Faction Quest", Color.WHITE);
            printCenteredLine("4. Back", Color.WHITE);
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-4): ", Color.YELLOW));
            int choice = getChoice(1, 4);
            switch (choice) {
                case 1:
                    joinFaction();
                    break;
                case 2:
                    viewFactions();
                    break;
                case 3:
                    doFactionQuest();
                    break;
                case 4:
                    return;
            }
        }
    }

    private void joinFaction() {
        printBorder("top");
        printCenteredLine("Available Factions", Color.PURPLE);
        printBorder("divider");
        int count = 0;
        for (int i = 0; i < availableFactions.size(); i++) {
            Faction faction = availableFactions.get(i);
            if (!player.isInFaction(faction.getName())) {
                count++;
                System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", count, faction.getName()) + " |", Color.WHITE));
            }
        }
        if (count == 0) {
            printCenteredLine("No factions available to join.", Color.GRAY);
            printBorder("bottom");
            return;
        }
        System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", count + 1, "Cancel") + " |", Color.WHITE));
        printBorder("bottom");
        System.out.print(Color.colorize("Choose a faction (1-" + (count + 1) + "): ", Color.YELLOW));
        int choice = getChoice(1, count + 1);
        if (choice <= count) {
            int factionIdx = 0;
            for (Faction faction : availableFactions) {
                if (!player.isInFaction(faction.getName())) {
                    if (factionIdx == choice - 1) {
                        player.joinFaction(faction);
                        System.out.println(Color.colorize("You have joined the " + faction.getName() + "!", Color.GREEN));
                        return;
                    }
                    factionIdx++;
                }
            }
        }
    }

    private void viewFactions() {
        printBorder("top");
        printCenteredLine("Your Factions", Color.PURPLE);
        printBorder("divider");
        if (player.getFactions().isEmpty()) {
            printCenteredLine("You are not a member of any factions.", Color.GRAY);
        } else {
            for (Faction faction : player.getFactions()) {
                String factionLine = faction.getName() + " (Reputation: " + faction.getReputation() + ")";
                System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", factionLine) + " |", Color.WHITE));
            }
        }
        printBorder("bottom");
    }

    private void doFactionQuest() {
        if (player.getFactions().isEmpty()) {
            printBorder("top");
            printCenteredLine("Faction Quest", Color.PURPLE);
            printBorder("divider");
            printCenteredLine("You must join a faction first!", Color.RED);
            printBorder("bottom");
            return;
        }
        printBorder("top");
        printCenteredLine("Choose a Faction Quest", Color.PURPLE);
        printBorder("divider");
        List<Faction> playerFactions = player.getFactions();
        for (int i = 0; i < playerFactions.size(); i++) {
            System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", i + 1, playerFactions.get(i).getName()) + " |", Color.WHITE));
        }
        System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", playerFactions.size() + 1, "Cancel") + " |", Color.WHITE));
        printBorder("bottom");
        System.out.print(Color.colorize("Choose a faction (1-" + (playerFactions.size() + 1) + "): ", Color.YELLOW));
        int choice = getChoice(1, playerFactions.size() + 1);
        if (choice <= playerFactions.size()) {
            Faction faction = playerFactions.get(choice - 1);
            String questName = getFactionQuestName(faction.getName());
            String objective = getFactionQuestObjective(faction.getName());
            questManager.addQuest(
                    questName,
                    objective,
                    Arrays.asList(objective),
                    Map.of("gold", 100, "xp", 50),
                    faction.getName()
            );
            player.addFactionReputation(faction.getName(), 50);
        }
    }

    private String getFactionQuestName(String factionName) {
        switch (factionName) {
            case "Hackers Alliance":
                return "Clear Virus Camp";
            case "Cyber Thieves":
                return "Steal Valuable Artifact";
            case "Shadow Coders":
                return "Assassinate Corrupt Merchant";
            case "Tech University":
                return "Retrieve Ancient Tome";
            case "Firewall Guardians":
                return "Defend Supply Caravan";
            default:
                return "No Quest";
        }
    }

    private String getFactionQuestObjective(String factionName) {
        switch (factionName) {
            case "Hackers Alliance":
                return "Clear a virus camp near RAM Bank";
            case "Cyber Thieves":
                return "Steal a valuable artifact in Network Interface";
            case "Shadow Coders":
                return "Assassinate a corrupt merchant in System32 Directory";
            case "Tech University":
                return "Retrieve an ancient tome in BIOS Firmware";
            case "Firewall Guardians":
                return "Defend a supply caravan near Network Interface";
            default:
                return "No quest available";
        }
    }
    private int getChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scan.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void travel() {
        printBorder("top");
        printCenteredLine("Available Locations", Color.PURPLE);
        printBorder("divider");
        int i = 1;
        List<String> locations = new ArrayList<>(worldMap.keySet());
        for (String name : locations) {
            Location loc = worldMap.get(name);
            String locLine = String.format("%d. %s (Danger: %d/5)", i, name, loc.dangerLevel);
            System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", locLine) + " |", Color.WHITE));
            System.out.println(Color.colorize("|   " + String.format("%-" + (MENU_WIDTH - 6) + "s", loc.description) + " |", Color.GRAY));
            i++;
        }
        printBorder("bottom");
        System.out.print(Color.colorize("Choose a location (1-" + worldMap.size() + "): ", Color.YELLOW));
        try {
            int choice = Integer.parseInt(scan.nextLine().trim()) - 1;
            if (choice >= 0 && choice < worldMap.size()) {
                String destination = locations.get(choice);
                Location loc = worldMap.get(destination);
                enterLocation(loc);
            } else {
                System.out.println(Color.colorize("Invalid choice.", Color.RED));
            }
        } catch (NumberFormatException e) {
            System.out.println(Color.colorize("Invalid input. Please enter a number.", Color.RED));
        }
    }

    private void enterLocation(Location loc) {
        System.out.println(Color.colorize("\nYou arrive at " + loc.name, Color.YELLOW));
        System.out.println(Color.colorize(loc.description, Color.GRAY));

        if (loc.hasTown) {
            System.out.println(Color.colorize("\nThis location has a town where you can rest and trade.", Color.GREEN));
            encounterNPC(loc);
        }

        int encounters = 1 + random.nextInt(loc.dangerLevel);
        for (int i = 0; i < encounters; i++) {
            if (random.nextFloat() < 0.6f) {
                generateEncounter(loc);
            } else {
                generateDiscovery(loc);
            }
        }
    }

    private void generateEncounter(Location loc) {
        if (player == null) {
            System.out.println(Color.colorize("Error: No player selected. Please start a new game.", Color.RED));
            return;
        }
        enemy = new Enemy(Enemy.Tier.values()[random.nextInt(3)], player.level);
        enemy.changeName(loc.enemyPool[random.nextInt(loc.enemyPool.length)]);
        displayEnemyArt(enemy);
        String color = enemy.getTier() == Enemy.Tier.WEAK ? Color.GRAY
        : enemy.getTier() == Enemy.Tier.NORMAL ? Color.YELLOW : Color.RED;
System.out.println("\n" + Color.colorize("You encounter a " + enemy.getDisplayName() + " in " + loc.name + "!", color));
        encounter();
    }

    private void displayEnemyArt(Enemy enemy) {
        String enemyName = enemy.getCurrentName();
        if (enemyName.equals("Dragon")) {
            System.out.println(Color.colorize(
                    "     /|\\\n"
                    + "    / 0 \\\n"
                    + "   / ===Y*===\n"
                    + "  /_______/",
                    Color.RED));
        } else if (enemyName.equals("Bandit")) {
            System.out.println(Color.colorize(
                    "   O\n"
                    + "  /|\\\n"
                    + "  / \\",
                    Color.YELLOW));
        }
    }

    private void encounter() {
        if (player == null || enemy == null) {
            System.out.println(Color.colorize("Error: Combat cannot start. Player or enemy is missing.", Color.RED));
            return;
        }

        Combat combat = new Combat(player, enemy);
        List<String> combatLog = new ArrayList<>();
        combatLog.add("Combat starts!");

        while (!combat.isCombatOver()) {
            displayCombatStatus(player, enemy, combatLog);

            // Player turn
            System.out.println("\nYour turn! Choose an action:");
            System.out.println("1. Attack");
            System.out.println("2. Use Skill");
            System.out.println("3. Use Shout");
            System.out.println("4. Flee");
            
            int choice = getChoice(1, 4);
            
            switch (choice) {
                case 1:
                    combat.processRound("attack");
                    break;
                case 2:
                    performPlayerSkill(player, enemy, combatLog);
                    break;
                case 3:
                    performPlayerShout(player, enemy, combatLog);
                    break;
                case 4:
                    if (random.nextInt(100) < 50) {
                        combatLog.add(player.getClassName() + " flees from battle!");
                        System.out.println(Color.colorize("You fled from battle!", Color.YELLOW));
                        return;
                    } else {
                        combatLog.add(player.getClassName() + " fails to flee!");
                        System.out.println(Color.colorize("You failed to flee!", Color.RED));
                        combat.processRound("skip");
                    }
                    break;
            }

            if (!enemy.isAlive()) {
                handleVictory(player, combatLog, enemy);
                break;
            }
        }
    }

    private void handleVictory(Hero player, List<String> combatLog, Enemy enemy) {
        player.addGold(20);
        player.addXP(20);
        combatLog.add("You gain 20 gold and 20 XP.");
        String color = enemy.getTier() == Enemy.Tier.WEAK ? Color.GRAY
                : enemy.getTier() == Enemy.Tier.NORMAL ? Color.YELLOW : Color.RED;
        combatLog.add("You defeated the " + Color.colorize(enemy.getDisplayName(), color) + "!");
        if (enemy.getCurrentName().equals("Virus")) {
            questManager.updateQuest("Defeat a Virus in RAM Bank", player);
            questManager.updateQuest("Clear a virus camp near RAM Bank", player);
        }
    }

    // Added missing method to fix compile error
    private void performPlayerSkill(Hero player, Enemy enemy, List<String> combatLog) {
        // Example implementation: player uses a skill to deal damage or heal
        if (player.mana >= 15) {
            int skillDamage = 20 + (int)(Math.random() * 10);
            enemy.hp -= skillDamage;
            player.mana -= 15;
            combatLog.add(player.getClassName() + " uses a special skill and deals " + skillDamage + " damage!");
            if (enemy.hp <= 0) {
                enemy.hp = 0;
                combatLog.add("The skill defeats the " + enemy.getDisplayName() + "!");
            }
        } else {
            combatLog.add("Not enough mana to use a skill!");
        }
    }

    private void performPlayerShout(Hero player, Enemy enemy, List<String> combatLog) {
        // Example implementation: player uses a shout to deal damage or apply an effect
        if (player.mana >= 20) {
            int shoutDamage = 30 + (int)(Math.random() * 10);
            enemy.hp -= shoutDamage;
            player.mana -= 20;
            combatLog.add(player.getClassName() + " uses a powerful shout and deals " + shoutDamage + " damage!");
            if (enemy.hp <= 0) {
                enemy.hp = 0;
                combatLog.add("The shout defeats the " + enemy.getDisplayName() + "!");
            }
        } else {
            combatLog.add("Not enough mana to use a shout!");
        }
    }

    private void displayCombatStatus(Hero player, Enemy enemy, List<String> combatLog) {
        printBorder("top");
        printCenteredLine("=== Combat Status ===", Color.YELLOW);
        printBorder("divider");
        System.out.printf("| %-30s | %-30s |%n",
                player.getClassName() + " (Lv " + player.level + ")",
                enemy.getDisplayName() + " (Lv " + enemy.level + ")");
        System.out.printf("| HP: %-25s | HP: %-25s |%n",
                player.hp + "/" + player.maxHP + " [" + "=".repeat(Math.max(0, player.hp * 20 / player.maxHP)) + "]",
                enemy.hp + "/" + enemy.maxHP + " [" + "=".repeat(Math.max(0, enemy.hp * 20 / enemy.maxHP)) + "]");
        System.out.printf("| Mana: %-25%s | %-30s |%n",
                player.mana + "/" + player.maxMana + " [" + "=".repeat(Math.max(0, player.mana * 20 / player.maxMana)) + "]", "");
        String playerStatus = player.getStatusEffects().stream().map(StatusEffect::getName).collect(Collectors.joining(", "));
        String enemyStatus = enemy.getStatusEffects().stream().map(StatusEffect::getName).collect(Collectors.joining(", "));
        if (!playerStatus.isEmpty() || !enemyStatus.isEmpty()) {
            System.out.printf("| Status: %-25s | Status: %-25s |%n", playerStatus, enemyStatus);
        }
        printBorder("divider");
        System.out.println("| Recent Actions:");
        int start = Math.max(0, combatLog.size() - 3);
        for (int i = start; i < combatLog.size(); i++) {
            System.out.println("| " + combatLog.get(i));
        }
        printBorder("bottom");
    }

    private String getItemRarity(String itemName) {
        if (itemName.contains("Dragonbone") || itemName.contains("Dawnbreaker")
                || itemName.contains("Chillrend") || itemName.contains("Dragonbane")
                || itemName.contains("Archmage") || itemName.contains("Daedric")) {
            return Color.PURPLE;
        } else if (itemName.contains("Elven") || itemName.contains("Glass")
                || itemName.contains("Greater") || itemName.contains("Major")
                || itemName.contains("Cloak of Shadows") || itemName.contains("Nightshade")
                || itemName.contains("Orb of Elements")) {
            return Color.BLUE;
        } else if (itemName.contains("Steel") || itemName.contains("Mithril")
                || itemName.contains("Leather") || itemName.contains("Mana")
                || itemName.contains("Chainmail") || itemName.contains("Composite")
                || itemName.contains("Longbow")) {
            return Color.GREEN;
        } else {
            return Color.WHITE;
        }
    }

    private void generateDiscovery(Location loc) {
        String[] discoveries = {
            "You find an abandoned campsite",
            "You discover a hidden cave",
            "You stumble upon an ancient relic",
            "You meet a traveling merchant"
        };

        String discovery = discoveries[random.nextInt(discoveries.length)];
        System.out.println("\n" + Color.colorize(discovery + " in " + loc.name, Color.GREEN));

        if (discovery.contains("relic")) {
            player.addItem("Ancient Relic", 1.0f);
            System.out.println(Color.colorize("You found an Ancient Relic!", Color.YELLOW));
            questManager.updateQuest("Find relic in Desktop", player);
            questManager.updateQuest("Find a Lost Relic for " + loc.name, player);
        } else if (random.nextBoolean()) {
            String[] loot = {"gold", "potion", "weapon", "armor", "food", "misc"};
            String found = loot[random.nextInt(loot.length)];

            if (found.equals("gold")) {
                int amount = 10 + random.nextInt(20 * loc.dangerLevel);
                player.addGold(amount);
                System.out.println(Color.colorize("You found " + amount + " gold!", Color.YELLOW));
            } else if (found.equals("potion")) {
                String[] potions = {
                    "Health Potion", "Mana Potion", "Greater Health Potion", "Major Health Potion",
                    "Minor Health Potion", "Greater Mana Potion", "Major Mana Potion", "Minor Mana Potion",
                    "Antidote Potion", "Fire Resistance Potion", "Frost Resistance Potion",
                    "Poison Resistance Potion", "Healing Elixir", "Potion of Ultimate Healing"
                };
                String potion = potions[random.nextInt(potions.length)];
                player.addItem(potion, 0.5f);
                System.out.println(Color.colorize("You found a " + potion + "!", getItemRarity(potion)));
            } else if (found.equals("weapon")) {
                String[] classWeapons = player instanceof Debugger ? new String[]{
                    "Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword",
                    "Daedric Sword", "Dragonbone Sword", "Dawnbreaker", "Chillrend", "Dragonbane"
                }
                        : player instanceof Hacker ? new String[]{
                            "Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storm",
                            "Staff of Healing", "Wand of Lightning", "Orb of Elements"
                        }
                        : player instanceof Tester ? new String[]{
                            "Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow",
                            "Daedric Bow", "Dragonbone Bow"
                        }
                        : player instanceof Architect ? new String[]{
                            "Warhammer", "Battleaxe", "Mace", "Flail"
                        }
                        : player instanceof PenTester ? new String[]{
                            "Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger",
                            "Daedric Dagger", "Ebony Dagger"
                        }
                        : new String[]{
                            "Staff of Healing", "Holy Scepter", "Divine Mace"
                        };
                String weapon = classWeapons[random.nextInt(classWeapons.length)];
                player.addItem(weapon, 2.0f);
                System.out.println(Color.colorize("You found a " + weapon + "!", getItemRarity(weapon)));
            } else if (found.equals("armor")) {
                String[] classNames = player instanceof Debugger ? new String[]{
                    "Plate Armor", "Dragonbone Armor"
                } : player instanceof Hacker ? new String[]{"Robe of Protection", "Archmage Robes"
                } : player instanceof Tester ? new String[]{
                    "Leather Armor", "Elven Armor"
                } : player instanceof Architect ? new String[]{
                    "Chainmail", "Dragonscale Armor"
                } : player instanceof PenTester ? new String[]{
                    "Cloak of Shadows", "Nightshade Cloak"
                } : new String[]{"Robe of Protection", "Holy Shroud"};
                String armor = classNames[random.nextInt(classNames.length)];
                player.addItem(armor, 3.0f);
                System.out.println(Color.colorize("You found a " + armor + "!", getItemRarity(armor)));
            } else if (found.equals("food")) {
                String[] foods = {
                    "Apple", "Bread Loaf", "Cheese Wheel", "Roasted Meat", "Vegetable Stew"
                };
                String food = foods[random.nextInt(foods.length)];
                player.addItem(food, 0.4f);
                System.out.println(Color.colorize("You found a " + food + "!", getItemRarity(food)));
            } else if (found.equals("misc")) {
                String[] misc = {"Torch", "Map of the Realm", "Ancient Coin", "Silver Ring", "Amulet of Talos"};
                String item = misc[random.nextInt(misc.length)];
                player.addItem(item, 0.3f);
                System.out.println(Color.colorize("You found a " + item + "!", getItemRarity(item)));
            }
        }
    }

    private void encounterNPC(Location loc) {
        printBorder("top");
        printCenteredLine("Town of " + loc.name, Color.PURPLE);
        printBorder("divider");
        printCenteredLine("You enter a bustling town...", Color.YELLOW);

        int npcCount = 4 + random.nextInt(2);
        List<Enemy> npcs = new ArrayList<>();
        List<String> npcNames = new ArrayList<>();
        LinkedList<String> availableNames = new LinkedList<>();
        Enemy dummyEnemy = new Enemy();
        availableNames.addAll(Arrays.asList(dummyEnemy.listEnemyNames()));

        for (int i = 0; i < npcCount && !availableNames.isEmpty(); i++) {
            Enemy npc = new Enemy();
            String uniqueName = availableNames.remove(random.nextInt(availableNames.size()));
            npc.changeName(uniqueName);
            npc.setHostile(random.nextInt(100) < 20);
            npcs.add(npc);
            String color = npc.isHostile() ? Color.RED : Color.GREEN;
            npcNames.add(Color.colorize(npc.getDisplayName(), color));
            System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", i + 1, npcNames.get(i)) + " |", Color.WHITE));
        }
        printBorder("bottom");

        while (true) {
            printBorder("top");
            printCenteredLine("Town Options", Color.PURPLE);
            printBorder("divider");
            for (int i = 0; i < npcs.size(); i++) {
                System.out.println(Color.colorize("| " + String.format("%d. Interact with %-" + (MENU_WIDTH - 14) + "s", i + 1, npcNames.get(i)) + " |", Color.WHITE));
            }
            System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", npcs.size() + 1, "Continue Exploring") + " |", Color.WHITE));
            System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", npcs.size() + 2, "Leave Town") + " |", Color.WHITE));
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-" + (npcs.size() + 2) + "): ", Color.YELLOW));
            String input = scan.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= npcs.size()) {
                    Enemy npc = npcs.get(choice - 1);
                    if (npc.isHostile()) {
                        interactWithHostileNPC(npc, loc.name);
                    } else if (npc.isDocile()) {
                        System.out.println(Color.colorize("This NPC is friendly and cannot be fought.", Color.GREEN));
                        interactWithDocileNPC(npc.getCurrentName(), loc);
                    } else {
                        System.out.println(Color.colorize("This NPC's status is unclear. Try another.", Color.YELLOW));
                    }
                } else if (choice == npcs.size() + 1) {
                    continueExploring(loc);
                } else if (choice == npcs.size() + 2) {
                    System.out.println(Color.colorize("You leave the town.", Color.YELLOW));
                    break;
                } else {
                    System.out.println(Color.colorize("Invalid choice.", Color.RED));
                }
            } catch (NumberFormatException e) {
                System.out.println(Color.colorize("Invalid input. Please enter a number.", Color.RED));
            }
        }
    }

    private void continueExploring(Location loc) {
        System.out.println(Color.colorize("\nYou continue exploring " + loc.name + "...", Color.YELLOW));
        if (random.nextFloat() < 0.5f) {
            generateRandomNPCEncounter(loc);
        } else {
            System.out.println(Color.colorize("You find nothing of interest.", Color.GRAY));
        }
    }

    private void generateRandomNPCEncounter(Location loc) {
        if (player == null) {
            System.out.println(Color.colorize("Error: No player selected. Please start a new game.", Color.RED));
            return;
        }
        Enemy npc = new Enemy(Enemy.Tier.values()[random.nextInt(3)], player.level);
        npc.setHostile(random.nextFloat() < 0.3f);
        String color = npc.isHostile() ? Color.RED : Color.GREEN;
        System.out.println("\n" + Color.colorize("You encounter a " + npc.getDisplayName() + " while exploring " + loc.name + "!", color));
        if (npc.isHostile()) {
            interactWithHostileNPC(npc, loc.name);
        } else if (npc.isDocile()) {
            System.out.println(Color.colorize("This NPC is friendly and cannot be fought.", Color.GREEN));
            interactWithDocileNPC(npc.getCurrentName(), loc);
        } else {
            System.out.println(Color.colorize("This NPC's status is unclear. You avoid them.", Color.YELLOW));
        }
    }

    private void interactWithHostileNPC(Enemy npc, String location) {
        if (!npc.isHostile()) {
            System.out.println(Color.colorize(npc.getCurrentName() + " is not hostile. You cannot fight them.", Color.YELLOW));
            interactWithDocileNPC(npc.getCurrentName(), worldMap.get(location));
            return;
        }
        System.out.println(Color.colorize(npc.getDisplayName() + " attacks!", Color.RED));
        this.enemy = npc;
        encounter();
    }

    private void interactWithDocileNPC(String npcName, Location loc) {
        printBorder("top");
        printCenteredLine("Friendly NPC: " + npcName, Color.GREEN);
        printBorder("divider");
        while (true) {
            printCenteredLine("What would you like to do?", Color.YELLOW);
            printCenteredLine("1. Trade (buy/sell items)", Color.WHITE);
            printCenteredLine("2. Talk (learn about " + loc.name + ")", Color.WHITE);
            printCenteredLine("3. Leave", Color.WHITE);
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-3): ", Color.YELLOW));
            String choice = scan.nextLine().trim();
            if (choice.equals("1")) {
                tradeWithNPC(npcName);
            } else if (choice.equals("2")) {
                talkToNPC(npcName, loc);
            } else if (choice.equals("3")) {
                System.out.println(Color.colorize("You leave " + npcName + ".", Color.YELLOW));
                break;
            } else {
                System.out.println(Color.colorize("Invalid option. Try again.", Color.RED));
            }
        }
    }

        private void tradeWithNPC(String npcName){
        while (true) {
            printBorder("top");
            printCenteredLine("Trading with " + npcName, Color.PURPLE);
            printBorder("divider");
            printCenteredLine("1. Buy Items", Color.WHITE);
            printCenteredLine("2. Sell Items", Color.WHITE);
            printCenteredLine("3. Cancel", Color.WHITE);
            printBorder("bottom");
            System.out.print(Color.colorize("Choose an option (1-3): ", Color.YELLOW));

            String choice = scan.nextLine().trim();
            if (choice.equals("1")) {
                buyItem(npcName);
            } else if (choice.equals("2")) {
                sellItem(npcName);
            } else if (choice.equals("3")) {
                System.out.println(Color.colorize("You stop trading with " + npcName + ".", Color.GREEN));
                break;
            } else {
                System.out.println(Color.colorize("Invalid option. Try again.", Color.RED));
            }
        }
    }

    private void buyItem(String npcName) {
        printBorder("top");
        printCenteredLine("Items for Sale from " + npcName, Color.PURPLE);
        printBorder("divider");
        String[] items;
        int[] prices;
        if (player instanceof Debugger) {
            items = new String[]{
                "Iron Sword", "Steel Sword", "Mithril Sword", "Elven Sword", "Glass Sword",
                "Daedric Sword", "Dragonbone Sword", "Dawnbreaker", "Chillrend", "Dragonbane",
                "Plate Armor", "Dragonbone Armor",
                "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
            };
            prices = new int[]{10, 15, 20, 25, 30, 35, 40, 45, 42, 40, 20, 35, 5, 15, 10};
        } else if (player instanceof Hacker) {
            items = new String[]{
                "Fire Staff", "Ice Wand", "Staff of Fireballs", "Staff of Ice Storm",
                "Staff of Healing", "Wand of Lightning", "Orb of Elements",
                "Robe of Protection", "Archmage Robes",
                "Mana Potion", "Potion of Ultimate Healing", "Amulet of Talos"
            };
            prices = new int[]{15, 20, 25, 30, 35, 40, 45, 15, 25, 7, 15, 10};
        } else if (player instanceof Tester) {
            items = new String[]{
                "Hunting Bow", "Longbow", "Composite Bow", "Elven Bow", "Glass Bow",
                "Daedric Bow", "Dragonbone Bow",
                "Leather Armor", "Elven Armor",
                "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
            };
            prices = new int[]{10, 15, 20, 25, 30, 35, 40, 15, 25, 5, 15, 10};
        } else if (player instanceof Architect) {
            items = new String[]{
                "Warhammer", "Battleaxe", "Mace", "Flail",
                "Chainmail", "Dragonscale Armor",
                "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
            };
            prices = new int[]{15, 20, 25, 30, 20, 35, 5, 15, 10};
        } else if (player instanceof PenTester) {
            items = new String[]{
                "Iron Dagger", "Steel Dagger", "Mithril Dagger", "Elven Dagger", "Glass Dagger",
                "Daedric Dagger", "Ebony Dagger",
                "Cloak of Shadows", "Nightshade Cloak",
                "Health Potion", "Potion of Ultimate Healing", "Amulet of Talos"
            };
            prices = new int[]{10, 15, 20, 25, 30, 35, 40, 15, 25, 5, 15, 10};
        } else { // Cleric
            items = new String[]{
                "Staff of Healing", "Holy Scepter", "Divine Mace",
                "Robe of Protection", "Holy Shroud",
                "Mana Potion", "Potion of Ultimate Healing", "Amulet of Talos"
            };
            prices = new int[]{15, 20, 25, 15, 25, 7, 15, 10};
        }

        for (int i = 0; i < items.length; i++) {
            System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 14) + "s", i + 1, items[i] + " (" + prices[i] + " gold)") + " |", getItemRarity(items[i])));
        }
        System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", items.length + 1, "Cancel") + " |", Color.WHITE));
        printBorder("bottom");
        System.out.print(Color.colorize("Choose an item to buy (1-" + (items.length + 1) + "): ", Color.YELLOW));
        int choice = getChoice(1, items.length + 1);

        if (choice <= items.length) {
            String item = items[choice - 1];
            int price = prices[choice - 1];
            if (player.getGold() >= price) {
                player.addItem(item, 1.0f);
                player.addGold(-price);
                System.out.println(Color.colorize("You bought a " + item + " for " + price + " gold!", getItemRarity(item)));
            } else {
                System.out.println(Color.colorize("Not enough gold!", Color.RED));
            }
        } else {
            System.out.println(Color.colorize("Purchase cancelled.", Color.YELLOW));
        }
    }

   private void sellItem(String npcName) {
    printBorder("top");
    printCenteredLine("Your Inventory for Sale to " + npcName, Color.PURPLE);
    printBorder("divider");
    List<Hero.InventoryItem> inventory = player.getInventory();
    if (inventory.isEmpty()) {
        printCenteredLine("Your inventory is empty!", Color.GRAY);
        printBorder("bottom");
        return;
    }

    int[] prices = new int[inventory.size()];
    for (int i = 0; i < inventory.size(); i++) {
        Hero.InventoryItem item = inventory.get(i);
        prices[i] = (int) (Math.random() * 10 + 5); // Random price 5-15 gold
        System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 14) + "s", i + 1, item.name + " x" + item.quantity + " (" + prices[i] + " gold)") + " |", getItemRarity(item.name)));
    }
    System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", inventory.size() + 1, "Cancel") + " |", Color.WHITE));
    printBorder("bottom");
    System.out.print(Color.colorize("Choose an item to sell (1-" + (inventory.size() + 1) + "): ", Color.YELLOW));
    int choice = getChoice(1, inventory.size() + 1);

    if (choice <= inventory.size()) {
        Hero.InventoryItem item = inventory.get(choice - 1);
        int price = prices[choice - 1];
        player.removeItem(item.name);
        player.addGold(price);
        System.out.println(Color.colorize("You sold a " + item.name + " for " + price + " gold!", getItemRarity(item.name)));
    } else {
        System.out.println(Color.colorize("Sale cancelled.", Color.YELLOW));
    }
}


    private void talkToNPC(String npcName, Location loc) {
        printBorder("top");
        printCenteredLine("Conversation with " + npcName, Color.GREEN);
        printBorder("divider");
        String[] rumors = {
            npcName + " shares a tale about a hidden treasure in " + loc.name + ".",
            npcName + " warns you about a dangerous " + loc.enemyPool[random.nextInt(loc.enemyPool.length)] + " nearby.",
            npcName + " mentions a local festival happening soon in " + loc.name + ".",
            npcName + " offers insight about a powerful artifact lost in " + loc.name + "."
        };
        String rumor = rumors[random.nextInt(rumors.length)];
        System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", rumor) + " |", Color.WHITE));
        printBorder("bottom");
        if (rumor.contains("powerful artifact")) {
            questManager.addQuest(
                    "Find a Lost Relic for " + loc.name,
                    "Locate the artifact mentioned by " + npcName + " in " + loc.name + ".",
                    Arrays.asList("Find a Lost Relic for " + loc.name),
                    Map.of("gold", 150, "xp", 75),
                    null
            );
        }
        if (loc.name.equals("RAM Bank")) {
            questManager.updateQuest("Return to RAM Bank", player);
        }
    }

    private void viewInventory() {
    printBorder("top");
    printCenteredLine("Your Inventory", Color.PURPLE);
    printBorder("divider");
    List<Hero.InventoryItem> inventory = player.getInventory();
    if (inventory.isEmpty()) {
        printCenteredLine("Your inventory is empty!", Color.GRAY);
    } else {
        for (Hero.InventoryItem item : inventory) {
            String itemLine = item.name + " x" + item.quantity + " (Weight: " + item.weight + ")";
            System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", itemLine) + " |", getItemRarity(item.name)));
        }
    }
    printBorder("bottom");
}

    private void viewEquipment() {
        printBorder("top");
        printCenteredLine("Your Equipment", Color.PURPLE);
        printBorder("divider");
        printCenteredLine("Weapon: " + equippedWeapon, getItemRarity(equippedWeapon));
        printCenteredLine("Armor: " + equippedArmor, getItemRarity(equippedArmor));
        printBorder("bottom");
    }

    private void equipOrUseItem() {
    printBorder("top");
    printCenteredLine("Equip or Use Item", Color.PURPLE);
    printBorder("divider");
    List<Hero.InventoryItem> inventory = player.getInventory();
    if (inventory.isEmpty()) {
        printCenteredLine("Your inventory is empty!", Color.GRAY);
        printBorder("bottom");
        return;
    }

    for (int i = 0; i < inventory.size(); i++) {
        Hero.InventoryItem item = inventory.get(i);
        System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", i + 1, item.name) + " |", getItemRarity(item.name)));
    }
    System.out.println(Color.colorize("| " + String.format("%d. %-" + (MENU_WIDTH - 6) + "s", inventory.size() + 1, "Cancel") + " |", Color.WHITE));
    printBorder("bottom");
    System.out.print(Color.colorize("Choose an item (1-" + (inventory.size() + 1) + "): ", Color.YELLOW));
    int choice = getChoice(1, inventory.size() + 1);

    if (choice <= inventory.size()) {
        Hero.InventoryItem item = inventory.get(choice - 1);
        String itemName = item.name;
        if (itemName.contains("Potion") || itemName.contains("Elixir")) {
            if (itemName.contains("Health")) {
                player.hp = Math.min(player.maxHP, player.hp + 50);
                System.out.println(Color.colorize("You used a " + itemName + " and restored 50 HP!", getItemRarity(itemName)));
            } else if (itemName.contains("Mana")) {
                player.mana = Math.min(player.maxMana, player.mana + 50);
                System.out.println(Color.colorize("You used a " + itemName + " and restored 50 Mana!", getItemRarity(itemName)));
            }
            player.removeItem(itemName);
        } else if (itemName.contains("Sword") || itemName.contains("Staff") || itemName.contains("Bow")
                || itemName.contains("Dagger") || itemName.contains("Mace") || itemName.contains("Warhammer")
                || itemName.contains("Battleaxe") || itemName.contains("Flail") || itemName.contains("Wand")
                || itemName.contains("Scepter")) {
            equippedWeapon = itemName;
            player.removeItem(itemName);
            System.out.println(Color.colorize("You equipped " + itemName + "!", getItemRarity(itemName)));
        } else if (itemName.contains("Armor") || itemName.contains("Robe") || itemName.contains("Cloak")
                || itemName.contains("Chainmail") || itemName.contains("Shroud")) {
            equippedArmor = itemName;
            player.removeItem(itemName);
            System.out.println(Color.colorize("You equipped " + itemName + "!", getItemRarity(itemName)));
        } else {
            System.out.println(Color.colorize("You cannot use or equip " + itemName + ".", Color.RED));
        }
    }
}

    private void viewGold() {
        printBorder("top");
        printCenteredLine("Your Wealth", Color.PURPLE);
        printBorder("divider");
        printCenteredLine("Gold: " + player.getGold(), Color.YELLOW);
        printBorder("bottom");
    }

    private void viewQuestLog() {
        printBorder("top");
        printCenteredLine("Quest Log", Color.BLUE);
        printBorder("divider");
        List<Quest> activeQuests = questManager.getActiveQuests();
        List<Quest> completedQuests = questManager.getCompletedQuests();
        if (activeQuests.isEmpty() && completedQuests.isEmpty()) {
            printCenteredLine("No quests available.", Color.GRAY);
        } else {
            if (!activeQuests.isEmpty()) {
                printCenteredLine("Active Quests", Color.YELLOW);
                for (Quest quest : activeQuests) {
                    System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", quest.getName()) + " |", Color.YELLOW));
                    System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", quest.getDescription()) + " |", Color.GRAY));
                    System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", "Objective: " + quest.getCurrentObjective()) + " |", Color.WHITE));
                }
            }
            if (!completedQuests.isEmpty()) {
                printCenteredLine("Completed Quests", Color.GREEN);
                for (Quest quest : completedQuests) {
                    System.out.println(Color.colorize("| " + String.format("%-" + (MENU_WIDTH - 4) + "s", quest.getName() + " (Completed)") + " |", Color.GREEN));
                }
            }
        }
        printBorder("bottom");
    }

    private void viewPlayerStats() {
        printBorder("top");
        printCenteredLine("Player Stats", Color.RED);
        printBorder("divider");
        printCenteredLine("Type: " + player.getClassName(), Color.YELLOW);
        printCenteredLine("Level: " + player.level, Color.WHITE);
        printCenteredLine("HP: " + player.hp + "/" + player.maxHP, Color.RED);
        printCenteredLine("Mana: " + player.mana + "/" + player.maxMana, Color.BLUE);
        printCenteredLine("Damage: " + player.minDmg + "-" + player.maxDmg, Color.WHITE);
        printCenteredLine("Gold: " + player.getGold(), Color.YELLOW);
        printCenteredLine("XP: " + player.xp + "/" + player.xpToLevel, Color.GREEN);
        String status = player.getStatusEffects().stream()
                .map(StatusEffect::getName)
                .collect(Collectors.joining(", "));
        if (!status.isEmpty()) {
            printCenteredLine("Status: " + status, Color.PURPLE);
        }
        printBorder("bottom");
    }

    private void restAtInn() {
        printBorder("top");
        printCenteredLine("Rest at Inn", Color.PURPLE);
        printBorder("divider");
        printCenteredLine("Resting costs 10 gold. Proceed?", Color.YELLOW);
        printCenteredLine("1. Yes", Color.WHITE);
        printCenteredLine("2. No", Color.WHITE);
        printBorder("bottom");
        System.out.print(Color.colorize("Choose an option (1-2): ", Color.YELLOW));
        int choice = getChoice(1, 2);
        if (choice == 1) {
            if (player.getGold() >= 10) {
                player.addGold(-10);
                player.hp = player.maxHP;
                player.mana = player.maxMana;
                player.clearStatusEffects();
                System.out.println(Color.colorize("You rest and recover fully! HP and Mana restored.", Color.GREEN));
            } else {
                System.out.println(Color.colorize("Not enough gold to rest!", Color.RED));
            }
        } else {
            System.out.println(Color.colorize("You decide not to rest.", Color.YELLOW));
        }
    }

    private void saveGame() {
        printBorder("top");
        printCenteredLine("Save Game", Color.PURPLE);
        printBorder("divider");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            oos.writeObject(player);
            oos.writeObject(worldMap);
            oos.writeObject(questManager);
            oos.writeObject(equippedWeapon);
            oos.writeObject(equippedArmor);
            oos.writeObject(new Integer(gameTimer));
            oos.writeObject(new Boolean(useColor));
            printCenteredLine("Game saved successfully!", Color.GREEN);
        } catch (IOException e) {
            printCenteredLine("Error saving game: " + e.getMessage(), Color.RED);
        }
        printBorder("bottom");
    }

    private void loadGame() {
        printBorder("top");
        printCenteredLine("Load Game", Color.PURPLE);
        printBorder("divider");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savegame.dat"))) {
            player = (Hero) ois.readObject();
            worldMap = (Map<String, Location>) ois.readObject();
            questManager = (QuestManager) ois.readObject();
            equippedWeapon = (String) ois.readObject();
            equippedArmor = (String) ois.readObject();
            gameTimer = (Integer) ois.readObject();
            useColor = (Boolean) ois.readObject();
            Color.USE_ANSI = useColor;
            printCenteredLine("Game loaded successfully!", Color.GREEN);
            inGameMenu();
        } catch (IOException | ClassNotFoundException e) {
            printCenteredLine("Error loading game: " + e.getMessage(), Color.RED);
            printBorder("bottom");

        }
    }

    private static class Combat {
        private Hero player;
        private Enemy enemy;
        private boolean combatOver = false;

        public Combat(Hero player, Enemy enemy) {
            this.player = player;
            this.enemy = enemy;
        }

        public void processRound(String action) {
            // Simple implementation for demonstration
            if ("attack".equals(action)) {
                int damage = Math.max(1, player.minDmg + (int)(Math.random() * (player.maxDmg - player.minDmg + 1)));
                enemy.hp -= damage;
                if (enemy.hp <= 0) {
                    enemy.hp = 0;
                    combatOver = true;
                }
            } else if ("skip".equals(action)) {
                // Player skips turn, enemy attacks
            }
            // Enemy attacks if still alive
            if (enemy.hp > 0) {
                int damage = Math.max(1, enemy.minDmg + (int)(Math.random() * (enemy.maxDmg - enemy.minDmg + 1)));
                player.hp -= damage;
                if (player.hp <= 0) {
                    player.hp = 0;
                    combatOver = true;
                }
            }
        }

        public boolean isCombatOver() {
            return combatOver || player.hp <= 0 || enemy.hp <= 0;
        }
    }
}


