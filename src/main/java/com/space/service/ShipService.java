package com.space.service;

import com.space.service.comparators.CompareShipByDateComparator;
import com.space.service.comparators.CompareShipByIdComparator;
import com.space.service.comparators.CompareShipByRatingComparator;
import com.space.service.comparators.CompareShipBySpeedComparator;
import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ShipService {

    @Autowired
    ShipRepository shipRepository;

    public List<Ship> getAllShips() {
        return (List<Ship>) shipRepository.findAll();
    }

    public Optional getById(Long id) {
            return shipRepository.findById(id);
    }

    public void deleteShip(Long shipId) {
        shipRepository.deleteById(shipId);
    }

    public boolean addPerson(Ship ship) {
        return shipRepository.save(ship) != null;
    }


    public boolean updatePerson(Ship ship) {
        return shipRepository.save(ship) != null;
    }


    public List<Ship> getAllShipsWithFiltration(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {

        Iterator<Ship> shipsIterator = shipRepository.findAll().iterator();
        List<Ship> ships = new ArrayList<>();

        while (shipsIterator.hasNext()){
            Ship ship = shipsIterator.next();
            if (name != null && !ship.getName().contains(name))  continue;
            if (planet != null && !ship.getPlanet().contains(planet))  continue;
            if (shipType != null && shipType != ship.getShipType()) continue;
            if (after != null && after > ship.getProdDate().getTime())  continue;
            if (before != null && before < ship.getProdDate().getTime())  continue;
            if (isUsed != null && isUsed != ship.isUsed())  continue;
            if (minSpeed != null && minSpeed >= ship.getSpeed())  continue;
            if (maxSpeed != null && maxSpeed <= ship.getSpeed())  continue;
            if (minCrewSize != null && minCrewSize >= ship.getCrewSize()) continue;
            if (maxCrewSize != null && maxCrewSize <= ship.getCrewSize()) continue;
            if (minRating != null && minRating > ship.getRating())  continue;
            if (maxRating != null && maxRating < ship.getRating()) continue;
            ships.add(ship);
        }
        return ships;
    }

    public boolean isShipHasCorrectFields(Ship ship) {
        if(ship.getName() != null){
            if(ship.getName().length() > 50 || ship.getName().length() < 1) return false;
        }
        if(ship.getPlanet() != null){
            if((ship.getPlanet().length() > 50) || (ship.getPlanet().length() < 1)) return false;
        }
        if(ship.getSpeed() != null){
            if(ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99) return false;
        }
        if(ship.getCrewSize() != null){
            if(ship.getCrewSize() < 1 || ship.getCrewSize() > 9999) return false;
        }
        if(ship.getProdDate() != null){
            if(ship.getProdDate().getTime() < 0) return false;
        }
        if(ship.getProdDate() != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(ship.getProdDate());
            if (cal.get(Calendar.YEAR) < 2800 || cal.get(Calendar.YEAR) > 3019) return false;
        }
        return true;
    }

    public boolean isShipHasNotNullFields(Ship ship) {
        if(ship == null) return false;
        if(ship.getName() == null) return false;
        if(ship.getPlanet() == null) return false;
        if(ship.getShipType() == null) return false;
        if(ship.getProdDate() == null) return false;
        if(ship.getSpeed() == null) return false;
        if(ship.getCrewSize() == null) return false;
        return true;
    }

    public Ship updateShipFields(Ship targetShip, Ship shipFromRequest) {
        if (shipFromRequest.getName() != null) targetShip.setName(shipFromRequest.getName());
        if (shipFromRequest.getPlanet() != null) targetShip.setPlanet(shipFromRequest.getPlanet());
        if (shipFromRequest.getShipType() != null) targetShip.setShipType(shipFromRequest.getShipType());
        if (shipFromRequest.getProdDate() != null) targetShip.setProdDate(shipFromRequest.getProdDate());
        if (shipFromRequest.getSpeed() != null) targetShip.setSpeed(shipFromRequest.getSpeed());
        if (shipFromRequest.getCrewSize() != null) targetShip.setCrewSize(shipFromRequest.getCrewSize());
        targetShip.setRating(targetShip.getNewShipRating());
        return targetShip;
    }

    public List<Ship> sortingAndPaginationShipsList(List<Ship> ships, ShipOrder order, Integer pageNumber, Integer pageSize){
        if(order != null){
            switch (order){
                case ID:
                    ships.sort(new CompareShipByIdComparator());
                    break;
                case DATE:{
                    ships.sort(new CompareShipByDateComparator());
                    break;
                }
                case SPEED:{
                    ships.sort(new CompareShipBySpeedComparator());
                    break;
                }
                case RATING:{
                    ships.sort(new CompareShipByRatingComparator());
                    break;
                }
            }
        }

        pageNumber = pageNumber == null? 0 : pageNumber;    //if pageNumber == null then use -> 0
        pageSize = pageSize == null? 3 : pageSize;          //if pageSize == null then use -> 3
        int firstElementAtCurrentPage = pageSize * pageNumber + 1;
        int lastElementAtCurrentPage = firstElementAtCurrentPage + pageSize - 1;
        if(lastElementAtCurrentPage > ships.size()){
            lastElementAtCurrentPage = ships.size();
        }
        return ships.subList(firstElementAtCurrentPage - 1, lastElementAtCurrentPage);
    }

    public boolean isIdCorrect(String id){
        int idTemp;
        try{
            idTemp = Integer.parseInt(id);                      //is whole number
            if(idTemp < 1) throw new NumberFormatException();   // is positive
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
