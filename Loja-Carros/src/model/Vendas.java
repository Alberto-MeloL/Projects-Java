package model;

public class Vendas {

    String carroPlaca;
    String valor;
    String clienteCpf;

    //Construtor
    public Vendas(String carroPlaca, String valor, String clienteCpf) {
        this.carroPlaca = carroPlaca;
        this.valor = valor;
        this.clienteCpf = clienteCpf;
    }

    // Getters and Setters
    public String getCarroPlaca() {
        return carroPlaca;
    }

    public void setCarroPlaca(String carroPlaca) {
        this.carroPlaca = carroPlaca;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }
}
