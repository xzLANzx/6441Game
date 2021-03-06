package model;

import javafx.scene.paint.Color;
import observer.Observable;
import service.MapEditorService;

import java.util.*;

/**
 * Initial Map and observer
 */
public class MapGraph extends Observable {
    String name;

    Integer height, width;

    List<Continent> continentList;

    LinkedHashMap<Country, Set<Country>> adjacentCountries;

    List<Country> countryList;

    List<Connection> connectionList;


    /**
     * Initial continent List
     */
    public MapGraph() {
        continentList = new LinkedList<>();
        countryList = new LinkedList<>();
        connectionList = new LinkedList<>();
        adjacentCountries = new LinkedHashMap<>();
    }

    /**
     * Set Continent Name
     *
     * @param name continent name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set Continent Height
     *
     * @param height Map height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Set Width
     *
     * @param width Map Width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Add new Continent
     *
     * @param continentName continent name
     * @param armyValue     number of army
     * @param color         selected color
     */
    public void addContinent(String continentName, Integer armyValue, Color color) {
        Continent continent = new Continent(continentName, armyValue, color);
        continentList.add(continent);
        notifyObservers(this);
    }

    /**
     * @param continentName continent name
     */
    public void deleteContinent(String continentName) {
        for (int i = 0; i < MapEditorService.mapGraph.continentList.size(); i++) {
            if (continentName.equals(continentList.get(i).getContinentName())) {
                List<Country> continentCountries = continentList.get(i).getCountries();
                MapEditorService.mapGraph.continentList.remove(i);

                for (int m = 0; m < continentCountries.size(); m++) {
                    deleteCountryfromAdjacentCountries(continentCountries.get(m));
                    for (int j = 0; j < MapEditorService.mapGraph.countryList.size(); j++) {
                        if (MapEditorService.mapGraph.countryList.get(j) == continentCountries.get(m))
                            MapEditorService.mapGraph.countryList.remove(j);
                    }

                }
            }
        }

        notifyObservers(this);
    }

    /**
     * Check whether add country correctly
     *
     * @param countryName   Country Name
     * @param continentName Continent Name
     * @return True or False
     */
    public boolean addCountry(String countryName, String continentName) {
        List<Continent> continentList = MapEditorService.mapGraph.getContinentList();
        boolean flag = false;
        for (int i = 0; i < continentList.size(); i++) {
            if (continentName.equals(continentList.get(i).getContinentName())) {
                flag = true;
            }
        }
        if (flag) {
            List<Country> countryList = MapEditorService.mapGraph.countryList;
            Country country;
            if (countryList.size() == 0) {
                country = new Country(countryName, continentName, 1);
            } else {
                //country id
                country = new Country(countryName, continentName, countryList.get(countryList.size() - 1).getId() + 1);
            }

            MapEditorService.mapGraph.countryList.add(country);
            MapEditorService.mapGraph.adjacentCountries.put(country, new HashSet<>());
            for (int i = 0; i < continentList.size(); i++) {
                if (continentName.equals(continentList.get(i).getContinentName())) {
                    List<Country> countryList1 = MapEditorService.mapGraph.getContinentList().get(i).countries;
                    if (countryList1 == null) {
                        countryList1 = new LinkedList<>();
                        countryList1.add(country);
                    }
                    {
                        countryList1.add(country);
                    }
                    MapEditorService.mapGraph.getContinentList().get(i).setCountries(countryList1);
                }
            }
            notifyObservers(this);
            return true;
        } else {
            return false;
        }


    }

    /**
     * Delete Country
     *
     * @param countryName Country Name
     */
    public void deleteCountry(String countryName) {
        List<Country> countryList = MapEditorService.mapGraph.countryList;
        for (int i = 0; i < countryList.size(); i++) {
            if (countryName.equals(countryList.get(i).getCountryName())) {
                deleteCountryfromAdjacentCountries(countryList.get(i));

                MapEditorService.mapGraph.countryList.remove(i);
                List<Continent> continentList = MapEditorService.mapGraph.getContinentList();
                for (int j = 0; j < continentList.size(); j++) {
                    for (int t = 0; t < continentList.get(j).getCountries().size(); t++) {
                        if (countryName.equals(continentList.get(j).getCountries().get(t).getCountryName())) {
                            List<Country> countryList1 = MapEditorService.mapGraph.getContinentList().get(j).getCountries();
                            countryList1.remove(t);
                            MapEditorService.mapGraph.getContinentList().get(j).setCountries(countryList1);
                        }
                    }
                }
            }
        }

        Country country = new Country(countryName);
        notifyObservers(this);
    }

