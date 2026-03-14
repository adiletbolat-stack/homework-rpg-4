package com.narxoz.rpg.battle;

import com.narxoz.rpg.bridge.Skill;
import com.narxoz.rpg.composite.CombatNode;

import java.util.Random;

public class RaidEngine {

    private Random random = new Random(1L);

    public RaidEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public RaidResult runRaid(CombatNode teamA, CombatNode teamB, Skill teamASkill, Skill teamBSkill) {

        if (teamA == null || teamB == null || teamASkill == null || teamBSkill == null) {
            throw new IllegalArgumentException("Raid inputs must not be null");
        }

        if (!teamA.isAlive() || !teamB.isAlive()) {
            throw new IllegalArgumentException("Both teams must be alive to start the raid");
        }

        RaidResult result = new RaidResult();
        int rounds = 0;
        int MAX_ROUNDS = 50;

        result.addLine("=== Raid Start ===");

        while (teamA.isAlive() && teamB.isAlive() && rounds < MAX_ROUNDS) {

            rounds++;
            result.addLine("");
            result.addLine("Round " + rounds);


            boolean critA = random.nextInt(100) < 10;

            result.addLine("Team A uses " + teamASkill.getSkillName() +
                    " (" + teamASkill.getEffectName() + ")");

            teamASkill.cast(teamB);

            if (critA) {
                result.addLine("Critical strike by Team A!");
                teamASkill.cast(teamB);
            }

            if (!teamB.isAlive())
                break;

            boolean critB = random.nextInt(100) < 10;

            result.addLine("Team B uses " + teamBSkill.getSkillName() +
                    " (" + teamBSkill.getEffectName() + ")");

            teamBSkill.cast(teamA);

            if (critB) {
                result.addLine("Critical strike by Team B!");
                teamBSkill.cast(teamA);
            }
        }

        if (teamA.isAlive() && !teamB.isAlive()) {
            result.setWinner("Team A");
        } else if (teamB.isAlive() && !teamA.isAlive()) {
            result.setWinner("Team B");
        } else {
            result.setWinner("Draw (Max rounds reached)");
        }

        result.setRounds(rounds);

        result.addLine("");
        result.addLine("Raid finished in " + rounds + " rounds.");
        result.addLine("Winner: " + result.getWinner());

        return result;
    }
}