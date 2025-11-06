package br.com.notifmais.dto.consulta;

import br.com.notifmais.dto.medico.DetalhesMedicoDto;
import br.com.notifmais.dto.paciente.DetalhesPacienteDto;

import java.time.LocalDateTime;

public class DetalhesConsultaDto {

    private int id;
    private DetalhesPacienteDto paciente;
    private DetalhesMedicoDto medico;
    private LocalDateTime dataHora;
    private String tipo;
    private String statusConfirmacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DetalhesPacienteDto getPaciente() {
        return paciente;
    }

    public void setPaciente(DetalhesPacienteDto paciente) {
        this.paciente = paciente;
    }

    public DetalhesMedicoDto getMedico() {
        return medico;
    }

    public void setMedico(DetalhesMedicoDto medico) {
        this.medico = medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatusConfirmacao() {
        return statusConfirmacao;
    }

    public void setStatusConfirmacao(String statusConfirmacao) {
        this.statusConfirmacao = statusConfirmacao;
    }
}
