package Calciatore;

import java.util.ArrayList;
import java.util.List;

public class Feature {
    String nome, descrizione;
    char tipo;
    List<Calciatore> calciatori;

    public Feature(String nome, char tipo, String descrizione) {
        this.nome = nome;
        this.tipo = tipo;
        this.descrizione = descrizione;
    }

    public String getNome() {
        return nome;
    }
}
