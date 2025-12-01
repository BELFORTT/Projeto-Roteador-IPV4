package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterfaceFisica {
    private String nome;
    private byte[] ip;

    // Vamos ter que dividir o IP em quatro octetos. Analisar se terá aqui mesmo. 

    public InterfaceFisica(String nome, String ipString) {
        this.nome = nome;
        this.ip = IpUtils.stringPraBytes(ipString);
    }
    public String toString() {
        return "Interface: " + nome + " | IP: " + IpUtils.bytesParaString(ip);
    }
}
