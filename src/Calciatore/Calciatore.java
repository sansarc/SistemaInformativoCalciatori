package Calciatore;

public class Calciatore {
    private String nome, cognome;
    private String dataNascita, dataRitiro;
    private char piede;
    private int golFatti, golSubiti;
    private String features, ruoli;

    public void setNome(String s) {nome = s;}
    public void setCognome(String s) {cognome = s;}
    public void setDataNascita(String s) {dataNascita = s;}
    public void setDataRitiro(String s) {dataRitiro = s;}
    public void setPiede(char c) {piede = c;}
    public void setGolFatti(int n) {golFatti = n;}
    public void setGolSubiti(int n) {golSubiti = n;}
    public void setRuolo(String s) {ruoli = s;}

    public String getRuolo() {return ruoli;}
    public String getNome() {return nome;}
    public String getCognome() {return cognome;}
    public String getDataNascita() {return dataNascita;}
    public String getDataRitiro() {return dataRitiro;}
    public char getPiede() {return piede;}
    public int getGolFatti() {return golFatti;}
    public int getGolSubiti() {return golSubiti;}
    public String getFeatures() {return features;}
}
