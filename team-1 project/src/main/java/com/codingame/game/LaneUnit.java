package com.codingame.game;

import java.util.List;

public class LaneUnit extends Unit {
    Point targetPoint;
    Unit aggroUnit;
    int aggroTimeLeft;
    double aggroTset;

    public LaneUnit(double x, double y, int health, int team, int moveSpeed, Point targetPoint, Player player) {
        super(x, y, health, team, moveSpeed, player);
        this.targetPoint = targetPoint;
    }

    @Override
    void afterRound(){
        super.afterRound();
        aggroTimeLeft--;
    }

    @Override
    public String getType() {
        return "UNIT";
    }

    void findAction() {
        if (isDead || stunTime > 0) return;
        if(aggroUnit != null && aggroTimeLeft > 0 && distance(aggroUnit) < Const.AGGROUNITRANGE && aggroUnit.visible){
            attackUnitOrMoveTowards(aggroUnit, 0.0);
            return;
        }

        aggroTset = 1.0;
        aggroUnit = null;
        aggroTimeLeft = -1;

        Unit closest = findClosestOnOtherTeam("UNIT");
        if(canAttack(closest)){
            fireAttack(closest);
        }else if(closest != null && distance2(closest) < Const.AGGROUNITRANGE2) {
            attackUnitOrMoveTowards(closest, 0);
        }else{
            closest = findClosestOnOtherTeam("HERO");
            if(canAttack(closest)){
                fireAttack(closest);
            }else if(closest != null && distance2(closest) < Const.AGGROUNITRANGE2 && allowedToAttack(closest)) {
                attackUnitOrMoveTowards(closest, 0);
            }else moveAttackTowards(targetPoint);
        }
    }
}
