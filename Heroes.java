package com.mycompany.programmer_escape;

import java.util.ArrayList;
import java.util.List;

public class Heroes {
    private List<Hero> heroes;

    public Heroes() {
        heroes = new ArrayList<>();
        initializeHeroes();
    }

    private void initializeHeroes() {
        heroes.add(new Debugger());
        heroes.add(new Hacker());
        heroes.add(new Tester());
        heroes.add(new Architect());
        heroes.add(new PenTester());
        heroes.add(new Cleric());
    }

    public List<Hero> getHeroes() {
        return heroes;
    }
}
