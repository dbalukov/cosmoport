package com.space.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String planet;
    @Enumerated(EnumType.STRING)
    private ShipType shipType;
    private Date prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;
    private Double rating;

    public Ship(String name, String planet, ShipType shipType, Date prodDate, boolean isUsed, double speed, int crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = getNewShipRating();
    }

    public Ship() {

    }

    public double getNewShipRating(){
        double useFactor = isUsed? 0.5d : 1d;
        Date currentDate = new Date();
        double numerator = 80 * speed * useFactor;
        double denominator = (currentDate.getYear() + 1000) - prodDate.getYear() + 1;
        double result = numerator / denominator;
        BigDecimal bd = BigDecimal.valueOf(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(int crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return isUsed == ship.isUsed &&
                Double.compare(ship.speed, speed) == 0 &&
                crewSize == ship.crewSize &&
                Double.compare(ship.rating, rating) == 0 &&
                name.equals(ship.name) &&
                planet.equals(ship.planet) &&
                shipType == ship.shipType &&
                prodDate.equals(ship.prodDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }


}
