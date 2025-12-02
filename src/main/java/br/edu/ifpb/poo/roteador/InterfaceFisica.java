package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterfaceFisica {
    String nome;
    String ipString;

    public InterfaceFisica(String nome, String ip) {
        this.nome = nome;
        this.ipString = ip;
        IPUtils.ipParaNumerico(ipString);
    }
    public String toString(){
        return "Interface: "+ nome + " / IP: "+ ipString;
    }
}
