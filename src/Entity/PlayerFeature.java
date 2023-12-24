package Entity;
import java.util.List;
import java.util.ArrayList;

public class PlayerFeature {
    private Player player;
    private final List<Feature> featureList = new ArrayList<>();

    public void setPlayer(Player p) {player = p;}
    public void addFeature(Feature f) {featureList.add(f);}

    public Player getPlayer() {return player;}
    public Feature getFeature(int index) {return featureList.get(index);}
    public List<Feature> getFeatureList() {return featureList;}
    public String getFeatureListString() {
        String featureListString = featureList.getFirst().getName();
        if (featureList.size() > 1) {
            for (int i=1; i< featureList.size(); i++) {
                featureListString = featureListString.concat(", " + featureList.get(i).getName());
            }
        }
        return featureListString;
    }
}
