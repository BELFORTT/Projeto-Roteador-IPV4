package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pacote {
    private Byte ipDestino;

    // Vamos ter que dividir o IP em quatro octetos. Analisar se terá aqui mesmo. 

    public Pacote(Byte ipDestino) {
        this.ipDestino = ipDestino;
    }

}
