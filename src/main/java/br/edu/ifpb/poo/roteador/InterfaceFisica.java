package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterfaceFisica {
    private String nome;
    private String ipString;

    public InterfaceFisica(String nome, String ipString) {
        this.nome = nome;
        this.ipString = ipString; 
        IpUtils.stringPraBytes(ipString); 
    }

    @Override
    public String toString(){
        return "Interface: "+ nome + " / IP: "+ ipString;
    }
}