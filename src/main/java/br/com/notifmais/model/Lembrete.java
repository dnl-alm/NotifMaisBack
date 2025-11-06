package br.com.notifmais.model;

import br.com.notifmais.model.Consulta;

public class Lembrete {

    private int id;
    private Consulta consulta;

    public Lembrete() {
    }

    public Lembrete(int id, Consulta consulta) {
        this.id = id;
        this.consulta = consulta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
}