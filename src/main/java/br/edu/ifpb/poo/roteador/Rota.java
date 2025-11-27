package br.edu.ifpb.poo.roteador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rota {
    private String enderecoDestino;
    private String enderecoGateway;
    private String mascaraDeSubRede;
    private InterfaceFisica interfac;

    public Rota(String destino, String gateway, String mascara, InterfaceFisica interfac) {
        this.enderecoDestino = destino;
        this.enderecoGateway = gateway;
        this.mascaraDeSubRede = mascara;
        this.interfac = interfac;
    }

}
