package com.space.service.comparators;

import com.space.model.Ship;

import java.util.Comparator;

public class CompareShipByIdComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Ship firstShip = (Ship)o1;
        Ship secondShip = (Ship)o2;
        if(firstShip.getId() > secondShip.getId()){
            return 1;
        } else if(firstShip.getId() == secondShip.getId()){
            return 0;
        }
        return -1;
    }
}
