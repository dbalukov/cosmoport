package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest")
public class ShipController {
    @Autowired
    private ShipService shipService;

    @GetMapping("/ships/count")
    @ResponseBody
    public ResponseEntity<Integer> getCount(
            @RequestParam(name = "name",        required = false) String name,
            @RequestParam(name = "planet",      required = false) String planet,
            @RequestParam(name = "shipType",    required = false) ShipType shipType,
            @RequestParam(name = "after",       required = false) Long after,
            @RequestParam(name = "before",      required = false) Long before,
            @RequestParam(name = "isUsed",      required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed",    required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed",    required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating",   required = false) Double minRating,
            @RequestParam(name = "maxRating",   required = false) Double maxRating
    ) {
        List<Ship> ships = shipService.getAllShipsWithFiltration(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        return new ResponseEntity<>(ships.size(), HttpStatus.OK);
    }

    @GetMapping("/ships/{id}")
    @ResponseBody
    public ResponseEntity<?> getShip(@PathVariable Long id) {
        if(id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional optional = shipService.getById(id);
        if(!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        }

    }

    @DeleteMapping(value = "/ships/{id}")
    @ResponseBody
    public ResponseEntity<Ship> deleteShip(@PathVariable Long id) {
        if (id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional optional = shipService.getById(id);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            shipService.deleteShip(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/ships")
    @ResponseBody
    public ResponseEntity<List<Ship>> getShipsList(
            @RequestParam(name = "name",        required = false) String name,
            @RequestParam(name = "planet",      required = false) String planet,
            @RequestParam(name = "shipType",    required = false) ShipType shipType,
            @RequestParam(name = "after",       required = false) Long after,
            @RequestParam(name = "before",      required = false) Long before,
            @RequestParam(name = "isUsed",      required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed",    required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed",    required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating",   required = false) Double minRating,
            @RequestParam(name = "maxRating",   required = false) Double maxRating,
            @RequestParam(name = "order",       required = false) ShipOrder order,
            @RequestParam(name = "pageNumber",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    required = false) Integer pageSize

    ) {
        List<Ship> ships = shipService.getAllShipsWithFiltration(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        List<Ship> result = shipService.sortingAndPaginationShipsList(ships, order, pageNumber, pageSize);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/ships")
    @ResponseBody
    public ResponseEntity<Ship> createNewShip(@RequestBody Ship ship) {
       if(shipService.isShipHasNotNullFields(ship) && shipService.isShipHasCorrectFields(ship)){
           if(ship.isUsed() == null){
               ship.setUsed(false);
           }
           ship.setRating(ship.getNewShipRating());
           shipService.addPerson(ship);
           return new ResponseEntity<>(ship, HttpStatus.OK);
       }
       return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }



    @PostMapping(value = "/ships/{id}")
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@RequestBody Ship shipFromRequest, @PathVariable String id){
        if(!shipService.isIdCorrect(id) || !shipService.isShipHasCorrectFields(shipFromRequest)){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Optional<Ship> optionalShip = shipService.getById(Long.parseLong(id));
        if(optionalShip.isPresent()){
            Ship currentShip = shipService.updateShipFields(optionalShip.get(), shipFromRequest);
            shipService.updatePerson(currentShip);
            return new ResponseEntity<>(currentShip, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


}
