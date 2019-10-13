package model;

import controller.Observer;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Country {

    Integer id;

    String countryName;

    double x;

    double y;

    // observers list
    private List<Observer> countryObservers = new ArrayList<>();

    public void attach(Observer observer){
        countryObservers.add(observer);
    }

    public void notifyAllObservers(){
        for(Observer observer : countryObservers){
            //observer.update();
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setNeighbours(List<Country> neighbours) {
        this.neighbours = neighbours;
    }

    public Continent getParentContinent() {
        return parentContinent;
    }

    public void setParentContinent(Continent parentContinent) {
        this.parentContinent = parentContinent;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getArmyValue() {
        return armyValue;
    }

    public void setArmyValue(Integer armyValue) {
        this.armyValue = armyValue;
    }

    List<Country> neighbours;

    Continent parentContinent;

    Player player;

    Integer armyValue;

    public Country() {
    }

    public Country(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Country Constructor
     * @param countryName
     * @param continentName
     */
    public Country(String countryName, String continentName){
        // TODO：create a new country here
    }

    public void editCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * set coordinators
     *
     * @param x
     * @param y
     */
    public void setCoordinator(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * return Point2D location of the country
     *
     * @return
     */
    public Point2D getCoordinator(){
        return new Point2D(x, y);
    }

    /**
     * add neighbor
     *
     * @param country
     */
    public void addNeighbor(Country country) {
        if (!this.neighbours.contains(country))
            this.neighbours.add(country);
    }

    /**
     * remove neighbor
     *
     * @param country
     */
    public void removeNeighbor(Country country) {
        this.neighbours.remove(country);
    }

    /**
     * check if countries are connected
     *
     * @param country
     * @return
     */
    public boolean connected(Country country) {
        return this.neighbours.contains(country);
    }

    /**
     * get country name
     *
     * @return
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * get all neighbours of a country
     *
     * @return
     */
    public List<Country> getNeighbours() {
        return neighbours;
    }

    /**
     * get the continent that the country belongs to
     * @return
     */
    public Continent getContinent(){
        Continent continent = new Continent();
        return continent;
    }

    public Country(Integer id, String countryName, Continent parentContinent, int positionX, int positionY) {
        this.id = id;
        this.countryName = countryName;
        this.parentContinent = parentContinent;
        this.x = positionX;
        this.y = positionY;
    }
}
