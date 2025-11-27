package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterfaceFisica {
    String nome;
    String ip;

    public InterfaceFisica(String nome, String ip) {
        this.nome = nome;
        this.ip = ip;
    }
}
