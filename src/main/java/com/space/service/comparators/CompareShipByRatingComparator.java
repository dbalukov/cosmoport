package com.space.service.comparators;

import com.space.model.Ship;

import java.util.Comparator;

public class CompareShipByRatingComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            Ship firstShip = (Ship)o1;
            Ship secondShip = (Ship)o2;
            return Double.compare(firstShip.getRating(), secondShip.getRating());
        }
}
