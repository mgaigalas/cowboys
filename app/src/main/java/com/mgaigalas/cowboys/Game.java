package com.mgaigalas.cowboys;

import com.mgaigalas.cowboys.exception.NoCowboysFoundException;
import com.mgaigalas.cowboys.model.Cowboy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Marius Gaigalas
 */
@Slf4j
public class Game {
    final Lock lock = new ReentrantLock();

    public void start(List<Cowboy> cowboyList) {
        while (true) {
           try {
               shootOnce(cowboyList);
           } catch (NoCowboysFoundException e) {
               break;
           }
        }
        log.info("list: {}", cowboyList);
    }

    private void shootOnce(List<Cowboy> cowboyList) {
        cowboyList.parallelStream()
                .forEach(cowboy -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        lock.lock();
                        if (cowboy.getHealth() > 0) {
                            final Cowboy targetCowboy = findTarget(cowboyList, cowboy);
                            executeFire(cowboy, targetCowboy);
                        }
                    } finally {
                        lock.unlock();
                    }
                });
    }

    private void executeFire(Cowboy sourceCowboy, Cowboy targetCowboy) {
        try {
            final var damage = sourceCowboy.getDamage();
            final var health = targetCowboy.getHealth();
            final var healthRemains = health - damage;

            log.info("Cowboy[{}] fires at Cowboy[{}] with health: {} and deals {} damage => health left: {}",
                    sourceCowboy.getName(),
                    targetCowboy.getName(),
                    health,
                    damage,
                    healthRemains);
            targetCowboy.setHealth(healthRemains);
        } catch (NoCowboysFoundException e) {
            log.error("error: {}", e.getMessage(), e);
        }
    }

    private Cowboy findTarget(List<Cowboy> cowboyList, Cowboy source) {
        return cowboyList.stream()
                .filter(cowboy -> !cowboy.equals(source))
                .filter(cowboy -> cowboy.getHealth() > 0)
                .findAny()
                .orElseThrow(() -> new NoCowboysFoundException("No Cowboys found"));
    }
}

/*
//Every cowboy starts at the same time in parallel, selects random target and shoots.

We subtract shooter damage points from target health points.
If target cowboy health points are 0 or lower,then target is dead.
Cowboys don’t shoot themselves and don’t shoot dead cowboys.
After the shot shooter sleeps for 1 second. In the end “there can be only one“.
Last standing cowboy is the winner.
The task is to print log of every action and print who is the winner in the end.
[
    {
      "name": "John",
      "health": 10,
      "damage": 1
    },
    {
      "name": "Bill",
      "health": 8,
      "damage": 2
    },
    {
      "name": "Sam",
      "health": 10,
      "damage": 1
    },
    {
      "name": "Peter",
      "health": 5,
      "damage": 3
    },
    {
      "name": "Philip",
      "health": 15,
      "damage": 1
    }
]
 */