package com.example.antnio.myapplication;

public class Alerta {
    private String estado;
    private String cidade;
    private String bairro;
    private String latitude;
    private String longitude;
    private String status;
    private String horaData;
    private Boolean ativo = true;

    public Alerta(String estado, String cidade, String bairro,
                  String latitude, String longitude, String horaData) {
        this.setEstado(estado);
        this.setCidade(cidade);
        this.setBairro(bairro);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setHoraData(horaData);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHoraData() {
        return horaData;
    }

    public void setHoraData(String horaData) {
        this.horaData = horaData;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return getBairro() + ", " +
                getCidade() + ", " + getEstado()
                + " \n" + getLatitude() + " \n" +
                getLongitude() + " \n" + getHoraData();
    }
}
