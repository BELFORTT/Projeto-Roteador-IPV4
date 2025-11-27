package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pacote {
    private String ipDestino;

    public Pacote(String ipDestino) {
        this.ipDestino = ipDestino;
    }

}
