package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterfaceFisica {
    String nome;
    Byte ip;

    // Vamos ter que dividir o IP em quatro octetos. Analisar se terá aqui mesmo. 

    public InterfaceFisica(String nome, Byte ip) {
        this.nome = nome;
        this.ip = ip;
    }
}
