package service;

import model.Country;

import java.util.List;

/**
 * service class for fortify phase
 */
public class FortifyService {

    /**
     * Fortify Action
     * @param fromCountry string
     * @param toCountry string
     * @param num string
     * @return Message
     */
    public String fortify(String fromCountry, String toCountry, String num){
        Integer fortifyArmyValue = Integer.parseInt(num);
        if(fortifyArmyValue<0){
            return "fortify num can be negative";
        }else{
            GamePlayerService gamePlayerService = new GamePlayerService();
            boolean flagc = gamePlayerService.checkPutAll();
            Integer flags = gamePlayerService.checkSamePlayer(fromCountry,toCountry);
            if(flagc){
                List<Country> countryList = MapEditorService.mapGraph.getCountryList();

                int flag = 0;
                for(int i=0;i<countryList.size();i++){
                    if(toCountry.equals(countryList.get(i).getCountryName())) {
                        flag = 1;
                        for (int j = 0; j < countryList.size(); j++) {
                            if (fromCountry.equals(countryList.get(j).getCountryName())) {
                                flag = 2;
                                if (countryList.get(j).getArmyValue() < fortifyArmyValue+1) {
                                    flag = 3;
                                } else if(flags==0){
                                    flag =4;
                                }else if(flags==2){
                                    flag=5;
                                }else{
                                    Integer newFromCountry = countryList.get(j).getArmyValue() - fortifyArmyValue;
                                    Integer newToCountry =countryList.get(i).getArmyValue() + fortifyArmyValue;
                                    MapEditorService.mapGraph.getCountryList().get(j).setArmyValue(newFromCountry);
                                    MapEditorService.mapGraph.getCountryList().get(i).setArmyValue(newToCountry);
                                }
                            }
                        }
                    }
                }

                if (flag==0){
                    return "to tountry name can not be found";
                }else if(flag==1){
                    return "from Country name can not be found";
                }else if(flag==3){
                    return "the army value of the form country is not enough";
                }else if(flag==5){
                    return "fromcountry and to country are not from the same player";
                }else if(flag==4){
                    return "countryname can not be found";
                }else {
                    return "fortify success";
                }
            }else {
                return "reinforce phase is not finish";
            }
        }
    }

    /**
     * No fortify
     * @return message
     */
    public String fortifyNone(){
        return "fortify none success";
    }
}
