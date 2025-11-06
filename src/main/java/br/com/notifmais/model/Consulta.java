package br.com.notifmais.model;

import java.time.LocalDateTime;

public class Consulta {

    private int id;
    private LocalDateTime dataHora;
    private String tipo;
    private String statusConfirmacao;
    private Paciente paciente;
    private Medico medico;

    public Consulta() {
    }

    public Consulta(int id, LocalDateTime dataHora, String tipo, String statusConfirmacao, Paciente paciente, Medico medico) {
        this.id = id;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.statusConfirmacao = statusConfirmacao;
        this.paciente = paciente;
        this.medico = medico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