    /**
     * Add connection between country Name 1 and country Name 2
     *
     * @param countryName1 Country name 1
     * @param countryName2 Country name 2
     */
    public void addConnection(String countryName1, String countryName2) {
        Country country1 = findCountryByName(countryName1);
        Country country2 = findCountryByName(countryName2);
        Connection connection = new Connection(country1, country2);
        Connection reverseconnection = new Connection(country2, country1);

        connectionList.add(connection);
        connectionList.add(reverseconnection);
        notifyObservers(this);
    }

    /**
     * Check whether connection has been deleted properly
     *
     * @param countryName1 Country name 1
     * @param countryName2 Country name 2
     * @return True or False
     */
    public boolean deleteConnection(String countryName1, String countryName2) {
        boolean flag = false;

        for (Iterator<Connection> iterator = MapEditorService.mapGraph.connectionList.iterator(); iterator.hasNext(); ) {
            Connection connection = iterator.next();
            if ((connection.getCountry1().countryName.equals(countryName1) && connection.getCountry2().countryName.equals(countryName2))
                    || ((connection.getCountry1().countryName.equals(countryName2) && connection.getCountry2().countryName.equals(countryName1)))) {
                iterator.remove();
                notifyObservers(this);
            }
        }

        return flag;
    }

    /**
     * Get all the connections associated with the country
     *
     * @return a list of Connection objects
     */
    public List<Connection> getConnections() {
        /**
         * TODO:
         * Get all the connections associated with the country
         */
        return connectionList;
    }

    /**
     * Remove Continent
     *
     * @param continent Continent list
     */
    public void removeContinent(Continent continent) {
        continentList.remove(continent);
    }

    /**
     * Get Continent Name
     *
     * @return continent name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get Continent Height
     *
     * @return Height
     */
    public Integer getHeight() {
        return this.height;
    }

    /**
     * Get Continent Width
     *
     * @return Width
     */
    public Integer getWidth() {
        return this.width;
    }

    /**
     * Get Adjacent Countries
     *
     * @return Country list
     */
    public LinkedHashMap<Country, Set<Country>> getAdjacentCountries() {
        return adjacentCountries;
    }

    /**
     * Set Adjacent Countries
     *
     * @param adjacentCountries Country List
     */
    public void setAdjacentCountries(LinkedHashMap<Country, Set<Country>> adjacentCountries) {
        this.adjacentCountries = adjacentCountries;
    }

    /**
     * Get Continent List
     *
     * @return Continent List
     */
    public List<Continent> getContinentList() {
        return continentList;
    }

    /**
     * Set Continent List
     *
     * @param continentList Continent List
     */
    public void setContinentList(List<Continent> continentList) {
        this.continentList = continentList;
    }

    /**
     * Get Country List
     *
     * @return list
     */
    public List<Country> getCountryList() {
        return countryList;
    }

    /**
     * Set Country List
     *
     * @param countryList list
     */
    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    /**
     * Get Connection List
     *
     * @return Connection List
     */
    public List<Connection> getConnectionList() {
        return connectionList;
    }

    /**
     * Set Connection List
     *
     * @param connectionList Connection List
     */
    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }


    /**
     * delete country from adjacentcountryList and connectionlist
     *
     * @param country Country Object
     */
    private void deleteCountryfromAdjacentCountries(Country country) {
        MapEditorService.mapGraph.adjacentCountries.remove(country);
        deleteCountryfromConnectionList(country.getCountryName());
        for (Map.Entry<Country, Set<Country>> entry : MapEditorService.mapGraph.adjacentCountries.entrySet()) {
            if (entry.getValue().contains(country)) {
                Set<Country> updatedNeighborCountries = entry.getValue();
                updatedNeighborCountries.remove(country);
                MapEditorService.mapGraph.adjacentCountries.put(entry.getKey(), updatedNeighborCountries);

                entry.getKey().removeNeighbor(country);
            }
        }
    }

    /**
     * Find required Country by searching name
     *
     * @param countryName country name
     * @return country name
     */
    private Country findCountryByName(String countryName) {
        for (Country country : MapEditorService.mapGraph.getCountryList()) {
            if (countryName.equals(country.getCountryName())) {
                return country;
            }
        }
        return null;
    }

    /**
     * Delete Country From Connected countries lists
     *
     * @param countryName country Name
     */
    private void deleteCountryfromConnectionList(String countryName) {
        for (Iterator<Connection> iterator = MapEditorService.mapGraph.connectionList.iterator(); iterator.hasNext(); ) {
            Connection connection = iterator.next();
            if (connection.getCountry1().getCountryName().equals(countryName) || connection.getCountry2().getCountryName().equals(countryName)) {
                iterator.remove();
                notifyObservers(this);
            }
        }
    }
}
