package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pacote {
    private byte[] ipDestino;

    // Vamos ter que dividir o IP em quatro octetos. Analisar se terá aqui mesmo. 

    public Pacote(String ipDestino) {
        this.ipDestino = IpUtils.stringPraBytes(ipDestino);
    }

}
