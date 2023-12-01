package Calciatore;

public class Calciatore {
    private String nome, cognome;
    private String dataNascita, dataRitiro;
    private char piede;
    private int golFatti, golSubiti;
    private String features, ruoli;

    /* public Calciatore(String nome, String cognome, String dataNascita, char piede, List<String> ruoli, List<Feature> features, int golFatti, int golSubiti, String dataRitiro) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.piede = piede;
        this.ruoli = ruoli;
        this.features = features;
        this.golFatti = golFatti;
        this.golSubiti = golSubiti;
        this.dataRitiro = dataRitiro;
    } */

    public void setNome(String s) {
        nome = s;
    }
    public void setCognome(String s) {
        cognome = s;
    }
    // TODO: completa le altre set

    public String getRuolo() {
        return ruoli;
    }
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getFeatures() {
        return features;
    }
}