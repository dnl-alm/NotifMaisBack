package br.com.notifmais.dto.lembrete;

import br.com.notifmais.dto.consulta.DetalhesConsultaDto;

public class DetalhesLembreteDto {

    private int id;
    private DetalhesConsultaDto consulta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DetalhesConsultaDto getConsulta() {
        return consulta;
    }

    public void setConsulta(DetalhesConsultaDto consulta) {
        this.consulta = consulta;
    }
}

